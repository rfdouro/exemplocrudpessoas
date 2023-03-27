create database minhabase;

create table minhabase.pessoa (
id bigint(20) not null auto_increment,
nome varchar(200) not null,
primary key (id)
) engine=InnoDB;

create table minhabase.tarefa (
id bigint(20) not null auto_increment primary key,
descricao varchar(255) not null,
`data` date default null,
idpessoa bigint(20) default null,
foreign key (idpessoa) references minhabase.pessoa (id) on delete cascade
) engine=InnoDB;


create table minhabase.endereco (
 id bigint(20) not null auto_increment,
 rua varchar(300) not null,
 idpessoa bigint(20) not null,
 primary key (id),
 key endereco_FK (idpessoa),
 constraint endereco_FK foreign key (idpessoa) REFERENCES minhabase.pessoa (id) ON DELETE CASCADE
) ENGINE=InnoDB;
