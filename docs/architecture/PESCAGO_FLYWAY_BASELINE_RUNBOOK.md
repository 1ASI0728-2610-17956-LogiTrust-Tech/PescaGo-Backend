# PescaGo — Runbook de baseline Flyway (PostgreSQL legacy `pescago`)

**Rama de trabajo:** `feature/pescago-v2-flyway-baseline-readiness`
**Fecha de preparación:** 2026-06-22
**Base de referencia:** `main` @ `00b44b9` (merge PR #6 — `feat: add guarded Flyway initial schema`)
**Migración de referencia:** `src/main/resources/db/migration/V1__initial_schema.sql`
**Flyway objetivo:** 11.20.0 (alineado con `pom.xml`)

> **Alcance de este documento:** evidencia de readiness, ensayo aislado de baseline y procedimiento operativo.
> **No se ha ejecutado baseline, migrate ni ninguna mutación sobre la base real `pescago`.**

---

## 1. Evidencia de readiness de la base real `pescago`

Auditoría JDBC read-only ejecutada el 2026-06-22 con las siguientes salvaguardas:

- `connection.setReadOnly(true)`
- `setAutoCommit(false)`
- `SET TRANSACTION READ ONLY`
- `rollback()` al cerrar la sesión

| Verificación | Resultado |
|--------------|-----------|
| Base de datos actual | `pescago` |
| Esquema actual | `public` |
| Tablas de negocio esperadas (6) | Presentes y coincidentes |
| Tablas de negocio inesperadas | Ninguna |
| Tablas faltantes | Ninguna |
| Privilegio `CREATE` en esquema `public` | **Sí** |
| Conexiones activas a `pescago` | 1 (sesión de auditoría) |
| `pg_dump` en PATH | **No disponible** (`PG_DUMP_NOT_IN_PATH`) |
| SHA-256 de `V1__initial_schema.sql` | `169D02583FD4057F2A8399CABA5FB064193BBF8D470C681E3B564111D0749F15` |
| Estado global de readiness | **READINESS_OK** |

### Tablas de negocio confirmadas

`users`, `carriers`, `entreprenuers`, `requests`, `hired_services`, `receipts`

---

## 2. Estado de `flyway_schema_history` en `pescago`

| Aspecto | Estado |
|---------|--------|
| Tabla `public.flyway_schema_history` | **No existe** |
| Versión Flyway registrada | N/A |
| Implicación | La base legacy nunca ha sido gestionada por Flyway; el primer paso operativo será `baseline` en versión `1`, no `migrate` de `V1` |

---

## 3. Conteos de tablas y distribución de estados

### Conteo de filas por tabla (sin exposición de registros)

| Tabla | Filas |
|-------|------:|
| `users` | 6 |
| `carriers` | 3 |
| `entreprenuers` | 3 |
| `requests` | 0 |
| `hired_services` | 0 |
| `receipts` | 0 |

### Distribución agrupada de `status`

| Tabla | `status` | Filas |
|-------|----------|------:|
| `requests` | *(sin filas)* | — |
| `hired_services` | *(sin filas)* | — |

> Ambas tablas están vacías en el momento de la auditoría; no hay valores de `status` que agrupar.

---

## 4. Resultado del ensayo temporal de baseline

Ensayo ejecutado en base temporal aislada (prefijo `pescago_flyway_baseline_rehearsal_`), **nunca** apuntando a `pescago`.

| Paso | Resultado |
|------|-----------|
| Creación de BD temporal | OK — `pescago_flyway_baseline_rehearsal_20260622_020800` |
| Confirmación de que `pescago` no es destino | OK |
| Aplicación directa de `V1__initial_schema.sql` por JDBC (sin `flyway_schema_history`) | OK |
| `Flyway.baseline()` versión `1`, descripción `Legacy schema baseline` | OK |
| Creación de `flyway_schema_history` | OK |
| Registro baseline: versión `1`, tipo `BASELINE`, éxito `true` | OK |
| Seis tablas de negocio intactas tras baseline | OK |
| `Flyway.validate()` | OK — 2 migraciones validadas |
| `Flyway.migrate()` | OK — esquema en versión `1`, **0 migraciones ejecutadas** (no reintenta `V1`) |
| Eliminación de BD temporal | OK — `TEMP_DB_DROPPED` |
| Estado global del ensayo | **REHEARSAL_OK** |

Motor PostgreSQL del ensayo: 18.4 (instancia local de desarrollo).

---

## 5. Condiciones obligatorias antes de ejecutar baseline en `pescago`

No proceder hasta cumplir **todas** las condiciones siguientes:

1. **Backup verificable**
   - Generar dump lógico completo de `pescago` con herramienta compatible (p. ej. `pg_dump`).
   - Verificar integridad del archivo (tamaño > 0, restauración de prueba en entorno aislado o `pg_restore --list` / inspección del SQL).
   - Registrar fecha, responsable y ubicación segura del artefacto (sin incluir credenciales en el registro).

2. **Aplicación detenida**
   - Ninguna instancia de Spring Boot u otro cliente de escritura conectado a `pescago`.
   - Confirmar conexiones activas ≈ 0 (salvo sesión de mantenimiento del operador).

3. **`FLYWAY_ENABLED=false`**
   - Mantener deshabilitado en el entorno objetivo hasta que el baseline manual haya sido revisado y aprobado.
   - Evita que un arranque accidental de la aplicación invoque Flyway automáticamente.

4. **Revisión humana**
   - Dos personas (o rol operador + rol técnico) revisan este runbook, los conteos de la sección 3 y el hash SHA-256 de `V1__initial_schema.sql`.
   - Aprobación explícita documentada antes de la ventana de cambio.

5. **Confirmación de esquema**
   - Re-ejecutar auditoría read-only y confirmar: 6 tablas legacy, sin `flyway_schema_history`, sin tablas inesperadas.
   - Comparar estructura con `V1__initial_schema.sql` (columnas, tipos, PKs) según auditoría previa en [PESCAGO_FLYWAY_SCHEMA_AUDIT.md](./PESCAGO_FLYWAY_SCHEMA_AUDIT.md).

---

## 6. Procedimiento exacto de ejecución real — **NO EJECUTADO**

> **Estado:** pendiente de ventana operativa. Los pasos siguientes son la referencia acordada; **no se han aplicado sobre `pescago`.**

### Pre-vuelo

```text
[ ] Backup verificable completado
[ ] Aplicación detenida
[ ] FLYWAY_ENABLED=false confirmado
[ ] Revisión humana aprobada
[ ] Auditoría read-only de esquema reconfirmada
```

### Ejecución de baseline (manual o script operativo dedicado)

1. Conectar a `pescago` con usuario de mantenimiento (no usar el perfil de arranque de la aplicación si comparte pool activo).
2. Configurar Flyway 11.20.0:
   - `locations`: `classpath:db/migration` (o ruta filesystem equivalente al artefacto desplegado)
   - `baselineVersion`: `1`
   - `baselineDescription`: `Legacy schema baseline`
   - `clean-disabled`: `true`
   - **No** invocar `migrate()` antes del baseline.
3. Ejecutar únicamente:

   ```java
   Flyway.configure()
       .dataSource(/* datasource de pescago */)
       .locations("classpath:db/migration")
       .baselineVersion("1")
       .baselineDescription("Legacy schema baseline")
       .cleanDisabled(true)
       .load()
       .baseline();
   ```

   Equivalente CLI (si se usa `flyway` standalone):

   ```bash
   flyway -url=<JDBC_URL> -user=<USER> -password=<PASSWORD> \
     -locations=filesystem:src/main/resources/db/migration \
     baseline -baselineVersion=1 -baselineDescription="Legacy schema baseline"
   ```

4. **No** ejecutar `Flyway.clean()` bajo ninguna circunstancia en producción.
5. Registrar hora de fin, operador y resultado (`success` / error).

### Post-ejecución inmediata (antes de habilitar migraciones automáticas)

1. Ejecutar `Flyway.validate()` — debe reportar éxito.
2. Ejecutar `Flyway.migrate()` — debe reportar **0 migraciones aplicadas** (esquema ya en versión `1`).
3. Mantener `FLYWAY_ENABLED=false` hasta completar la verificación de la sección 7.

---

## 7. Procedimiento de verificación posterior

Tras el baseline real en `pescago`, verificar en modo read-only o con transacción de solo lectura:

| # | Comprobación | Criterio de éxito |
|---|--------------|-------------------|
| 1 | `flyway_schema_history` existe | Tabla presente en `public` |
| 2 | Registro baseline | Una fila con `version = 1`, `type = BASELINE`, `description = Legacy schema baseline`, `success = true` |
| 3 | Ausencia de `V1` como migración aplicada | No debe existir fila `type = SQL` para versión `1` (solo baseline) |
| 4 | Tablas de negocio | Las 6 tablas legacy siguen presentes |
| 5 | Conteos de filas | Coinciden con snapshot pre-baseline (sección 3) ± tráfico legítimo durante la ventana |
| 6 | `Flyway.validate()` | Sin errores de checksum ni migraciones pendientes inconsistentes |
| 7 | `Flyway.migrate()` | 0 scripts nuevos ejecutados hasta existir `V2__...` |
| 8 | Arranque de aplicación con `FLYWAY_ENABLED=false` | Sin intentos Flyway; Hibernate `ddl-auto=update` no debe alterar estructura ya alineada |

Documentar resultados en ticket/incidente de cambio con timestamp y responsable.

---

## 8. Criterios de rollback y contingencia

### Cuándo abortar antes de baseline

- Aparecen tablas inesperadas o faltan tablas legacy.
- Existe ya `flyway_schema_history` con historial incompatible.
- No hay backup verificable.
- Hay conexiones de escritura activas (aplicación u otros clientes).

### Si el baseline falla a mitad de ejecución

- **No** reintentar `baseline()` sin diagnóstico: Flyway puede haber creado `flyway_schema_history` parcialmente.
- Capturar logs y estado de `flyway_schema_history`.
- Si la tabla de historial se creó pero el baseline no completó: evaluar con DBA eliminación **solo** de `flyway_schema_history` (única mutación permitida en contingencia) y restaurar desde backup si hay duda sobre integridad.
- **No** usar `Flyway.clean()` en `pescago`.

### Rollback completo

- Restaurar `pescago` desde el backup verificable de la sección 5.
- Confirmar ausencia de `flyway_schema_history` tras restauración.
- Re-auditar conteos y esquema antes de planificar nuevo intento.

### Contingencia operativa

- Si `pg_dump` no está en PATH del operador (condición detectada en esta preparación), instalar cliente PostgreSQL o ejecutar backup desde host/container con herramientas disponibles **antes** de la ventana de cambio.

---

## 9. Primera migración evolutiva tras baseline real

Después del baseline exitoso en `pescago`:

- La versión efectiva del esquema quedará en **`1`** (marcador baseline, no ejecución de `V1__initial_schema.sql`).
- La **primera migración evolutiva** que Flyway aplicará debe ser **`V2__...`** (nuevo archivo en `db/migration`).
- **No** renombrar ni reutilizar `V1__initial_schema.sql` para cambios incrementales; su checksum ya está fijado (`169D02583FD4057F2A8399CABA5FB064193BBF8D470C681E3B564111D0749F15`).

---

## 10. Recomendación: transición de `ddl-auto=update` a `validate`

Estado actual (`application.properties`): `spring.jpa.hibernate.ddl-auto=update`.

| Fase | Configuración recomendada | Motivo |
|------|---------------------------|--------|
| **Antes del baseline** | `update` (sin cambio) + `FLYWAY_ENABLED=false` | Evitar doble fuente de verdad mientras Flyway no gobierna el esquema |
| **Inmediatamente después del baseline verificado** | Mantener `update` temporalmente + `FLYWAY_ENABLED=false` | Permite arranques sin sorpresas mientras se valida paridad JPA ↔ BD |
| **Tras primera migración `V2__...` exitosa en entorno de staging** | Cambiar a `spring.jpa.hibernate.ddl-auto=validate` | Hibernate deja de mutar el esquema; Flyway es la única vía de evolución DDL |
| **Producción** | `validate` + `FLYWAY_ENABLED=true` (cuando el pipeline operativo lo apruebe) | Alineación con estrategia v2: migraciones versionadas, sin drift Hibernate |

**Señales para hacer el cambio a `validate`:**

- `Flyway.validate()` y `migrate()` estables en staging y producción.
- Auditoría confirma que el esquema físico coincide con entidades JPA y con el historial Flyway.
- Equipo acuerda que todo cambio DDL futuro pasa por `Vn__...`.

---

## Anexo — Confirmaciones de esta preparación

| Afirmación | Estado |
|------------|--------|
| Base real `pescago` modificada (DDL/DML/Flyway) | **No** — solo lectura |
| Baseline ejecutado en `pescago` | **No** |
| Ensayo baseline en BD temporal | **Sí** — eliminada al finalizar |
| Documento generado | `docs/architecture/PESCAGO_FLYWAY_BASELINE_RUNBOOK.md` |
