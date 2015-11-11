use crimestatistics;

alter table utvary_linking
drop column uzemi;

alter table utvary_linking
drop column sm;

select ul.kr, ul.ok, ul.ut, ul.nazev from utvary_linking ul
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/linking/utvar/utvary_linking.txt'
character set utf8
fields terminated by '#'
lines terminated by '/n';

select * from utvary_linking_jednoducheObce;

insert into utvary_linking_jednoducheObce(kr, ok, ut, nazev)
select ul.kr as kr, ul.ok as ok, ul.ut as ut, ul.nazev as nazev
from
	utvary_linking ul
where
	ul.nazev like 'OOP%'
	and
	(ul.nazev not like '%-%'
	 and ul.nazev not rlike '[0-9]+'
	 and ul.nazev not rlike 'I(I*|V)\\.+'
	);

select * from obce;
select * from okresy;

truncate table okresy;
load data infile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/linking/utvar/aktualniOkresy.csv'
into table okresy
character set utf8
fields enclosed by '"' terminated by ','
lines terminated by '\n'
(nazev, uri);

update obce o
inner join obce2okresymapping oom
on oom.obecURI = o.uri
inner join okresy ok on ok.uri = oom.okresURI
set o.okres = ok.kod;

select * from okresy_linking ol order by ol.kr, ol.ok;
select * from okresy_actual;
select * from 2008ku.okresy o order by o.kr, o.ok;

truncate table okresy_linking;
load data infile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/linking/utvar/okresyDB.csv'
into table okresy_linking
character set utf8
fields terminated by '#'
lines terminated by '\n'
(kr, ok, nazev);

select * from obce o where o.okres is null or o.okres = 0 or not exists(select 1 from okresy ok where ok.kod = o.okres);
select * from utvary_linking_jednoducheObce ujo where not exists(select 1 from utvary_keywords_jednoducheObce ukjo where ukjo.kr = ujo.kr and ukjo.ok = ujo.ok and ukjo.ut = ujo.ut);
select * from utvary_linking_jednoducheObce ujo where not exists(select 1 from okresy_linking ol where ol.kr = ujo.kr and ol.ok = ujo.ok) order by ujo.kr, ujo.ok;

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

truncate table utvary_linked_jednoducheObce;
insert into utvary_linked_jednoducheObce(kr, ok, ut, obec)
select
	ul.kr as kr, ul.ok as ok, ul.ut as ut, o.kod as obec
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

select uljo.kr, uljo.ok, uljo.ut
from
	utvary_linking_jednoducheObce uljo
group by uljo.kr, uljo.ok, uljo.ut
having count(*) > 1;

select
	ul.kr, ul.ok, ul.ut, ul.nazev as nazevUtvaru, o.nazev as nazevObce, o.uri, ok.nazev
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
	inner join okresy ok
	on
		ok.kod = o.okres

where ul.kr = '01' and ul.ok = '14';

select
	ul.kr, ul.ok, ul.ut, ul.nazev as nazevUtvaru, o.nazev as nazevObce, o.uri, ok.nazev
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
	inner join okresy ok
	on
		ok.kod = o.okres

where ul.kr = '01' and ul.ok = '15';

select
	ul.kr, ul.ok, ul.ut, ul.nazev as nazevUtvaru, o.nazev as nazevObce, o.uri, ok.nazev
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
	inner join okresy ok
	on
		ok.kod = o.okres

where ul.kr = '01' and ul.ok = '16';

select
	ul.kr, ul.ok, ul.ut, ul.nazev as nazevUtvaru, o.nazev as nazevObce, o.uri, ok.nazev
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
	inner join okresy ok
	on
		ok.kod = o.okres

where ul.kr = '03' and ul.ok = '11';

select
	ul.kr, ul.ok, ul.ut, ul.nazev as nazevUtvaru, o.nazev as nazevObce, o.uri, ok.nazev
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
	inner join okresy ok
	on
		ok.kod = o.okres

where ul.kr = '06' and ul.ok = '18';

select
	CONCAT('crimestatistics-code-codelist-hash:', uljo.kr, uljo.ok, uljo.ut, ' crimestatistics-property:obec <', o.uri, '> .')
from
	utvary_linked_jednoducheObce uljo
	inner join obce o on o.kod = uljo.obec
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/linking/utvar/utvary_linked.ttl'
charset utf8
lines terminated by '\n';

select * from obce where nazev like '%holice%';
select * from utvary_actual ul where ul.kr = '01' and ul.ok = '15' and ul.ut = '10';

select * from
utvary_linking_jednoducheObce uljo
where not exists(select 1 from utvary_linked_jednoducheObce ul where ul.kr = uljo.kr and ul.ok = uljo.ok and ul.ut = uljo.ut);

load data infile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/linking/utvar/utvary_manually_linked.txt'
into table utvary_linked_jednoducheObce
charset utf8
fields terminated by '#'
lines terminated by '\n'
(kr, ok, ut, @obec)
set obec = (select o.kod from obce o where o.uri = @obec);

select o.nazev, o.uri, ok.nazev, ok.kod from obce o inner join okresy ok on ok.kod = o.okres and o.nazev rlike 'kostelec';
select * from castiObci co inner join obce o on co.obec = o.kod inner join okresy ok on ok.kod = o.okres
and co.nazev rlike 'hoděj';


select * from utvary_keywords_jednoducheObce ukjo where ukjo.keyword rlike 'holice';
select * from utvary_keywords_jednoducheObce ukjo where ukjo.kr = '03' and ukjo.ok = '07' and ukjo.ut = '10';

create table ulice2obcemapping(
	nazevUlice varchar(200) not null,
	uliceuri varchar(200) not null,
	obecuri varchar(200) not null
);

load data infile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/linking/utvar/ulice.csv'
into table ulice2obcemapping
charset utf8
fields enclosed by '"' terminated by ','
lines terminated by '\n'
(nazevUlice, uliceuri, obecuri);

create table ulice(
	kod int auto_increment primary key,
	nazev varchar(200) not null,
	uri varchar(200) not null,
	obec int references obec(kod)
);

create index IX_ulice2obcemapping_uliceuri_hash using hash
on ulice2obcemapping(uliceuri);

create index IX_ulice2obcemapping_obecuri_hash using hash
on ulice2obcemapping(obecuri);

truncate table ulice;
insert into ulice(nazev, uri)
select um.nazevUlice as nazev, um.uliceuri as uri
from ulice2obcemapping um;

select * from ulice2obcemapping;
select * from ulice u inner join obce o on o.kod = u.obec inner join okresy ok on ok.kod = o.okres where ok.kod = 13;
select * from obce o where o.kod = 3695;


select * from ulice u where not exists(select 1 from obce o where o.kod = u.obec);


update ulice u
inner join ulice2obcemapping um on um.uliceuri = u.uri
inner join obce o on o.uri = um.obecuri
set obec = o.kod;

select ok.* from okresy ok where ok.nazev rlike 'sokolov';
select * from obce o where o.okres = 18 and o.nazev rlike 'loket';

select u.*
from ulice u
where
	u.obec = 2763
	and u.nazev rlike 'Zámecká';