# PescaGo Backend

Servidor desarrollado con Java 17, Spring Boot 3 y PostgreSQL.

## 1. Instalación de dependencias

Para ejecutar este proyecto, necesitas configurar tu entorno local:

1. **Java Development Kit (JDK) 17**:
   - Descarga e instala [Eclipse Temurin 17](https://adoptium.net/es/temurin/releases?version=17&os=any&arch=any).
   - Asegúrate de marcar la opción "Set or override JAVA_HOME variable" y luego eliges "Entire feature will be installed on local hard drive" y procedes con la instalación.
   - Verifica la instalación abriendo una terminal y ejecutando: `java -version` (debe mostrar la versión 17).

2. **Apache Maven**:
   (Nota: Si no deseas instalar Maven manualmente, puedes saltar este paso. En el apartado de Ejecución usaremos el "Maven Wrapper" que ya viene incluido en el proyecto).
   - Descarga [Maven](https://maven.apache.org/download.cgi) de preferencia la linea 3.9.x el archivo que termina en -bin.zip.
   - Extrae la carpeta y la mueves a un lugar seguro como en tu disco `C:\apache-maven-3.9.x`
   - Configura la variable de entorno `PATH` apuntando a la carpeta `/bin` de Maven que seria para este caso `C:\apache-maven-3.9.x\bin`.
   - Verifica la instalación abriendo una terminal y ejecutando: `mvn --version`
   - Cierra tu IDE y vuelve a abrir todo el proyecto para aplicar los cambios.

3. **PostgreSQL**:
   - Instala PostgreSQL en tu máquina.
   - Abre pgAdmin, crea una base de datos nueva llamada `pescago` todo en minúsculas y deja todo lo demás por default.
   - Si deseaas, crea un nuevo usuario o usa el que ya tenias de base de datos con permisos totales sobre esa tabla y recuerda bien esos datos.

## 2. Configuración del entorno

1. Crea un archivo llamado `.env` en la raíz de la carpeta del backend (puedes copiarlo desde `.env.example`).
2. Edita el archivo `.env` con tus credenciales locales:
   - `DATASOURCE_URL`: jdbc:postgresql://localhost:5432/pescago
   - `DATASOURCE_USER`: (tu usuario de postgres)
   - `DATASOURCE_PASSWORD`: (tu contraseña de postgres)

## 3. Ejecución

Para iniciar el servidor, abre una terminal en la carpeta del backend y sigue estos pasos:

1. Dar permisos de ejecución al script (solo la primera vez):
   - Git bash: `chmod +x start-backend.sh`

2. Iniciar el servidor (Elige tu método)

   Opción A: Usando Maven (Si lo tienes instalado globalmente)
   - Git Bash: `./start-backend.sh`
   - CMD / PowerShell: `mvn spring-boot:run`

   Opción B: Usando Maven Wrapper (No requiere instalación de Maven)
   Si no quieres configurar variables de entorno, puedes usar el script incluido que descarga la versión necesaria automáticamente:
   - Git Bash: `./mvnw spring-boot:run`
   - CMD / PowerShell: `mvnw.cmd spring-boot:run`

El servidor arrancará en el puerto 8080.
