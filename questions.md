# Spring Boot Interview — Quick Reference

Common questions to be ready for in a junior to early-mid Spring Boot interview, with short answers calibrated to this repo's patterns.

## Question table

| Question | Answer to have ready |
|---|---|
| How would Spring Boot find your beans? | Component scanning. `@SpringBootApplication` on `CrudDemoApplication` implicitly includes `@ComponentScan`, which scans the base package (`com.example.crud_demo`) and all sub-packages for classes annotated with `@Component` or one of its specializations (`@Service`, `@Repository`, `@Controller`, `@RestController`, `@RestControllerAdvice`). Each match is instantiated and registered as a bean in the application context, then injected wherever requested via constructor parameters. |
| How would you test the controller? | `@WebMvcTest(ProductController.class)` + `MockMvc` for HTTP-layer tests, with `@MockBean ProductService` to stub the service. For the repository layer: `@DataJpaTest`. For full end-to-end: `@SpringBootTest` + `TestRestTemplate` or `MockMvc`. |
| How would you validate input? | Add `spring-boot-starter-validation`, annotate `Product` fields with `@NotBlank`/`@Positive`/etc., add `@Valid` on the `@RequestBody` parameter in the controller, and add a handler for `MethodArgumentNotValidException` in `GlobalExceptionHandler` returning 400 (same pattern as the existing `MethodArgumentTypeMismatchException` handler). |
| How would you secure it? | Add `spring-boot-starter-security`, define a `SecurityFilterChain` bean, configure `.authorizeHttpRequests()` to require auth on `/api/**`, choose a mechanism (HTTP basic for demos, JWT/OAuth2 for real APIs), and optionally `@PreAuthorize` on methods that need role checks. |
| What's wrong with returning the entity directly? | Couples the API contract to the DB schema, exposes internal fields, and risks mass-assignment when binding `@RequestBody Product`. Fix: separate `ProductRequest`/`ProductResponse` DTOs and map between them and the entity in the service. |
| What is the N+1 query problem? | Loading a collection with lazy associations fires one query for the parent and N more for each child. Fix: `JOIN FETCH` in JPQL, or `@EntityGraph` on the repository method. (Not present in this repo because `Product` has no relations.) |
| Where do you put `@Transactional` and why? | At the service layer, on write methods. The service is the boundary of a business operation, so a multi-step write either commits fully or rolls back on exception. Reads here don't need it — Spring Data already wraps each `JpaRepository` call in a read-only transaction, so a class-level `@Transactional(readOnly = true)` would be decorative in this codebase. |
| Constructor injection vs field injection — which is better and why? | Constructor injection. Dependencies become `final` (immutable), the class is instantiable in tests without Spring, missing dependencies fail at compile time, and circular dependencies are caught at startup instead of silently working. `@Autowired` on the constructor is optional when there is only one. |
| `@Component` vs `@Service` vs `@Repository` vs `@Controller`? | All are picked up by component scanning. `@Service`, `@Repository`, `@Controller` are semantic specializations of `@Component`. `@Repository` additionally translates persistence-layer exceptions into Spring's `DataAccessException` hierarchy. `@RestController` is `@Controller` + `@ResponseBody` so return values are serialized to JSON instead of resolved as view names. |
| What HTTP status codes do you return for CRUD? | `GET` → 200 (or 404 if missing). `POST` → 201 Created. `PUT` → 200 (or 204 if no body). `DELETE` → 204 No Content. Validation errors → 400. Unauthenticated → 401. Forbidden → 403. Server-side failure → 500. |
| Difference between `save()` and `saveAndFlush()`? | `save()` schedules the insert/update in the persistence context; the actual SQL is flushed at the end of the transaction. `saveAndFlush()` forces an immediate flush so the SQL hits the DB right away — useful when you need the generated id or a downstream query to see the change inside the same transaction. |
| What does `@SpringBootApplication` actually do? | It is a meta-annotation combining `@SpringBootConfiguration` (marks the class as a config source), `@EnableAutoConfiguration` (turns on Spring Boot's conditional auto-configuration based on classpath), and `@ComponentScan` (scans the package of the annotated class downward). |

## Watch-outs in this repo to fix before showing the code

- `Product.Id` uses `int` and capital-I naming — change to `private Long id;` (Spring Data convention is `Long` for auto-generated PKs, and Java fields are lowerCamelCase).
- Italian comment `//CRUD operations fornite da spring` in `ProductService` — translate it.
- Two DELETE endpoints (one with `@RequestBody`, one with `@PathVariable`) — own it proactively: *"kept both for demo purposes; in production I would only keep `DELETE /{id}`."* Reframes a smell as a deliberate teaching choice.
