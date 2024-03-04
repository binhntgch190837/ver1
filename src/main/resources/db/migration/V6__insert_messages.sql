---
---Insert to chats entity
---
insert into chats (id,created_time,last_access,user1,user2) values
('38eacdbe-cbf9-11ed-afa1-0242ac120002',timestamp '2023-01-01 22:34:10',timestamp '2023-01-01 22:34:11','a00e70ea-c6c2-11ed-afa1-0242ac120002','6e7cc348-cbf8-11ed-afa1-0242ac120002'),
('c1151994-0757-11ee-be56-0242ac120002',timestamp '2023-02-01 22:34:14',timestamp '2023-02-01 22:34:19','a00e70ea-c6c2-11ed-afa1-0242ac120002','a00e6d84-c6c2-11ed-afa1-0242ac120002'),
('cf4125b2-0757-11ee-be56-0242ac120002',timestamp '2023-05-01 22:34:16',timestamp '2023-05-01 22:34:23','6e7cc348-cbf8-11ed-afa1-0242ac120002','a00e6d84-c6c2-11ed-afa1-0242ac120002'),
('09b19e1a-11c7-11ee-be56-0242ac120002',timestamp '2023-05-01 22:34:14',timestamp '2023-05-01 22:35:32','f21ae502-1191-11ee-be56-0242ac120002','abd271cc-1192-11ee-be56-0242ac120002'),
('3f4e176a-11c7-11ee-be56-0242ac120002',timestamp '2023-05-01 22:34:17',timestamp '2023-05-01 22:35:43','f21ae502-1191-11ee-be56-0242ac120002','14fed474-1193-11ee-be56-0242ac120002'),
('5684f8fe-11c7-11ee-be56-0242ac120002',timestamp '2023-05-01 22:34:18',timestamp '2023-05-01 22:34:54','abd271cc-1192-11ee-be56-0242ac120002','a00e70ea-c6c2-11ed-afa1-0242ac120002');


---
---Insert to messages entity for users
---
insert into messages (id,text,created_time,sender_id,chat_id) values
('e2640f30-08f9-11ee-be56-0242ac120002','Hello',timestamp '2023-02-01 22:34:10','a00e70ea-c6c2-11ed-afa1-0242ac120002','38eacdbe-cbf9-11ed-afa1-0242ac120002'),
('e264126e-08f9-11ee-be56-0242ac120002','Hi. How can i help you?',timestamp '2023-02-01 23:34:10','6e7cc348-cbf8-11ed-afa1-0242ac120002','38eacdbe-cbf9-11ed-afa1-0242ac120002'),
('e264148a-08f9-11ee-be56-0242ac120002','I''m curious about the new added books. Do you know where they came from?',timestamp '2023-02-01 23:36:10','a00e70ea-c6c2-11ed-afa1-0242ac120002','38eacdbe-cbf9-11ed-afa1-0242ac120002'),
('e26415f2-08f9-11ee-be56-0242ac120002','Oh! It is quite late. I will figure out and ask you later, sorry!',timestamp '2023-02-01 23:37:10','a00e70ea-c6c2-11ed-afa1-0242ac120002','38eacdbe-cbf9-11ed-afa1-0242ac120002'),
('e2641aca-08f9-11ee-be56-0242ac120002','No problem. You can ask me at anytime, sir.',timestamp '2023-02-01 23:39:10','6e7cc348-cbf8-11ed-afa1-0242ac120002','38eacdbe-cbf9-11ed-afa1-0242ac120002'),

('0eb60b88-08fa-11ee-be56-0242ac120002','Konichiwa!',timestamp '2023-02-01 23:34:10','a00e70ea-c6c2-11ed-afa1-0242ac120002','c1151994-0757-11ee-be56-0242ac120002'),
('0eb61056-08fa-11ee-be56-0242ac120002','Nani! You''re annoying! Get lost!',timestamp '2023-02-01 23:35:10','a00e6d84-c6c2-11ed-afa1-0242ac120002','c1151994-0757-11ee-be56-0242ac120002'),
('0eb61236-08fa-11ee-be56-0242ac120002','Ahihi! See you then.',timestamp '2023-02-01 23:37:10','a00e70ea-c6c2-11ed-afa1-0242ac120002','c1151994-0757-11ee-be56-0242ac120002'),

('1f3409f6-08fa-11ee-be56-0242ac120002','Hi. Can i ask you something?',timestamp '2023-03-01 20:00:10','a00e6d84-c6c2-11ed-afa1-0242ac120002','cf4125b2-0757-11ee-be56-0242ac120002'),
('1f340e1a-08fa-11ee-be56-0242ac120002','Sure. How can i help you?',timestamp '2023-03-01 20:02:10','6e7cc348-cbf8-11ed-afa1-0242ac120002','cf4125b2-0757-11ee-be56-0242ac120002'),
('1f340f64-08fa-11ee-be56-0242ac120002','I wonder there is any book name The Hammer of God left?',timestamp '2023-03-01 20:03:10','a00e6d84-c6c2-11ed-afa1-0242ac120002','cf4125b2-0757-11ee-be56-0242ac120002'),
('1f341086-08fa-11ee-be56-0242ac120002','Let me check, sir.',timestamp '2023-03-01 20:03:55','6e7cc348-cbf8-11ed-afa1-0242ac120002','cf4125b2-0757-11ee-be56-0242ac120002'),
('1f3411b2-08fa-11ee-be56-0242ac120002','Oh i''m very sorry sir. The Hammer of God were sold out yesterday. But i can promise you they will arrive to the store in 2 days.',timestamp '2023-07-01 20:11:55','6e7cc348-cbf8-11ed-afa1-0242ac120002','cf4125b2-0757-11ee-be56-0242ac120002'),
('1f3412c0-08fa-11ee-be56-0242ac120002','That''s fine. I can wait until they arrive.',timestamp '2023-03-01 20:14:55','a00e6d84-c6c2-11ed-afa1-0242ac120002','cf4125b2-0757-11ee-be56-0242ac120002'),
('3b532e82-08fa-11ee-be56-0242ac120002','Thank you for your patience sir. If you have further questions, please just let me know.',timestamp '2023-03-01 20:16:55','6e7cc348-cbf8-11ed-afa1-0242ac120002','cf4125b2-0757-11ee-be56-0242ac120002');





