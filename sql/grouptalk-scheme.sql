drop database if exists grouptalk;
create database grouptalk;

use grouptalk;

CREATE TABLE grupos (
    idgrupo BINARY(16) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    PRIMARY KEY (idgrupo)
);

CREATE TABLE temas (
    idtema BINARY(16) NOT NULL,
    idgrupo BINARY(16) NOT NULL,
    idusuario BINARY(16) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (idgrupo) REFERENCES grupos(idgrupo) on delete cascade,
    PRIMARY KEY (idtema)
);

CREATE TABLE users (
    id BINARY(16) NOT NULL,
    loginid VARCHAR(15) NOT NULL UNIQUE,
    password BINARY(16) NOT NULL,
    email VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_roles (
    userid BINARY(16) NOT NULL,
    role ENUM ('registered', 'admin'),
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (userid, role)
);

CREATE TABLE auth_tokens (
    userid BINARY(16) NOT NULL,
    token BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (token)
);

CREATE TABLE mensaje (
    idmensaje BINARY(16) NOT NULL,
    idtema BINARY(16) NOT NULL,
    userid BINARY(16) NOT NULL,
    content VARCHAR(500) NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    FOREIGN KEY (idtema) REFERENCES temas(idtema) on delete cascade,
    PRIMARY KEY (idmensaje)
);

CREATE TABLE suscripcion (
    userid BINARY(16) NOT NULL,
    grupoid BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    FOREIGN KEY (grupoid) REFERENCES grupos(idgrupo) on delete cascade
);
