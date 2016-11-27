DELETE FROM exam_results;
DELETE FROM entrance_subjects;
DELETE FROM admins;
DELETE FROM enrollees;
DELETE FROM subjects;
DELETE FROM directions;
DELETE FROM faculties;
DELETE FROM universities;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO enrollees (name, second_name, address, email, password, phone_number, birth_date, average_mark) VALUES
  ('Дмитро', 'Васильков', 'Ужгород, Загорська 35', 'vas_dim@gmail.com', 'dim-dim', '+380671823452', '2000-3-22', '10.5'),
  ('Катерина', 'Руденко', 'Умань, Європейська 12', 'kate2000@gmail.com', 'kk-kkate', '+380632212612', '1999-12-11', '9.8'),
  ('Максим', 'Панченко', 'Шостка, Сімейна 1', 'pan_max@gmail.com', 'maxim(pan)', '+380952235616', '2001-1-2', '8.7');