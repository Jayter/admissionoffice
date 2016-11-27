DROP TABLE IF EXISTS exam_results;
DROP TABLE IF EXISTS entrance_subjects;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS enrollees;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS directions;
DROP TABLE IF EXISTS faculties;
DROP TABLE IF EXISTS universities;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 10000;

CREATE TABLE admins (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  second_name VARCHAR NOT NULL,
  address VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  birth_date DATE NOT NULL
);

CREATE TABLE enrollees (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  second_name VARCHAR NOT NULL,
  address VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  birth_date TIMESTAMP NOT NULL,
  average_mark REAL
);

CREATE TABLE subjects (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE exam_results (
  enrollee_id BIGINT,
  subject_id BIGINT,
  mark REAL,
  FOREIGN KEY (enrollee_id) REFERENCES enrollees(id) ON DELETE CASCADE,
  FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE universities (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  city VARCHAR NOT NULL
);

CREATE TABLE faculties (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  university_id BIGINT NOT NULL,
  office_phone VARCHAR NOT NULL,
  office_email VARCHAR NOT NULL,
  FOREIGN KEY (university_id) REFERENCES universities(id) ON DELETE CASCADE
);

CREATE TABLE directions (
  id BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name VARCHAR NOT NULL,
  average_coef REAl,
  count_Of_Students SMALLINT,
  faculty_id BIGINT NOT NULL,
  FOREIGN KEY (faculty_id) REFERENCES faculties(id) ON DELETE CASCADE
);

CREATE TABLE entrance_subjects (
  direction_id BIGINT NOT NULL,
  subject_id BIGINT NOT NULL,
  coefficient REAl,
  FOREIGN KEY (direction_id) REFERENCES directions(id) ON DELETE CASCADE,
  FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
)