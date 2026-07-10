# PescaGo Blockchain Demo Guide

Guía reproducible para demostrar la integración de trazabilidad blockchain en PescaGo usando **minichain** en entorno local. No requiere modificar el frontend ni el código de negocio.

---

## 1. Contexto académico

PescaGo es una plataforma logística para el sector pesquero. La **fuente principal de verdad** del sistema es **PostgreSQL**: usuarios, solicitudes, recibos de pago y servicios contratados viven allí y sostienen el flujo operativo V1.

La **blockchain no reemplaza** la base de datos relacional. El módulo externo **minichain** (Node.js) actúa como una **capa educativa de trazabilidad**: el backend Spring Boot registra evidencia de hitos del proceso logístico mediante HTTP, después de persistir correctamente cada operación en PostgreSQL.

En **producción** la integración permanece desactivada por defecto (`BLOCKCHAIN_ENABLED=false`). En **demo local** se activa con `BLOCKCHAIN_ENABLED=true` apuntando a un nodo minichain en `localhost:3001`.

Si minichain no está disponible, PescaGo **sigue funcionando**; solo se registra un warning en logs del backend.

---

## 2. Arquitectura de la demo

```
Angular (frontend)
       │
       ▼ HTTP /api/v1
Spring Boot (PescaGo-Backend)
       │
       ├──► PostgreSQL  (fuente de verdad)
       │
       └──► minichain HTTP  (trazabilidad, solo si BLOCKCHAIN_ENABLED=true)
                 │
                 ├── Nodo :3001  (primario para el backend)
                 ├── Nodo :3002  (peer)
                 └── Nodo :3003  (peer)
```

- El **frontend** solo consume el backend; no habla con minichain.
- El **backend** emite transacciones a `POST /transactions/new` con payload `{ from, to, amount }`.
- **Minichain** mantiene transacciones **pendientes** y, tras minar (`GET /mine`), las consolida en **bloques** consultables en `GET /chain`.

---

## 3. Eventos registrados

| Evento minichain (`from`) | Momento en el flujo V1 |
|---------------------------|-------------------------|
| `PESCAGO:REQUEST_CREATED` | Creación de solicitud |
| `PESCAGO:REQUEST_QUOTED` | Cotización (estado cotizado) |
| `PESCAGO:PAYMENT_REGISTERED` | Registro de recibo de pago |
| `PESCAGO:HIRED_SERVICE_CREATED` | Creación de servicio contratado |
| `PESCAGO:SERVICE_CONFIRMED` | Confirmación del servicio |

El campo `to` contiene referencias de negocio (IDs de solicitud, recibo, servicio, etc.). El campo `amount` es un **valor numérico de referencia**, no dinero real.

---

## 4. Variables de entorno (demo local)

```properties
BLOCKCHAIN_ENABLED=true
BLOCKCHAIN_BASE_URL=http://localhost:3001
BLOCKCHAIN_CONNECT_TIMEOUT_MS=2000
BLOCKCHAIN_READ_TIMEOUT_MS=3000
```

Para el backend local en esta demo se recomienda además:

```properties
SERVER_PORT=8081
SPRING_PROFILES_ACTIVE=local
```

`JWT_SECRET` y credenciales de PostgreSQL deben tomarse del archivo `.env` local del desarrollador (no incluir secretos en scripts ni en este documento).

---

## 5. Pasos de la demo

### Paso 1 — Levantar tres nodos minichain

Desde la raíz de `PescaGo-Backend`:

```powershell
.\scripts\blockchain\start-minichain-nodes.ps1
```

Se abren tres ventanas con nodos en puertos **3001**, **3002** y **3003**.

### Paso 2 — Registrar nodos entre sí

```powershell
.\scripts\blockchain\register-minichain-nodes.ps1
```

### Paso 3 — Verificar minichain

```powershell
.\scripts\blockchain\check-minichain.ps1
```

### Paso 4 — Levantar backend con blockchain activo

Asegúrate de tener `.env` configurado (PostgreSQL local, `JWT_SECRET` válido para perfil `local`).

```powershell
.\scripts\blockchain\start-backend-blockchain-local.ps1
```

El backend queda en `http://localhost:8081`.

### Paso 5 — Ejecutar flujo V1

Puedes usar el frontend legacy o Postman contra `http://localhost:8081/api/v1`:

1. **Crear solicitud** — `POST /api/v1/requests`
2. **Cotizar** — `PUT /api/v1/requests/{id}` con status `Cotizado` y precio
3. **Pagar** — `POST /api/v1/receipts`, luego `PUT /api/v1/requests/{id}` con status `Pagado`
4. **Contratar** — `POST /api/v1/hired-services`
5. **Confirmar** — `PUT /api/v1/hired-services/{id}` con status `Confirmado`

### Paso 6 — Consultar trazabilidad en minichain

```http
GET http://localhost:3001/transactions/pending
GET http://localhost:3001/mine
GET http://localhost:3001/chain
```

También puedes usar:

```powershell
.\scripts\blockchain\check-minichain.ps1
```

---

## 6. Resultado esperado

Tras completar el flujo y minar, en `GET /chain` deben aparecer **cinco transacciones** con `from` iniciando por `PESCAGO:`:

- `PESCAGO:REQUEST_CREATED`
- `PESCAGO:REQUEST_QUOTED`
- `PESCAGO:PAYMENT_REGISTERED`
- `PESCAGO:HIRED_SERVICE_CREATED`
- `PESCAGO:SERVICE_CONFIRMED`

Cada una dentro de un bloque minado (junto con la transacción `SYSTEM` de recompensa del minichain, que es normal en el módulo educativo).

---

## 7. Mensaje para exposición en clase

PescaGo gestiona el proceso logístico pesquero con una base de datos PostgreSQL que sigue siendo la fuente principal de información. Para complementar ese modelo, integramos una blockchain educativa que registra evidencia de los hitos críticos del flujo —creación de solicitud, cotización, pago, contratación y confirmación— sin reemplazar la base de datos ni cambiar la experiencia del usuario cuando la trazabilidad está desactivada.

En la demo, cada acción relevante genera una transacción en minichain que puede consultarse y minarse, mostrando cómo la trazabilidad distribuida aporta transparencia y auditoría al proceso logístico. En producción la funcionalidad permanece apagada por defecto; la blockchain actúa como capa opcional de evidencia técnica alineada con la transformación digital del sector.

---

## 8. Scripts incluidos

| Script | Propósito |
|--------|-----------|
| `scripts/blockchain/start-minichain-nodes.ps1` | Abre 3 nodos en 3001–3003 |
| `scripts/blockchain/register-minichain-nodes.ps1` | Registra peers entre nodos |
| `scripts/blockchain/check-minichain.ps1` | Health, pending y chain |
| `scripts/blockchain/start-backend-blockchain-local.ps1` | Backend en :8081 con blockchain on |

---

## 9. Limitaciones (mencionar en defensa)

- Minichain es **educativo**; la cadena vive en **memoria** y se pierde al reiniciar nodos.
- No hay autenticación en minichain.
- PostgreSQL prevalece ante cualquier discrepancia.
- `amount` en minichain no representa montos monetarios reales.
