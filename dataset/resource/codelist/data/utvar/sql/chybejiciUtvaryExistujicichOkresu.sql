select * from 2008ku.chybejiciUtvary;

select
CONCAT(chu.kr, '#', chu.ok, '#', chu.ut, '#', ua.nazev, '#1#')
from
2008ku.chybejiciUtvary chu inner join crimestatistics.utvary_actual ua
on ua.kr = chu.kr and ua.ok = chu.ok and ua.ut = chu.ut;

delete from 2008ku.chybejiciUtvary
where exists
	(select 1 from crimestatistics.utvary_actual ua
	 where
		ua.kr = 2008ku.chybejiciUtvary.kr
		and ua.ok = 2008ku.chybejiciUtvary.ok
		and ua.ut = 2008ku.chybejiciUtvary.ut);

select
CONCAT(chu.kr, '#', chu.ok, '#', chu.ut, '#', o.nazev, ' - neznámý ', chu.kr, chu.ok, chu.ut, '#1#')
from
2008ku.chybejiciUtvary chu
inner join 2008ku.okresy o
on o.KR = chu.kr and o.OK = chu.ok;

select *
