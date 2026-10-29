CREATE DATABASE IF NOT EXISTS backend_t7;
USE backend_t7;
CREATE TABLE IF NOT EXISTS user( username varchar(255) PRIMARY KEY NOT NULL, password varchar(255) NOT NULL);