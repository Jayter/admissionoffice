DELETE FROM exam_results;
DELETE FROM entrance_subjects;
DELETE FROM admins;
DELETE FROM enrollees;
DELETE FROM subjects;
DELETE FROM directions;
DELETE FROM faculties;
DELETE FROM universities;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO faculties (name, university_id, office_phone, office_email, address) VALUES
  ('Факультет інформаційних технологій', 10006, 'сайт не працює',
   'fit_knu@ukr.net', 'вул. Ванди Василевської, 24'),
  ('Факультет кібернетики', 10006, '+38044-521-3554', 'ava@unicyb.kiev.ua',
   'просп. Академіка Глушкова, 4д'),
  ('Факультет Географії та Психології', 10006, '+38044-521-3270',
   'psy-univ@ukr.net', 'проспект Академіка Глушкова, 2'),
  ('Факультет гуманітарниї наук', 10007, '+38044-425-1420',
   'svo@ukma.kiev.ua', 'вулиця Григорія Сковороди, 2'),
  ('Факультет інформатики', 10007, '+38044-463-6985', 'gor@ukma.kiev.ua',
   'вулиця Григорія Сковороди, 2, Київ'),
  ('Факультет гуманітарниї наук', 10007, '+38044-425-5188',
   'vakuliuk@ukma.edu.ua', 'вулиця Григорія Сковороди, 2'),
  ('Інститут геодезії', 10008, '+38032-258-2698', 'kornel@polynet.lviv.ua',
   'вул. Карпінського 6, 2-й н.к,'),
  ('Інститут економіки і менеджменту', 10008, '+38032-258-2210',
   'Oleh.Y.Kuzmin@lpnu.ua', 'вул. Митрополита Андрея 5, 4 -й н.к'),
  ('Інститут архітектури', 10008, '+38032-258-22-39',
   'kornel@polynet.lviv.ua', 'вул. С. Бандери 12, Головний корпус');