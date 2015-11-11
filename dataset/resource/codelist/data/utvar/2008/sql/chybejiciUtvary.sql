-- chybejici utvary
    (select
        ftc.t01_kr as kr, ftc.t01_ok as ok, ftc.t01_ut as ut
    from
        2008ku.utvary_actual u
    right outer join 2008ku.ks_zapistc ftc ON (ftc.t01_kr = u.kr and ftc.t01_ok = u.ok and ftc.t01_ut = u.UT)
    where
        u.KR is null)

	union distinct

	(select
         ftc.t05_kr as kr, ftc.t05_ok, ftc.t05_ut as ut
    from
        2008ku.utvary_actual u
    right outer join 2008ku.ks_zapistc ftc ON (ftc.t05_kr = u.kr and ftc.t05_ok = u.ok and ftc.t05_ut = u.UT)
    where
        u.KR is null)

	union distinct
    
	(select 
        fzp.p01_kr as kr, fzp.p01_ok as ok, fzp.p01_ut as ut
    from
        2008ku.utvary_actual u
    right outer join 2008ku.ks_zapispa fzp ON (fzp.p01_kr = u.kr and fzp.p01_ok = u.ok and fzp.p01_ut = u.UT)
    where
        u.KR is null)