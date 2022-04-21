alter table employee drop column user_id;
alter table employee add username varchar(50) not null unique;
alter table employee add foreign key(username) references users(username);