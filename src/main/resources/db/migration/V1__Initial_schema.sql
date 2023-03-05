BEGIN;

DROP TABLE IF EXISTS public."user" CASCADE;

CREATE TABLE IF NOT EXISTS public."user"
(
    id   BIGSERIAL NOT NULL,
    name CHARACTER VARYING COLLATE pg_catalog."default",
    role VARCHAR,
    dob TIMESTAMP,
    created_by  TEXT,
    created_at  TIMESTAMP,
    modified_by TEXT,
    modified_at TIMESTAMP
);

END;