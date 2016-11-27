DELETE FROM exam_results;
DELETE FROM entrance_subjects;
DELETE FROM admins;
DELETE FROM enrollees;
DELETE FROM subjects;
DELETE FROM directions;
DELETE FROM faculties;
DELETE FROM universities;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO universities (name, city, address) VALUES
  ('Київський національний університет ім. Тараса Шевченка', 'Київ', 'вул. Володимирська, 60'),
  ('Національний університет "Києво-Могилянська Академія"', 'Київ', 'вул. Григорія Сковороди, 2'),
  ('Національний університет "Львівська політехніка"', 'Львів', 'вул. Степана Бандери, 12');

INSERT INTO faculties (name, university_id, office_phone, office_email, address) VALUES
  ('Факультет інформаційних технологій', 10000, 'сайт не працює',
   'fit_knu@ukr.net', 'вул. Ванди Василевської, 24'),
  ('Факультет кібернетики', 10000, '+38044-521-3554', 'ava@unicyb.kiev.ua',
   'просп. Академіка Глушкова, 4д'),
  ('Факультет гуманітарних наук', 10001, '+38044-425-5188',
   'vakuliuk@ukma.edu.ua', 'вулиця Григорія Сковороди, 2'),
  ('Факультет інформатики', 10001, '+38044-463-6985', 'gor@ukma.kiev.ua',
   'вулиця Григорія Сковороди, 2');

INSERT INTO directions (name, average_coef, count_of_students, faculty_id) VALUES
  ('Інформатика', 0.1, 30, 10003),
  ('Комп`ютерні науки', 0.1, 45, 10003),
  ('Безпека інформаційних систем', 0.1, 20, 10003),
  ('Прикладна математика', 0.05, 40, 10006),
  ('Програмне забезпечення автоматизованих систем', 0.05, 25, 10006);