create table markaz_donation_category
(
    donation_id int not null,
    category varchar(128) not null,
    constraint markaz_donation_category_pk
        primary key (donation_id, category),
    constraint markaz_donation_category_donation_detail_id_fk
        foreign key (donation_id) references donation_detail (id)
);

