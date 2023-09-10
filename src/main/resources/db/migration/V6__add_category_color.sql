alter table category
    add color varchar default '0xff7F3DFF';

alter table wallet
    drop column color;