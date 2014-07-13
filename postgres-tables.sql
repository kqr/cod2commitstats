-- To delete:
-- DROP TABLE players CASCADE; DROP TABLE rounds CASCADE; DROP TABLE roundplayers CASCADE; DROP TABLE deaths CASCADE;

CREATE TABLE players
(
  id serial PRIMARY KEY,
  name text NOT NULL UNIQUE,
  playtime interval DEFAULT '0 S',
  kills int NOT NULL DEFAULT 0,
  deaths int NOT NULL DEFAULT 0
);

CREATE TABLE rounds
(
  id serial PRIMARY KEY,
  map text NOT NULL
);

CREATE TABLE roundplayers
(
  id serial PRIMARY KEY,
  round_id int REFERENCES rounds(id),
  player_id int REFERENCES players(id),
  kills int NOT NULL,
  deaths int NOT NULL,
  playtime interval NOT NULL
);

CREATE TABLE deaths
(
  id serial PRIMARY KEY,
  round_id int REFERENCES rounds(id),
  dead_id int REFERENCES players(id),
  killer_id int REFERENCES players(id),
  weapon text NOT NULL,
  killfeed text NOT NULL,
  headshot boolean NOT NULL
);

