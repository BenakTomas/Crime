-- Vypis vysledku
-- Zapis do souboru kraje.
select
	k.kod, '00', '', k.kraje, '0', ''
from kraje k
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/2008/kraje/kraje_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Zapis do souboru centralni utvary.
select
	cua.kr, cua.ok, '', cua.nazev, 0, ''
from crimestatistics.centralni_utvary_actual cua
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/2008/centralni_utvary_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Zapis do souboru okresy.
select
	oa.kr, oa.ok, '', oa.nazev, 1, ''
from okresy_actual oa
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/2008/okresy/okresy_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru zapis utvary linkovane primo na okresy.
select
	uto.kr, uto.ok, uto.ut, uto.nazev, 1, ''
from utvary_toOkresy uto
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/2008/utvary/utvary_toOkresy_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru nyni zapis utvary linkovane primo na kraje.
select
	utk.kr, utk.ok, utk.ut, utk.nazev, 1, CONCAT('00', utk.kr)
from utvary_toKraje utk
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/2008/utvary/utvary_toKraje_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru nyni zapis utvary linkovane na centralni utvary.
select
	utcu.kr, utcu.ok, utcu.ut, utcu.nazev, 1, ''
from utvary_toCentralniUtvary utcu
into outfile 'C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/2008/utvary/utvary_toCentralniUtvary_final.csv'
character set utf8
fields terminated by '#'
lines terminated by '\n';