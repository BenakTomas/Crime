-- truncate the county table
truncate table okresy_actual;

-- Load the data from the file with county codes and names
-- into the okresy_actual table.
load data local infile '$okresyActualPath'
replace
into table okresy_actual
character set utf8
fields terminated by '#'
lines terminated by '\n'
(kr, ok, @ut, nazev, @doLink, @linkTo);

-- Truncate the table of the missing county departments.
-- It is not needed anymore as the missing county departments
-- had been assigned a name in the county file. (See above)
truncate table chybejiciOkresy;

-- Load the base department file into the 'utvary_toOkresy' table.
-- The file contains the base department names and codes.
-- The loaded base departments are considered departments with
-- a county department as a parent. The parent is assigned automatically
-- based on the base department's code using the 4-character prefix as the
-- parent county department' code.
load data local infile '$utvaryActualPath'
replace
into table utvary_toOkresy
character set utf8
fields terminated by '#'
lines terminated by '\r\n'
(kr, ok, ut, nazev, @doLink, @linkTo);

-- Insert the base departments from the 'utvary_toOkresy' table
-- to the 'utvary_actual' table to mark them as processed.
insert into utvary_actual(kr, ok, ut)
select sql_cache uto.kr as kr, uto.ok as ok, uto.ut as ut
from utvary_toOkresy uto;

-- At this point some base departments are already processed
-- and stored in the 'utvary_actual' table. Those are the departments,
-- for which there was a code-to-name information in some county department file.

-- There may still be county-parented base departments for which there was no such information,
-- base departments under the unknown county departments.

-- Select those missing county-parented departments into the
-- 'chybejiciUtvary' helper table.
insert into chybejiciUtvary(kr, ok, ut)
select sql_cache ftc.t01_kr as kr, ftc.t01_ok as ok, ftc.t01_ut as ut
from
	ks_zapistc ftc
where
	ftc.t01_kr not in('20', '21', '30', '50') and ftc.t01_ok not in ('00', '30', '40')
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select ftc.t05_kr as kr, ftc.t05_ok as ok, ftc.t05_ut as ut
from
	ks_zapistc ftc
where
	ftc.t05_kr not in('20', '21', '30', '50') and ftc.t05_ok not in ('00', '30', '40')
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select fzp.p01_kr as kr, fzp.p01_ok as ok, fzp.p01_ut as ut
from
	ks_zapispa fzp
where
	fzp.p01_kr not in('20', '21', '30', '50') and fzp.p01_ok not in ('00', '30', '40')
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);

-- The selected missing county-parented departments lack a name. This
-- information might be available in the 'crimestatistics.utvary_actual'
-- helper table. If the required information is not found there, a missing
-- department is joined to its county parent and derives its name from here,
-- naming itself "[county department name] - unknown - [code]".
-- The result is stored in the 'utvary_toOkresy' table.
insert into utvary_toOkresy(kr, ok, ut, nazev)
select sql_cache chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(oa.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu inner join okresy_actual oa on oa.KR = chu.kr and oa.OK = chu.ok
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Insert the selected missing county-parented departments
-- into the 'utvary_actual' to mark it processed.
insert into utvary_actual(kr, ok, ut)
select sql_cache chu.kr as kr, chu.ok as ok, chu.ut as ut
from chybejiciUtvary chu;

-- Az se dostanu tady, budu resit zbyle utvary, ktere maji jako kod okresu uvedeno '00', tedy prislusi pod kraj.
-- Truncate the table of missing departments.
truncate table chybejiciUtvary;

-- Select the region-parented base departments into the 'chybejiciUtvary' table.
insert into chybejiciUtvary(kr, ok, ut)
select sql_cache ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from
	ks_zapistc ftc
where
	ftc.t01_kr not in('20', '21', '30', '50') and ftc.t01_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	ftc.t05_kr not in('20', '21', '30', '50') and ftc.t05_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	fzp.p01_kr not in('20', '21', '30', '50') and fzp.p01_ok = '00'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);

-- Do tabulky utvaru linkovanych primo na kraje dopln nalezene utvary.
-- 'utvary_toKraje' table is a table where the region-parented base departments
-- are stored.
-- Like in the case of county-parented base departments, the name information
-- is first searched in a helper 'crimestatistics.utvary_actual', only then
-- the generic name is created using the name of the parent region department and the
-- base departments's code.
insert into utvary_toKraje(kr, ok, ut, nazev)
select sql_cache chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(k.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu inner join kraje k on k.kr = chu.kr
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Mark the region-parented base departments in the 'utvary_toKraje' table
-- processed by inserting them into the 'utvary_actual' table.
insert into utvary_actual(kr, ok, ut)
select sql_cache uto.kr as kr, uto.ok as ok, uto.ut as ut
from  utvary_toKraje uto;

-- Zde si selectni vsechny utvary prislusne pod konkretni centralni utvary mimo obecnych utvaru pod '2000'.
-- Truncate the 'chybejiciUtvary' table.
truncate table chybejiciUtvary;

-- Select the country-level base departments. Filter out the '2000' departments.
insert into chybejiciUtvary(kr, ok, ut)
select sql_cache ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from
	ks_zapistc ftc
where
	((ftc.t01_kr = '20' and ftc.t01_ok <> '00') or ftc.t01_kr = '21')
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	((ftc.t05_kr = '20' and ftc.t05_ok <> '00') or ftc.t05_kr = '21')
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	((fzp.p01_kr = '20' and fzp.p01_ok <> '00') or fzp.p01_kr = '21')
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);

-- Do tabulky utvaru linkovanych na centralni utvary dopln nalezene utvary.
-- Insert the selected country-level base departments into the 'utvary_toCentralniUtvary'
-- table.
-- Once again, the name information is first searched in the helper 'crimestatistics.utvary_actual'
-- table, only then the parent country-level department's name is used to generate the
-- generic name.
insert into utvary_toCentralniUtvary(kr, ok, ut, nazev)
select sql_cache chu.kr, chu.ok, chu.ut, IFNULL(ua.nazev, CONCAT(cua.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut))
from
	chybejiciUtvary chu
	inner join crimestatistics.centralni_utvary_actual cua on cua.kr = chu.kr and cua.ok = chu.ok
	left outer join crimestatistics.utvary_actual ua on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

-- Dopln centralni utvary do seznamu aktualnich utvaru.
-- Mark the selected country-level base departments processed.
insert into utvary_actual(kr, ok, ut)
select sql_cache utcu.kr as kr, utcu.ok as ok, utcu.ut as ut
from utvary_toCentralniUtvary utcu;