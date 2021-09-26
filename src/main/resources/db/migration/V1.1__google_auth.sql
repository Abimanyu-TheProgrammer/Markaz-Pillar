create table google_token
(
    id int auto_increment,
    state varchar(30) null,
    user_id int null,
    access_token varchar(2048) null,
    refresh_token varchar(2048) null,
    id_token varchar(4096) null,
    created_at timestamp default current_timestamp not null,
    expire_at timestamp not null,
    is_active boolean default 1 not null,
    constraint google_token_pk
        primary key (id),
    constraint google_token_google_account_user_id_fk
        foreign key (user_id) references google_token (id)
            on delete cascade
);

create unique index google_token_state_uindex
    on google_token (state);