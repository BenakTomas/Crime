-- chybejici okresy
    (select
        ftc.t01_kr as kr, ftc.t01_ok as ok
    from
        2008ku.okresy o
    right outer join 2008ku.ks_zapistc ftc ON (ftc.t01_kr = o.kr and ftc.t01_ok = o.ok)
    where
        o.KR is null)

	union distinct

	(select
         ftc.t05_kr as kr, ftc.t05_ok
    from
        2008ku.okresy o
    right outer join 2008ku.ks_zapistc ftc ON (ftc.t05_kr = o.kr and ftc.t05_ok = o.ok)
    where
        o.KR is null)

	union distinct
    
	(select 
        fzp.p01_kr as kr, fzp.p01_ok as ok
    from
        2008ku.okresy o
    right outer join 2008ku.ks_zapispa fzp ON (fzp.p01_kr = o.kr and fzp.p01_ok = o.ok)
    where
        o.KR is null)
        
	union distinct
	
	(select 
        fzp.p15_kr as kr, fzp.p15_ok as ok
    from
        2008ku.okresy o
    right outer join 2008ku.ks_zapispa fzp ON (fzp.p15_kr = o.kr and fzp.p15_ok = o.ok)
    where
        o.KR is null)
        
	union distinct
    
	(select 
        fzp28.r05_kr as kr, fzp28.r05_ok as ok
    from
        2008ku.okresy o
    right outer join 2008ku.ks_zapisp28 fzp28 ON (fzp28.r05_kr = o.kr
        and fzp28.r05_ok = o.ok)
    where
        o.KR is null)