create table user_role
(
    id int auto_increment,
    name varchar(128) not null,
    constraint user_role_pk
        primary key (id)
);

create unique index user_role_name_uindex
    on user_role (name);

create table user_menu
(
    id int auto_increment,
    name varchar(128) not null,
    constraint user_menu_pk
        primary key (id)
);

create unique index user_menu_name_uindex
    on user_menu (name);

create table user_role_mapping
(
    id int auto_increment,
    role_id int not null,
    menu_id int not null,
    constraint user_role_mapping_pk
        primary key (id),
    constraint user_role_mapping_user_menu_id_fk
        foreign key (menu_id) references user_menu (id)
            on delete cascade,
    constraint user_role_mapping_user_role_id_fk
        foreign key (role_id) references user_role (id)
            on delete cascade
);

create table auth_user
(
    id int auto_increment,
    role_id int not null,
    username varchar(64) not null,
    email varchar(512) not null,
    password varchar(512) not null,
    full_name varchar(512) not null,
    profile_url varchar(2048) null,
    description varchar(2048) null,
    phone_num varchar(30) not null,
    address varchar(4096) not null,
    created_at timestamp default current_timestamp() not null,
    updated_at timestamp default current_timestamp() not null,
    is_active boolean default true not null,
    constraint auth_user_pk
        primary key (id),
    constraint auth_user_user_role_id_fk
        foreign key (role_id) references user_role (id)
            on delete cascade
);

create unique index auth_user_username_email_uindex
    on auth_user (username, email);

create table auth_refresh
(
    id int auto_increment,
    refresh_token varchar(512) not null,
    email varchar(64) null,
    salt varchar(64) not null,
    created_at timestamp default current_timestamp() not null,
    expire_at timestamp not null,
    is_valid boolean default true not null,
    constraint auth_refresh_pk
        primary key (id)
);

create unique index auth_refresh_refresh_token_uindex
    on auth_refresh (refresh_token);



