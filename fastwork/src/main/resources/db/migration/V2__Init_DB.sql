create table if not exists users
(
    id                 bigint not null
        primary key,
    user_date_create   timestamp(6),
    user_email         varchar(255),
    user_original_name varchar(255),
    user_password      varchar(255),
    user_role          varchar(255),
    user_soname        varchar(255),
    user_status        varchar(255),
    username           varchar(255)
);
create table if not exists type_work
(
    id                    bigint not null
        primary key,
    type_work_date_create timestamp(6),
    type_work_describe    varchar(255),
    type_work_name        varchar(255)
);
create table if not exists work
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

create table if not exists location
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

create table if not exists work_application
(
    id                      bigint not null
        primary key,
    date_applicaton         timestamp(6),
    work_id                 bigint
        constraint fkl5mdxbig1onhlsw17njkumdk2
            references work
            on delete cascade,
    worker_id               bigint
        constraint fk8joagvc8yvnbrgl2e130wku7x
            references users
            on delete cascade,
    status_work_application varchar(255)
);

create table if not exists users_works
(
    users_id      bigint not null
        constraint fknsd2esyslaw6dgmolmkgrhia
            references users,
    user_works_id bigint not null
        constraint uk_bl2exwwfp6x6kthakr182g6u2
            unique
        constraint fkri6tp7xwm6ef152y02dte5v4d
            references work
);

create table if not exists notification
(
    id                      bigint not null
        primary key,
    date_send_notification  timestamp(6),
    message_notification    varchar(255),
    name_topic_notification varchar(255),
    status_notification     varchar(255)
);

create table if not exists refresh_token
(
    id          bigint                      not null
        primary key,
    expiry_date timestamp(6) with time zone not null,
    token       varchar(255)                not null
        constraint uk_r4k4edos30bx9neoq81mdvwph
            unique,
    user_id     bigint
        constraint fkjtx87i0jvq2svedphegvdwcuy
            references users
);