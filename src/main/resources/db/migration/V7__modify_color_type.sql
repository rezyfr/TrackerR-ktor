alter table category
    alter column color drop default,
    alter column color type bigint using color::bigint,
    alter column color set default -8438273;

