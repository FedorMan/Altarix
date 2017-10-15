INSERT INTO positions VALUES(1,'Менеджер');
INSERT INTO positions VALUES(2,'Младший разработчик');
INSERT INTO positions VALUES(3,'Разработчик');
INSERT INTO positions VALUES(4,'Старший разработчик');

INSERT INTO departments VALUES(1,'Управление','2000-02-02',null);
INSERT INTO employes VALUES(1,'Иванов','Иван','Иванович','М','1950-03-17','2000-02-02',NULL,1,120000,1,TRUE);
INSERT INTO employes VALUES(2,'Бруствер','Кингсли','Вакитович','М','1960-03-1','2000-02-02',NULL,1,110000,1,FALSE);

INSERT INTO departments VALUES(2,'Отдел разработки','2000-02-02',1);
INSERT INTO employes VALUES(3,'Ростенковски','Бернадетт','Мери','Ж','1980-07-13','2000-02-02',NULL,1,90000,2,TRUE );
INSERT INTO employes VALUES(4,'Воловиц','Говард','Джоэл','М','1979-07-13','2000-02-02',NULL,3,70000,2,FALSE);

INSERT INTO departments VALUES(3,'Отдел интеграции','2000-02-02',1);
INSERT INTO employes VALUES(5,'Петрова','Пенни','Александрова','Ж','1980-07-13','2000-02-02',NULL,1,90000,3,TRUE );
INSERT INTO employes VALUES(6,'Хофстедер','Леонард','Джоэл','М','1979-07-13','2000-02-02',NULL,3,70000,3,FALSE);

INSERT INTO departments VALUES(4,'Отдел внедрения','2000-02-02',3);
INSERT INTO employes VALUES(7,'Петрова','Наталья','Олеговна','Ж','1956-07-13','2000-02-02',NULL,1,90000,4,TRUE );
INSERT INTO employes VALUES(8,'Никулин','Василий','Николаевич','М','1967-07-13','2000-02-02',NULL,3,70000,4,FALSE);
INSERT INTO employes VALUES(9,'Васильева','Валерия','Арменовна','Ж','1989-07-13','2000-02-02',NULL,2,70000,4,FALSE);