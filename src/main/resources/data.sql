insert into user_details(id,birth_date,name)
values(1001,current_date(),'guru');

insert into user_details(id,birth_date,name)
values(1002,current_date(),'ranga');

insert into user_details(id,birth_date,name)
values(1003,current_date(),'ragu');

insert into Post(id,description,user_id)
values(2001,'I want to learn spring boot',1001);

insert into Post(id,description,user_id)
values(2002,'I want to learn DevOps',1001);

insert into Post(id,description,user_id)
values(2003,'I want to learn AWS',1003);

insert into Post(id,description,user_id)
values(2004,'I want to learn Azure',1003);