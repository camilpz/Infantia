-- Insertar roles
INSERT INTO role (name, description) VALUES ('TUTOR', 'Rol para padres');
INSERT INTO role (name, description) VALUES ( 'DIRECTOR', 'Rol para directores');
INSERT INTO role (name, description) VALUES ('MAESTRO', 'Rol para maestros');

-- Insertar tipos de documento
INSERT INTO document_type (name) VALUES ('DNI');
INSERT INTO document_type (name) VALUES ('PASAPORTE');

-- Insertar tipos de contacto
INSERT INTO contact_type (name) VALUES ('CELULAR');
INSERT INTO contact_type (name) VALUES ('TELEFONO_FIJO');
INSERT INTO contact_type (name) VALUES ('EMAIL_ADICIONAL');