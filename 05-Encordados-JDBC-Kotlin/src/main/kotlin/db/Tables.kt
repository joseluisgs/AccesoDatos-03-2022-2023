package db

fun createTables() = """
    
CREATE TABLE IF NOT EXISTS raquetas (
    uuid UUID PRIMARY KEY,
    marca VARCHAR(255) NOT NULL,
    precio DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS tenistas (
    uuid UUID PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    ranking INTEGER NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    año_profesional INTEGER NOT NULL,
    altura INTEGER NOT NULL,
    peso INTEGER NOT NULL,
    pais VARCHAR(255) NOT NULL,
    ganancias DOUBLE NOT NULL,
    mano_dominante VARCHAR(255) NOT NULL,
    tipo_reves VARCHAR(255) NOT NULL,
    puntos INTEGER NOT NULL,
    raqueta_uuid UUID, -- Llave foranea no nula --
    FOREIGN KEY (raqueta_uuid) REFERENCES raquetas(uuid)
);
    """.trimIndent()
