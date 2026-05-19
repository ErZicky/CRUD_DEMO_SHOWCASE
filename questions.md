# Spring Boot Interview — Quick Reference

Disclaimer: AI was used to scan the repo and generate this questions. While most of the repo code was written manually


Common questions to be ready for in a junior to early-mid Spring Boot interview, with short answers calibrated to this repo's patterns.

## Question table

| Question | Answer to have ready |
|---|---|
| How would Spring Boot find your beans? | Component scanning. `@SpringBootApplication` on `CrudDemoApplication` implicitly includes `@ComponentScan`, which scans the base package (`com.example.crud_demo`) and all sub-packages for classes annotated with `@Component` or one of its specializations (`@Service`, `@Repository`, `@Controller`, `@RestController`, `@RestControllerAdvice`). Each match is instantiated and registered as a bean in the application context, then injected wherever requested via constructor parameters. |
| How would you validate input? | Add `spring-boot-starter-validation`, annotate `Product` fields with `@NotBlank`/`@Positive`/etc., add `@Valid` on the `@RequestBody` parameter in the controller, and add a handler for `MethodArgumentNotValidException` in `GlobalExceptionHandler` returning 400 (same pattern as the existing `MethodArgumentTypeMismatchException` handler). |
| What's wrong with returning the entity directly? | could potentially expose internal and sensitive fields. Fix: separate `ProductRequest`/`ProductResponse` DTOs and map between them and the entity in the service. |
| What is the N+1 query problem? | Loading a collection with lazy associations fires one query for the parent and N more for each child. Fix: `JOIN FETCH` in JPQL, or `@EntityGraph` on the repository method. (Not present in this repo because `Product` has no relations.) |

