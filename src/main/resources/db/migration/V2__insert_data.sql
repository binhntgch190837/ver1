---
---Insert to roles entity
---
insert into roles (name) values ('admin'),('user');

---
---Insert to users entity for admin
---

insert into users (id,username,image_url,email,phone_number,last_updated,password,user_role) values
--- Admin test account: adminno1@gmail.com
--- Password: Admin2000#
('a00e6d84-c6c2-11ed-afa1-0242ac120002','AdminNo1','https://greenwichbook.blob.core.windows.net/thegioisach/admin/admin.jpg','adminno1@gmail.com','01663403287',timestamp '2021-11-11 17:30:00','$2a$12$UCTdC2fCVD0kADyVvj04/.P05FmbLWdQvNg.z2Er7RBBs57XunHnq',1);

---
---Insert to users entity for users
---
insert into users (id,username,image_url,email,phone_number,status,last_updated,password,user_role) values
--- User test account 1: userno1@gmail.com
--- Password: User3000$
('a00e70ea-c6c2-11ed-afa1-0242ac120002','UserNo1','https://greenwichbook.blob.core.windows.net/thegioisach/user/user.jpg','userno1@gmail.com','0144222543','Enabled',timestamp '2022-03-02 21:35:08','$2a$12$ZPxO9r9vijv8u2rbwbne1OzkMIoY22RaO7XnYsUrgDLtfhnIlC8AS',2),

--- User test account 2: usersupreme007@gmail.com
--- Password: User007%
('6e7cc348-cbf8-11ed-afa1-0242ac120002','UserSupreme','https://greenwichbook.blob.core.windows.net/thegioisach/user/user_supreme.jpg','usersupreme007@gmail.com','0987263176','Enabled',timestamp '2022-09-07 20:18:00','$2a$12$obXp50BXPX5wshSjw4uYSOGBwQHme3DyjlsaEhIZ61P9JucyX3TgG',2),

--- User test account 3: hongxiem22@fpt.edu.vn
--- Password: Hongxiemfpt12$$
('f21ae502-1191-11ee-be56-0242ac120002','HongXiemFPT','https://greenwichbook.blob.core.windows.net/thegioisach/user/hongxiemGW.jpg','hongxiem22@fpt.edu.vn','0456833172','Enabled',timestamp '2022-09-07 20:30:00','$2a$12$yLw8.MBXZw/6lKqeG3Io4.o8sIsPq/ggdaJ6xx2row5FL.FEGRIR6',2),

--- User test account 4: hatrung1998@fpt.edu.vn
--- Password: HaTrung9889&&
('abd271cc-1192-11ee-be56-0242ac120002','Ha The Trung','https://greenwichbook.blob.core.windows.net/thegioisach/user/hatrung98.jpg','hatrung1998@fpt.edu.vn','0123456789','Enabled',timestamp '2022-09-08 20:20:00','$2a$12$SAX0wBR8V467kBf.44UxoujzbNqWrHA1fuf9UpAdhIGi/Nni.2xoK',2),

--- User test account 5: thanhBinh2k@fpt.edu.vn
--- Password: ThanhBinh123++
('14fed474-1193-11ee-be56-0242ac120002','Nguyen Thanh Binh','https://greenwichbook.blob.core.windows.net/thegioisach/user/thanhbinh22.jpg','thanhBinh2k@fpt.edu.vn','0987654321','Disabled',timestamp '2022-09-08 20:24:00','$2a$12$h.0pA7NrEVUks4BB2nH7FucSembmqKlwfxLi2QprTpTIIt8kDuWay',2);

---
---Insert to user_friends entity
---
insert into user_friends(user_id, friend_id) values
('a00e70ea-c6c2-11ed-afa1-0242ac120002','6e7cc348-cbf8-11ed-afa1-0242ac120002'),
('a00e6d84-c6c2-11ed-afa1-0242ac120002','abd271cc-1192-11ee-be56-0242ac120002'),
('6e7cc348-cbf8-11ed-afa1-0242ac120002','14fed474-1193-11ee-be56-0242ac120002'),
('f21ae502-1191-11ee-be56-0242ac120002','a00e70ea-c6c2-11ed-afa1-0242ac120002'),
('a00e70ea-c6c2-11ed-afa1-0242ac120002','a00e6d84-c6c2-11ed-afa1-0242ac120002'),
('f21ae502-1191-11ee-be56-0242ac120002','abd271cc-1192-11ee-be56-0242ac120002');

---
---Insert to categories entity
---
insert into categories (name) values
--- Currently 21 categories: row 1 has 8, row 2 has 16, row 3 has 21.
('Education'),('Business'),('Cookery'),('Law & Politics'),('Romance'),('Thriller'),('Computer'),('Art'),
('Horror'),('Science'),('Fantasy'),('History'),('Religion'),('Travel'),('Science Fiction'),('Entertainment'),
('Health & Fitness'),('Sport'),('Nature'),('Language & Literature'),('Mystery & Crime');