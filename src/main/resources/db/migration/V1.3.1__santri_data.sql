create table santri_data
(
    id int auto_increment,
    markaz_id int not null,
    name varchar(256) not null,
    slug varchar(256) not null,
    thumbnail_url varchar(2048) not null,
    background varchar(2048) not null,
    address varchar(2048) not null,
    gender varchar(64) not null,
    birth_place varchar(128) not null,
    birth_date date not null,
    is_active boolean not null default true,
    constraint santri_data_pk
        primary key (id),
    constraint santri_data_name_uindex
        unique (name),
    constraint santri_data_slug_uindex
        unique (slug),
    constraint santri_data_markaz_data_id_fk
        foreign key (markaz_id) references markaz_data (id)
);

create index santri_data_birth_date_index
    on santri_data (birth_date);