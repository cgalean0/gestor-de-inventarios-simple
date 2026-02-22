


#  Inventory Management System API

Este proyecto es una API REST profesional para la gestión de inventarios, desarrollada con un enfoque en **Arquitectura Limpia**, **Escalabilidad** y **Trazabilidad**. Diseñada para ser ejecutada en entornos contenedorizados bajo principios de desarrollo moderno.

##  Características Principales

-   **Soft Delete:** Implementación de borrado lógico para preservar la integridad referencial y auditoría de datos.
    
-   **Stock Audit Trail:** Historial automatizado y paginado de cada movimiento de stock (Entradas, Salidas, Ajustes).
    
-   **Gestión Transaccional:** Uso de `@Transactional` para garantizar la consistencia ACID en operaciones complejas de inventario.
    
-   **Dockerización Completa:** Entorno reproducible con Docker Compose (App + MariaDB).
    
-   **Documentación Interactiva:** OpenAPI 3.0 (Swagger UI) para pruebas y especificación de endpoints.
    

----------

##  Stack Tecnológico

-   **Backend:** Java 17, Spring Boot 3, Spring Data JPA.
    
-   **Base de Datos:** MariaDB 11.
    
-   **Mapeo de Objetos:** MapStruct & Lombok.
    
-   **Documentación:** SpringDoc OpenAPI.
    
-   **Contenedores:** Docker & Docker Compose.
    
-   **OS de Desarrollo:** Arch Linux.
    

----------

##  Arquitectura y Decisiones de Diseño

Como parte de mi formación en **Ciencias de la Computación**, el sistema aplica conceptos avanzados de ingeniería:

### 1. Desacoplamiento de Responsabilidades (SRP)

Se aplicó el principio de responsabilidad única al separar el `ProductService` del `StockManagerService`. Esto permite que la lógica de auditoría sea independiente, facilitando el mantenimiento y permitiendo que otros módulos futuros (como un módulo de Ventas) utilicen el sistema de historial sin duplicar código.

### 2. Paginación y Rendimiento

Todas las consultas de historial están optimizadas mediante `Pageable`, evitando el desbordamiento de memoria (RAM) al manejar grandes volúmenes de datos.

### 3. Seguridad de Datos

El sistema utiliza variables de entorno (.env) para gestionar credenciales, siguiendo las mejores prácticas de seguridad de OWASP para evitar la exposición de secretos en el historial de Git.


### 4. Flujo de Transaccionalidad
Para garantizar la integridad de los datos, el sistema sigue un flujo estrictamente transaccional. El siguiente diagrama describe cómo se coordina la actualización del stock con el registro automático en el historial de auditoría:

