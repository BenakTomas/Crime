use 2010ku;

CREATE TABLE `okresy_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- tabulka utvaru
CREATE TABLE `utvary_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- Vytvor indexy pro vyhledavani
create index IX_ks_zapistc_kr_ok_ut using btree
on ks_zapistc(t01_kr, t01_ok, t01_ut);

create index IX_ks_zapistc_kr5_ok5_ut5 using btree
on ks_zapistc(t05_kr, t05_ok, t05_ut);

create index IX_ks_zapispa_kr_ok_ut using btree
on ks_zapispa(p01_kr, p01_ok, p01_ut);

create index IX_ks_zapispa_kr15_ok15 using btree
on ks_zapispa(p15_kr, p15_ok);

create index IX_ks_zapistc_idtc using btree
on ks_zapistc(id_tc);

create index IX_ks_zapispa_idpa using btree
on ks_zapispa(id_pa);

create index IX_ks_zapisp28_ptr_tc using btree
on ks_zapisp28(ptr_tc);

create index IX_ks_zapisp28_ptr_pa using btree
on ks_zapisp28(ptr_pa);

create index IX_ks_zapisp28_kr5_ok5 using btree
on ks_zapisp28(r05_kr, r05_ok);

create index IX_okresy_actual_kr_ok using hash
on okresy_actual(kr, ok);

create index IX_utvary_actual_kr_ok_ut using hash
on utvary_actual(kr, ok, ut);

-- Extrahuj data kraju do souboru 'kraje_actual'.
select
CONCAT(k.kr, '#00##', trim(k.nazev), '#0#')
from kraje k
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\2010\\kraje\\kraje_actual.csv'
character set utf8;

-- Extrahuj pouzivane okresy.
insert into okresy_actual(kr, ok, nazev)
-- pouzite okresy mimo 0600 a 0700
select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapistc ftc on ftc.t01_kr = o.kr and ftc.t01_ok = o.ok and ftc.t01_ok <> '30' and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapistc ftc on ftc.t05_kr = o.kr and ftc.t05_ok = o.ok and ftc.t05_ok <> '30' and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapispa fzp on fzp.p01_kr = o.kr and fzp.p01_ok = o.ok and fzp.p01_ok <> '30' and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapispa fzp on fzp.p15_kr = o.kr and fzp.p15_ok = o.ok and fzp.p15_ok <> '30' and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapisp28 fzp28 on fzp28.r05_kr = o.kr and fzp28.r05_ok = o.ok and fzp28.r05_ok <> '30' and o.ok <> '00';

-- chybejici okresy - mimo ciziny
create temporary table chybejiciOkresy(
	kr varchar(2) not null,
	ok varchar(2) not null
)
	(select
        ftc.t01_kr as kr, ftc.t01_ok as ok
    from
        ks_zapistc ftc
    where
        not exists(select 1 from okresy o where o.kr = ftc.t01_kr and o.ok = ftc.t01_ok)
		and ftc.t01_kr <> '30' and ftc.t01_ok <> '00')

	union distinct

	(select
        ftc.t05_kr as kr, ftc.t05_ok as ok
    from
        ks_zapistc ftc
    where
        not exists(select 1 from okresy o where o.kr = ftc.t05_kr and o.ok = ftc.t05_ok)
		and ftc.t05_kr <> '30' and ftc.t05_ok <> '00')

	union distinct
    
	(select
        fzp.p01_kr as kr, fzp.p01_ok as ok
    from
        ks_zapispa fzp
    where
        not exists(select 1 from okresy o where o.kr = fzp.p01_kr and o.ok = fzp.p01_ok)
		and fzp.p01_kr <> '30' and fzp.p01_ok <> '00');

-- pridej chybejici soubory do tabulky okresy_actual
insert into okresy_actual(kr, ok, nazev)
select 
 cho.kr, cho.ok, IFNULL(oa.nazev, CONCAT(k.nazev, ' - neznámý ', cho.kr, cho.ok))
from
	chybejiciOkresy cho inner join kraje k on k.kr = cho.kr
	left outer join crimestatistics.okresy_actual oa on oa.kr = cho.kr and oa.ok = cho.ok;

