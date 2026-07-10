# PescaGo — Documentación final de demo y estado actual

> **Propósito:** guía de exposición y checklist de entrega para el equipo.  
> **Alcance:** solo documentación operativa. No describe secretos ni credenciales.  
> **Fecha de referencia:** julio 2026  
> **Demo blockchain local:** ver también [blockchain/BLOCKCHAIN_DEMO_GUIDE.md](./blockchain/BLOCKCHAIN_DEMO_GUIDE.md)

---

## 1. Estado final del sistema

| Componente | Estado | URL / nota |
|------------|--------|------------|
| **Frontend producción** | Desplegado (SPA Angular; hosting tipo Render Static / Netlify) | Confirmar URL viva en el panel de hosting del repo `PescaGo-Frontend`. El build de producción ya apunta al backend Render (`fileReplacements` → `enviroment.prod.ts`). |
| **Backend producción** | Activo en Render | `https://pescago-backend-cb7w.onrender.com` |
| **API V1** | `…/api/v1` | Flujo legacy completo (solicitud → cotización → pago → hired service → confirmación) |
| **API V2** | `…/api/v2` | Auth JWT + flota carrier operativa |
| **V1 legacy** | Funcionando | Login/registro, requests, receipts, hired services; frontend Angular usa este flujo para la demo principal |
| **V2 flota** | Funcionando | Pantallas/API de flota bajo `/api/v2` (JWT); no reemplaza el flujo de pago V1 |
| **Blockchain** | Integrado, **apagado por defecto** en producción | `BLOCKCHAIN_ENABLED=false` (default en `application.properties`) |
| **Demo blockchain local** | Documentada y reproducible | Guía + scripts en `docs/blockchain/` y `scripts/blockchain/` con `BLOCKCHAIN_ENABLED=true` |

**Desarrollo local típico**

| Servicio | URL |
|----------|-----|
| Frontend | `http://localhost:4200` |
| Backend | `http://localhost:8080` (o `8081` en demo blockchain) |
| Minichain (solo demo) | `http://localhost:3001` (+ peers 3002/3003) |

---

## 2. Flujo principal para exposición

Orden recomendado en la demo en vivo:

1. **Registro / login entrepreneur** — crear cuenta o entrar con un entrepreneur de prueba.
2. **Crear solicitud** — publicar necesidad de transporte (origen, destino, carga, etc.).
3. **Login carrier** — entrar como transportista (perfil resuelto por `userId` vía endpoint by-user).
4. **Cotizar solicitud** — el carrier ofrece precio; la solicitud queda cotizada.
5. **Entrepreneur paga** — elige método de pago simulado y confirma.
6. **Pago atómico** — el frontend llama a un solo endpoint:
   - `POST /api/v1/requests/{id}/pay`
   - En una transacción: registra el recibo, crea el hired service y actualiza el estado de la solicitud.
7. **Servicio contratado pendiente** — aparece el hired service en estado pendiente.
8. **Carrier completa datos de envío** — información de shipping / confirmación operativa.
9. **Entrepreneur ve servicio confirmado** — el hired service pasa a confirmado y es visible en la lista.

---

## 3. Métodos de pago disponibles

Métodos simulados en el formulario de pago:

- **Visa**
- **MasterCard**
- **Yape**
- **Plin**

### Pago simulado educativo

No hay pasarela real (ni Culqi, ni Niubiz, ni PSP externo). Los montos y “autorizaciones” son **educativos**: demuestran el flujo de negocio y la persistencia de recibos, no un cobro bancario.

### Sanitización de datos sensibles (backend)

Antes de persistir el recibo, el backend sanitiza:

| Dato de entrada | Valor almacenado |
|-----------------|------------------|
| CVV | `NOT_STORED` |
| Número de tarjeta | `CARD-****-{last4}` (solo últimos 4 dígitos visibles) |
| Wallets (Yape / Plin) | Identificadores de tipo `WALLET-YAPE` / `WALLET-PLIN` (sin secretos de billetera) |

Así la demo puede mostrar “receipts” sin guardar PAN completo ni CVV.

---

## 4. Blockchain

### Eventos registrados (minichain)

| Evento | Momento del flujo V1 |
|--------|----------------------|
| `REQUEST_CREATED` | Creación de solicitud |
| `REQUEST_QUOTED` | Cotización |
| `PAYMENT_REGISTERED` | Registro de pago / recibo |
| `HIRED_SERVICE_CREATED` | Creación del servicio contratado |
| `SERVICE_CONFIRMED` | Confirmación del servicio |

(En minichain el campo `from` suele ir prefijado como `PESCAGO:…`; ver guía de demo.)

### Roles de cada capa

