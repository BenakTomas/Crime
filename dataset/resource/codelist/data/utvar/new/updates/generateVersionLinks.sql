select
CONCAT(
'<http://linked.opendata.cz/resource/domain/crimestatistics/dataset/trestneCiny/2011/',
ftc2011.t01_kr, '/',
ftc2011.t01_ok, '/',
ftc2011.t01_ut, '/',
ftc2011.t01_cvs, '/',
ftc2011.t01_rok, '/',
ftc2011.t01_pc1, '>',
' crime-property:hasUpdatedVersion ',
'<http://linked.opendata.cz/resource/domain/crimestatistics/dataset/trestneCiny/2012/',
ftc2012.t01_kr, '/',
ftc2012.t01_ok, '/',
ftc2012.t01_ut, '/',
ftc2012.t01_cvs, '/',
ftc2012.t01_rok, '/',
ftc2012.t01_pc1, '>',
' .')
from 2011ku.ks_zapistc ftc2011
inner join 2012ku.ks_zapistc ftc2012
on ftc2012.id_tc = ftc2011.id_tc