use 2008ku;

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
-- Vytvor indexy pro vyhledavan
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