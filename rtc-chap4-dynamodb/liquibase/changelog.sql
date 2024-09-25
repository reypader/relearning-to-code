--liquibase formatted sql

--changeset rmpader:chap4-1 
--comment: example comment
create table events (
    "id" uuid primary key,
    "name" varchar(50) not null,
    "start" timestamptz not null,
    "end" timestamptz not null,
    "version_lock" int not null
)
--rollback DROP TABLE events;
