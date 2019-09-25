--liquibase formatted sql

--changeset jwirth:5
ALTER TABLE users
    MODIFY COLUMN id SERIAL;
--rollback ALTER TABLE users MODIFY COLUMN id INT NOT NULL AUTO_INCREMENT;