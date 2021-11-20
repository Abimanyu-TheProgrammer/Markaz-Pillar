insert into user_menu (name) values ('CRUD_PROGRAM');
insert into user_role_mapping (role_id, menu_id) VALUES (1, (
    select user_menu.id
    from user_menu
    where name = 'CRUD_PROGRAM'
    limit 1
));

insert into user_menu (name) values ('CRUD_TESTIMONY');
insert into user_role_mapping (role_id, menu_id) VALUES (1, (
    select user_menu.id
    from user_menu
    where name = 'CRUD_TESTIMONY'
    limit 1
));

insert into user_menu (name) values ('CRUD_VOLUNTEER');
insert into user_role_mapping (role_id, menu_id) VALUES (1, (
    select user_menu.id
    from user_menu
    where name = 'CRUD_VOLUNTEER'
    limit 1
));