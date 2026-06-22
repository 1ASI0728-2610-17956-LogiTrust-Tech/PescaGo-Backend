# PescaGo — Registro de ejecución del baseline Flyway (`pescago`)

**Fecha de ejecución registrada:** `20260622_032020`
**Resultado:** `BASELINE_SUCCESS`
**Registro operativo externo:** `Documents\PescaGo-Backups\pescago_flyway_baseline_execution_20260622_032020.txt`

> Documento sanitizado. No contiene credenciales, usuarios, passwords ni URLs JDBC completas.

---

## 1. Contexto de ejecución

| Aspecto | Valor |
|---------|-------|
| Base de datos | `pescago` |
| Esquema | `public` |
| Flyway | **11.20.0** |
| Operación ejecutada | `Flyway.baseline()` (una sola vez) |
| `Flyway.migrate()` | **No ejecutado** |
| `Flyway.clean()` | **No ejecutado** |
| Spring Boot | **No arrancado** durante la ventana |

---

## 2. Estado de `flyway_schema_history`

| Aspecto | Estado |
|---------|--------|
| Tabla `public.flyway_schema_history` | **Existe** |
| Registros totales | **1** |

### Registro baseline

| Campo | Valor |
|-------|-------|
| `installed_rank` | `1` |
| `version` | `1` |
| `description` | `Legacy schema baseline` |
| `type` | `BASELINE` |
| `success` | `true` |

### Confirmaciones adicionales

- **No** existe fila `type = SQL` para versión `1`.
- `V1__initial_schema.sql` **no fue ejecutado** sobre `pescago`.
- `Flyway.validate()` resultó **exitoso** tras el baseline.

---

## 3. Integridad de tablas legacy

Las seis tablas de negocio permanecieron presentes e intactas. No hubo DDL ni DML sobre tablas legacy durante la operación (solo creación de `flyway_schema_history`).

### Conteos pre y post

| Tabla | Pre | Post | ¿Cambió? |
|-------|----:|-----:|:--------:|
| `users` | 6 | 6 | No |
| `carriers` | 3 | 3 | No |
| `entreprenuers` | 3 | 3 | No |
| `requests` | 0 | 0 | No |
| `hired_services` | 0 | 0 | No |
| `receipts` | 0 | 0 | No |

---

## 4. Artefactos de referencia verificados

| Artefacto | SHA-256 |
|-----------|---------|
| Backup pre-baseline (local y copia OneDrive) | `81BB278B0174D1610A43E9FE3EE1662FD1E736F75ED9EEB036C684021C2B018E` |
| `src/main/resources/db/migration/V1__initial_schema.sql` | `169D02583FD4057F2A8399CABA5FB064193BBF8D470C681E3B564111D0749F15` |

---

## 5. Configuración vigente tras el baseline

| Parámetro | Estado actual |
|-----------|---------------|
| `FLYWAY_ENABLED` | **No definido** en entorno (equivalente operativo a `false` en `application.properties`) |
| `spring.jpa.hibernate.ddl-auto` | **`update`** (sin cambio) |

---

## 6. Próximo cambio permitido

1. **Primera migración evolutiva:** crear y aplicar `V2__...` en `db/migration` cuando el equipo lo apruebe.
2. **No editar ni renombrar** `V1__initial_schema.sql` — permanece como referencia greenfield; su checksum está fijado.
3. **Transición a `ddl-auto=validate`:** pendiente de una migración evolutiva `V2__...` validada en staging (ver [PESCAGO_FLYWAY_BASELINE_RUNBOOK.md](./PESCAGO_FLYWAY_BASELINE_RUNBOOK.md) §10).

---

## 7. Documentos relacionados

- [PESCAGO_FLYWAY_BASELINE_RUNBOOK.md](./PESCAGO_FLYWAY_BASELINE_RUNBOOK.md) — procedimiento histórico, readiness y post-baseline
- [PESCAGO_FLYWAY_SCHEMA_AUDIT.md](./PESCAGO_FLYWAY_SCHEMA_AUDIT.md) — auditoría de esquema legacy