-- V tomto bode se vygeneruje obsah souboru 'okresy_actual.csv'.
-- Pomoci externi utility se vygeneruje obsah souboru 'utvary_actual'. Tento se nahraje do tabulky 'utvary_actual'.
select
	CONCAT(oa.kr, '#', oa.ok, '##', oa.nazev, '#1#')
from okresy_actual oa
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\2010\\okresy\\okresy_actual.csv'
character set utf8;

-- V tomto miste se pripadne upravi nazvy dosud neznamych okresu
SET novyNazev = '';
SET kraj = '';
SET okres = '';

update okresy_actual
set nazev = novyNazev
where kr = kraj and ok = okres;

-- Zde se nahraji data ze souboru s utvary, ktery je vygenerovan, do tabulky 'utvary_actual'.
load data local infile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\2010\\utvary\\utvary_actual.csv'
replace
into table utvary_actual
character set utf8
fields terminated by '#'
lines terminated by '\r\n';

-- Zde se vygeneruje seznam chybejicich utvaru.
create temporary table chybejiciUtvary(
	kr varchar(2) not null,
	ok varchar(2) not null,
	ut varchar(2) not null
)
select
	ftc.t01_kr as kr, ftc.t01_ok as ok, ftc.t01_ut as ut
from
	ks_zapistc ftc
where
	ftc.t01_kr <> '30' and ftc.t01_ok <> '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok)

union distinct

select
	ftc.t05_kr as kr, ftc.t05_ok as ok, ftc.t05_ut as ut
from
	ks_zapistc ftc
where
	ftc.t05_kr <> '30' and ftc.t05_ok <> '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok)

union distinct

select
	fzp.p01_kr as kr, fzp.p01_ok as ok, fzp.p01_ut as ut
from
	ks_zapispa fzp
where
	fzp.p01_kr <> '30' and fzp.p01_ok <> '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok);

-- Zde vlozim do tabulky 'utvary_actual' dosud chybejici utvary.
insert into utvary_actual(kr, ok, ut, nazev)
select
	chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(oa.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu inner join okresy_actual oa on oa.KR = chu.kr and oa.OK = chu.ok
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Az se dostanu tady, budu resit zbyle utvary, ktere maji jako kod okresu uvedeno '00', tedy prislusi pod kraj.
-- Vyprazdni tabulku chybejicich utvaru
truncate table chybejiciUtvary;
-- Napln ji zbyvajicimi chybejicimi utvary
insert into chybejiciUtvary(kr, ok, ut)
select
	ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from
	ks_zapistc ftc
where
	ftc.t01_kr <> '30' and ftc.t01_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok)

union distinct

select
	ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	ftc.t05_kr <> '30' and ftc.t05_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok)

union distinct

select
	fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	fzp.p01_kr <> '30' and fzp.p01_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok);

-- Do souboru nyni zapis utvary linkovane primo na kraje.
select
	CONCAT(chu.kr, '#', chu.ok, '#', chu.ut, '#', IFNULL(ua.nazev, CONCAT(k.kraje, ' - neznámý ', chu.kr, chu.ok, chu.ut)), '#1#', k.kod, '00')
from
	chybejiciUtvary chu inner join kraje k on k.kod = chu.kr
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\2010\\utvary\\utvary_actual_toKraje.csv'
character set utf8;

-- Zde vytvorim obsah souboru 'utvary_actual_missing' (bez utvaru linkovanych primo na kraje).
select
	CONCAT(ua.kr, '#', ua.ok, '#', ua.ut, '#', ua.nazev, '#1#')
from
	utvary_actual ua
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\2010\\utvary\\utvary_actual.csv'
character set utf8;

-- Dopln do tabulky utvaru take ty, ktere se linkovaly primo na kraje.
insert into utvary_actual(kr, ok, ut, nazev)
select
	chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(k.kraje, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu inner join kraje k on k.kod = chu.kr
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Zde si pro kontrolu selectni chybejici utvary
select
	ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from
	ks_zapistc ftc
where
	ftc.t01_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok)

union distinct

select
	ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	ftc.t05_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok)

union distinct

select
	fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	fzp.p01_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok);