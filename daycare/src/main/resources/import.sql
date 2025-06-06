-- Insertar roles
INSERT INTO role (name, description) VALUES ('TUTOR', 'Rol para padres');
INSERT INTO role (name, description) VALUES ('DIRECTOR', 'Rol para directores');
INSERT INTO role (name, description) VALUES ('MAESTRO', 'Rol para maestros');
INSERT INTO role (name, description) VALUES ('ADMINISTRADOR', 'Rol para administradores');

-- Insertar tipos de documento
INSERT INTO document_type (name) VALUES ('DNI');
INSERT INTO document_type (name) VALUES ('PASAPORTE');

-- Insertar tipos de contacto
INSERT INTO contact_type (name) VALUES ('CELULAR');
INSERT INTO contact_type (name) VALUES ('TELEFONO_FIJO');
INSERT INTO contact_type (name) VALUES ('EMAIL_ADICIONAL');

-- Insertar tipos de titulos
INSERT INTO title (name) VALUES ('Profesor/a de Educación Inicial');
INSERT INTO title (name) VALUES ('Licenciado/a en Educación Inicial');
INSERT INTO title (name) VALUES ('Técnico/a Superior en Nivel Inicial');
INSERT INTO title (name) VALUES ('Psicopedagogo/a');
INSERT INTO title (name) VALUES ('Licenciado/a en Psicología (con especialización en niñez)');
INSERT INTO title (name) VALUES ('Diplomatura en Jardín Maternal');
INSERT INTO title (name) VALUES ('Máster en Atención Temprana');
INSERT INTO title (name) VALUES ('Especialista en Desarrollo Infantil');