INSERT INTO users (`username`, `password`, `enabled`)
VALUES ('admin', '{bcrypt}$2a$10$skztL6zwvuGQnJLfxbNffe4Ekcgz4FNx5dUJMMNrdyzDWlt4ss4yS', 1),
       ('department_head', '{bcrypt}$2a$10$H2AYP31d9w.juKt87kXNg.QtU51ILjUxeWrAGabN0TGZ7M4K8pvcO', 1),
       ('department_head2', '{bcrypt}$2a$10$G6w.afyOcnN5DGHmCvAki.eWKycJGMft.4D155.Wq2yNdRHpaZ9.S', 1),
       ('department_head3', '{bcrypt}$2a$10$PED5Z8Ff55ViRsJE2H63w.5TfKCMrJ49jNCjVyZ3nYRw1l1eCrSL2', 0),
       ('department_head4', '{bcrypt}$2a$10$ApHedkcIFTnJHzpNzm4GVOCkSSMQvpUYG3iB.3pRHo6up43ZCGOaC', 1),
       ('project_manager', '{bcrypt}$2a$10$X0yz7fYgyRQmnLetsQ2eduIIU87meClNKqRCm6z7YtSCAWAyBnrXi', 1),
       ('project_manager2', '{bcrypt}$2a$10$5ViS1AEgVzVJiSobJsnuD.ZZDZdPFZXkAoBODUiG0obBgb1DzgX0e', 1),
       ('project_manager3', '{bcrypt}$2a$10$AHvvY4K5vMr5YF/DYilqmelMG5scBFJoOJuSYf5eUSaiWSPOTftHe', 0),
       ('project_manager4', '{bcrypt}$2a$10$Bvzt06AZNHFVwLnR3auCneTQ6vicgLS3VIcvFKY4YhFIhEumCUOZu', 1),
       ('employee', '{bcrypt}$2a$10$7EM5wnM1TSOQIpYrX.NyFuH2eTluP5Qc1tPiP5kAxvjDgxF9b39FS', 1),
       ('employee2', '{bcrypt}$2a$10$g5.TCGyccvpZFpecx3SbOu3riqQcc7piR1AosPB/uOuAoLsOmzIjS', 1),
       ('employee3', '{bcrypt}$2a$10$YzziyaQA/ROVevz2rFS3DOmbFwFEfRD7vUnM/LnRaBjZOV06tKLiK', 1),
       ('employee4', '{bcrypt}$2a$10$JQYfbjlEmGNFINKJa1jL.ul0zNbgE8uMLUOEAxaFM0iS93Z6b1vBS', 1),
       ('employee5', '{bcrypt}$2a$10$2e8m6F5SI/kcYgJqYG4hYuKdrBHafWC/WyxPfx7Y6F0c5yDKlgIxq', 0),
       ('employee6', '{bcrypt}$2a$10$jjLmzGbPh.PyeEU4kMP0GeSmQdAN3850sn5sTVZq/UvOakub1suyK', 1),
       ('employee7', '{bcrypt}$2a$10$cWJUJX3PxZwTOwOIeIJGkeOSh.f/Fdvo4BzeG7aWiARI0JcqJC/7.', 1),
       ('employee8', '{bcrypt}$2a$10$kJXXE5IKiwLpmrVii5L3EeHkurj/C.qgsta/XiHO6XdmUaLvLwJbO', 1),
       ('employee9', '{bcrypt}$2a$10$aU9OcIT6tBd9UHiBdTi/.eHuM3E1tTr2O5y0rbRX/Z/C3OEXrSbWC', 1),
       ('employee10', '{bcrypt}$2a$10$L3mhyZy31ee8wer9s1Zevu7NZHzt03euaLxzXdojB8PdDzumarTfm', 1),
       ('employee11', '{bcrypt}$2a$10$gcw5SEQxhfx1C0RN1sbDW.Vo/6EhjZCiGjtQJkdPd.IoHdmIXtqRS', 1),
       ('employee12', '{bcrypt}$2a$10$KzmmWcfzA9NZMx1X59y57.WDChQ3fsoHlV5cztVw/OvI9jJuzYI9m', 1);
