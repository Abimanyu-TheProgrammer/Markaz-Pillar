DROP TABLE IF EXISTS registered_users;

CREATE TABLE registered_users (
    id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar(50) NOT NULL,
    username varchar(50) NOT NULL,
    full_name varchar(50) NOT NULL,
    phone_num bigint(20) NOT NULL,
    address varchar(100) NOT NULL,
    password varchar(100) NOT NULL
) ENGINE=INNODB ;