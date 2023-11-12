CREATE TABLE Aluno (
  id int PRIMARY KEY,
  nome varchar NOT NULL,
  data_nascimento date NOT NULL,
  filiacao varchar NOT NULL,
  RG varchar NOT NULL,
  deletado boolean NOT NULL,
  turma_id int NOT NULL
);

CREATE TABLE Turma (
  id int PRIMARY KEY,
  nome varchar NOT NULL,
  ano varchar NOT NULL
);

CREATE TABLE Disciplina (
  id int PRIMARY KEY,
  nome varchar NOT NULL,
  carga_horaria int NOT NULL
);

CREATE TABLE Professor (
  id int PRIMARY KEY,
  nome varchar NOT NULL,
  area_especializacao varchar NOT NULL,
  contato varchar NOT NULL,
  deletado boolean NOT NULL,
  disciplina_id int
);

CREATE TABLE Ministra (
  professor_id int,
  turma_id int,
  PRIMARY KEY (professor_id, turma_id)
);

CREATE TABLE Turma_Disciplina (
  turma_id int,
  disciplina_id int,
  PRIMARY KEY (turma_id, disciplina_id)
);

CREATE TABLE Presenca (
  aluno_id int,
  disciplina_id int,
  data_presenca varchar,
  presente boolean NOT NULL,
  PRIMARY KEY (aluno_id, disciplina_id, data_presenca)
);

CREATE TABLE Nota (
  aluno_id int,
  disciplina_id int,
  nota decimal NOT NULL,
  PRIMARY KEY (aluno_id, disciplina_id)
);

CREATE TABLE Usuario (
  id int,
  senha varchar NOT NULL,
  permissao_id int NOT NULL,
  deletado boolean NOT NULL
);

CREATE TABLE Permissao (
  id int PRIMARY KEY,
  nome varchar NOT NULL
);

ALTER TABLE Aluno ADD FOREIGN KEY (turma_id) REFERENCES Turma (id);

ALTER TABLE Professor ADD FOREIGN KEY (disciplina_id) REFERENCES Disciplina (id);

ALTER TABLE Ministra ADD FOREIGN KEY (professor_id) REFERENCES Professor (id);

ALTER TABLE Ministra ADD FOREIGN KEY (turma_id) REFERENCES Turma (id);

ALTER TABLE Turma_Disciplina ADD FOREIGN KEY (turma_id) REFERENCES Turma (id);

ALTER TABLE Turma_Disciplina ADD FOREIGN KEY (disciplina_id) REFERENCES Disciplina (id);

ALTER TABLE Presenca ADD FOREIGN KEY (aluno_id) REFERENCES Aluno (id);

ALTER TABLE Presenca ADD FOREIGN KEY (disciplina_id) REFERENCES Disciplina (id);

ALTER TABLE Nota ADD FOREIGN KEY (aluno_id) REFERENCES Aluno (id);

ALTER TABLE Nota ADD FOREIGN KEY (disciplina_id) REFERENCES Disciplina (id);

ALTER TABLE Usuario ADD FOREIGN KEY (id) REFERENCES Professor (id);

ALTER TABLE Usuario ADD FOREIGN KEY (permissao_id) REFERENCES Permissao (id);



INSERT INTO permissao VALUES (1, 'Administrador');
INSERT INTO permissao VALUES (2, 'Usuário');

INSERT INTO professor VALUES (1, 'Admin', 'Admin', 'admin@email.com', FALSE);
INSERT INTO usuario VALUES (1, '$2a$10$dtAPkculHQqRuDd6Znn0KOQQIa61Jlt0iL73ZbmyPm3gD6VSfGcQa', 1, FALSE);

INSERT INTO disciplina (id, nome, carga_horaria)
VALUES
    (1, 'História', 60),
    (2, 'Português', 60),
    (3, 'Matemática', 60),
    (4, 'Geografia', 60),
    (5, 'Ciências', 60),
    (6, 'Ensino Religioso', 60),
    (7, 'Inglês', 60),
    (8, 'Educação Física', 60);
	
INSERT INTO turma (id, nome, ano)
VALUES
    (1, 'Turma A', '8º Ano'),
    (2, 'Turma B', '7º Ano'),
    (3, 'Turma C', '8º Ano');