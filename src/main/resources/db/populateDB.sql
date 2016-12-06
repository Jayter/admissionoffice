DELETE FROM applications;
DELETE FROM exam_results;
DELETE FROM entrance_subjects;
DELETE FROM credentials;
DELETE FROM users;
DELETE FROM subjects;
DELETE FROM directions;
DELETE FROM faculties;
DELETE FROM universities;
DELETE FROM session_info;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO session_info (year, session_start, session_end) VALUES
  ('2016', '2016-7-01 12:00:00', '2016-7-21 12:00:00');

INSERT INTO credentials (login, password, is_admin) VALUES
  ('admin@gmail.com', 'admin1', TRUE),
  ('dimasik@yandex.ru', 'dim_dim', FALSE),
  ('kate@in.ua', 'katherine3', FALSE),
  ('maksim@maxim.com', 'maXMax', FALSE);

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
/**10009*/
INSERT INTO users (name, last_name, address, email, phone_number, birth_date, average_mark) VALUES
  ('Дмитро', 'Васильков', 'Ужгород, Загорська 35', 'dimasik@yandex.ru', '+380671823452', '2000-3-22', '10.5'),
  ('Катерина', 'Руденко', 'Умань, Європейська 12', 'kate@in.ua', '+380632212612', '1999-12-11', '9.8'),
  ('Максим', 'Панченко', 'Шостка, Сімейна 1', 'maksim@maxim.com', '+380952235616', '2001-1-2', '8.7');
/**10012*/
INSERT INTO exam_results (user_id, subject_id, mark) VALUES
  (10010, 10000, 192.5),
  (10010, 10001, 187.0),
  (10010, 10002, 183.25),
  (10010, 10003, 181.2),
  (10011, 10000, 190.6),
  (10011, 10001, 184.22),
  (10011, 10002, 183.4),
  (10012, 10000, 191.5),
  (10012, 10001, 187.0),
  (10012, 10005, 182.25),
  (10012, 10006, 156.25);

INSERT INTO universities (name, city, address) VALUES
  ('Київський національний університет ім. Тараса Шевченка', 'Київ', 'вул. Володимирська, 60'),
  ('Національний університет "Києво-Могилянська Академія"', 'Київ', 'вул. Григорія Сковороди, 2'),
  ('Національний університет "Львівська політехніка"', 'Львів', 'вул. Степана Бандери, 12'),
  ('Національний технічний університет України "КПІ ім. Ігоря Сікорського"', 'Київ', 'просп. Перемоги, 37'),
  ('Чернігівський національний технологічний університет', 'Чернігів', 'вул. Шевченка, 95');
/**10017*/
INSERT INTO faculties (name, office_phone, office_email, address, university_id) VALUES
  ('Факультет інформаційних технологій', '+38044-55-6776',
   'fit_knu@ukr.net', 'вул. Ванди Василевської, 24', 10013),
  ('Факультет кібернетики', '+38044-521-3554', 'ava@unicyb.kiev.ua',
   'просп. Академіка Глушкова, 4д', 10013),
  ('Факультет гуманітарних наук', '+38044-425-5188',
   'vakuliuk@ukma.edu.ua', 'вулиця Григорія Сковороди, 2', 10014),
  ('Факультет інформатики', '+38044-463-6985', 'gor@ukma.kiev.ua',
   'вулиця Григорія Сковороди, 2', 10014),
  ('Інститут гуманітарних та соціальних наук', '+38032-258-2350',
   'igsn@lp.edu.ua', 'вул. Митрополита Андрея 5', 10015),
  ('Інститут геодезії', '+38032-258-2698', 'kornel@polynet.lviv.ua',
   'вул. Карпінського 6', 10015),
  ('Радіотехнічний факультет', '+38044-362-9851', 'dek@rtf.kpi.ua',
   'вул. Політехнічна, 12', 10016),
  ('Інженерно-фізичний факультет', '+38044-204-8215', 'iff@kpi.ua',
   'вул. Карпінського 6', 10016),
  ('Факультет лінгвістики', '+38025-241-8455', 'lingv@gk.ua',
   'вул. Мирна 2', 10017),
  ('Інженернийй факультет', '+38025-244-8312', 'krasnov@k.kl',
  'вул. Мирна 6', 10017);
