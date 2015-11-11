create index IX_okresy_actual_crimestatistics_kr_ok_hash using hash
on crimestatistics.okresy_actual(kr, ok);

create index IX_utvary_actual_crimestatistics_kr_ok_ut_hash using hash
on crimestatistics.utvary_actual(kr, ok, ut);