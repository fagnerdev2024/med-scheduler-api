-- Usuario de exemplo para testes no ambiente dev
-- login: dev@example.com
-- senha: password
DELETE FROM usuarios WHERE login = 'dev@example.com';

insert into usuarios (login, senha)
values ('dev@example.com', '$2a$10$r/pe8wGgAE0UYX3l9tzapOv5bkiOEjgY0ImbmzPJAFe8ni.3kyaXu');
