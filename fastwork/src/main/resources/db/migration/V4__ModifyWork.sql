alter table work
drop constraint fkqq6t8b5ops7owcedffardp1hu;

alter table work
    add constraint fkqq6t8b5ops7owcedffardp1hu
        foreign key (work_hirer_id) references users
            on update cascade on delete cascade;

alter table work
drop constraint fktbqu70by41jstmfbdmgo7a9k3;

alter table work
    add constraint fktbqu70by41jstmfbdmgo7a9k3
        foreign key (work_type_id) references type_work
            on update cascade on delete cascade;
