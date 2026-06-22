# Graph Report - PescaGo-Backend  (2026-06-21)

## Corpus Check
- 109 files · ~17,003 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 697 nodes · 974 edges · 95 communities (45 shown, 50 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 12 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `bf64d6dc`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]
- [[_COMMUNITY_Community 17|Community 17]]
- [[_COMMUNITY_Community 18|Community 18]]
- [[_COMMUNITY_Community 19|Community 19]]
- [[_COMMUNITY_Community 20|Community 20]]
- [[_COMMUNITY_Community 21|Community 21]]
- [[_COMMUNITY_Community 22|Community 22]]
- [[_COMMUNITY_Community 23|Community 23]]
- [[_COMMUNITY_Community 24|Community 24]]
- [[_COMMUNITY_Community 25|Community 25]]
- [[_COMMUNITY_Community 26|Community 26]]
- [[_COMMUNITY_Community 27|Community 27]]
- [[_COMMUNITY_Community 28|Community 28]]
- [[_COMMUNITY_Community 29|Community 29]]
- [[_COMMUNITY_Community 30|Community 30]]
- [[_COMMUNITY_Community 31|Community 31]]
- [[_COMMUNITY_Community 32|Community 32]]
- [[_COMMUNITY_Community 33|Community 33]]
- [[_COMMUNITY_Community 34|Community 34]]
- [[_COMMUNITY_Community 35|Community 35]]
- [[_COMMUNITY_Community 36|Community 36]]
- [[_COMMUNITY_Community 37|Community 37]]
- [[_COMMUNITY_Community 38|Community 38]]
- [[_COMMUNITY_Community 39|Community 39]]
- [[_COMMUNITY_Community 40|Community 40]]
- [[_COMMUNITY_Community 41|Community 41]]
- [[_COMMUNITY_Community 42|Community 42]]
- [[_COMMUNITY_Community 43|Community 43]]
- [[_COMMUNITY_Community 44|Community 44]]
- [[_COMMUNITY_Community 45|Community 45]]
- [[_COMMUNITY_Community 46|Community 46]]
- [[_COMMUNITY_Community 47|Community 47]]
- [[_COMMUNITY_Community 48|Community 48]]
- [[_COMMUNITY_Community 49|Community 49]]
- [[_COMMUNITY_Community 50|Community 50]]
- [[_COMMUNITY_Community 51|Community 51]]
- [[_COMMUNITY_Community 52|Community 52]]
- [[_COMMUNITY_Community 53|Community 53]]
- [[_COMMUNITY_Community 54|Community 54]]
- [[_COMMUNITY_Community 55|Community 55]]
- [[_COMMUNITY_Community 56|Community 56]]
- [[_COMMUNITY_Community 57|Community 57]]
- [[_COMMUNITY_Community 58|Community 58]]
- [[_COMMUNITY_Community 59|Community 59]]
- [[_COMMUNITY_Community 60|Community 60]]
- [[_COMMUNITY_Community 61|Community 61]]
- [[_COMMUNITY_Community 62|Community 62]]
- [[_COMMUNITY_Community 63|Community 63]]
- [[_COMMUNITY_Community 64|Community 64]]
- [[_COMMUNITY_Community 65|Community 65]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 67|Community 67]]
- [[_COMMUNITY_Community 68|Community 68]]
- [[_COMMUNITY_Community 69|Community 69]]
- [[_COMMUNITY_Community 70|Community 70]]
- [[_COMMUNITY_Community 71|Community 71]]
- [[_COMMUNITY_Community 72|Community 72]]
- [[_COMMUNITY_Community 73|Community 73]]
- [[_COMMUNITY_Community 74|Community 74]]
- [[_COMMUNITY_Community 75|Community 75]]
- [[_COMMUNITY_Community 76|Community 76]]
- [[_COMMUNITY_Community 77|Community 77]]
- [[_COMMUNITY_Community 78|Community 78]]
- [[_COMMUNITY_Community 79|Community 79]]
- [[_COMMUNITY_Community 80|Community 80]]
- [[_COMMUNITY_Community 81|Community 81]]
- [[_COMMUNITY_Community 82|Community 82]]
- [[_COMMUNITY_Community 83|Community 83]]
- [[_COMMUNITY_Community 84|Community 84]]
- [[_COMMUNITY_Community 85|Community 85]]
- [[_COMMUNITY_Community 86|Community 86]]
- [[_COMMUNITY_Community 87|Community 87]]
- [[_COMMUNITY_Community 88|Community 88]]
- [[_COMMUNITY_Community 89|Community 89]]
- [[_COMMUNITY_Community 90|Community 90]]
- [[_COMMUNITY_Community 91|Community 91]]
- [[_COMMUNITY_Community 92|Community 92]]
- [[_COMMUNITY_Community 93|Community 93]]
- [[_COMMUNITY_Community 94|Community 94]]

