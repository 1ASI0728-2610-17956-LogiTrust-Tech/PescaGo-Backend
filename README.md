# PescaGo Backend

Servidor desarrollado con Java 17, Spring Boot 3 y PostgreSQL.

## 1. Instalación de dependencias
Para ejecutar este proyecto, necesitas configurar tu entorno local:

1. **Java Development Kit (JDK) 17**: 
   - Descarga e instala [Eclipse Temurin 17](https://adoptium.net/es/temurin/releases?version=17&os=any&arch=any).
   - Asegúrate de marcar la opción "Set JAVA_HOME variable" durante la instalación.
   - Verifica la instalación abriendo una terminal y ejecutando: `java -version` (debe mostrar la versión 17).

2. **Apache Maven**:
   - Descarga [Maven](https://maven.apache.org/download.cgi).
   - Configura la variable de entorno `PATH` apuntando a la carpeta `/bin` de Maven.

3. **PostgreSQL**:
   - Instala PostgreSQL en tu máquina.
   - Abre pgAdmin, crea una base de datos nueva llamada `pescago` todo en minúsculas.
   - Crea un usuario de base de datos con permisos totales sobre esa tabla y recuerda bien esos datos.

## 2. Configuración del entorno
1. Crea un archivo llamado `.env` en la raíz de la carpeta (puedes copiarlo desde `.env.example`).
2. Edita el archivo `.env` con tus credenciales locales:
   - `DATASOURCE_URL`: jdbc:postgresql://localhost:5432/pescago
   - `DATASOURCE_USER`: (tu usuario de postgres)
   - `DATASOURCE_PASSWORD`: (tu contraseña de postgres)

## 3. Ejecución
Para iniciar el backend primero navega hacia la carpeta del backend, abre Git Bash en esa carpeta y sigue estos pasos:

1. Dar permisos de ejecución al script (solo la primera vez):
   `chmod +x start-backend.sh`

2. Iniciar el servidor:
   `./start-backend.sh`

El servidor arrancará en el puerto 8080.