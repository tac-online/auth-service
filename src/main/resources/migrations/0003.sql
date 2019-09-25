--liquibase formatted sql

--changeset jwirth:3
ALTER TABLE users
    ADD (
        username VARCHAR(50) NOT NULL
        );
--rollback ALTER TABLE users DROP username;