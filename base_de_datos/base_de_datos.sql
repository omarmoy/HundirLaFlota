-- Crear la base de datos
DROP DATABASE IF EXISTS HundirLaFlota;
CREATE DATABASE HundirLaFlota;
USE HundirLaFlota;

-- Tabla Jugadores
CREATE TABLE Jugadores (
    nombre VARCHAR(255) PRIMARY KEY,
    contrasena VARCHAR(255) NOT NULL
);

-- Tabla Partidas
CREATE TABLE Partidas (
    ID_Partida INT AUTO_INCREMENT PRIMARY KEY,
    ganador VARCHAR(255),
	terminada BOOLEAN DEFAULT FALSE
);

-- Tabla Jugador-Partida
CREATE TABLE JugadoresPartida (
	partida INT,
	jugador VARCHAR(255),
	turno BOOLEAN,
	PRIMARY KEY (partida, jugador),
    FOREIGN KEY (partida) REFERENCES Partidas(ID_Partida),
    FOREIGN KEY (jugador) REFERENCES Jugadores(nombre)	
);

-- Tabla con la posición de los barcos
CREATE TABLE Posicion_Barcos (
    partida INT,
    jugador VARCHAR(255),
    fila INT,
    columna INT,
    PRIMARY KEY (partida, jugador, fila, columna),
    FOREIGN KEY (partida, jugador) REFERENCES JugadoresPartida(partida, jugador),
    CHECK (fila >= 0 AND fila <= 9),
    CHECK (columna >= 0 AND columna <= 9)
);

-- Tablas con las jugadas
CREATE TABLE Jugadas (
    partida INT,
    jugador VARCHAR(255),
	n_jugada INT,
    fila INT,
    columna INT,
    resultado VARCHAR(255) CHECK(resultado IN ('AGUA', 'TOCADO', 'HUNDIDO')),
    PRIMARY KEY (partida, jugador, n_jugada),
    FOREIGN KEY (partida, jugador) REFERENCES JugadoresPartida(partida, jugador),
    CHECK (fila >= 0 AND fila <= 9),
    CHECK (columna >= 0 AND columna <= 9)
);


/*ya hay un script expotado con los datos de la tabla jugadores*/
/*
INSERT INTO Jugadores (nombre, contrasena) VALUES ('oscar', 'oscar');
INSERT INTO Jugadores (nombre, contrasena) VALUES ('angela', 'angela');
INSERT INTO Jugadores (nombre, contrasena) VALUES ('usuario', 'usuario');
INSERT INTO Jugadores (nombre, contrasena) VALUES ('mari', 'mari');
*/




