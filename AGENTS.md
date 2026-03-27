# AGENTS.md — Coding Agent Guidelines

This file provides context for agentic coding tools operating in this repository.

---

## Project Overview

A multi-module Maven project demonstrating a Bitcoin price streaming system using Spring Boot, Apache Kafka (via Spring Cloud Stream), WebSocket, Thymeleaf, Spring Security, and MySQL.

- **Java version:** 25
- **Spring Boot:** 4.0.4
- **Spring Cloud:** 2025.1.0
- **Modules:** `bitcoin-api` (REST API + Kafka producer + JPA/MySQL), `bitcoin-client` (Kafka consumer + WebSocket + Thymeleaf UI + Security)
- **Root package:** `com.ivanfranchin`

---

## Build & Test Commands

All commands use the Maven wrapper (`./mvnw`). There is no Makefile, Gradle, or custom lint runner.

### Build

```bash
# Build all modules
./mvnw clean package

# Build skipping tests
./mvnw clean package -DskipTests
```

### Run Tests

```bash
# Run all tests (all modules)
./mvnw test

# Run tests for a single module
./mvnw test --projects bitcoin-api
./mvnw test --projects bitcoin-client

# Run a single test class
./mvnw test --projects bitcoin-api -Dtest=PriceControllerTests

# Run a single test method
./mvnw test --projects bitcoin-api -Dtest=PriceControllerTests#testGetLastPriceReturnsLatestBitcoinPrice
```

### Run Applications Locally

Both apps require a running MySQL + Kafka (start with `docker compose up -d`).

```bash
./mvnw clean spring-boot:run --projects bitcoin-api -Dspring-boot.run.jvmArguments="-Dserver.port=9081"
./mvnw clean spring-boot:run --projects bitcoin-client -Dspring-boot.run.jvmArguments="-Dserver.port=9082"
```

### Docker

```bash
# Start infrastructure (MySQL, Kafka, Kafdrop)
docker compose up -d
docker compose down -v

# Build Docker images for both modules
./build-docker-images.sh

# Run/stop apps as Docker containers
./start-apps.sh
./stop-apps.sh

# Remove Docker images for both modules
./remove-docker-images.sh
```

---

## Project Structure

```
springboot-kafka-websocket/         ← Root multi-module Maven project
├── pom.xml                         ← Parent POM
├── docker-compose.yml
├── bitcoin-api/                    ← Module 1
│   └── src/main/java/com/ivanfranchin/bitcoinapi/
│       ├── config/                 ← @Configuration classes
│       ├── price/                  ← Feature package (domain slice)
│       │   ├── dto/                ← Records used as response types
│       │   ├── event/              ← Records used as Kafka event types
│       │   └── model/              ← JPA @Entity classes
│       └── runner/                 ← ApplicationRunner implementations
└── bitcoin-client/                 ← Module 2
    └── src/main/java/com/ivanfranchin/bitcoinclient/
        ├── controller/             ← @Controller (HTTP + @MessageMapping chat handler)
        ├── kafka/                  ← Kafka consumer + event record
        ├── security/
        └── websocket/              ← WebSocketConfig + ChatMessage record
```

Package structure follows **feature/domain slicing** (e.g., `price/`, `kafka/`, `websocket/`), with sub-packages `dto/`, `event/`, `model/` inside feature packages where needed.

---

## Code Style Guidelines

### Indentation & Formatting

- **4 spaces** — never tabs
- Opening brace on the **same line** as the declaration (K&R style)
- One blank line between methods; one blank line after the class opening brace before the first member
- No enforced line-length limit, but keep lines readable

### Import Organization

Imports are organized in **three groups**, each separated by a blank line, in this order:

1. Project imports (`com.ivanfranchin.*`)
2. Third-party / framework imports (Lombok, Spring, Jakarta, etc.)
3. Standard library imports (`java.*`)

No static imports are used in `src/main`. In `src/test`, static imports are encouraged for readability (AssertJ, Mockito, MockMvc helpers).

```java
import com.ivanfranchin.bitcoinapi.price.model.Price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
```

| Element | Convention | Example |
|---|---|---|
| Classes / Records | `PascalCase` | `PriceController`, `PriceChanged` |
| Methods | `camelCase` | `getLastPrice()`, `streamNewPrice()` |
| Local variables | `camelCase` | `priceMessage`, `currentPrice` |
| Constants (`static final`) | `UPPER_SNAKE_CASE` | `BINDING_NAME` |
| Packages | lowercase, domain-segment style | `com.ivanfranchin.bitcoinapi.price.dto` |
| Test classes | `[TestedClass]Tests` | `PriceControllerTests` |

### Annotations

- One annotation **per line**, stacked above the declaration
- **Class annotation ordering:** Lombok annotations first, then Spring stereotype/config annotations last:

```java
@Slf4j
@RequiredArgsConstructor
@Component
public class PriceEventEmitter { ... }

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bitcoin")
public class PriceController { ... }

@EnableScheduling
@Configuration
public class SchedulingConfig { ... }
```

- `@Bean` methods are **package-private** (no `public` modifier):

