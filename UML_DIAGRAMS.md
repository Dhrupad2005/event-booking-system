# UML Class Diagrams

## Core Domain Model

### Event Management

```
┌─────────────────────────────────────────┐
│              Event                       │
├─────────────────────────────────────────┤
│ - eventId: String                       │
│ - name: String                          │
│ - description: String                   │
│ - eventDateTime: LocalDateTime          │
│ - venue: Venue                          │
│ - category: EventCategory               │
│ - status: EventStatus                   │
│ - ticketTypes: List<TicketType>         │
│ - totalCapacity: int                    │
├─────────────────────────────────────────┤
│ + addTicketType(TicketType): void      │
│ + isBookable(): boolean                 │
│ + getAvailableCapacity(): int          │
└─────────────────────────────────────────┘
              ┃
              ┃ contains
              ▼
┌─────────────────────────────────────────┐
│           TicketType                     │
├─────────────────────────────────────────┤
│ - ticketTypeId: String                  │
│ - name: String                          │
│ - description: String                   │
│ - price: BigDecimal                     │
│ - totalQuantity: int                    │
│ - bookedCount: int                      │
│ - tier: TicketTier                      │
├─────────────────────────────────────────┤
│ + isAvailable(): boolean                │
│ + getAvailableQuantity(): int          │
│ + reserveTickets(int): boolean         │
│ + releaseTickets(int): void            │
└─────────────────────────────────────────┘
```

### User and Booking

```
┌─────────────────────────────────────────┐
│              User                        │
├─────────────────────────────────────────┤
│ - userId: String                        │
│ - email: String                         │
│ - password: String                      │
│ - firstName: String                     │
│ - lastName: String                      │
│ - phoneNumber: String                   │
│ - role: UserRole                        │
│ - registeredAt: LocalDateTime           │
│ - bookingHistory: List<Booking>         │
│ - isActive: boolean                     │
├─────────────────────────────────────────┤
│ + getFullName(): String                │
│ + addBooking(Booking): void            │
│ + deactivate(): void                   │
│ + activate(): void                     │
└─────────────────────────────────────────┘
              ┃
              ┃ makes
              ▼
┌─────────────────────────────────────────┐
│            Booking                       │
├─────────────────────────────────────────┤
│ - bookingId: String                     │
│ - user: User                            │
│ - event: Event                          │
│ - tickets: List<Ticket>                 │
│ - status: BookingStatus                 │
│ - bookingDateTime: LocalDateTime        │
│ - totalAmount: BigDecimal               │
│ - payment: Payment                      │
├─────────────────────────────────────────┤
│ + addTicket(Ticket): void              │
│ + confirm(): void                      │
│ + cancel(): void                       │
│ + getTotalTickets(): int               │
│ + canBeCancelled(): boolean            │
└─────────────────────────────────────────┘
              ┃
              ┃ contains
              ▼
┌─────────────────────────────────────────┐
│            Ticket                        │
├─────────────────────────────────────────┤
│ - ticketId: String                      │
│ - event: Event                          │
│ - ticketType: TicketType                │
│ - seatNumber: String                    │
│ - status: TicketStatus                  │
│ - issuedAt: LocalDateTime               │
│ - pricePaid: BigDecimal                 │
├─────────────────────────────────────────┤
│ + cancel(): void                       │
│ + use(): void                          │
│ + isValid(): boolean                   │
└─────────────────────────────────────────┘
```

### Payment Processing

```
┌─────────────────────────────────────────┐
│            Payment                       │
├─────────────────────────────────────────┤
│ - paymentId: String                     │
│ - booking: Booking                      │
│ - amount: BigDecimal                    │
│ - paymentMethod: PaymentMethod          │
│ - status: PaymentStatus                 │
│ - createdAt: LocalDateTime              │
│ - completedAt: LocalDateTime            │
│ - transactionReference: String          │
├─────────────────────────────────────────┤
│ + complete(String): void               │
│ + fail(): void                         │
│ + refund(): void                       │
└─────────────────────────────────────────┘
```

## Service Layer Architecture

```
┌─────────────────────────────────────────┐
│      BookingSystemFacade                │
│                                         │
│  Simplified API for client code        │
└────────────┬────────────────────────────┘
             │
             ├─────────────┐
             │             │
    ┌────────▼──────┐  ┌──▼──────────┐
    │ UserService   │  │EventService │
    └────────┬──────┘  └──┬──────────┘
             │            │
             │            │
    ┌────────▼────────────▼──────────┐
    │      BookingService            │
    │                                 │
    │  Core booking workflow         │
    └────────┬───────────────────────┘
             │
    ┌────────▼──────┐
    │PaymentService │
    └───────────────┘
```

