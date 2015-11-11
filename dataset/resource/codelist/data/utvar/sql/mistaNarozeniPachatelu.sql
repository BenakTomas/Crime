use 2012ku;

select
    *
from
    ks_zapispa fzp
where
    fzp.p04narod is null or fzp.p04narod = '';

-- statni prislusnosti "obcanu CR"
select
    distinct(fzp.p03_sta)
from
    ks_zapispa fzp
where
    fzp.p04narod = '0';

-- mista pobytu "obcanu CR" dle statni prislusnosti
select
    fzp.p15_kr, fzp.p15_ok, fzp.p15_mist
from
    ks_zapispa fzp
where
    fzp.p04narod = '0' and fzp.p03_sta = '658'
order by
	fzp.p15_kr, fzp.p15_ok;

-- statni prislusnosti "ne-obcanu CR"
select
    distinct(fzp.p03_sta)
from
    ks_zapispa fzp
where
    fzp.p04narod <> '0';

-- mista pobytu "obcanu CR" dle statni prislusnosti
select
    fzp.p15_kr, fzp.p15_ok, fzp.p15_mist
from
    ks_zapispa fzp
where
    fzp.p04narod <> '0' and fzp.p03_sta = '104'
order by
	fzp.p15_kr, fzp.p15_ok;