INSERT INTO authorities (`username`, `authority`)
VALUES ('admin', 'ADMIN'),
       ('department_head', 'DEPARTMENT_HEAD'),
       ('department_head2', 'DEPARTMENT_HEAD'),
       ('department_head3', 'DEPARTMENT_HEAD'),
       ('department_head4', 'DEPARTMENT_HEAD'),
       ('project_manager', 'PROJECT_MANAGER'),
       ('project_manager2', 'PROJECT_MANAGER'),
       ('project_manager3', 'PROJECT_MANAGER'),
       ('project_manager4', 'PROJECT_MANAGER'),
       ('employee', 'EMPLOYEE'),
       ('employee2', 'EMPLOYEE'),
       ('employee3', 'EMPLOYEE'),
       ('employee4', 'EMPLOYEE'),
       ('employee5', 'EMPLOYEE'),
       ('employee6', 'EMPLOYEE'),
       ('employee7', 'EMPLOYEE'),
       ('employee8', 'EMPLOYEE'),
       ('employee9', 'EMPLOYEE'),
       ('employee10', 'EMPLOYEE'),
       ('employee11', 'EMPLOYEE'),
       ('employee12', 'EMPLOYEE');
INSERT INTO profile (`id`, `education`, `address`, `phone`, `email`, `skills`)
VALUES (1,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)123-12-12','mail@mail.ru',''),
       (2,'БГУ','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (3,'ПГУ','г. Минск пр-т. Независимости 4','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (4,'БНТУ','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (5,'МГЛУ','г. Минск пр-т. Независимости 4','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (6,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (7,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (8,'БГУИР','г. Минск пр-т. Независимости 4','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (9,'ПГУ','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (10,'ВГТУ','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (11,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (12,'БНТУ','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (13,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)112-12-12','fsdfsdffds@mail.ru',''),
       (14,'ВГТУ','г. Минск пр-т. Независимости 43','+375(33)143-12-12','fsdfsdffds@mail.ru',''),
       (15,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)023-12-12','fsdfsdffds@mail.ru',''),
       (16,'БГУИР','г. Минск пр-т. Независимости 4','+375(33)113-12-12','fsdfsdffds@mail.ru',''),
       (17,'ВГТУ','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (18,'БНТУ','г. Минск пр-т. Независимости 4','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (19,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (20,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru',''),
       (21,'БГУИР','г. Минск пр-т. Независимости 43','+375(33)123-12-12','fsdfsdffds@mail.ru','');
INSERT INTO employee (`id`, `department_id`, `profile_id`, `name`, `active`, `create_date`, `modified_date`, `username`)
VALUES (1,1,1,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','admin'),
       (2,1,2,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','department_head'),
       (3,3,3,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','department_head2'),
       (4,4,4,'Иванов Иван Иванович',0,'2022-04-17','2022-04-17','department_head3'),
       (5,5,5,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','department_head4'),
       (6,1,6,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','project_manager'),
       (7,4,7,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','project_manager2'),
       (8,3,8,'Иванов Иван Иванович',0,'2022-04-17','2022-04-17','project_manager3'),
       (9,5,9,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','project_manager4'),
       (10,1,10,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee'),
       (11,3,11,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee2'),
       (12,4,12,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee3'),
       (13,5,13,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee4'),
       (14,1,14,'Иванов Иван Иванович',0,'2022-04-17','2022-04-17','employee5'),
       (15,3,15,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee6'),
       (16,4,16,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee7'),
       (17,5,17,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee8'),
       (18,1,18,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee9'),
       (19,3,19,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee10'),
       (20,4,20,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee11'),
       (21,5,21,'Иванов Иван Иванович',1,'2022-04-17','2022-04-17','employee12');