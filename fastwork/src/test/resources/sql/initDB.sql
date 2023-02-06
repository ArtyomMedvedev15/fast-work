create table users
(
    id               bigint not null
        primary key,
    user_date_create timestamp(6),
    user_email       varchar(255),
    user_name        varchar(255),
    user_password    varchar(255),
    user_role        varchar(255),
    user_soname      varchar(255),
    user_status      varchar(255)
);
create table type_work
(
    id                    bigint not null
        primary key,
    type_work_date_create timestamp(6),
    type_work_describe    varchar(255),
    type_work_name        varchar(255)
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
    work_type_id      bigint
        constraint fktbqu70by41jstmfbdmgo7a9k3
            references type_work
);

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
    location_street      varchar(255),
    location_work_id     bigint
        constraint fkosww2xgcvjofop85q6i7egdjt
            references work
);