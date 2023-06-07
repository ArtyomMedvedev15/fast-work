insert into type_work(id, type_work_date_create, type_work_describe, type_work_name)
values (777, '2023-01-23', 'TestTestTestTest', 'Type');
insert into type_work(id, type_work_date_create, type_work_describe, type_work_name)
values (778, '2023-01-23', 'TestTestTestTest', 'Test');
insert into type_work(id, type_work_date_create, type_work_describe, type_work_name)
values (999, '2023-01-23', 'TestTestTestTest', 'Delete');
insert into users(id, user_date_create, username,user_email, user_original_name, user_password, user_role, user_soname, user_status)
values (777, '2023-04-02','Login123','vcseca@fexbox.org','hirer', '$2a$10$5vL/ixWFMTtHwzhfCpqkCOxCgl9hB7pkZ5nBuTZK1athhq/IRckx2', 'ADMIN', 'Testtest', 'ACTIVE');
insert into users(id, user_date_create, username,user_email, user_original_name, user_password, user_role, user_soname, user_status)
values (881, '2023-04-02','Logi123s','user2@mail.tex','hirer', 'Horrison@123s', 'WORKER', 'Testtest', 'ACTIVE');
insert into users(id, user_date_create, username,user_email, user_original_name, user_password, user_role, user_soname, user_status)
values (780, '2023-04-02','Login124','user3@mail.tex','hirer', '$2a$10$5vL/ixWFMTtHwzhfCpqkCOxCgl9hB7pkZ5nBuTZK1athhq/IRckx2', 'ADMIN', 'Testtest', 'DELETED');
insert into users(id, user_date_create, username,user_email, user_original_name, user_password, user_role, user_soname, user_status)
values (781, '2023-04-02','Logi124s','user4@mail.tex','hirer', 'Horrison@123s', 'WORKER', 'Testtest', 'BLOCKED');
insert into work(id, work_count_person, work_date_create,work_describe, work_name,work_price, work_status, work_hirer_id,work_type_id)
values (777, 2, '2022-02-04', 'test', 'Unique', 22.3, 'OPEN', 777, 777);
insert into work(id, work_count_person, work_date_create,work_describe, work_name,work_price, work_status, work_hirer_id,work_type_id)
values (778, 2, '2022-02-04', 'test', 'Teste', 22.3, 'OPEN', 777, 777);
insert into work(id, work_count_person, work_date_create,work_describe, work_name,work_price, work_status, work_hirer_id,work_type_id)
values (779, 2, '2022-02-04', 'test', 'test2', 22.3, 'CLOSE', 777, 777);
insert into work(id, work_count_person, work_date_create,work_describe, work_name,work_price, work_status, work_hirer_id,work_type_id)
values (780, 2, '2022-02-04', 'test', 'test3', 22.3, 'EXPECTATION', 777, 777);
insert into location(id, location_city, location_country, location_date_create, x, y, location_region, location_street,location_work_id)
values (777, 'test', 'test', '2023-02-05', 78.23, 54.23, 'test', 'test', 778);
insert into users(id, user_date_create, user_email, user_original_name, user_password, user_role, user_soname, user_status)
values (778, '2023-04-02', 'vcseca@fexbox.org', 'worker', 'test', 'WORKER', 'test', 'ACTIVE');
insert into work_application(id, date_applicaton, status_work_application, work_id, worker_id)
values(777,'2023-01-23','EXPECTATION',779,881);

insert into work_application(id, date_applicaton, status_work_application, work_id, worker_id)
values(888,'2023-01-23','EXPECTATION',777,781);
insert into users_works(users_id, user_works_id) values(781,777);
insert into users_works(users_id, user_works_id) values(780,778);

insert into notification(id, date_send_notification, message_notification, name_topic_notification, status_notification)values(777,'2023-09-01','test','test','OTHER');
insert into notification(id, date_send_notification, message_notification, name_topic_notification, status_notification)values(778,'2023-09-01','test','delete','OTHER');
insert into refresh_token(id, expiry_date, token, user_id) values(787,'2023-04-12','token',777);
insert into refresh_token(id, expiry_date, token, user_id) values(788,'2023-04-12','token2',881);