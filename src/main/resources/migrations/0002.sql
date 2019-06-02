--liquibase formatted sql

--changeset jwirth:2
ALTER TABLE users
  ADD (
    email VARCHAR(50) NOT NULL
  );
--rollback ALTER TABLE users DROP email;