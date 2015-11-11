insert into okresy_actual(kr, ok, nazev)
select o.kr, o.ok, o.nazev
from okresy o
inner join ks_zapistc ftc on ftc.t01_kr = o.kr and ftc.t01_ok = o.ok

union distinct

select o.kr, o.ok, o.nazev
from okresy o
inner join ks_zapistc ftc on ftc.t05_kr = o.kr and ftc.t05_ok = o.ok

union distinct

select o.kr, o.ok, o.nazev
from okresy o
inner join ks_zapispa fzp on fzp.p01_kr = o.kr and fzp.p01_ok = o.ok

union distinct

select o.kr, o.ok, o.nazev
from okresy o
inner join ks_zapispa fzp on fzp.p15_kr = o.kr and fzp.p15_ok = o.ok

union distinct

select o.kr, o.ok, o.nazev
from okresy o
inner join ks_zapisp28 fzp28 on fzp28.r05_kr = o.kr and fzp28.r05_ok = o.ok;