/**10027*/
INSERT INTO directions (name, average_coef, count_of_students, faculty_id) VALUES
  ('Інформатика', 0.1, 30, 10018),
  ('Комп`ютерні науки', 0.05, 45, 10018),
  ('Безпека інформаційних систем', 0.1, 20, 10018),
  ('Прикладна математика', 0.1, 40, 10019),
  ('Програмне забезпечення автоматизованих систем', 0.05, 25, 10019),
  ('Історія', 0.1, 30, 10020),
  ('Культурологія', 0.1, 45, 10020),
  ('Програмна інженерія', 0.5, 30, 10021),
  ('Інформатика', 0.1, 20, 10021),
  ('Соціальні потреби', 0.1, 45, 10022),
  ('Іноземна мова', 0.1, 50, 10022),
  ('Геодезія', 0.1, 40, 10023),
  ('Земельні ресурси', 0.1, 50, 10023),
  ('Радіотехніка', 0.1, 25, 10024),
  ('Інформатика', 0.1, 30, 10024),
  ('Радіотехніка', 0.1, 15, 10025),
  ('Комп`ютерна інженерія', 0.1, 40, 10025),
  ('Іноземна мова', 0.1, 20, 10026),
  ('Технічний переклад', 0.1, 30, 10026),
  ('Радіотехніка', 0.1, 15, 10027),
  ('Інформатика', 0.2, 30, 10027);
/**10048*/
INSERT INTO entrance_subjects (direction_id, subject_id, coefficient) VALUES
  (10028, 10000, 0.4),
  (10028, 10001, 0.3),
  (10028, 10002, 0.2),
  (10029, 10000, 0.4),
  (10029, 10001, 0.3),
  (10029, 10002, 0.25),
  (10030, 10000, 0.4),
  (10030, 10001, 0.35),
  (10030, 10002, 0.2),
  (10031, 10000, 0.4),
  (10031, 10001, 0.3),
  (10031, 10002, 0.2),
  (10032, 10000, 0.4),
  (10032, 10001, 0.3),
  (10032, 10002, 0.2),
  (10033, 10000, 0.4),
  (10033, 10001, 0.3),
  (10033, 10002, 0.2),
  (10034, 10000, 0.4),
  (10034, 10001, 0.3),
  (10034, 10002, 0.2),
  (10035, 10000, 0.4),
  (10035, 10001, 0.3),
  (10035, 10002, 0.2),
  (10036, 10000, 0.4),
  (10036, 10001, 0.3),
  (10036, 10002, 0.2),
  (10037, 10000, 0.4),
  (10037, 10002, 0.3),
  (10037, 10003, 0.2),
  (10038, 10000, 0.4),
  (10038, 10001, 0.3),
  (10038, 10005, 0.2),
  (10039, 10000, 0.4),
  (10039, 10001, 0.3),
  (10039, 10002, 0.2),
  (10040, 10000, 0.4),
  (10040, 10001, 0.3),
  (10040, 10002, 0.2),
  (10041, 10000, 0.4),
  (10041, 10001, 0.3),
  (10041, 10002, 0.2),
  (10042, 10000, 0.4),
  (10042, 10001, 0.3),
  (10042, 10002, 0.2),
  (10043, 10000, 0.4),
  (10043, 10001, 0.3),
  (10043, 10002, 0.2),
  (10044, 10000, 0.4),
  (10044, 10002, 0.3),
  (10044, 10006, 0.2),
  (10045, 10000, 0.4),
  (10045, 10002, 0.3),
  (10045, 10004, 0.2),
  (10046, 10000, 0.4),
  (10046, 10007, 0.3),
  (10046, 10005, 0.2),
  (10047, 10000, 0.4),
  (10047, 10001, 0.3),
  (10047, 10004, 0.2),
  (10048, 10000, 0.3),
  (10048, 10002, 0.3),
  (10048, 10005, 0.2);