create index IX_ks_zapistc_kr_ok_ut using btree
on ks_zapistc(t01_kr, t01_ok, t01_ut);

create index IX_ks_zapistc_kr5_ok5_ut5 using btree
on ks_zapistc(t05_kr, t05_ok, t05_ut);

create index IX_ks_zapispa_kr_ok_ut using btree
on ks_zapispa(p01_kr, p01_ok, p01_ut);

create index IX_ks_zapispa_kr15_ok15 using btree
on ks_zapispa(p15_kr, p15_ok);

create index IX_ks_zapistc_idtc using btree
on ks_zapistc(id_tc);

create index IX_ks_zapispa_idpa using btree
on ks_zapispa(id_pa);

create index IX_ks_zapisp28_ptr_tc using btree
on ks_zapisp28(ptr_tc);

create index IX_ks_zapisp28_ptr_pa using btree
on ks_zapisp28(ptr_pa);

create index IX_ks_zapisp28_kr5_ok5 using btree
on ks_zapisp28(r05_kr, r05_ok);

create index IX_kraje_kr
on kraje(kod) using hash;

create index IX_kraje_kr
on kraje(kr) using hash;

create index IX_okresy_kr_ok using btree
on okresy(kr, ok);

create index IX_okresy_actual_kr_ok using hash
on okresy_actual(kr, ok);

create index IX_utvary_actual_kr_ok_ut using hash
on utvary_actual(kr, ok, ut);