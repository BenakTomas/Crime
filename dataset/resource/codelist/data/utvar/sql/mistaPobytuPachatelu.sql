use 2008ku;

-- mista pobytu "obcanu CR" dle statni prislusnosti
select
    fzp.p15_kr, fzp.p15_ok, fzp.p15_mist, fzp28.r05_kr, fzp28.r05_ok
from
    ks_zapispa fzp inner join ks_zapisp28 fzp28 on fzp28.ptr_pa = fzp.id_pa
where
    fzp.p04narod = '0' and fzp.p03_sta = ''
order by
	fzp.p15_kr, fzp.p15_ok, fzp28.r05_kr, fzp28.r05_ok;

-- statni prislusnosti "ne-obcanu CR"
select
    distinct(fzp28.p03_sta)
from
    ks_zapisp28 fzp28
where
    fzp28.p04narod <> '0';

-- mista pobytu "ne-obcanu CR" dle statni prislusnosti
select
    fzp.p15_kr, fzp.p15_ok, fzp.p15_mist, fzp28.r05_kr, fzp28.r05_ok
from
    ks_zapispa fzp inner join ks_zapisp28 fzp28 on fzp28.ptr_pa = fzp.id_pa
where
    fzp.p04narod <> '0' and fzp.p03_sta = '104'
order by
	fzp.p15_kr, fzp.p15_ok, fzp28.r05_kr, fzp28.r05_ok;

select distinct(concat(fzp28.r05_kr, fzp28.r05_ok)) from
ks_zapisp28 fzp28
where substring(fzp28.r05_kr, 1, 1) <> '3';

