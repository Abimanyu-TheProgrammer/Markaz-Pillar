create table markaz_data
(
    id int auto_increment
        primary key,
    name varchar(256) not null,
    slug varchar(256) not null,
    category varchar(32) not null,
    background varchar(2048) not null,
    thumbnail_url varchar(2048) not null,
    address varchar(2048) not null,
    is_active boolean not null default true,
    constraint markaz_data_name_uindex
        unique (name),
    constraint markaz_data_slug_uindex
        unique (slug)
);

create index markaz_data_category_index
    on markaz_data (category);