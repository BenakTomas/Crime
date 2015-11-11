-- Zde si pro kontrolu selectni chybejici utvary
select
	ftc.t01_kr, ftc.t01_ok, ftc.t01_ut
from
	ks_zapistc ftc
where
	ftc.t01_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t01_kr and ua.ok = ftc.t01_ok and ua.ut = ftc.t01_ut)

union distinct

select
	ftc.t05_kr, ftc.t05_ok, ftc.t05_ut
from
	ks_zapistc ftc
where
	ftc.t05_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = ftc.t05_kr and ua.ok = ftc.t05_ok and ua.ut = ftc.t05_ut)

union distinct

select
	fzp.p01_kr, fzp.p01_ok, fzp.p01_ut
from
	ks_zapispa fzp
where
	fzp.p01_kr <> '30'
	and not exists(select 1 from utvary_actual ua where ua.kr = fzp.p01_kr and ua.ok = fzp.p01_ok and ua.ut = fzp.p01_ut);