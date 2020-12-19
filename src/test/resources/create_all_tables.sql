create table if not exists AUTHORS (
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT current_timestamp
);

create table if not exists BOOKS (
    id SERIAL NOT NULL PRIMARY KEY,
    title VARCHAR NOT NULL,
    author_id INTEGER NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT current_timestamp
);

alter table BOOKS
    add constraint if not exists author_fk foreign key(author_id) references AUTHORS(id) on update RESTRICT on delete CASCADE;