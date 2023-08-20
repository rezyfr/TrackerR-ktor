CREATE TYPE CategoryType AS ENUM ('INCOME', 'EXPENSE');

create table if not exists "user" (
    name varchar(128) not null,
    email varchar(255) primary key not null,
    hash_password varchar(512) not null
);

create table if not exists category (
    id serial primary key,
    name varchar(255) not null,
    type CategoryType not null,
    user_email varchar not null references "user" (email)
);

create table if not exists wallet (
    id serial primary key,
    name varchar(255) not null,
    balance decimal(12, 2) not null,
    color bigint not null,
    icon varchar(255),
    user_email varchar not null references "user" (email)
);

create table if not exists transaction (
    id serial primary key,
    amount decimal(12, 2) not null,
    "desc" varchar(255),
    wallet_id int not null references wallet (id),
    category_id int not null references category (id),
    user_email varchar not null references "user" (email),
    type CategoryType not null,
    created_at timestamp not null
);
