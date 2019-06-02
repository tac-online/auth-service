--liquibase formatted sql

--changeset jwirth:1
CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT , password VARCHAR(50) NOT NULL , salt VARCHAR(50) NOT NULL , PRIMARY KEY (`id`));
--rollback drop table users;