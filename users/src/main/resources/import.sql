-- Insertar roles
INSERT INTO Role (name, description) VALUES ('TUTOR', 'Rol para padres');
INSERT INTO Role (name, description) VALUES ('DIRECTOR', 'Rol para directores');
INSERT INTO Role (name, description) VALUES ('MAESTRO', 'Rol para maestros');

-- Insertar tipos de documento
INSERT INTO Document_type (name) VALUES ('DNI');
INSERT INTO Document_type (name) VALUES ('PASAPORTE');

-- Insertar tipos de contacto
INSERT INTO Contact_type (name) VALUES ('CELULAR');
INSERT INTO Contact_type (name) VALUES ('TELEFONO_FIJO');
INSERT INTO Contact_type (name) VALUES ('EMAIL_ADICIONAL');