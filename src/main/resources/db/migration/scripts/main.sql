-- liquibase formatted sql

-- changeset fpavel:1
CREATE TABLE IF NOT EXISTS public.users
(
     id BIGSERIAL PRIMARY KEY,
     username character varying(255) NOT NULL,
     role character varying(32) NOT NULL,
     password_encrypted character varying(255) NOT NULL,
     entry_date timestamp with time zone NOT NULL
);

CREATE TABLE IF NOT EXISTS public.cards
(
     id BIGSERIAL PRIMARY KEY,
     description character varying(255) NOT NULL,
     number_encrypted character varying(255) NOT NULL,
     number_masked character varying(255) NOT NULL,
     user_id BIGINT REFERENCES public.users(id),
     entry_date timestamp with time zone NOT NULL,
     expiration_date timestamp with time zone NOT NULL,
     status character varying(32) NOT NULL,
     balance NUMERIC(19, 2) NOT NULL
);