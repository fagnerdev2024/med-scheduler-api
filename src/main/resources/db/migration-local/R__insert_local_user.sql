-- Usuario de exemplo para testes locais
-- login: local@example.com
-- senha: password
DELETE FROM usuarios WHERE login = 'local@example.com';

insert into usuarios (login, senha)
values ('local@example.com', '$2a$10$r/pe8wGgAE0UYX3l9tzapOv5bkiOEjgY0ImbmzPJAFe8ni.3kyaXu');
