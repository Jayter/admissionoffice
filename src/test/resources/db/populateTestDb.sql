DELETE FROM subjects;
DELETE FROM admins;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO subjects(name) VALUES
  ('Українська мова та література'),
  ('Англійська мова'),
  ('Математика'),
  ('Хімія'),
  ('Біологія'),
  ('Фізика'),
  ('Географія'),
  ('Історія України'),
  ('Всесвітня історія'),
  ('Всесвітня література');

INSERT INTO admins (name, second_name, address, email, password, phone_number, birth_date) VALUES
  ('Андрій', 'Пилипенко', 'Київ, Васильківська 25', 'a_pylip@gmail.com', 'pylip19', '+380674562212', CURRENT_DATE),
  ('Василь', 'Крилов', 'Львів, Некрасова 12', 'krylov@gmail.com', '1kr45kr', '+380632245612', CURRENT_DATE);