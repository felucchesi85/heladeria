#Listar productos con su categoría, precio y stock
SELECT c.nombre AS categoria, p.nombre AS producto, p.precio, p.stock
FROM pro_producto p
JOIN cat_categoria c ON c.id_categoria = p.id_categoria
ORDER BY c.nombre, p.nombre;
#Filtrar por categoría específica
SELECT p.nombre, p.descripcion, p.precio, p.stock
FROM pro_producto p
JOIN cat_categoria c ON c.id_categoria = p.id_categoria
WHERE c.nombre = 'Helados por Caja (12 litros)'
ORDER BY p.nombre;
#Buscar por texto (ej.: “Combi”)
SELECT c.nombre AS categoria, p.nombre, p.precio, p.stock
FROM pro_producto p
JOIN cat_categoria c ON c.id_categoria = p.id_categoria
WHERE p.nombre LIKE '%Combi%'
ORDER BY c.nombre, p.nombre;
#Top N más caros / más baratos
SELECT p.nombre, p.precio, c.nombre AS categoria
FROM pro_producto p JOIN cat_categoria c ON c.id_categoria = p.id_categoria
ORDER BY p.precio DESC
LIMIT 10;
#Rango de precios
SELECT p.nombre, p.precio, c.nombre AS categoria
FROM pro_producto p JOIN cat_categoria c ON c.id_categoria = p.id_categoria
WHERE p.precio BETWEEN 10000 AND 30000
ORDER BY p.precio;
#Conteo por categoría
SELECT c.nombre AS categoria, COUNT(*) AS cantidad
FROM pro_producto p
JOIN cat_categoria c ON c.id_categoria = p.id_categoria
GROUP BY c.id_categoria, c.nombre
ORDER BY cantidad DESC;
#Precio promedio/mínimo/máximo por categoría
SELECT c.nombre AS categoria,
       ROUND(AVG(p.precio),2) AS precio_promedio,
       MIN(p.precio) AS precio_min,
       MAX(p.precio) AS precio_max
FROM pro_producto p
JOIN cat_categoria c ON c.id_categoria = p.id_categoria
GROUP BY c.id_categoria, c.nombre
ORDER BY c.nombre;
#Productos con stock 0 o nulo
SELECT c.nombre AS categoria, p.nombre, p.stock, p.precio
FROM pro_producto p
JOIN cat_categoria c ON c.id_categoria = p.id_categoria
WHERE p.stock IS NULL OR p.stock = 0
ORDER BY c.nombre, p.nombre;
#Duplicados por nombre (si sospechás repetidos)
SELECT p.nombre, COUNT(*) AS repeticiones
FROM pro_producto p
GROUP BY p.nombre
HAVING COUNT(*) > 1
ORDER BY repeticiones DESC, p.nombre;
#Categorías sin productos
SELECT c.nombre
FROM cat_categoria c
LEFT JOIN pro_producto p ON p.id_categoria = c.id_categoria
WHERE p.id_categoria IS NULL
ORDER BY c.nombre;