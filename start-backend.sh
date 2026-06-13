#!/bin/bash

echo "Starting PescaGo Backend..."

# 1. Moverse automáticamente a la carpeta donde está este script (funcionará en cualquier PC)
cd "$(dirname "$0")"

# 2. Cargar las variables de entorno desde el archivo .env local
if [ -f .env ]; then
    echo "Loading environment variables from .env..."
    # Lee el .env ignorando los comentarios y exporta las variables
    export $(grep -v '^#' .env | xargs)
else
    echo "ERROR: .env file not found!"
    echo "Please copy .env.example to .env and configure your local PostgreSQL credentials."
    exit 1
fi

echo "Booting up Spring Boot..."

# 3. Ejecutar el servidor
mvn spring-boot:run