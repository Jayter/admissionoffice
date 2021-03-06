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

INSERT INTO universities (name, city, address) VALUES
  ('Київський національний університет ім. Тараса Шевченка', 'Київ', 'вул. Володимирська, 60'),
  ('Національний університет ''Києво-Могилянська Академія''', 'Київ', 'вул. Григорія Сковороди, 2');

INSERT INTO faculties (name, office_phone, office_email, address, university_id) VALUES
  ('Факультет інформаційних технологій', '+38044-55-6776',
    'fit_knu@ukr.net', 'вул. Ванди Василевської, 24', 10010),
  ('Інститут гуманітарних та соціальних наук', '+38032-258-2350',
    'igsn@lp.edu.ua', 'вул. Митрополита Андрея 5', 10011);

INSERT INTO directions (name, average_coef, count_of_students, faculty_id) VALUES
  ('Інформатика', 0.1, 5, 10012),
  ('Комп`ютерні науки', 0.05, 3, 10012),
  ('Історія', 0.1, 4, 10013),
  ('Культурологія', 0.1, 3, 10013);
/*10017*/
INSERT INTO users(name, last_name, address, email, phone_number, birth_date, average_mark) VALUES
  ('','','','a','',now(),'10'),
  ('','','','b','',now(),'10'),
  ('','','','c','',now(),'10'),
  ('','','','d','',now(),'10'),
  ('','','','e','',now(),'10'),
  ('','','','f','',now(),'10'),
  ('','','','g','',now(),'10'),
  ('','','','h','',now(),'10'),
  ('','','','j','',now(),'10'),
  ('','','','k','',now(),'10'),
  ('','','','l','',now(),'10'),
  ('','','','m','',now(),'10'),
  ('','','','n','',now(),'10'),
  ('','','','o','',now(),'10'),
  ('','','','p','',now(),'10'),
  ('','','','q','',now(),'10');
/*10033*/
INSERT INTO applications (user_id, direction_id, created_time, mark) VALUES
  (10018, 10014, '2016-12-14 01:47:38', 130.2),
  (10018, 10015, '2016-12-14 02:47:38', 180.5),
  (10019, 10014, '2016-12-14 01:47:38', 182.5),
  (10019, 10016, '2016-12-14 02:47:38', 180.5),
  (10020, 10014, '2016-12-14 01:47:38', 171.3),
  (10020, 10016, '2016-12-14 02:47:38', 180.5),
  (10021, 10014, '2016-12-14 01:47:38', 192.5),
  (10021, 10017, '2016-12-14 02:47:38', 180.5),
  (10022, 10014, '2016-12-14 01:47:38', 198.7),
  (10022, 10015, '2016-12-14 02:47:38', 180.5),
  (10023, 10015, '2016-12-14 01:47:38', 191.1),
  (10023, 10016, '2016-12-14 02:47:38', 180.5),
  (10024, 10015, '2016-12-14 01:47:38', 192.3),
  (10024, 10016, '2016-12-14 02:47:38', 180.5),
  (10025, 10015, '2016-12-14 01:47:38', 174.3),
  (10025, 10017, '2016-12-14 02:47:38', 180.5),
  (10026, 10014, '2016-12-14 01:47:38', 192.3),
  (10026, 10016, '2016-12-14 02:47:38', 180.5),
  (10027, 10016, '2016-12-14 01:47:38', 192.3),
  (10027, 10017, '2016-12-14 02:47:38', 171.2),
  (10028, 10016, '2016-12-14 01:47:38', 171.7),
  (10028, 10017, '2016-12-14 02:47:38', 180.5),
  (10029, 10016, '2016-12-14 01:47:38', 142.6),
  (10029, 10017, '2016-12-14 02:47:38', 181.5),
  (10030, 10016, '2016-12-14 01:47:38', 156.3),
  (10030, 10014, '2016-12-14 02:47:38', 134.2),
  (10031, 10017, '2016-12-14 01:47:38', 192.3),
  (10031, 10015, '2016-12-14 02:47:38', 180.5),
  (10032, 10017, '2016-12-14 01:47:38', 186.4),
  (10032, 10015, '2016-12-14 02:47:38', 180.5),
  (10033, 10017, '2016-12-14 01:47:38', 172.3),
  (10033, 10015, '2016-12-14 02:47:38', 180.5);