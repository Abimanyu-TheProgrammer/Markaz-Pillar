create table donation_progress
(
    id            int auto_increment,
    donation_id   int           not null,
    thumbnail_url varchar(2048) not null,
    progress_date date          not null,
    description   varchar(2048) not null,
    constraint donation_progress_pk
        primary key (id),
    constraint donation_progress_donation_detail_id_fk
        foreign key (donation_id) references donation_detail (id)
            on delete cascade
);