alter table category
    alter column type type varchar using type::varchar;

alter table transaction
    alter column type type varchar using type::varchar;

drop type categorytype;