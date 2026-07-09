create table medicos (
    id bigserial not null primary key,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    telefone varchar(20) not null,
    crm varchar(6) not null unique,
    especialidade varchar(100) not null,
    logradouro varchar(100) not null,
    bairro varchar(100) not null,
    cep varchar(9) not null,
    complemento varchar(100),
    numero varchar(20) not null,
    uf char(2) not null,
    cidade varchar(100) not null,
    ativo boolean not null default true
);

create table pacientes (
    id bigserial not null primary key,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    cpf varchar(14) not null unique,
    telefone varchar(20) not null,
    logradouro varchar(100) not null,
    bairro varchar(100) not null,
    cep varchar(9) not null,
    complemento varchar(100),
    numero varchar(20) not null,
    uf char(2) not null,
    cidade varchar(100) not null,
    ativo boolean not null default true
);

create table usuarios (
    id bigserial not null primary key,
    login varchar(100) not null,
    senha varchar(255) not null
);

create table consultas (
    id bigserial not null primary key,
    medico_id bigint not null,
    paciente_id bigint not null,
    data timestamp not null,
    motivo_cancelamento varchar(100),
    constraint fk_consultas_medico_id foreign key (medico_id) references medicos(id),
    constraint fk_consultas_paciente_id foreign key (paciente_id) references pacientes(id)
);
