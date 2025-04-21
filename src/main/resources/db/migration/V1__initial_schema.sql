-- Create initial tables
create table if not exists "user" (
    name varchar(128) not null,
    email varchar(255) primary key not null,
    hash_password varchar(512) not null
);

create table if not exists "icon" (
    id serial primary key,
    type varchar(128) not null,
    url varchar(255) not null
);

create table if not exists category (
    id serial primary key,
    name varchar(255) not null,
    type varchar not null,
    color bigint not null default -8438273,
    icon_id integer references icon (id),
    user_email varchar not null references "user" (email)
);

create table if not exists wallet (
    id serial primary key,
    name varchar(255) not null,
    balance decimal(12, 2) not null,
    icon int8 references icon (id),
    user_email varchar not null references "user" (email)
);

create table if not exists transaction (
    id serial primary key,
    amount decimal(12, 2) not null,
    "desc" varchar(255),
    wallet_id int not null references wallet (id),
    category_id int not null references category (id),
    user_email varchar not null references "user" (email),
    type varchar not null,
    created_at timestamp not null
);

-- Convert enum types to varchar
alter table category
    alter column type type varchar using type::varchar;

alter table transaction
    alter column type type varchar using type::varchar;

drop type categorytype;

-- Modify wallet icon to reference icon table
alter table wallet
    alter column icon type int8 using icon::int8;

alter table wallet
    add constraint wallet_icon_id__fkey
        foreign key (icon) references icon (id);

-- Add icon to category
alter table category
    add "icon_id" integer;

alter table category
    add constraint category_icon_id_fkey
        foreign key ("icon_id") references icon (id);

-- Move color from wallet to category
alter table category
    add color varchar default '#7F3DFF';

alter table wallet
    drop column color;

-- Modify color type to bigint
alter table category
    alter column color drop default,
    alter column color type bigint using color::bigint,
    alter column color set default -8438273; 