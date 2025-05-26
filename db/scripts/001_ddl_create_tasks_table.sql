CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   title VARCHAR,
   description VARCHAR,
   created TIMESTAMP,
   done BOOLEAN
);