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
