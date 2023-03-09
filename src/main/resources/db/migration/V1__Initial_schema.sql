BEGIN;

DROP TYPE IF EXISTS user_role CASCADE;

CREATE TYPE user_role AS ENUM ('ADMIN', 'REGULAR', 'PREMIUM');

DROP TABLE IF EXISTS public."user" CASCADE;

CREATE TABLE IF NOT EXISTS public."user"
(
    id           BIGSERIAL NOT NULL,
    name         CHARACTER VARYING COLLATE pg_catalog."default",
    email        VARCHAR,
    phone_number VARCHAR,
    role         user_role,
    dob          TIMESTAMP,
    created_by   TEXT,
    created_at   TIMESTAMP,
    modified_by  TEXT,
    modified_at  TIMESTAMP
);

END;