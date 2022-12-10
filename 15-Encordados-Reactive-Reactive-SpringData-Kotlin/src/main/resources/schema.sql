-- Mis tablas
CREATE TABLE IF NOT EXISTS PERSONS
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name TEXT NOT NULL
);

-- Al día de Hoy con Spring Data lo mejor es usar id autonuméricos

CREATE TABLE IF NOT EXISTS RAQUETAS
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    marca       TEXT    NOT NULL,
    precio      TEXT    NOT NULL,
    new_product BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tengo que ponerle uuid porque no lo sabemos para la raqueta_uuid
/*insert into RAQUETAS (id, marca, precio)
values (1, 'Babolat', '200');
insert into RAQUETAS (id, marca, precio)
values (2, 'Wilson', '250');
insert into RAQUETAS (id, marca, precio)
values (3, 'Head', '250');*/

CREATE TABLE IF NOT EXISTS TENISTAS
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre           TEXT    NOT NULL,
    ranking          INTEGER NOT NULL,
    fecha_nacimiento DATE    NOT NULL,
    año_profesional  INTEGER NOT NULL,
    altura           INTEGER NOT NULL,
    peso             INTEGER NOT NULL,
    ganancias        DOUBLE  NOT NULL,
    mano_dominante   TEXT    NOT NULL,
    tipo_reves       TEXT    NOT NULL,
    puntos           INTEGER NOT NULL,
    pais             TEXT    NOT NULL,
    raqueta_id       BIGINT REFERENCES RAQUETAS (id)
);

/*
insert into TENISTAS (nombre, ranking, fecha_nacimiento, año_profesional, altura, peso, ganancias, mano_dominante,
                      tipo_reves, puntos, pais, raqueta_id)
values ('Rafael Nadal', 2, '1985-06-04', 2005, 185, 85, 100000000.0, 'IZQUIERDA', 'DOS MANOS', 6789, 'España', 1);

insert into TENISTAS (nombre, ranking, fecha_nacimiento, año_profesional, altura, peso, ganancias, mano_dominante,
                      tipo_reves, puntos, pais, raqueta_id)
values ('Roger Federer', 3, '1981-08-08', 2000, 188, 83, 200000000.0, 'DERECHA', 'UNA MANO', 3789, 'Suiza', 2);

insert into TENISTAS (nombre, ranking, fecha_nacimiento, año_profesional, altura, peso, ganancias, mano_dominante,
                      tipo_reves, puntos, pais, raqueta_id)
values ('Novak Djokovic', 4, '1986-05-05', 2004, 189, 81, 100000000.0, 'DERECHA', 'DOS MANOS', 1970, 'Serbia', 3);

insert into TENISTAS (nombre, ranking, fecha_nacimiento, año_profesional, altura, peso, ganancias, mano_dominante,
                      tipo_reves, puntos, pais, raqueta_id)
values ('Dominic Thiem', 5, '1993-09-03', 2008, 188, 83, 10000.0, 'DERECHA', 'UNA MANO', 1234, 'Austria', 1);

insert into TENISTAS (nombre, ranking, fecha_nacimiento, año_profesional, altura, peso, ganancias, mano_dominante,
                      tipo_reves, puntos, pais, raqueta_id)
values ('Carlos Alcaraz', 1, '2003-05-05', 2019, 185, 80, 5000000.0, 'DERECHA', 'DOS MANOS', 6880, 'España', 1);

*/

-- Mete los datos que necesites en las tablas que se carga
INSERT INTO PERSONS (name)
values ('John');