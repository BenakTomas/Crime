select 
    *
from
    (select 
        o.kr as kr, o.ok as ok, o.Nazev as nazev
    from
        okresy o
    left outer join ks_zapistc ftc ON (ftc.t01_kr = o.kr and ftc.t01_ok = o.ok)
        or (ftc.t05_kr = o.kr and ftc.t05_ok = o.ok)
    where
        ftc.id_tc is null) as ftc_zprac

	inner join
    
	(select 
        o.kr as kr, o.ok as ok, o.Nazev as nazev
    from
        okresy o
    left outer join ks_zapispa fzp ON (fzp.p01_kr = o.kr and fzp.p01_ok = o.ok)
    where
        fzp.id_pa is null) as fzp_zprac ON fzp_zprac.kr = ftc_zprac.kr
        and fzp_zprac.ok = ftc_zprac.ok
        
	inner join
	
	(select 
        o.kr as kr, o.ok as ok, o.Nazev as nazev
    from
        okresy o
    left outer join ks_zapispa fzp ON (fzp.p15_kr = o.kr and fzp.p15_ok = o.ok)
    where
        fzp.id_pa is null) as fzp_mistoNarozeni ON fzp_mistoNarozeni.kr = fzp_zprac.kr
        and fzp_mistoNarozeni.ok = fzp_zprac.ok
        
	inner join
    
	(select 
        o.kr as kr, o.ok as ok, o.Nazev as nazev
    from
        okresy o
    left outer join ks_zapisp28 fzp28 ON (fzp28.r05_kr = o.kr
        and fzp28.r05_ok = o.ok)
    where
        fzp28.ptr_pa is null) as fzp28_bydliste ON fzp28_bydliste.kr = fzp_mistoNarozeni.kr
        and fzp28_bydliste.ok = fzp_mistoNarozeni.ok