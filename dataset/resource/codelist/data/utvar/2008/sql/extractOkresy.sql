-- smaz data tabulek
truncate table okresy_actual;
truncate table utvary_actual;
truncate table utvary_toOkresy;
truncate table utvary_toKraje;
truncate table utvary_toCentralniUtvary;
truncate table chybejiciOkresy;
truncate table chybejiciUtvary;

-- Extrahuj pouzivane okresy.
insert into okresy_actual(kr, ok, nazev)
-- pouzite okresy mimo 0600 a 0700
select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapistc ftc on ftc.t01_kr = o.kr and ftc.t01_ok = o.ok and ftc.t01_kr not in ('20', '21', '30', '50') and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapistc ftc on ftc.t05_kr = o.kr and ftc.t05_ok = o.ok and ftc.t05_kr not in ('20', '21', '30', '50') and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapispa fzp on fzp.p01_kr = o.kr and fzp.p01_ok = o.ok and fzp.p01_kr not in ('20', '21', '30', '50') and o.ok <> '00';

-- chybejici okresy - mimo ciziny
insert into chybejiciOkresy(kr, ok)
	(select
        ftc.t01_kr as kr, ftc.t01_ok as ok
    from
        ks_zapistc ftc
    where
        not exists(select 1 from okresy o where o.kr = ftc.t01_kr and o.ok = ftc.t01_ok)
		and ftc.t01_kr not in ('20', '21', '30', '50') and ftc.t01_ok <> '00')

	union distinct

	(select
        ftc.t05_kr as kr, ftc.t05_ok as ok
    from
        ks_zapistc ftc
    where
        not exists(select 1 from okresy o where o.kr = ftc.t05_kr and o.ok = ftc.t05_ok)
		and ftc.t05_kr not in ('20', '21', '30', '50') and ftc.t05_ok <> '00')

	union distinct
    
	(select
        fzp.p01_kr as kr, fzp.p01_ok as ok
    from
        ks_zapispa fzp
    where
        not exists(select 1 from okresy o where o.kr = fzp.p01_kr and o.ok = fzp.p01_ok)
		and fzp.p01_kr not in ('20', '21', '30', '50') and fzp.p01_ok <> '00');

-- pridej chybejici soubory do tabulky okresy_actual
insert into okresy_actual(kr, ok, nazev)
select 
 cho.kr, cho.ok, IFNULL(oa.nazev, CONCAT(k.kraje, ' - neznámý ', cho.kr, cho.ok))
from
	chybejiciOkresy cho inner join kraje k on k.kod = cho.kr
	left outer join crimestatistics.okresy_actual oa on oa.kr = cho.kr and oa.ok = cho.ok;

-- V tomto bode se vygeneruje obsah souboru 'okresy_actual.csv'.
-- Pomoci externi utility se vygeneruje obsah souboru 'utvary_actual'. Tento se nahraje do tabulky 'utvary_actual'.
select
	oa.kr, oa.ok, '', oa.nazev, 1, ''
from okresy_actual oa
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/2008/okresy/okresy_actual.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';