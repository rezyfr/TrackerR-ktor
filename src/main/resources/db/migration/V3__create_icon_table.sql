create table if not exists "icon" (
    id serial primary key,
    type varchar(128) not null,
    url varchar(255) not null
);