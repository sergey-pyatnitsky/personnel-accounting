create table role(
	id int primary key auto_increment,
    name varchar(20) not null unique
);

create table user(
	id int primary key auto_increment,
    username varchar(10) not null unique,
    password varchar(16) not null unique,
    role_id int not null,
    active bool default false,
    foreign key (role_id) references role (id)
);

create table positions(
	id int primary key auto_increment,
    name varchar(40) not null unique
);

create table profile(
	id int primary key auto_increment,
    education varchar(2048),
    address varchar(256),
    phone varchar(60),
	email varchar(256),
    skills text
);

create table department(
	id int primary key auto_increment,
    name varchar(256) not null,
    active bool default false,
    start_date date not null,
    end_date date
);

create table project(
	id int primary key auto_increment,
    name varchar(256) not null,
    department_id int not null,
    active bool default false, 
    start_date date not null, 
    end_date date,
    foreign key (department_id) references department (id)
);

create table employee(
	id int primary key auto_increment,
    user_id int not null unique,
    department_id int, 
    profile_id int not null, 
    name varchar(256) not null,
    active bool default false, 
    create_date date not null,
    modified_date date,
    foreign key (profile_id) references profile (id) on delete cascade,    
    foreign key (department_id) references department (id) 
);

create table employee_position(
	id int primary key auto_increment,
    employee_id int not null, 
    position_id int not null, 
    project_id int not null, 
    department_id int not null, 
    active bool default false, 
    start_date date not null, 
    end_date date,
    foreign key (employee_id) references employee (id),
    foreign key (position_id) references positions (id),
    foreign key (department_id) references department (id),
    foreign key (project_id) references project (id)    
);

create table tack_status(
	id int primary key auto_increment,
    name varchar(10) not null unique
);

create table task(
	id int primary key auto_increment,
    name varchar(100) not null,
    description text, 
    project_id int not null, 
    reporter_id int not null, 
    assignee_id int not null, 
    status_id int not null, 
    create_date date not null, 
    modified_date date, 
    foreign key (project_id) references project (id),
    foreign key (reporter_id) references employee (id),
    foreign key (assignee_id) references employee (id),
    foreign key (status_id) references tack_status (id)
);

create table report_card(
	id int primary key auto_increment,
    date date not null, 
    task_id int not null,
    employee_id int not null,
    working_time time not null,
    foreign key (task_id) references task (id),
    foreign key (employee_id) references employee (id)
);