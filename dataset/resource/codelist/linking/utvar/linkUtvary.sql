use crimestatistics;

select
	ul.kr, ul.ok, ul.ut, ul.nazev as nazevUtvaru, o.nazev as nazevObce, o.uri
from
	utvary_linking_jednoducheObce ul
	inner join obce o
	on
		(select
			count(*)
		 from
			utvary_keywords_jednoducheObce ukjo
		where
			ukjo.kr = ul.kr and ukjo.ok = ul.ok and ukjo.ut = ul.ut
			and o.nazev rlike CONCAT('[[:<:]]', ukjo.keyword, '[[:>:]]'))
				
		=
		(select
			count(*)
		 from
			utvary_keywords_jednoducheObce ukjo
		 where
			ukjo.kr = ul.kr and ukjo.ok = ul.ok and ukjo.ut = ul.ut)
	
	inner join okresy ok on ok.kod = o.okres
	inner join okresy_linking ol
	on
		ol.kr = ul.kr and ol.ok = ul.ok
where
	ol.nazev = ok.nazev
order by ul.kr, ul.ok, ul.ut;

select
	ul.kr, ul.ok, ul.ut, ul.nazev as nazevUtvaru, o.nazev as nazevObce, o.uri
from
	utvary_linking_jednoducheObce ul
	inner join obce o
	on
		(select
			count(*)
		 from
			utvary_keywords_jednoducheObce ukjo
		where
			ukjo.kr = ul.kr and ukjo.ok = ul.ok and ukjo.ut = ul.ut
			and o.nazev rlike CONCAT('[[:<:]]', ukjo.keyword, '[[:>:]]'))
				
		=
		(select
			count(*)
		 from
			utvary_keywords_jednoducheObce ukjo
		 where
			ukjo.kr = ul.kr and ukjo.ok = ul.ok and ukjo.ut = ul.ut)
	
	inner join okresy ok on ok.kod = o.okres
	inner join okresy_linking ol
	on
		ol.kr = ul.kr and ol.ok = ul.ok
where
	ol.nazev = ok.nazev;