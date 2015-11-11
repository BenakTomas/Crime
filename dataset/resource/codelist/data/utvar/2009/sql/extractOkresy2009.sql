use 2009ku;

-- tabulka okresu
CREATE TABLE `okresy_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- tabulka kodu jiz zavedenych utvaru
CREATE TABLE `utvary_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- tabulka utvaru linkovanych na okresy
CREATE TABLE `utvary_toOkresy` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- tabulka utvaru linkovanych na kraje
CREATE TABLE `utvary_toKraje` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- tabulka utvaru linkovanych na centralni utvary
CREATE TABLE `utvary_toCentralniUtvary` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- pomocna tabulka s kody chybejicich utvaru urciteho druhu
create table chybejiciUtvary(
	kr varchar(2) not null,
	ok varchar(2) not null,
	ut varchar(2) not null
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- pomocna tabulka chybejicich okresu
create table chybejiciOkresy(
	kr varchar(2) not null,
	ok varchar(2) not null
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- smaz data tabulek
truncate table okresy_actual;
truncate table utvary_actual;
truncate table utvary_toOkresy;
truncate table utvary_toKraje;
truncate table utvary_toCentralniUtvary;
truncate table chybejiciOkresy;
truncate table chybejiciUtvary;

-- Vytvor indexy pro vyhledavani
create index IX_ks_zapistc_kr_ok_hash using hash
on ks_zapistc(t01_kr, t01_ok);

create index IX_ks_zapistc_kr_ok_ut_hash using hash
on ks_zapistc(t01_kr, t01_ok, t01_ut);

create index IX_ks_zapistc_kr5_ok5_hash using hash
on ks_zapistc(t05_kr, t05_ok);

create index IX_ks_zapistc_kr5_ok5_ut5_hash using hash
on ks_zapistc(t05_kr, t05_ok, t05_ut);

create index IX_ks_zapispa_kr_ok_hash using hash
on ks_zapispa(p01_kr, p01_ok);

create index IX_ks_zapispa_kr15_ok15_hash using hash
on ks_zapispa(p15_kr, p15_ok);

create index IX_ks_zapisp28_kr5_ok5_hash using hash
on ks_zapisp28(r05_kr, r05_ok);

create index IX_kraje_kr_hash
on kraje(kr) using hash;

create index IX_kraje_kr_hash
on kraje(kod) using hash;

create index IX_okresy_actual_kr_ok_hash using hash
on okresy_actual(kr, ok);

create index IX_utvary_actual_kr_ok_ut_hash using hash
on utvary_actual(kr, ok, ut);

create index IX_utvary_toOkresy_kr_ok_ut_hash using hash
on utvary_toOkresy(kr, ok, ut);

create index IX_utvary_toKraje_kr_ok_ut_hash using hash
on utvary_toKraje(kr, ok, ut);

create index IX_utvary_toCentralniUtvary_kr_ok_ut_hash using hash
on utvary_toCentralniUtvary(kr, ok, ut);

create index IX_chybejiciOkresy_kr_ok_hash using hash
on chybejiciOkresy(kr, ok);

create index IX_chybejiciUtvary_kr_ok_ut_hash using hash
on chybejiciUtvary(kr, ok, ut);

-- Extrahuj pouzivane okresy.
insert into okresy_actual(kr, ok, nazev)
-- pouzite okresy mimo 0600 a 0700
select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapistc ftc on ftc.t01_kr = o.kr and ftc.t01_ok = o.ok and ftc.t01_kr not in ('20', '21', '22', '30', '50') and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapistc ftc on ftc.t05_kr = o.kr and ftc.t05_ok = o.ok and ftc.t05_kr not in ('20', '21', '22', '30', '50') and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapispa fzp on fzp.p01_kr = o.kr and fzp.p01_ok = o.ok and fzp.p01_kr not in ('20', '21', '22', '30', '50') and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapispa fzp on fzp.p15_kr = o.kr and fzp.p15_ok = o.ok and fzp.p15_kr not in ('20', '21', '22', '30', '50') and o.ok <> '00'

union distinct

select o.kr as kr, o.ok as ok, o.nazev as nazev
from okresy o
inner join ks_zapisp28 fzp28 on fzp28.r05_kr = o.kr and fzp28.r05_ok = o.ok and fzp28.r05_kr not in ('20', '21', '22', '30', '50') and o.ok <> '00';

-- chybejici okresy - mimo ciziny
insert into chybejiciOkresy(kr, ok)
	(select
        ftc.t01_kr as kr, ftc.t01_ok as ok
    from
        ks_zapistc ftc
    where
        not exists(select 1 from okresy o where o.kr = ftc.t01_kr and o.ok = ftc.t01_ok)
		and ftc.t01_kr not in('20', '21', '22', '30', '50') and ftc.t01_ok <> '00')

	union distinct

	(select
        ftc.t05_kr as kr, ftc.t05_ok as ok
    from
        ks_zapistc ftc
    where
        not exists(select 1 from okresy o where o.kr = ftc.t05_kr and o.ok = ftc.t05_ok)
		and ftc.t05_kr not in('20', '21', '22', '30', '50') and ftc.t05_ok <> '00')

	union distinct
    
	(select
        fzp.p01_kr as kr, fzp.p01_ok as ok
    from
        ks_zapispa fzp
    where
        not exists(select 1 from okresy o where o.kr = fzp.p01_kr and o.ok = fzp.p01_ok)
		and fzp.p01_kr not in('20', '21', '22', '30', '50') and fzp.p01_ok <> '00');

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
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\2009\\okresy\\okresy_actual.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';