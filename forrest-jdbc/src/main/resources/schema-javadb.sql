--schema-javadb.sql
--DDL commands for JavaDB/Derby

CREATE TABLE POT (
  ID       INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  ROW      INT,
  COL      INT,
  CAPACITY INT,
  NOTE     VARCHAR(255)
);

CREATE TABLE TREES (
  ID       INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  NAME     VARCHAR(50),
  TREETYPE  VARCHAR(150),
  ISPROTECTED  BOOLEAN
);

CREATE TABLE PLANTERS (
  id          INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  potId      INT REFERENCES POT (ID) ON DELETE CASCADE,
  treeId  INT REFERENCES TREES (ID) ON DELETE CASCADE
);