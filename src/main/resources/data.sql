-- Semillas de ejemplo (se ejecutan al iniciar la app)
-- Idempotentes: INSERT IGNORE evita duplicados si ya existen (requiere UNIQUE en nombre).
INSERT IGNORE INTO cat_categoria (nombre) VALUES
  ('Helados'),
  ('Postres'),
  ('Bebidas');

-- ============================
-- CATEGORÍAS ADICIONALES
-- ============================
INSERT IGNORE INTO cat_categoria (nombre) VALUES
  ('Helados por Caja (12 litros)'),
  ('Postres Helados'),
  ('Panificados y Salados'),
  ('Empanadas'),
  ('Impulsivos Helados'),
  ('Simplot (Congelados)'),
  ('Glup''s Market'),
  ('Miguelas');

-- ============================
-- HELADOS POR CAJA (12 LITROS)
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Agua (12 L)', 'Frutilla; Limón; Maracuyá; Frambuesa; Naranja/Mango; Limón Menta y Jengibre', 22100.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Helados por Caja (12 litros)'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Agua (12 L)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Comunes (12 L)', 'Chantilly; Chocolate; Cielo; Dulce de Leche; Frutilla a la Crema; Vainilla', 29700.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Helados por Caja (12 litros)'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Comunes (12 L)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Especiales (12 L)', 'Granizado; Dulce de Leche Granizado; Crema Crunch; Arándano; Menta Granizada; Mantecol; Banana c/DdL; Flan c/DdL; Frutilla Nevada', 39000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Helados por Caja (12 litros)'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Especiales (12 L)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Premium (12 L)', 'Choc. c/Almendras; Choco Roger; Choc. Blanco; Choc. Suizo; Rusa; Bananita Dolce; Cookies; Cereza; Tramontana; Tiramisú; Súper Dulce; Mascarpone; Vainitella; DdL c/Nuez; Pistacho; Chocotorta', 50200.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Helados por Caja (12 litros)'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Premium (12 L)');