## God Nodes (most connected - your core abstractions)
1. `SnakeCasePhysicalNamingStrategy` - 9 edges
2. `8. Roadmap técnico priorizado` - 9 edges
3. `HiredServiceController` - 8 edges
4. `RequestController` - 8 edges
5. `Identifier` - 8 edges
6. `9. Plan de implementación inicial recomendado` - 8 edges
7. `Operation` - 7 edges
8. `ApiResponses` - 7 edges
9. `Operation` - 7 edges
10. `ApiResponses` - 7 edges

## Surprising Connections (you probably didn't know these)
- `CarrierCommandServiceImpl` --implements--> `CarrierCommandService`  [EXTRACTED]
  src/main/java/pe/upc/pescagobackend/carrier/application/internal/commandservices/CarrierCommandServiceImpl.java → src/main/java/pe/upc/pescagobackend/carrier/domain/services/CarrierCommandService.java
- `CarrierQueryServiceImpl` --implements--> `CarrierQueryService`  [EXTRACTED]
  src/main/java/pe/upc/pescagobackend/carrier/application/internal/queryservices/CarrierQueryServiceImpl.java → src/main/java/pe/upc/pescagobackend/carrier/domain/services/CarrierQueryService.java
- `EntreprenuerCommandServiceImpl` --implements--> `EntreprenuerCommandService`  [EXTRACTED]
  src/main/java/pe/upc/pescagobackend/entrepreneur/application/internal/commandservices/EntreprenuerCommandServiceImpl.java → src/main/java/pe/upc/pescagobackend/entrepreneur/domain/services/EntreprenuerCommandService.java
- `EntreprenuerQueryServiceImpl` --implements--> `EntreprenuerQueryService`  [EXTRACTED]
  src/main/java/pe/upc/pescagobackend/entrepreneur/application/internal/queryservices/EntreprenuerQueryServiceImpl.java → src/main/java/pe/upc/pescagobackend/entrepreneur/domain/services/EntreprenuerQueryService.java
- `HIredServiceCommandServiceImpl` --implements--> `HiredServiceCommandService`  [EXTRACTED]
  src/main/java/pe/upc/pescagobackend/hiredService/application/internal/commandservices/HIredServiceCommandServiceImpl.java → src/main/java/pe/upc/pescagobackend/hiredService/domain/services/HiredServiceCommandService.java

## Import Cycles
- None detected.

## Communities (95 total, 50 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.20
Nodes (13): CarrierCommandServiceImpl, CarrierQueryServiceImpl, CarrierController, Carrier, CarrierResource, CreateCarrierResource, DeleteMapping, GetMapping (+5 more)

### Community 1 - "Community 1"
Cohesion: 0.21
Nodes (14): RequestController, ApiResponses, CreateRequestResource, DeleteMapping, GetMapping, Long, Operation, PostMapping (+6 more)

### Community 2 - "Community 2"
Cohesion: 0.15
Nodes (11): CarrierData, HiredService, CreateHiredServiceCommand, UpdateHiredServiceCommand, CreateHiredServiceCommand, CreateHiredServiceResource, Long, UpdateHiredServiceCommand (+3 more)