```java
@Bean
OpenAPI customOpenAPI() { ... }

@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) { ... }
```

### Dependency Injection

Use **constructor injection exclusively** via Lombok's `@RequiredArgsConstructor` and `private final` fields. Never use `@Autowired`.

```java
@RequiredArgsConstructor
@Service
public class PriceService {
    private final PriceRepository priceRepository;
    // ...
}
```

`@Value` is acceptable only for injecting property values in configuration classes. `@Bean` methods receive collaborators as method parameters.

### Logging

- Use **SLF4J** via Lombok's `@Slf4j` annotation — never declare `Logger` manually
- Log level in use: `log.info(...)` only
- Always use `{}` placeholder style (SLF4J parameterized logging):

```java
log.info("{} sent to bus.", priceMessage);
log.info("Event id {}, value '{}', topic: {}", id, value, topic);
```

Only add `@Slf4j` to classes that actually emit log statements.

### DTOs and Event Types

Use **Java `record`** for DTOs, Kafka event types, and message types:

```java
public record PriceChanged(Long id, BigDecimal value, LocalDateTime timestamp) {}
public record PriceResponse(BigDecimal value, LocalDateTime timestamp) {}
public record ChatMessage(String fromUser, String toUser, String comment, Instant timestamp) {}
```

Note that `ChatMessage` uses `java.time.Instant` for its timestamp (not `LocalDateTime`).

**Response DTOs** expose a `public static from(Entity)` factory method for clean controller mapping:

```java
public record PriceResponse(BigDecimal value, LocalDateTime timestamp) {
    public static PriceResponse from(Price price) {
        return new PriceResponse(price.getValue(), price.getTimestamp());
    }
}
```

Controllers call `PriceResponse.from(priceService.getLastPrice())` — never construct the DTO inline.

### Seed / Init Data

`CommandLineRunner` / `ApplicationRunner` implementations that seed data must **not** use `static final` entity instances. Instead, use a private instance method that creates the entity at runtime (so that `LocalDateTime.now()` is captured at run time, not class-load time):

```java
// Good — called at run time
private Price initialPrice() {
    return new Price(BigDecimal.valueOf(37000), LocalDateTime.now());
}

// Bad — LocalDateTime captured when class is loaded
private static final Price INITIAL_PRICE = new Price(..., LocalDateTime.now());
```

### Error Handling

The project relies on Spring's default exception propagation. There are no custom exception classes, `@ControllerAdvice`, or `@ExceptionHandler` in use. For new code, follow the same convention — allow Spring to handle exceptions unless there is a specific need for custom error behavior.

---

## Configuration

Use `application.properties` (not YAML). Use environment variable substitution with sensible defaults:

```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/bitcoindb
spring.cloud.stream.kafka.binder.brokers=${KAFKA_HOST:localhost}:${KAFKA_PORT:29092}
```

Spring Cloud Stream binding names follow the pattern `<name>-out-0` (producer) and `<name>-in-0` (consumer), matching the functional bean name.

The Kafka topic destination shared between both modules is `com.ivanfranchin.bitcoin.api.price`. The consumer side (`bitcoin-client`) sets a consumer group via `spring.cloud.stream.bindings.prices-in-0.group=bitcoinClientGroup`.

---

## Testing

- **Framework:** JUnit 5 (`@Test` from `org.junit.jupiter.api`)
- **Spring slice tests:** `@WebMvcTest` for controllers (import from `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`)
- **Mockito:** use `@MockitoBean` (from `org.springframework.test.context.bean.override.mockito.MockitoBean`) for Spring context tests; use `@Mock` + `@InjectMocks` with `@ExtendWith(MockitoExtension.class)` for pure unit tests
- **Assertions:** AssertJ (`assertThat(...)`) — not JUnit 5 `assertEquals`
- **BDD-style Mockito stubs:** `given(...).willReturn(...)`, `then(...).should()...` — not `when(...)` or `verify(...)`
- **Test class visibility:** package-private (no `public` on class)
- **Test naming:** `testVerbNounWhenCondition()` — e.g., `testGetLastPriceReturnsLatestBitcoinPrice()`
- Tests that require live external infrastructure (MySQL, Kafka) are annotated with `@Disabled`
- Use `@ExtendWith(MockitoExtension.class)` with `@Mock` / `@InjectMocks` for unit-testing components that depend on Spring Cloud Stream infrastructure beans (e.g., `StreamBridge`)
- **`@WebMvcTest` slices:** Custom `SecurityConfig` is **not** auto-loaded — always add `@Import(SecurityConfig.class)` explicitly. Without it, Spring Security falls back to defaults and requests return 401 instead of the expected redirect.
- **`@Autowired MockMvc`** is the correct and only idiom in `@WebMvcTest` tests — the "never `@Autowired`" rule applies to production code only.
- **`BDDMockito` has no `lenient()` method** — `lenient()` exists only on `Mockito` and returns a non-BDD `LenientStubber`. Restructure tests to eliminate the need for lenient stubs rather than mixing BDD and non-BDD styles.
- **LSP errors for Lombok in test files are false positives** — methods generated by `@Slf4j`, `@RequiredArgsConstructor`, `@Getter`, `@Setter`, etc. may appear as unresolved in the IDE's language server. Always verify with `./mvnw clean test` (not incremental) to avoid stale Lombok `.class` files.

