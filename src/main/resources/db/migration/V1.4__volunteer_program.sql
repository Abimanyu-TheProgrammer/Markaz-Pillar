create table volunteer_program
(
    id               int auto_increment,
    name             varchar(256)  not null,
    slug             varchar(256)  not null,
    thumbnail_url    varchar(2048) not null,
    description      varchar(2048) not null,
    term             varchar(2048) not null,
    benefit          varchar(2048) not null,
    volunteer_needed int           not null,
    location         varchar(2048) not null,
    schedule         varchar(2048) not null,
    created_at       timestamp     not null default current_timestamp(),
    created_by       varchar(512)  not null,
    is_active        boolean       not null default true,
    constraint volunteer_program_pk
        primary key (id)
);

create unique index volunteer_program_name_uindex
    on volunteer_program (name);

create unique index volunteer_program_slug_uindex
    on volunteer_program (slug);

create table volunteer_testimony
(
    id            int auto_increment,
    program_id    int           not null,
    thumbnail_url varchar(2048) not null,
    name          varchar(256)  not null,
    description   varchar(2048) not null,
    constraint volunteer_testimony_pk
        primary key (id),
    constraint volunteer_testimony_volunteer_program_id_fk
        foreign key (program_id) references volunteer_program (id)
            on delete cascade
);

create table volunteer_registration
(
    id          int auto_increment,
    program_id  int                                   not null,
    name        varchar(256)                          not null,
    ktp         varchar(30)                           not null,
    phone_num   varchar(30)                           not null,
    email       varchar(512)                          not null,
    address     varchar(2048)                         not null,
    picture_url varchar(2048)                         not null,
    essay_url   varchar(2048)                         not null,
    cv_url      varchar(2048)                         not null,
    status      varchar(128)                          not null,
    reason      varchar(2048)                         null,
    created_at  timestamp default current_timestamp() not null,
    created_by  varchar(512)                          not null,
    updated_at  timestamp                             null,
    updated_by  varchar(512)                          null,
    constraint volunteer_registration_pk
        primary key (id),
    constraint volunteer_registration_volunteer_program_id_fk
        foreign key (program_id) references volunteer_program (id)
        on delete cascade
);

create index volunteer_registration_name_index
    on volunteer_registration (name);

create index volunteer_registration_status_index
    on volunteer_registration (status);