### Community 3 - "Community 3"
Cohesion: 0.20
Nodes (16): HiredServiceController, ApiResponses, CreateHiredServiceResource, DeleteMapping, GetMapping, HiredServiceCommandService, HiredServiceQueryService, HiredServiceResource (+8 more)

### Community 4 - "Community 4"
Cohesion: 0.15
Nodes (11): Dimensions, Request, CreateRequestCommand, UpdateRequestCommand, CreateRequestCommand, CreateRequestResource, Long, UpdateRequestCommand (+3 more)

### Community 5 - "Community 5"
Cohesion: 0.45
Nodes (5): Identifier, JdbcEnvironment, PhysicalNamingStrategy, Override, SnakeCasePhysicalNamingStrategy

### Community 6 - "Community 6"
Cohesion: 0.12
Nodes (20): CarrierQueryService, CarrierQueryServiceImpl, CarrierRepository, CarrierQueryService, Carrier, CarrierRepository, GetCarrierByIdQuery, GetCarriersQuery (+12 more)

### Community 7 - "Community 7"
Cohesion: 0.20
Nodes (12): EntreprenuerCommandServiceImpl, EntreprenuerQueryServiceImpl, EntreprenuerController, CreateEntreprenuerResource, DeleteMapping, Entreprenuer, EntreprenuerResource, GetMapping (+4 more)

### Community 8 - "Community 8"
Cohesion: 0.20
Nodes (12): ReceiptCommandServiceImpl, ReceiptQueryServiceImpl, ReceiptController, CreateReceiptResource, DeleteMapping, GetMapping, Long, Operation (+4 more)

### Community 9 - "Community 9"
Cohesion: 0.18
Nodes (13): CarrierCommandService, CarrierCommandServiceImpl, CarrierCommandService, Carrier, CarrierRepository, CreateCarrierCommand, DeleteCarrierCommand, Optional (+5 more)

### Community 10 - "Community 10"
Cohesion: 0.18
Nodes (13): EntreprenuerCommandServiceImpl, EntreprenuerCommandService, EntreprenuerCommandService, CreateEntreprenuerCommand, DeleteEntreprenuerCommand, Entreprenuer, EntreprenuerRepository, Optional (+5 more)

### Community 11 - "Community 11"
Cohesion: 0.20
Nodes (11): EntreprenuerQueryService, EntreprenuerQueryServiceImpl, EntreprenuerQueryService, Entreprenuer, EntreprenuerRepository, GetEntreprenuerByIdQuery, Optional, Override (+3 more)

### Community 12 - "Community 12"
Cohesion: 0.44
Nodes (5): HiredServiceRepository, HiredService, List, Long, Optional

### Community 13 - "Community 13"
Cohesion: 0.12
Nodes (18): UserQueryServiceImpl, UserRepository, UserQueryService, GetUserByAuthenticationQuery, GetUserByIdQuery, Optional, Override, User (+10 more)

### Community 14 - "Community 14"
Cohesion: 0.18
Nodes (13): ReceiptCommandServiceImpl, ReceiptCommandService, ReceiptCommandService, CreateReceiptCommand, DeleteReceiptCommand, Optional, Override, Receipt (+5 more)

### Community 15 - "Community 15"
Cohesion: 0.20
Nodes (11): ReceiptQueryServiceImpl, ReceiptQueryService, ReceiptQueryService, GetReceiptByIdQuery, Optional, Override, Receipt, ReceiptRepository (+3 more)

### Community 16 - "Community 16"
Cohesion: 0.44
Nodes (5): RequestRepository, List, Long, Optional, Request

### Community 17 - "Community 17"
Cohesion: 0.19
Nodes (13): UserController, CreateUserResource, DeleteMapping, GetMapping, Long, Operation, PostMapping, ResponseEntity (+5 more)

### Community 18 - "Community 18"
Cohesion: 0.40
Nodes (4): 1. Instalación de dependencias, 2. Configuración del entorno, 3. Ejecución, PescaGo Backend

