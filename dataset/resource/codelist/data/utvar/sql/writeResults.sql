-- Vypis vysledku
-- Zapis do souboru kraje.
select sql_cache k.kr, '00', '', k.nazev, '0', ''
from kraje k
into outfile '$krajeFinalPath'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Zapis do souboru centralni utvary.
select sql_cache cua.kr, cua.ok, '', cua.nazev, 0, ''
from crimestatistics.centralni_utvary_actual cua
into outfile '$centralniUtvaryFinalPath'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Zapis do souboru okresy.
select sql_cache oa.kr, oa.ok, '', oa.nazev, 1, ''
from okresy_actual oa
into outfile '$okresyFinalPath'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru zapis utvary linkovane primo na okresy.
select sql_cache uto.kr, uto.ok, uto.ut, uto.nazev, 1, ''
from utvary_toOkresy uto
into outfile '$utvaryToOkresyFinalPath'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru nyni zapis utvary linkovane primo na kraje.
select sql_cache utk.kr, utk.ok, utk.ut, utk.nazev, 1, CONCAT(utk.kr, '00')
from utvary_toKraje utk
into outfile '$utvaryToKrajeFinalPath'
character set utf8
fields terminated by '#'
lines terminated by '\n';

-- Do souboru nyni zapis utvary linkovane na centralni utvary.
select sql_cache utcu.kr, utcu.ok, utcu.ut, utcu.nazev, 1, ''
from utvary_toCentralniUtvary utcu
into outfile '$utvaryToCentralniUtvaryFinalPath'
character set utf8
fields terminated by '#'
lines terminated by '\n';