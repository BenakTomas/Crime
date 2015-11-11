-- This table is used to hold the information about the
-- country-level departments.
-- These departments operate on the territory of the whole Czech
-- republic.

create table crimestatistics.centralni_utvary_actual(
	kr varchar(2) not null,
	ok varchar(2) not null,
	nazev varchar(200) null
) DEFAULT CHARSET=utf8;

truncate table crimestatistics.centralni_utvary_actual;

-- Fill in the departments.
insert into crimestatistics.centralni_utvary_actual(kr, ok, nazev)
values('00', '90', 'CENTRÁLNÍ ÚTVARY');

insert into crimestatistics.centralni_utvary_actual(kr, ok, nazev)
values('20', '01', 'PP ČR ÚSKPV');

insert into crimestatistics.centralni_utvary_actual(kr, ok, nazev)
values('20', '02', 'PČR ÚOKFK SKPV');

insert into crimestatistics.centralni_utvary_actual(kr, ok, nazev)
values('20', '03', 'PČR NPC SKPV');

insert into crimestatistics.centralni_utvary_actual(kr, ok, nazev)
values('20', '04', 'PČR ÚOOZ SKPV');

insert into crimestatistics.centralni_utvary_actual(kr, ok, nazev)
values('20', '05', 'PČR ÚDV SKPV');

insert into crimestatistics.centralni_utvary_actual(kr, ok, nazev)
values('21', '00', 'INSPEKCE PČR');

-- Create a hash index on kr, ok fields of the table to
-- ensure good performance for the SQL queries using those fields.
create index IX_centralni_utvary_actual_kr_ok_hash
on crimestatistics.centralni_utvary_actual(kr, ok) using hash;

-- Perform a select to check the inserted records.
select * from crimestatistics.centralni_utvary_actual;

-- Create the indexes.
create index IX_okresy_actual_crimestatistics_kr_ok_hash using hash
on crimestatistics.okresy_actual(kr, ok);

create index IX_utvary_actual_crimestatistics_kr_ok_ut_hash using hash
on crimestatistics.utvary_actual(kr, ok, ut);

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