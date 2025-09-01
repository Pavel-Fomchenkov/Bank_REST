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

-- changeset fpavel:2
ALTER TABLE public.cards ADD COLUMN
    credit_limit NUMERIC(19, 2) NOT NULL DEFAULT 0.00;

-- changeset fpavel:3
CREATE TABLE IF NOT EXISTS public.transactions
(
  id BIGSERIAL PRIMARY KEY,
  from_card_id BIGINT NOT NULL,
  to_card_id BIGINT NOT NULL,
  amount NUMERIC(19, 2) NOT NULL,
  initiator_id BIGINT NOT NULL,
  transaction_date timestamp with time zone NOT NULL,
  status character varying(32) NOT NULL
);

-- changeset fpavel:4
ALTER TABLE public.transactions ADD COLUMN
     description character varying(255) NOT NULL;