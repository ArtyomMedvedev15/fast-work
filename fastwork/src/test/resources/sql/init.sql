create table location
(
    id                   bigint not null
        primary key,
    location_city        varchar(255),
    location_country     varchar(255),
    location_date_create timestamp(6),
    x                    numeric(38, 2),
    y                    numeric(38, 2),
    location_region      varchar(255),
    location_street      varchar(255)
);


create table work
(
    id                bigint not null
        primary key,
    work_count_person integer,
    work_date_create  timestamp(6),
    work_describe     varchar(255),
    work_name         varchar(255),
    work_price        real,
    work_status       varchar(255),
    work_hirer_id     bigint
        constraint fkqq6t8b5ops7owcedffardp1hu
            references users,
    work_location_id  bigint
        constraint fkp2ow5kam31f0h427dsg7gyc6h
            references location
);


create table users_user_works
(
    users_id      bigint not null
        constraint fk54to0e288s9ash1g6bys033f8
            references users,
    user_works_id bigint not null
        constraint uk_a1so3r8f3mdn6o9odnmnr9hht
            unique
        constraint fk80n7k0rfmkhradead21l8jmpm
            references work
);

insert into users(id, user_date_create, user_email, user_name, user_password, user_role, user_soname, user_status)values(777,'2023-04-02','user@mail.text','test','test','WORKER','test','ACTIVE');
insert into location(id,location_city)values(777,'test');
insert into work(id,work_name)values (777,'test');