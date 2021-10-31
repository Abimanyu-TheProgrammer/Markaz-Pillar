alter table donation_detail drop column contact_person;

alter table markaz_data
    add contact_name varchar(512) default '' not null;

alter table markaz_data
    add contact_info varchar(512) default '' not null;

alter table donation_detail
    add unique_id varchar(30) not null after id;

delete from donation_detail;

create unique index donation_detail_unique_id_uindex
    on donation_detail (unique_id);

alter table donation_detail
    add name varchar(256) not null;

create index donation_detail_name_index
    on donation_detail (name);

alter table donation_detail alter column is_active drop default;
