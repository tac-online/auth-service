--liquibase formatted sql

--changeset jwirth:4
ALTER TABLE users
    MODIFY COLUMN email VARCHAR(50);
--rollback ALTER TABLE users MODIFY COLUMN email VARCHAR(50) NOT NULL;