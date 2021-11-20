alter table volunteer_registration
    add user_id int not null after program_id;

alter table volunteer_registration
    add constraint volunteer_registration_auth_user_id_fk
        foreign key (user_id) references auth_user (id)
            on delete cascade;

