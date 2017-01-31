DELETE FROM applications;
DELETE FROM session_info;
DELETE FROM exam_results;
DELETE FROM entrance_subjects;
DELETE FROM credentials;
DELETE FROM users;
DELETE FROM subjects;
DELETE FROM directions;
DELETE FROM faculties;
DELETE FROM universities;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO session_info (year, session_start, session_end) VALUES
  ('2016', '2016-7-01 12:00:00', '2016-7-21 12:00:00'),
  ('2017', '2016-7-01 12:00:00', '2016-7-21 12:00:00');

INSERT INTO credentials (login, password, is_admin) VALUES
  ('admin@gmail.com', '$2a$10$nr5CFHCJ7xj7bu6RIQZLGODs1l1wLRYOrIgAMHgNxhDpa79larXXK', TRUE),
  ('dimasik@yandex.ru', '$2a$10$KyuU93aT1ivfvYDX5rGUL./jSgarpXbqj4gJnno9fSrwRnu3xlHqm', FALSE),
  ('kate@in.ua', '$2a$10$ziPvYnzz9qc3wamVzwnZSOzjzT4zxG4xWBtAlbSC0VyK.1DNhNhV.', FALSE),
  ('maksim@maxim.com', '$2a$10$fFQUOYLTefEj1PmVnh0T4eHj.PthtjXZIWeQbeQazr6EZ7I/Sjcgm', FALSE);

INSERT INTO subjects(name) VALUES
  ('Українська мова та література'),
  ('Англійська мова'),
  ('Математика'),
  ('Хімія');

INSERT INTO users (name, last_name, address, email, phone_number, birth_date, average_mark) VALUES
  ('Дмитро', 'Васильков', 'Ужгород, Загорська 35', 'vas_dim@gmail.com', '+380671823452', '2000-3-22', '10.5'),
  ('Катерина', 'Руденко', 'Умань, Європейська 12', 'kate2000@gmail.com', '+380632212612', '1999-12-11', '9.8'),
  ('Максим', 'Панченко', 'Шостка, Сімейна 1', 'pan_max@gmail.com', '+380952235616', '2001-1-2', '8.7');

INSERT INTO exam_results (user_id, subject_id, mark) VALUES
  (10004, 10000, 192.5),
  (10004, 10001, 187.0),
  (10004, 10002, 183.25),
  (10004, 10003, 181.2),
  (10005, 10000, 190.6),
  (10005, 10001, 184.22),
  (10005, 10002, 181.5);

INSERT INTO universities (name, city, address) VALUES
  ('Київський національний університет ім. Тараса Шевченка', 'Київ', 'вул. Володимирська, 60'),
  ('Національний університет ''Києво-Могилянська Академія''', 'Київ', 'вул. Григорія Сковороди, 2'),
  ('Національний університет ''Львівська політехніка''', 'Львів', 'вул. Степана Бандери, 12');

INSERT INTO faculties (name, office_phone, office_email, address, university_id) VALUES
  ('Факультет інформаційних технологій', 'сайт не працює',
   'fit_knu@ukr.net', 'вул. Ванди Василевської, 24', 10007),
  ('Факультет кібернетики', '+38044-521-3554', 'ava@unicyb.kiev.ua',
   'просп. Академіка Глушкова, 4д', 10007),
  ('Факультет гуманітарних наук', '+38044-425-5188',
   'vakuliuk@ukma.edu.ua', 'вулиця Григорія Сковороди, 2', 10008),
  ('Факультет інформатики', '+38044-463-6985', 'gor@ukma.kiev.ua',
   'вулиця Григорія Сковороди, 2', 10008);

INSERT INTO directions (name, average_coef, count_of_students, faculty_id) VALUES
  ('Інформатика', 0.1, 30, 10010),
  ('Комп`ютерні науки', 0.05, 45, 10010),
  ('Безпека інформаційних систем', 0.1, 20, 10010),
  ('Прикладна математика', 0.05, 40, 10011),
  ('Програмне забезпечення автоматизованих систем', 0.05, 25, 10011);

INSERT INTO entrance_subjects (direction_id, subject_id, coefficient) VALUES
  (10014, 10000, 0.4),
  (10014, 10001, 0.3),
  (10014, 10002, 0.2),
  (10015, 10000, 0.4),
  (10015, 10001, 0.3),
  (10015, 10002, 0.25),
  (10016, 10000, 0.4),
  (10016, 10003, 0.3);

INSERT INTO applications (user_id, direction_id, created_time, status, mark) VALUES
  (10004, 10015, '2016-11-29 12:15:55', '1', 192.3),
  (10004, 10014, '2016-11-30 20:49:30', '0', 180.5),
  (10004, 10016, '2016-11-30 18:01:45', '0', 178.2);