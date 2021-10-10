create table donation_detail
(
    id int auto_increment,
    markaz_id int null,
    santri_id int null,
    description varchar(2048) not null,
    nominal bigint not null,
    contact_person varchar(2048) null,
    is_active boolean default true not null,
    created_by varchar(512) not null,
    created_at timestamp not null default current_timestamp(),
    constraint donation_detail_pk
        primary key (id),
    constraint donation_detail_markaz_data_id_fk
        foreign key (markaz_id) references markaz_data (id),
    constraint donation_detail_santri_data_id_fk
        foreign key (santri_id) references santri_data (id)
);

create table user_donation
(
    id int auto_increment,
    trx_id varchar(32) not null,
    donation_id int not null,
    user_id int not null,
    donation_url varchar(2048) not null,
    amount bigint not null,
    status varchar(128) not null,
    reason varchar(2048) null,
    created_at timestamp default current_timestamp() not null,
    created_by varchar(512) not null,
    updated_at timestamp null,
    updated_by varchar(512) null,
    constraint user_donation_pk
        primary key (id),
    constraint user_donation_auth_user_id_fk
        foreign key (user_id) references auth_user (id),
    constraint user_donation_donation_detail_id_fk
        foreign key (donation_id) references donation_detail (id)
);

create unique index user_donation_trx_id_uindex
    on user_donation (trx_id);

