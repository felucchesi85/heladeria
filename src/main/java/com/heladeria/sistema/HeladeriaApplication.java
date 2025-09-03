package com.heladeria.sistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;

/**
 * Clase principal de la aplicación de facturación para heladería.
 * 
 * Sistema de facturación portable diseñado para funcionar en notebooks
 * sin necesidad de conexión constante a internet.
 * 
 * Características principales:
 * - Gestión de clientes, productos y facturas
 * - Generación de facturas en PDF
 * - Historial de facturación
 * - Funcionamiento offline
 * 
 * @author Sistema Heladería
 * @version 1.0.0
 */
@SpringBootApplication
public class HeladeriaApplication {

    private static final Logger log = LoggerFactory.getLogger(HeladeriaApplication.class);

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("🍦 SISTEMA DE FACTURACIÓN HELADERÍA");
        System.out.println("📱 Versión: 1.0.0");
        System.out.println("💻 Modo: Desarrollo");
        System.out.println("=================================================");
        
        SpringApplication.run(HeladeriaApplication.class, args);
        
        System.out.println("✅ Aplicación iniciada correctamente");
        System.out.println("🌐 URL: http://localhost:8080");
        System.out.println("📋 Panel Admin: http://localhost:8080/admin");
    }

    @Bean
    CommandLineRunner dbCheck(DataSource dataSource) {
        return args -> {
            try (var conn = dataSource.getConnection();
                 var st = conn.createStatement();
                 var rs = st.executeQuery("SELECT 1")) {
                if (rs.next()) {
                    log.info("✅ Conexión a MySQL OK (SELECT 1 = {})", rs.getInt(1));
                    var md = conn.getMetaData();
                    log.info("🔌 DataSource URL: {}", md.getURL());
                    log.info("🗄️ Base de datos: {} {}", md.getDatabaseProductName(), md.getDatabaseProductVersion());
                }
            } catch (Exception e) {
                log.error("❌ Error de conexión a MySQL: {}", e.getMessage(), e);
            }

            // Comprobar datos sembrados (si existen las tablas)
            try (var conn = dataSource.getConnection(); var st = conn.createStatement()) {
                try (var rs = st.executeQuery("SELECT COUNT(*) FROM cat_categoria")) {
                    if (rs.next()) log.info("📦 cat_categoria: {} filas", rs.getLong(1));
                } catch (Exception ignore) {
                    log.debug("Tabla cat_categoria no existe aún.");
                }
                try (var rs = st.executeQuery("SELECT COUNT(*) FROM pro_producto")) {
                    if (rs.next()) log.info("📦 pro_producto: {} filas", rs.getLong(1));
                } catch (Exception ignore) {
                    log.debug("Tabla pro_producto no existe aún.");
                }
            } catch (Exception e) {
                log.warn("No se pudo verificar conteos de tablas: {}", e.getMessage());
            }
        };
    }
}
