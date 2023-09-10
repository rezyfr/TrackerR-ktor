alter table category
    add color varchar default '#7F3DFF';

alter table wallet
    drop column color;