insert into user_menu (name) values ('SAMPLE');
insert into user_role_mapping (role_id, menu_id) VALUES (1, 1);
insert into auth_user (role_id, username, email, password, full_name, profile_url, description, phone_num, address)
values (1, 'admin', 'achmadafriza123@gmail.com', '$2a$10$aIJ00JLS..V2uSaxQNver.prDOJToYMmcapgXaAsofvNjrs1JICRC', 'Achmad Afriza Wibawa', '', '', '08111111111111', 'Jl. Tebet Timur 1G No. 29')