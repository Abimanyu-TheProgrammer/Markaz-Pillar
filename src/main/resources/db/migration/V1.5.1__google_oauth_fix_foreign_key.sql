alter table google_token drop foreign key google_token_google_account_user_id_fk;

alter table google_token
    add constraint google_token_google_account_user_id_fk
        foreign key (user_id) references auth_user (id)
            on delete cascade;