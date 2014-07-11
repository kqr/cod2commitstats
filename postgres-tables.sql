CREATE TABLE players
(
  id serial PRIMARY KEY,
  name text NOT NULL UNIQUE,
  playtime interval DEFAULT '0 S'
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
  playtime interval NOT NULL,
  efficacy double precision DEFAULT 0
);

CREATE TABLE deaths
(
  id serial PRIMARY KEY,
  round_id int REFERENCES rounds(id),
  dead_id int REFERENCES players(id),
  killer_id int REFERENCES players(id),
  weapon text NOT NULL,
  headshot boolean NOT NULL
);