[![](https://mermaid.ink/img/pako:eNp9lN1u2kAQhV9ltVcgOdThJ4ClpiKGC6SSIJveVEjVdj0hq9i77njthqA8TB-gV32EvFjHfylJUbjB3v3mzJ4zCwcuTQTc4xn8yEFLmCuxQ5FsNaOPyK3RefIdsH5PBVolVSq0ZT4TGVujiXJpfaMtmjg-xa3DIzAELJSEZZLG_5PhqkJDa-T9SmixA3yXXwdHygGkJlPW4P6UcPBW-D18flXSK4FK0GNnTlWA3a2uyWtjgZkCkPkOoR7boNCZkFI9_9EsAjaTNhexehQvK1Xjtt4_u7xchx5TWiKIDKrNjoqcGptvbro1uA5LMvDYrdIRiaoCrvbLiNAWCM4arSYDttBW2cZRM8HYMiDrs8_L-YztWVb2oN5Zfkt2QVuouaYh6fkes3dofrJlCTVUdbTFg4TUKtMYgTijIFLAxmjx_CtWkXilVx_PF7HMY4FM51AYJhKTU8ydLE8EfkDIrMDu2zLynYkCOmlt7Xg_oP0y-C_r-WyzYA1BJhebVvsj-_Sv4I00XTOPIUiD0YrmmJC9tovDJF0AMkHTsCo1DksM5W6OulN1pdGerwqUcsf9K6g94_I6XAQb-trc1Nl_S-oLyDq9Xq976pBlhya4wqjoxHyacdNVYZ3i-bdoF1YiTaGNEnTEHb5DFXHPYg4OTwATUb7yQ4lsub0j91vu0WMk8H7Lt_qJauhH8NWYpC1Dk-_uuHcraN4Oz9NI2PZf4mUVqRugX2bPvelkWolw78AfuDea9iaji35_6F647mgymg4dvude3x33JuPz0aTvDs77oyeHP1ZN3d5kMHDHg9HYHZ8PB_3h8OkvFw56zg?type=png)](https://mermaid.live/edit#pako:eNp9lN1u2kAQhV9ltVcgOdThJ4ClpiKGC6SSIJveVEjVdj0hq9i77njthqA8TB-gV32EvFjHfylJUbjB3v3mzJ4zCwcuTQTc4xn8yEFLmCuxQ5FsNaOPyK3RefIdsH5PBVolVSq0ZT4TGVujiXJpfaMtmjg-xa3DIzAELJSEZZLG_5PhqkJDa-T9SmixA3yXXwdHygGkJlPW4P6UcPBW-D18flXSK4FK0GNnTlWA3a2uyWtjgZkCkPkOoR7boNCZkFI9_9EsAjaTNhexehQvK1Xjtt4_u7xchx5TWiKIDKrNjoqcGptvbro1uA5LMvDYrdIRiaoCrvbLiNAWCM4arSYDttBW2cZRM8HYMiDrs8_L-YztWVb2oN5Zfkt2QVuouaYh6fkes3dofrJlCTVUdbTFg4TUKtMYgTijIFLAxmjx_CtWkXilVx_PF7HMY4FM51AYJhKTU8ydLE8EfkDIrMDu2zLynYkCOmlt7Xg_oP0y-C_r-WyzYA1BJhebVvsj-_Sv4I00XTOPIUiD0YrmmJC9tovDJF0AMkHTsCo1DksM5W6OulN1pdGerwqUcsf9K6g94_I6XAQb-trc1Nl_S-oLyDq9Xq976pBlhya4wqjoxHyacdNVYZ3i-bdoF1YiTaGNEnTEHb5DFXHPYg4OTwATUb7yQ4lsub0j91vu0WMk8H7Lt_qJauhH8NWYpC1Dk-_uuHcraN4Oz9NI2PZf4mUVqRugX2bPvelkWolw78AfuDea9iaji35_6F647mgymg4dvude3x33JuPz0aTvDs77oyeHP1ZN3d5kMHDHg9HYHZ8PB_3h8OkvFw56zg)



----------

##  Guía de Inicio Rápido

### Requisitos previos

-   Docker y Docker Compose instalados.
    
-   (Opcional) Java 17 y Maven para desarrollo local.
    

### Instalación y Despliegue

1.  Clona el repositorio:


    
    ```bash
    git clone https://github.com/tu-usuario/inventario-simple-app.git
    cd inventario-simple-app
    
    ```
    
2.  Configura tus credenciales en el archivo `.env` (usa el archivo `.env.example` como base).
    
3.  Levanta la infraestructura:

    ```bash
    docker compose up --build
    
    ```
    
4.  Accede a la documentación:
    
    Navega a `http://localhost:8080/swagger-ui/index.html` para probar los endpoints.
    

----------

## Endpoints Destacados

-   `GET /api/products`: Listado de productos activos (filtrado automático de eliminados).
    
-   `POST /api/stock/increase/{id}`: Incremento de stock con registro automático en historial.
    
-   `GET /api/stock?type=SALIDA`: Consulta paginada de movimientos filtrados por tipo.
    
    
![enter image description here](https://private-user-images.githubusercontent.com/109038060/553074378-e7c6f024-31b5-478b-bc33-752c07d04f91.png?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NzE3MjEzNDUsIm5iZiI6MTc3MTcyMTA0NSwicGF0aCI6Ii8xMDkwMzgwNjAvNTUzMDc0Mzc4LWU3YzZmMDI0LTMxYjUtNDc4Yi1iYzMzLTc1MmMwN2QwNGY5MS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjYwMjIyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI2MDIyMlQwMDQ0MDVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xYzYwOWFjMzlmNDMxOTEwMzc0ZTQyYjRkMmZhNjRmZjFkZDU3ZDI0NTVkMGViMjRjMTEzYTVlM2RhNzJiYTZmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.HjromOjmb55ZVQB8c_Khk8V6WSIDzIyD9jcXhCZII20)
----------

##  Autor

-   **cgalean0** - _Estudiante de Lic. en Ciencias de la Computación_ - [Tu GitHub](https://github.com/cgalean0)
    

----------
