# 🍦 Sistema de Facturación para Heladería

Aplicación Spring Boot para gestión de productos, clientes y facturación con generación de PDF.

## Stack
- Java 17, Spring Boot 3.3.2
- Spring MVC + Thymeleaf + Bootstrap
- Spring Data JPA (MySQL)
- PDFs: OpenPDF (LibrePDF) + Apache PDFBox

## Dependencias destacadas
- Web, Thymeleaf, Data JPA, Actuator
- MySQL Connector/J
- OpenPDF 1.3.39 y PDFBox 2.0.31

## Estructura del proyecto (Maven)
- Raíz (pom.xml): parent/aggregator (no ejecuta la app)
- Módulo app: ./heladeria (este directorio)

## Ejecutar
1) MySQL (Docker):
```bash
docker run -d --name mysql-heladeria -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=heladeria_db -p 3306:3306 mysql:8.0
docker exec -it mysql-heladeria mysql -uroot -proot -e "CREATE USER IF NOT EXISTS 'heladeria'@'%' IDENTIFIED WITH mysql_native_password BY 'heladeria'; GRANT ALL PRIVILEGES ON heladeria_db.* TO 'heladeria'@'%'; FLUSH PRIVILEGES;"
```
2) Configurar [application.properties](src/main/resources/application.properties)

3) Correr:
- Desde la raíz del repo:
```bash
mvn -pl heladeria spring-boot:run
```
- O dentro de este módulo:
```bash
mvn clean spring-boot:run
```

## Comandos útiles (multi-módulo)
```bash
# Compilar desde raíz solo el módulo app y dependencias
mvn -pl heladeria -am clean package

# Ejecutar tests del módulo
mvn -pl heladeria test
```

- Home: http://localhost:8080
- Actuator: http://localhost:8080/actuator/health

## Funcionalidades
- Productos: /productos (ABM)
- Clientes: /clientes (ABM)
- Facturas: /facturas (nueva, lista, detalle)
- PDF de factura: /facturas/{id}/pdf
- Estadísticas: /estadisticas

## Código relevante
- PDF: [`PdfFacturaService`](src/main/java/com/heladeria/sistema/service/PdfFacturaService.java)
- Controladores: [`ProductoController`](src/main/java/com/heladeria/sistema/controller/ProductoController.java), [`ClienteController`](src/main/java/com/heladeria/sistema/controller/ClienteController.java), [`FacturaController`](src/main/java/com/heladeria/sistema/controller/FacturaController.java), [`EstadisticasController`](src/main/java/com/heladeria/sistema/controller/EstadisticasController.java)

## Verificar DB y tablas JPA
```bash
# Ver tablas creadas por JPA
docker exec -it mysql-heladeria mysql -uroot -proot -D heladeria_db -e "SHOW TABLES;"
```
Si no ves tablas, asegurá que la app esté corriendo sin errores y que las entidades estén bajo `com.heladeria.sistema`.

## Semillas de datos (opcional)
Podés crear `src/main/resources/data.sql` con INSERTs. Con `spring.sql.init.mode=always` se ejecuta al iniciar.
Ejemplo (idempotente con INSERT IGNORE):
```sql
INSERT IGNORE INTO cat_categoria (nombre) VALUES
  ('Helados'),
  ('Postres'),
  ('Bebidas');
```

## Verificar tablas y cargar semillas
1) Con la app en marcha, listar tablas:
```bash
docker exec -it mysql-heladeria mysql -uroot -proot -D heladeria_db -e "SHOW TABLES;"
```
2) Cargar semillas (data.sql ya incluido):
- Reiniciá la app y verificá:
```bash
docker exec -it mysql-heladeria mysql -uheladeria -pheladeria -D heladeria_db -e "SELECT * FROM cat_categoria;"
```

## Conexión rápida a MySQL (Docker)
```bash
# Con usuario app (recomendado)
docker exec -it mysql-heladeria mysql -uheladeria -pheladeria -D heladeria_db

# Con root (te pide la clave luego)
docker exec -it mysql-heladeria mysql -uroot -p
# Password: root
```

## Troubleshooting MySQL
- Access denied for user 'root'@'localhost':
  1) Asegurate de escribir bien la clave (ej.: -proot o usar -p y escribirla cuando la pida).
  2) Verificá qué clave se configuró:
     ```bash
     docker inspect -f "{{range .Config.Env}}{{println .}}{{end}}" mysql-heladeria | grep MYSQL_ROOT_PASSWORD
     ```
  3) Si querés resetear la clave o recrear la DB rápida:
     ```bash
     docker rm -f mysql-heladeria
     docker run -d --name mysql-heladeria \
       -e MYSQL_ROOT_PASSWORD=root \
       -e MYSQL_DATABASE=heladeria_db \
       -p 3306:3306 mysql:8.0
     ```

- Verificar estado/puerto:
  ```bash
  docker ps --filter name=mysql-heladeria
  docker logs -f mysql-heladeria
  ```

## Próximos pasos recomendados
- Crear categorías/productos desde la UI y confirmar que aparecen en la base.
- Crear un cliente y emitir una factura de prueba; descargar el PDF.
- Añadir más semillas en `src/main/resources/data.sql` (productos, clientes).
- Ejecutar healthcheck: http://localhost:8080/actuator/health
- Si agregás más módulos, ejecutá desde raíz con `mvn -pl heladeria spring-boot:run`.
