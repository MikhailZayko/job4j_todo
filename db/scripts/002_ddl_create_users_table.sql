CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR        not null,
    login    VARCHAR unique not null,
    password VARCHAR        not null
);