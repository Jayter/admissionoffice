DROP TABLE IF EXISTS session_info;
DROP TABLE IF EXISTS exam_results;
DROP TABLE IF EXISTS entrance_subjects;
DROP TABLE IF EXISTS credentials;
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS directions;
DROP TABLE IF EXISTS faculties;
DROP TABLE IF EXISTS universities;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 10000;

CREATE TABLE session_info (
  year INTEGER UNIQUE PRIMARY KEY,
  session_start TIMESTAMP NOT NULL,
  session_end TIMESTAMP NOT NULL
);

CREATE TABLE credentials (
  login TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL,
  is_admin BOOL NOT NULL DEFAULT FALSE
);

CREATE TABLE users (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  address VARCHAR NOT NULL,
  email VARCHAR UNIQUE NOT NULL,
  phone_number VARCHAR NOT NULL,
  birth_date TIMESTAMP NOT NULL,
  average_mark REAL NOT NULL
);

CREATE TABLE subjects (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE exam_results (
  user_id BIGINT NOT NULL,
  subject_id BIGINT NOT NULL,
  mark REAL NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE universities (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  city VARCHAR NOT NULL,
  address VARCHAR NOT NULL
);

CREATE TABLE faculties (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  university_Id BIGINT NOT NULL,
  office_phone VARCHAR NOT NULL,
  office_email VARCHAR NOT NULL,
  address VARCHAR NOT NULL,
  FOREIGN KEY (university_Id) REFERENCES universities(id) ON DELETE CASCADE
);

CREATE TABLE directions (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  average_coef REAl NOT NULL,
  count_Of_Students SMALLINT,
  faculty_id BIGINT NOT NULL,
  FOREIGN KEY (faculty_id) REFERENCES faculties(id) ON DELETE CASCADE
);

CREATE TABLE entrance_subjects (
  direction_id BIGINT NOT NULL,
  subject_id BIGINT NOT NULL,
  coefficient REAL NOT NULL,
  FOREIGN KEY (direction_id) REFERENCES directions(id) ON DELETE CASCADE,
  FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE applications (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  user_id BIGINT NOT NULL,
  direction_id BIGINT NOT NULL,
  created_time TIMESTAMP NOT NULL DEFAULT now(),
  status SMALLINT NOT NULL DEFAULT 0,
  mark REAL NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (direction_id) REFERENCES directions(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX unique_applications ON applications (user_id, direction_id);
CREATE UNIQUE INDEX unique_results ON exam_results (user_id, subject_id);