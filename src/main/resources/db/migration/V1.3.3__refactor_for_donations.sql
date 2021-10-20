alter table donation_detail drop column contact_person;

alter table markaz_data
    add contact_name varchar(512) default '' not null;

alter table markaz_data
    add contact_info varchar(512) default '' not null;