## Repository Pattern

```
┌─────────────────────────────────────────┐
│   <<interface>>                         │
│   Repository<T, ID>                     │
├─────────────────────────────────────────┤
│ + save(T): T                           │
│ + findById(ID): Optional<T>            │
│ + findAll(): List<T>                   │
│ + update(T): T                         │
│ + deleteById(ID): boolean              │
│ + existsById(ID): boolean              │
└────────────┬────────────────────────────┘
             │
             ▲
    ┌────────┴────────┬─────────────────┐
    │                 │                 │
┌───▼─────────┐  ┌───▼─────────┐  ┌───▼─────────┐
│EventRepo    │  │UserRepo     │  │BookingRepo  │
│Interface    │  │Interface    │  │Interface    │
└───┬─────────┘  └───┬─────────┘  └───┬─────────┘
    │                │                 │
    ▼                ▼                 ▼
┌───────────┐  ┌───────────┐  ┌───────────────┐
│EventRepo  │  │UserRepo   │  │BookingRepo    │
│Impl       │  │Impl       │  │Impl           │
└───────────┘  └───────────┘  └───────────────┘
```

## Relationships

### Aggregation and Composition

- **Event** *composes* **TicketType** (strong ownership)
- **Event** *aggregates* **Venue** (shared)
- **Booking** *composes* **Ticket** (strong ownership)
- **Booking** *aggregates* **User** and **Event** (shared)
- **Booking** *composes* **Payment** (strong ownership)

### Inheritance

- All custom exceptions extend **BookingSystemException**
- All repository implementations implement **Repository<T, ID>**

### Dependencies

- Services depend on Repositories (via interfaces)
- Facade depends on Services
- Factory creates and wires all components

## Enumerations

```
EventStatus          EventCategory        TicketTier
- UPCOMING           - CONCERT            - VIP
- ONGOING            - SPORTS             - PREMIUM
- COMPLETED          - THEATER            - STANDARD
- CANCELLED          - CONFERENCE         - ECONOMY
- POSTPONED          - WORKSHOP           - EARLY_BIRD
                     - FESTIVAL
                     - EXHIBITION

BookingStatus        PaymentStatus        PaymentMethod
- PENDING            - PENDING            - CREDIT_CARD
- CONFIRMED          - PROCESSING         - DEBIT_CARD
- CANCELLED          - COMPLETED          - PAYPAL
- REFUNDED           - FAILED             - UPI
- FAILED             - REFUNDED           - NET_BANKING
                                          - WALLET

TicketStatus         UserRole
- ACTIVE             - ADMIN
- USED               - EVENT_ORGANIZER
- CANCELLED          - CUSTOMER
- REFUNDED
- EXPIRED
```

## Key Design Patterns Illustrated

### 1. Factory Pattern
```
BookingSystemFactory
    │
    ├─> creates EventRepository
    ├─> creates UserRepository
    ├─> creates BookingRepository
    ├─> creates EventService (injecting repo)
    ├─> creates UserService (injecting repo)
    ├─> creates PaymentService
    ├─> creates BookingService (injecting all deps)
    └─> creates BookingSystemFacade (injecting services)
```

### 2. Facade Pattern
```
Client Code
    │
    └─> BookingSystemFacade (simple interface)
           │
           ├─> EventService
           ├─> UserService
           └─> BookingService (complex subsystem)
```

### 3. Repository Pattern
```
Service Layer (business logic)
    │
    └─> Repository Interface (abstraction)
           │
           └─> Repository Implementation (concrete)
                  │
                  └─> Data Storage (in-memory/database)
```

## Thread Safety Considerations

- Repositories use **ConcurrentHashMap**
- TicketType reservation methods are **synchronized**
- Atomic operations for critical booking workflow
- Immutable value objects (Address)

## Extensibility Points

1. **Add new Payment Method**: Extend PaymentMethod enum
2. **Add new Event Category**: Extend EventCategory enum
3. **Add Database**: Implement Repository interfaces with JPA
4. **Add Authentication**: Extend UserService with JWT
5. **Add Notifications**: Create NotificationService
6. **Add Analytics**: Create AnalyticsService
