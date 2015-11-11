use $rokku;

-- tabulka okresu
CREATE TABLE `okresy_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) DEFAULT CHARSET=utf8;

-- tabulka kodu jiz zavedenych utvaru
CREATE TABLE `utvary_actual` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL
) DEFAULT CHARSET=utf8;

-- tabulka utvaru linkovanych na okresy
CREATE TABLE `utvary_toOkresy` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) DEFAULT CHARSET=utf8;

-- tabulka utvaru linkovanych na kraje
CREATE TABLE `utvary_toKraje` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) DEFAULT CHARSET=utf8;

-- tabulka utvaru linkovanych na centralni utvary
CREATE TABLE `utvary_toCentralniUtvary` (
  `kr` varchar(2) NOT NULL,
  `ok` varchar(2) NOT NULL,
  `ut` varchar(2) NOT NULL,
  `nazev` varchar(200) DEFAULT NULL
) DEFAULT CHARSET=utf8;

-- pomocna tabulka s kody chybejicich utvaru urciteho druhu
create table chybejiciUtvary(
	kr varchar(2) not null,
	ok varchar(2) not null,
	ut varchar(2) not null
) DEFAULT CHARSET=utf8;

-- pomocna tabulka chybejicich okresu
create table chybejiciOkresy(
	kr varchar(2) not null,
	ok varchar(2) not null
) DEFAULT CHARSET=utf8;

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
 cho.kr, cho.ok, IFNULL(oa.nazev, CONCAT(k.nazev, ' - neznámý ', cho.kr, cho.ok))
from
	chybejiciOkresy cho inner join kraje k on k.kr = cho.kr
	left outer join crimestatistics.okresy_actual oa on oa.kr = cho.kr and oa.ok = cho.ok;

-- V tomto bode se vygeneruje obsah souboru 'okresy_actual.csv'.
-- Pomoci externi utility se vygeneruje obsah souboru 'utvary_actual'. Tento se nahraje do tabulky 'utvary_actual'.
select
	oa.kr, oa.ok, '', oa.nazev, 1, ''
from okresy_actual oa
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\okresy\\okresy_actual.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';
-- V tomto miste se pripadne upravi nazvy dosud neznamych okresu
truncate table okresy_actual;
-- Zde se nahraji okresy s doplnenymi jmeny
load data local infile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\okresy\\okresy_actual.csv'
replace
into table okresy_actual
character set utf8
fields terminated by '#'
lines terminated by '\n'
(kr, ok, @ut, nazev, @doLink, @linkTo);

-- Nyni jiz zadne chzbejici okresy nejsou.
truncate table chybejiciOkresy;

-- Zde se nahraji data ze souboru s utvary, ktery je vygenerovan, do tabulky 'utvary_actual'.
load data local infile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\utvary\\utvary_actual.csv'
replace
into table utvary_toOkresy
character set utf8
fields terminated by '#'
lines terminated by '\n'
(kr, ok, ut, nazev, @doLink, @linkTo);
-- zaznamenej tzto utvary jako jiz zavedene
insert into utvary_actual(kr, ok, ut)
select uto.kr as kr, uto.ok as ok, uto.ut as ut
from utvary_toOkresy uto;

-- Zde se vygeneruje seznam chybejicich utvaru, ktere by jinak spadaly pod okresy.
insert into chybejiciUtvary(kr, ok, ut)
select
	ftc.t01_kr as kr, ftc.t01_ok as ok, ftc.t01_ut as ut
from
	ks_zapistc ftc
where
	ftc.t01_kr not in('20', '21', '22', '30', '50') and ftc.t01_ok <> '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select
	ftc.t05_kr as kr, ftc.t05_ok as ok, ftc.t05_ut as ut
from
	ks_zapistc ftc
where
	ftc.t05_kr not in('20', '21', '22', '30', '50') and ftc.t05_ok <> '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select
	fzp.p01_kr as kr, fzp.p01_ok as ok, fzp.p01_ut as ut
from
	ks_zapispa fzp
where
	fzp.p01_kr not in('20', '21', '22', '30', '50') and fzp.p01_ok <> '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);

-- Zde vlozim do tabulky 'utvary_actual' dosud chybejici utvary.
insert into utvary_toOkresy(kr, ok, ut, nazev)
select
	chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(oa.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu inner join okresy_actual oa on oa.KR = chu.kr and oa.OK = chu.ok
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Pridej tyto utvary i do aktualnich utvaru.
insert into utvary_actual(kr, ok, ut)
select chu.kr as kr, chu.ok as ok, chu.ut as ut
from chybejiciUtvary chu;

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
	ftc.t01_kr not in('20', '21', '22', '30', '50') and ftc.t01_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select
	ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	ftc.t05_kr not in('20', '21', '22', '30', '50') and ftc.t05_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select
	fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	fzp.p01_kr not in('20', '21', '22', '30', '50') and fzp.p01_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);

-- Do tabulky utvaru linkovanych primo na kraje dopln nalezene utvary.
insert into utvary_toKraje(kr, ok, ut, nazev)
select
	chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(k.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu inner join kraje k on k.kr = chu.kr
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Dopln tyto utvary prislusne pod kraj do seznamu aktualnich utvaru.
insert into utvary_actual(kr, ok, ut)
select uto.kr as kr, uto.ok as ok, uto.ut as ut
from  utvary_toKraje uto;

-- Zde si selectni vsechny utvary prislusne pod konkretni centralni utvary mimo obecnych utvaru pod '2000'.
truncate table chybejiciUtvary;
insert into chybejiciUtvary(kr, ok, ut)
select
	ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from
	ks_zapistc ftc
where
	((ftc.t01_kr = '20' and ftc.t01_ok <> '00') or ftc.t01_kr = '21')
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select
	ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	((ftc.t05_kr = '20' and ftc.t05_ok <> '00') or ftc.t05_kr = '21')
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select
	fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	((fzp.p01_kr = '20' and fzp.p01_ok <> '00') or fzp.p01_kr = '21')
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);

-- Do tabulky utvaru linkovanych na centralni utvary dopln nalezene utvary.
insert into utvary_toCentralniUtvary(kr, ok, ut, nazev)
select
	chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(cua.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu
	inner join crimestatistics.centralni_utvary_actual cua on cua.kr = chu.kr and cua.ok = chu.ok
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Dopln centralni utvary do seznamu aktualnich utvaru.
insert into utvary_actual(kr, ok, ut)
select
	utcu.kr as kr, utcu.ok as ok, utcu.ut as ut
from utvary_toCentralniUtvary utcu;

-- Zde si pro kontrolu selectni chybejici utvary
select
	ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from
	ks_zapistc ftc
where
	ftc.t01_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select
	ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	ftc.t05_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select
	fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	fzp.p01_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);

-- Vypis vysledku
-- Zapis do souboru kraje.
select
	k.kr, '00', '', k.nazev, '0', ''
from kraje k
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\kraje\\kraje_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Zapis do souboru centralni utvary.
select
	cua.kr, cua.ok, '', cua.nazev, 0, ''
from crimestatistics.centralni_utvary_actual cua
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\centralni_utvary_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Zapis do souboru okresy.
select
	oa.kr, oa.ok, '', oa.nazev, 1, ''
from okresy_actual oa
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\okresy\\okresy_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru zapis utvary linkovane primo na okresy.
select
	uto.kr, uto.ok, uto.ut, uto.nazev, 1, ''
from utvary_toOkresy uto
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\utvary\\utvary_toOkresy_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru nyni zapis utvary linkovane primo na kraje.
select
	utk.kr, utk.ok, utk.ut, utk.nazev, 1, CONCAT('00', utk.kr)
from utvary_toKraje utk
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\utvary\\utvary_toKraje.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru nyni zapis utvary linkovane primo na kraje.
select
	utcu.kr, utcu.ok, utcu.ut, utcu.nazev, 1, ''
from utvary_toCentralniUtvary utcu
into outfile 'C:\\Documents and Settings\\Tom\\Plocha\\bcPrace\\new\\ontology\\codelists\\final\\data\\utvar\\$rok\\utvary\\utvary_toCentralniUtvary.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';