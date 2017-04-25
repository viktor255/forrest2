--schema-javadb.sql
--DDL commands for JavaDB/Derby

CREATE TABLE POT (
  ID       INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  COL      INT,
  ROW      INT,
  CAPACITY INT,
  NOTE     VARCHAR(255)
);

CREATE TABLE TREES (
  ID       INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  NAME     VARCHAR(50),
  TREETYPE  VARCHAR(150),
  ISPROTECTED  BOOLEAN
);

-- CREATE TABLE leases (
--   id          INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
--   bookId      INT REFERENCES books (id)
--     ON DELETE CASCADE,
--   customerId  INT REFERENCES customers (id)
--     ON DELETE CASCADE,
--   startDate   DATE,
--   expectedEnd DATE,
--   realEnd     DATE
-- );

-- CREATE TABLE trees (
--   id  INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
--   --   "POTID" BIGINT REFERENCES POT (ID),
--   name VARCHAR(255) NOT NULL,
--   treetype VARCHAR(6) NOT NULL,
--   isprotected BOOLEAN NOT NULL
-- );