DELETE FROM subjects;
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