### Community 19 - "Community 19"
Cohesion: 0.47
Nodes (4): CorsConfig, CorsRegistry, Override, WebMvcConfigurer

### Community 24 - "Community 24"
Cohesion: 0.60
Nodes (3): Carrier, CarrierResource, CarrierResourceFromEntityAssembler

### Community 25 - "Community 25"
Cohesion: 0.60
Nodes (3): CreateCarrierCommand, CreateCarrierResource, CreateCarrierCommandFromResourceAssembler

### Community 29 - "Community 29"
Cohesion: 0.47
Nodes (4): EntreprenuerRepository, Entreprenuer, Long, Optional

### Community 30 - "Community 30"
Cohesion: 0.60
Nodes (3): CreateEntreprenuerCommand, CreateEntreprenuerResource, CreateEntreprenuerCommandFromResourceAssembler

### Community 31 - "Community 31"
Cohesion: 0.60
Nodes (3): Entreprenuer, EntreprenuerResource, EntreprenuerResourceFromEntityAssembler

### Community 32 - "Community 32"
Cohesion: 0.17
Nodes (15): HIredServiceCommandServiceImpl, HiredServiceCommandService, HiredServiceCommandService, CreateHiredServiceCommand, DeleteHiredServiceCommand, HiredService, HiredServiceRepository, Optional (+7 more)

### Community 33 - "Community 33"
Cohesion: 0.16
Nodes (17): HiredServiceQueryService, HiredServiceQueryServiceImpl, HiredServiceQueryService, GetHiredServiceByIdQuery, GetHiredServicesByCarrierIdQuery, GetHiredServicesByEntrepreneurIdQuery, HiredService, HiredServiceRepository (+9 more)

### Community 34 - "Community 34"
Cohesion: 0.60
Nodes (3): HiredService, HiredServiceResource, HiredServiceResourceFromEntityAssembler

### Community 36 - "Community 36"
Cohesion: 0.18
Nodes (13): UserCommandServiceImpl, UserCommandService, CreateUserCommand, DeleteUserCommand, Optional, Override, User, UserRepository (+5 more)

### Community 37 - "Community 37"
Cohesion: 0.60
Nodes (3): CreateUserCommand, CreateUserResource, CreateUserCommandFromResourceAssembler

### Community 38 - "Community 38"
Cohesion: 0.60
Nodes (3): User, UserResource, UserResourceFromEntityAssembler

### Community 42 - "Community 42"
Cohesion: 0.47
Nodes (4): ReceiptRepository, Long, Optional, Receipt

### Community 43 - "Community 43"
Cohesion: 0.60
Nodes (3): CreateReceiptCommand, CreateReceiptResource, CreateReceiptCommandFromResourceAssembler

### Community 44 - "Community 44"
Cohesion: 0.60
Nodes (3): Receipt, ReceiptResource, ReceiptResourceFromEntityAssembler

### Community 45 - "Community 45"
Cohesion: 0.17
Nodes (15): RequestCommandServiceImpl, RequestCommandService, RequestCommandService, CreateRequestCommand, DeleteRequestCommand, Optional, Override, Request (+7 more)

### Community 46 - "Community 46"
Cohesion: 0.16
Nodes (17): RequestQueryServiceImpl, RequestQueryService, RequestQueryService, GetRequestByIdQuery, GetRequestsByCarrierIdQuery, GetRequestsByEntrepreneurIdQuery, List, Optional (+9 more)

### Community 47 - "Community 47"
Cohesion: 0.50
Nodes (3): Request, RequestResource, RequestResourceFromEntityAssembler

### Community 87 - "Community 87"
Cohesion: 0.08
Nodes (25): 3.1 Fleet, 3.2.1 Estados del dominio y compatibilidad legacy, 3.2.2 Concurrencia en aceptación rápida, 3.2 TransportRequest (ampliación de `Request`), 3.3 ServiceExecution (ampliación de `HiredService`), 3.4 Payment (ampliación), 3.5 Notification, 3.6 Reputation (soporte) (+17 more)

