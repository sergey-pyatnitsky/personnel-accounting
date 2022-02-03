create table user(
	id bigint primary key auto_increment,
    username varchar(10) not null unique,
    password varchar(16) not null unique,
    role varchar(20) not null,
    active bool default false
);

create table positions(
    id bigint primary key auto_increment,
    name varchar(40) not null unique
);

create table profile(
	id bigint primary key auto_increment,
    education varchar(2048),
    address varchar(256),
    phone varchar(60),
	email varchar(256),
    skills text
);

create table department(
	id bigint primary key auto_increment,
    name varchar(256) not null,
    active bool default false,
    start_date date,
    end_date date,
    create_date date not null,
    modified_date date
);

create table project(
	id bigint primary key auto_increment,
    name varchar(256) not null,
    department_id bigint not null,
    active bool default false, 
    start_date date,
    end_date date,
    create_date date not null,
    modified_date date,
    foreign key (department_id) references department (id)
);

create table employee(
	id bigint primary key auto_increment,
    user_id int not null unique,
    department_id bigint,
    profile_id bigint not null,
    name varchar(256) not null,
    active bool default false, 
    create_date date not null,
    modified_date date,
    foreign key (profile_id) references profile (id) on delete cascade,    
    foreign key (department_id) references department (id) 
);

create table employee_position(
	id bigint primary key auto_increment,
    employee_id bigint not null,
    position_id bigint not null,
    project_id bigint not null,
    department_id bigint not null,
    active bool default false, 
    start_date date,
    end_date date,
    create_date date not null,
    modified_date date,
    foreign key (employee_id) references employee (id),
    foreign key (position_id) references positions (id),
    foreign key (department_id) references department (id),
    foreign key (project_id) references project (id)    
);

create table task(
	id bigint primary key auto_increment,
    name varchar(100) not null,
    description text, 
    project_id bigint not null,
    reporter_id bigint not null,
    assignee_id bigint not null,
    status varchar(10) not null,
    create_date date not null, 
    modified_date date, 
    foreign key (project_id) references project (id),
    foreign key (reporter_id) references employee (id),
    foreign key (assignee_id) references employee (id)
);

create table report_card(
	id bigint primary key auto_increment,
    date date not null, 
    task_id bigint not null,
    employee_id bigint not null,
    working_time time not null,
    create_date date not null,
    modified_date date,
    foreign key (task_id) references task (id),
    foreign key (employee_id) references employee (id)
);