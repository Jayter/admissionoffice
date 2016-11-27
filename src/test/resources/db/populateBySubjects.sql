DELETE FROM exam_results;
DELETE FROM entrance_subjects;
DELETE FROM admins;
DELETE FROM enrollees;
DELETE FROM subjects;
DELETE FROM directions;
DELETE FROM faculties;
DELETE FROM universities;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO subjects(name) VALUES
  ('Українська мова та література'),
  ('Англійська мова'),
  ('Математика');