### Community 88 - "Community 88"
Cohesion: 0.18
Nodes (10): 4.1 Flujo objetivo (coexistiendo con manual), 4.2 Coexistencia con flujo manual, 4.3 Bloqueo de otras opciones y concurrencia, 4. Diseño del flujo automatizado, 5. Contrato API propuesto, 7.1 Transición segura de contraseñas, 7. Seguridad y correcciones previas necesarias, Endpoints v1 modificados (comportamiento interno, misma firma) (+2 more)

### Community 89 - "Community 89"
Cohesion: 0.20
Nodes (10): 8.1 Testing mínimo Fase 0, 8. Roadmap técnico priorizado, Fase 0 — Estabilización obligatoria del MVP, Fase 1 — Foundation de datos y Fleet, Fase 2 — Pricing + matching básico, Fase 3 — Aceptación rápida + ServiceExecution, Fase 4 — Notification, Fase 5 — Tracking GPS (+2 more)

### Community 90 - "Community 90"
Cohesion: 0.25
Nodes (8): 2.1 Mapeo módulos actuales → bounded contexts objetivo, 2.2 Compatibilidad temporal (no romper web Angular actual), 2.3 Nombres con typo que NO deben cambiar aún, 2.4 Principios v2, 2.5 Compatibilidad real entre v1 y v2, 2.6 Transición de roles (datos existentes), 2. Decisiones de arquitectura v2, Tarea frontend futura (roadmap)

### Community 91 - "Community 91"
Cohesion: 0.25
Nodes (8): 9. Plan de implementación inicial recomendado, Bloque 0A — Documento y estrategia de migración, Bloque 0B — Autenticación v2 compatible, Bloque 0C — Consultas de perfil y corrección coordinada, Bloque 0D — Flyway preparado, Criterios de aceptación Fase 0 (conjunto 0A–0D), Qué NO hacer en Fase 0, Tests mínimos (transversal Fase 0 / Bloques 0B–0D)

### Community 92 - "Community 92"
Cohesion: 0.29
Nodes (7): 1.1 Stack y arquitectura, 1.2 Módulos actuales (bounded contexts de facto), 1.3 Endpoints actuales (26, todos públicos), 1.4 Flujo de negocio implementado hoy, 1.5 Limitaciones que impiden matching automático directo, 1.6 Monolito modular actual vs arquitectura objetivo del curso, 1. Estado actual confirmado

### Community 93 - "Community 93"
Cohesion: 0.29
Nodes (7): 6.1 Adopción de Flyway, 6.2 Tablas nuevas propuestas, 6.4 Restricciones y auditoría, 6. Persistencia PostgreSQL y migraciones, Configuración propuesta (documental), Escenario A — Base PostgreSQL **nueva** (desarrollo greenfield), Escenario B — Base PostgreSQL **existente** (datos reales / dev con historial)

### Community 94 - "Community 94"
Cohesion: 0.40
Nodes (5): 6.3 Cambios a tablas existentes (migraciones futuras), `hired_services`, `receipts` (deprecación), `requests`, `users`

## Knowledge Gaps
- **146 isolated node(s):** `pe.upc:pescago-backend`, `String`, `Override`, `Override`, `CreateCarrierCommand` (+141 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **50 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `3. Diseño del dominio nuevo` connect `Community 87` to `Community 88`?**
  _High betweenness centrality (0.006) - this node is a cross-community bridge._
- **What connects `pe.upc:pescago-backend`, `String`, `Override` to the rest of the system?**
  _146 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 4` be split into smaller, more focused modules?**
  _Cohesion score 0.14619883040935672 - nodes in this community are weakly interconnected._
- **Should `Community 6` be split into smaller, more focused modules?**
  _Cohesion score 0.1164021164021164 - nodes in this community are weakly interconnected._
- **Should `Community 13` be split into smaller, more focused modules?**
  _Cohesion score 0.12433862433862433 - nodes in this community are weakly interconnected._
- **Should `Community 87` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._