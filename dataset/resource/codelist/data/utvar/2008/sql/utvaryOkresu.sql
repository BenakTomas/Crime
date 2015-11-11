set @kr = '00';
set @ok = '00';


(select ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from 2008ku.ks_zapistc ftc
where ftc.t01_kr = @kr and ftc.t01_ok = @ok)

union distinct

(select ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from 2008ku.ks_zapistc ftc
where ftc.t05_kr = @kr and ftc.t05_ok = @ok)

union distinct

(select fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from 2008ku.ks_zapispa fzp
where fzp.p01_kr = @kr and fzp.p01_ok = @ok)