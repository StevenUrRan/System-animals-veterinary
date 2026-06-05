-- =========================================================================
-- 1. INSERCIÓN DE ROLES (Si no existen)
-- =========================================================================
INSERT INTO roles (name, enable)
SELECT 'ROLE_ADMIN', true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name, enable)
SELECT 'ROLE_VETERINARIAN', true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_VETERINARIAN');

INSERT INTO roles (name, enable)
SELECT 'ROLE_KEEPER', true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_KEEPER');

INSERT INTO roles (name, enable)
SELECT 'ROLE_MANAGER', true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_MANAGER');

INSERT INTO roles (name, enable)
SELECT 'ROLE_AUDITOR', true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_AUDITOR');

INSERT INTO roles (name, enable)
SELECT 'ROLE_USER', true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');


-- =========================================================================
-- INSERCIÓN DEL ADMINISTRADOR CON HASH NATIVO (Contraseña: password123)
-- =========================================================================
INSERT INTO users (username, email, password, nit, document, gender, enable)
SELECT 
    'AdminPerfecto', 
    'admin.perfecto@system-animals.local', 
    '$2a$10$L4jNEOvlhS7kviyz6OstXeobrAsnVCJ8TZ4DjV4UCD8/1XxgN1VQG', -- Tu hash nativo real
    1000000010,   -- Nuevo NIT único
    'CC',         
    'MALE',       
    true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin.perfecto@system-animals.local');


-- =========================================================================
-- ASOCIACIÓN DE ROLES PARA EL ADMINISTRADOR PERFECTO (user_role)
-- =========================================================================
-- Vincula ROLE_ADMIN
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r
WHERE u.email = 'admin.perfecto@system-animals.local' 
  AND r.name = 'ROLE_ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM user_role ur 
      WHERE ur.user_id = u.id AND ur.role_id = r.id
  );

-- Vincula ROLE_AUDITOR
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r
WHERE u.email = 'admin.perfecto@system-animals.local' 
  AND r.name = 'ROLE_AUDITOR'
  AND NOT EXISTS (
      SELECT 1 FROM user_role ur 
      WHERE ur.user_id = u.id AND ur.role_id = r.id
  );