-- ============================
-- POSTRES HELADOS
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Mixto Lunch (26 u.)', NULL, 17700.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Postres Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Mixto Lunch (26 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Almendrado (26 u.)', NULL, 19500.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Postres Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Almendrado (26 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Bombón Suizo (26 u.)', NULL, 30600.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Postres Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Bombón Suizo (26 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Bombón Escocés (24 u.)', NULL, 28300.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Postres Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Bombón Escocés (24 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Tiramisú (26 u.)', NULL, 30500.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Postres Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Tiramisú (26 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Merengatta (26 u.)', NULL, 30500.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Postres Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Merengatta (26 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Torta Helada Grande', 'Sabores: Cookies, Chocotorta', 13900.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Postres Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Torta Helada Grande');

-- ============================
-- PANIFICADOS Y SALADOS
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Medialunas (40 gr x 168 u.)', NULL, 38500.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Medialunas (40 gr x 168 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Gran Medialuna (120 u.)', NULL, 37300.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Gran Medialuna (120 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Medialunas fraccionadas (168 u.)', NULL, 56550.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Medialunas fraccionadas (168 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Donut DDL (48 u.)', NULL, 33000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Donut DDL (48 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Donut bañadas con chocolate y granas (48 u.)', NULL, 36500.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Donut bañadas con chocolate y granas (48 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Baguetín (35 u.)', NULL, 12800.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Baguetín (35 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Churro relleno c/DdL (120 u.)', NULL, 74000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Churro relleno c/DdL (120 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Tortilla Tucumana (200 u.)', NULL, 44200.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Tortilla Tucumana (200 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Pan de Campo (10 u.)', NULL, 15680.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Pan de Campo (10 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Pan Campero Multicereal (10 u.)', NULL, 25000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Pan Campero Multicereal (10 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Pan Campero Blanco (10 u.)', NULL, 15680.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Pan Campero Blanco (10 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Panal de Membrillo (200 u.)', NULL, 52300.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Panal de Membrillo (200 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Ventanita Mixta (140 u.)', NULL, 31000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Ventanita Mixta (140 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Criollo (180 u.)', NULL, 33700.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Criollo (180 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Chipa (4 kg aprox)', NULL, 52500.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Chipa (4 kg aprox)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Gran Croissant (30 u.)', NULL, 22500.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Panificados y Salados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Gran Croissant (30 u.)');

-- ============================
-- EMPANADAS
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Empanadas surtidas (96 u.)', NULL, 68000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Empanadas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Empanadas surtidas (96 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Empanadas árabes (96 u.)', NULL, 68000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Empanadas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Empanadas árabes (96 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Empanadas osobuco (96 u.)', NULL, 68000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Empanadas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Empanadas osobuco (96 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Empanadas jamón y queso (96 u.)', NULL, 68000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Empanadas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Empanadas jamón y queso (96 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Empanadas criollas suaves (96 u.)', NULL, 68000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Empanadas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Empanadas criollas suaves (96 u.)');

-- ============================
-- IMPULSIVOS HELADOS
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Tacita 110cc (18 u.)', 'Sabores: Chantilly, Chocolate, DdL, Frutilla', 10700.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Tacita 110cc (18 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Tacita Combi 220cc (20 u.)', 'Sabores: Choco, DdL, Frutilla', 20800.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Tacita Combi 220cc (20 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Barrita de DdL 70cc (24 u.)', NULL, 14600.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Barrita de DdL 70cc (24 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Conobola 100cc (26 u.)', NULL, 33700.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Conobola 100cc (26 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Bomboncito (12 u.)', NULL, 21600.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Bomboncito (12 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Bombón Crocante 70cc (24 u.)', NULL, 23600.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Bombón Crocante 70cc (24 u.)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Palito (30 u.) – Frutilla/Limón/Naranja', NULL, 7400.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Palito (30 u.) – Frutilla/Limón/Naranja');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Palito (30 u.) – Crema/Dulce de Leche', NULL, 10900.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Palito (30 u.) – Crema/Dulce de Leche');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Palito Bombón (30 u.) – Crema/Dulce de Leche', NULL, 16000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Impulsivos Helados'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Palito Bombón (30 u.) – Crema/Dulce de Leche');

-- ============================
-- SIMPLOT (CONGELADOS)
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Corte fino 7 mm (18 kg)', NULL, 65000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Simplot (Congelados)'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Corte fino 7 mm (18 kg)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Papa corte tradicional 10x10', NULL, 53200.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Simplot (Congelados)'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Papa corte tradicional 10x10');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Smoothies (50 u.)', NULL, 66600.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Simplot (Congelados)'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Smoothies (50 u.)');

-- ============================
-- GLUP'S MARKET
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT '3 Lts (Cereza, Granizado, DdL)', NULL, 8000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='3 Lts (Cereza, Granizado, DdL)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT '3 Lts (Chocolate, Frutilla, Vainilla)', NULL, 8000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='3 Lts (Chocolate, Frutilla, Vainilla)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT '3 Lts (DdL, Frutilla, Chantilly)', NULL, 8000.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='3 Lts (DdL, Frutilla, Chantilly)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Barra Mixta', NULL, 6400.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Barra Mixta');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Barra Merengata', NULL, 8100.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Barra Merengata');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Barra Tiramisú', NULL, 8100.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Barra Tiramisú');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Barra Almendrado', NULL, 6400.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Barra Almendrado');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Chocotorta', NULL, 9400.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Chocotorta');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT 'Torta de Fresa', NULL, 9400.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Glup''s Market'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='Torta de Fresa');

-- ============================
-- MIGUELAS
-- ============================
INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT '3 Lts (Chocolate, Frutilla, Vainilla)', NULL, 7200.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Miguelas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='3 Lts (Chocolate, Frutilla, Vainilla)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT '3 Lts (DdL, Frutilla, Chantilly)', NULL, 7200.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Miguelas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='3 Lts (DdL, Frutilla, Chantilly)');

INSERT INTO pro_producto (nombre, descripcion, precio, stock, id_categoria)
SELECT '3 Lts (Cereza, Granizado, DdL)', NULL, 7200.00, 0, c.id_categoria
FROM cat_categoria c WHERE c.nombre='Miguelas'
AND NOT EXISTS (SELECT 1 FROM pro_producto p WHERE p.nombre='3 Lts (Cereza, Granizado, DdL)');
