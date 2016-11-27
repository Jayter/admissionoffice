DELETE FROM exam_results;
DELETE FROM entrance_subjects;
DELETE FROM admins;
DELETE FROM enrollees;
DELETE FROM subjects;
DELETE FROM directions;
DELETE FROM faculties;
DELETE FROM universities;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO admins (name, second_name, address, email, password, phone_number, birth_date) VALUES
  ('Андрій', 'Пилипенко', 'Київ, Васильківська 25', 'a_pylip@gmail.com', 'pylip19', '+380674562212', '1984-3-22'),
  ('Василь', 'Крилов', 'Львів, Некрасова 12', 'krylov@gmail.com', '1kr45kr', '+380632245612', '1995-12-11'),
  ('Ігор', 'Дудік', 'Житомир, Польова 1', 'duddik@gmail.com', '12dud12', '+380672235616', '1989-6-2');
