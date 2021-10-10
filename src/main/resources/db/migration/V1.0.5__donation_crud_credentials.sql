insert into user_menu (name) values ('CRUD_DONATION');
insert into user_role_mapping (role_id, menu_id) VALUES (1, (
    select user_menu.id
    from user_menu
    where name = 'CRUD_DONATION'
    limit 1
));