```java
@WebMvcTest(UIController.class)
@Import(SecurityConfig.class)
class UIControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    @WithMockUser
    void testGetPricesPageReturnsOk() throws Exception {
        // ...
    }
}
```

Unit test example (no Spring context — for event emitters, listeners, services):

```java
@ExtendWith(MockitoExtension.class)
class PriceEventEmitterTests {

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private PriceEventEmitter priceEventEmitter;

    @Test
    void testSendPublishesMessageWithCorrectPayloadAndBinding() {
        // ...
        ArgumentCaptor<PriceChanged> captor = ArgumentCaptor.forClass(PriceChanged.class);
        then(streamBridge).should().send(eq("prices-out-0"), captor.capture());
        assertThat(captor.getValue().value()).isEqualByComparingTo(BigDecimal.valueOf(42000));
    }
}
```

`@MessageMapping` handler methods (e.g., in `UIController`) are also unit-tested with `@ExtendWith(MockitoExtension.class)` — not with `@WebMvcTest`. Instantiate the controller directly, mock `SimpMessagingTemplate`, and invoke the handler method directly (e.g., `uiController.addChatComment(chatMessage)`). Verify interactions with `then(simpMessagingTemplate).should().convertAndSend(...)` or `then(simpMessagingTemplate).should().convertAndSendToUser(...)`.

---

## Frontend (bitcoin-client)

The `bitcoin-client` UI is server-rendered with **Thymeleaf** and styled with **Tailwind CSS**. There is no frontend build step — no Node.js, no `package.json`, no `tailwind.config.js` file.

### CSS & Fonts

- **Tailwind CSS** is loaded via the official CDN play script (`cdn.tailwindcss.com`). Tailwind config (custom fonts, keyframes, animations) is declared inline in a `<script>` block in `prices.html`.
- **JetBrains Mono** is loaded from Google Fonts (`fonts.googleapis.com`).
- No other CSS frameworks are in use. Materialize CSS has been removed.

### JavaScript Libraries (all from `cdnjs.cloudflare.com`)

| Library | Version | Purpose |
|---|---|---|
| jQuery | 3.7.1 | DOM manipulation in `app.js` |
| SockJS | 1.6.1 | WebSocket transport fallback |
| STOMP.js | 2.3.3 | STOMP messaging over WebSocket |
| day.js | 1.11.13 | Date formatting (replaces Moment.js) |

All CDN scripts load from `cdnjs.cloudflare.com` — the two exceptions are Tailwind (`cdn.tailwindcss.com`) and Google Fonts (`fonts.googleapis.com`), which are not available on cdnjs.

day.js uses the same `.format()` API as Moment.js. Do not reintroduce Moment.js.

### Thymeleaf Fragments

- `fragments/header.html` — contains the fixed top navbar. The `<header>` element declares `th:fragment="header"` and is inserted with `th:insert="~{fragments/header :: header}"`.
- `fragments/footer.html` — contains the page footer, declared with `th:fragment="footer"`.

### UI Behaviour Notes

- The chat message input is a `<textarea>` (not `<input type="text">`). Plain `Enter` inserts a newline. **`Ctrl+Enter`** (or **`Cmd+Enter`** on Mac) submits the form.
- The WebSocket connection status indicator (pulsing dot + text) in the navbar is toggled by `setWsStatus(connected)` in `app.js` — green when connected, red when disconnected.
- A toggle switch (`#websocketSwitch`) in the UI calls `connect()` or `disconnect()` to establish or tear down the STOMP/SockJS connection.
- The navbar also displays the authenticated username and a Logout button. The logout form submits `POST /logout` with a CSRF token via Thymeleaf's `th:name="${_csrf.parameterName}"` / `th:value="${_csrf.token}"`. Spring Security invalidates the session and redirects to `/login?logout`.
- Price variation is color-coded: green (`emerald`) for positive, red for negative, neutral grey for zero.

---

## Key Rules Summary

1. Always use `@RequiredArgsConstructor` + `private final` — never `@Autowired`
2. Always use `@Slf4j` for logging — never `LoggerFactory.getLogger(...)` — use `log.info()` with `{}`
3. Annotation order on classes: Lombok first, Spring last
4. `@Bean` methods are package-private
5. Use `record` for DTOs and event/message types
6. Imports: project → third-party → `java.*`, each group blank-line separated, no static imports in `src/main` (static imports are fine in tests)
7. 4-space indentation, K&R brace style
8. Properties use `${ENV_VAR:default}` substitution pattern
9. Test classes are package-private, named `[Class]Tests`, use JUnit 5; choose `@WebMvcTest` for controller slices, `@ExtendWith(MockitoExtension.class)` for pure unit tests; mark `@Disabled` when external infrastructure is required
10. No automated formatter is configured — maintain style manually by following existing code conventions
11. CSRF must be disabled (or explicitly configured to ignore) for WebSocket (`/websocket/**`) endpoints