- **PostgreSQL** sigue siendo la **fuente de verdad**: usuarios, solicitudes, recibos y hired services viven ahí y sostienen la operación.
- **Minichain** es una **capa educativa de trazabilidad**: evidencia de hitos vía HTTP tras persistir en PostgreSQL. No reemplaza la base relacional.
- Si minichain no está disponible con el flag activo, PescaGo **sigue funcionando**; solo se registran warnings en logs.

### Flags

| Entorno | Configuración |
|---------|----------------|
| **Producción** | `BLOCKCHAIN_ENABLED=false` (por defecto) |
| **Demo local** | `BLOCKCHAIN_ENABLED=true` + `BLOCKCHAIN_BASE_URL=http://localhost:3001` |

Procedimiento paso a paso: [BLOCKCHAIN_DEMO_GUIDE.md](./blockchain/BLOCKCHAIN_DEMO_GUIDE.md).

---

## 5. Checklist de prueba final

Usar antes de la exposición (producción o local estable):

- [ ] Login entrepreneur (mensaje de error visible si falla).
- [ ] Login carrier (perfil carrier correcto, sin confusión userId vs carrierId).
- [ ] Crear solicitud (solo navega / confirma éxito si el POST responde OK).
- [ ] Cotizar (validación de precio, refresh de estado).
- [ ] Pagar (flujo atómico `POST …/requests/{id}/pay`).
- [ ] Confirmar servicio (carrier completa datos; await + refresh).
- [ ] Ver servicio confirmado (lista con estados Pendiente / Confirmado y badge).
- [ ] Probar V2 flota (login V2 / pantallas de flota según rol carrier).
- [ ] Verificar que no haya errores en consola del navegador ni fallos 5xx inesperados en Network.

---

## 6. Qué no tocar antes de la entrega

| No hacer | Motivo |
|----------|--------|
| **No activar blockchain en producción** | Minichain es demo local; en prod el flag debe permanecer en `false`. |
| **No cerrar `/api/v1/**` con JWT** | El Angular V1 no envía Bearer; rompería la demo. |
| **No tocar Flyway** | Baseline ya ejecutado; no re-baseline, no `clean`, no migraciones improvisadas. |
| **No migrar datos** | Evitar scripts de migración / dumps sobre la BD de demo en caliente. |
| **No cambiar auth V1** | Login legacy (`GET /api/v1/users/authentication`) debe seguir operativo. |
| **No subir fotos reales** | Las fotos son vista previa local; no hay almacenamiento de objetos productivo. |
| **No integrar pasarela real** | El pago simulado + sanitización es el alcance académico acordado. |

---

## 7. Riesgos conocidos documentados

1. **V1 sigue siendo público** (`/api/v1/**` sin JWT) por compatibilidad con el frontend legacy. Es intencional hasta una fase FE+BE coordinada.
2. **Fotos** son vista previa local en el navegador; no hay pipeline de upload a storage en la nube.
3. **Minichain** es educativo y, en la demo típica, opera **en memoria** (cadena se pierde al reiniciar nodos).
4. **Endpoints legacy de pago en 3 pasos** (crear receipt → crear hired service → actualizar request por separado) **siguen existiendo** por compatibilidad, pero el **frontend actual usa el endpoint atómico** `POST /api/v1/requests/{id}/pay`.
5. Cold start en Render puede demorar la primera petición tras inactividad.

---

## 8. Guion breve de exposición (~1 minuto)

> En el sector pesquero, coordinar el transporte de mercancía entre empresarios y transportistas suele ser manual, lento y poco trazable.  
> **PescaGo** digitaliza ese flujo: el entrepreneur publica una solicitud, el carrier cotiza, se simula el pago y se confirma el servicio contratado.  
> Mejoramos la confiabilidad del cobro con un **pago atómico** en un solo endpoint, para que recibo y servicio contratado no queden a medias.  
> Los **receipts** no guardan CVV ni el número completo de tarjeta: el backend sanitiza a `NOT_STORED` y `CARD-****-last4`.  
> Además, en demo local registramos hitos en una **minichain educativa** de trazabilidad, mientras **PostgreSQL** sigue siendo la fuente de verdad; en producción la blockchain permanece apagada.

---

## Referencias rápidas

| Tema | Ubicación |
|------|-----------|
| Demo blockchain local | `docs/blockchain/BLOCKCHAIN_DEMO_GUIDE.md` |
| Scripts PowerShell minichain | `scripts/blockchain/` |
| Plan / arquitectura V2 | `docs/architecture/` |
| Backend prod | `https://pescago-backend-cb7w.onrender.com` |
| Frontend prod | Confirmar en panel de hosting (build ya cableado a backend `cb7w`) |

---

*Documento de entrega / exposición. Sin secretos. No implica cambios de código.*
