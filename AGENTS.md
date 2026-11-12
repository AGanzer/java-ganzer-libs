# Agent Guidelines & Development Standards

This document serves as the central, universal source of truth for all AI agents (Air-Agents, Junie, Codex, Claude, etc.) operating on this codebase. Tool-specific operational guidelines are located in their respective directories (`.junie/`, `.codex/`, `.claude/`, `.air/`).

---

## 1. Project Overview & Architecture

- **Domain:** Java GUI component and utility libraries (`ganzer`).
- **Target Java Version:** Java 17+ (LTS).
- **Build System:** Maven multi-module (`core`, `dv`, `fx`, `swing`, `swing-dfw`, `swing-dv`, `ui-tests`).
- **Character Encoding:** UTF-8 across all source and resource files.
- **Key Modules:**
  - `core`: Base utilities, models, and general data abstractions.
  - `swing`: Custom Swing components, actions, wizards, and controls (e.g., `GButton`, `GTextField`, `GAction`).
  - `swing-dfw`: Dialog framework and form window extensions.
  - `swing-dv`: Data-view and tabular presentation bindings for Swing.
  - `fx`: JavaFX bindings and visual components.
  - `ui-tests`: End-to-end and UI-specific test suites.

---

## 2. Coding Conventions

Follow the official **Java Code Conventions** with project-specific refinements:

### 2.1 Identifiers & Language
- All identifiers (class, interface, record, method, variable, package names) MUST be written in **US-English**.
- Use descriptive, intention-revealing names without cryptic abbreviations.
- Boolean getters should follow `isProperty()` or `hasProperty()` conventions.
  - Except: Boolean properties that starts with a verb like `useColors` (`setUseColors(boolean)`), `initAtStartup` (`setInitAtStartup(boolen)`); in this case the `shouldProperty()` convention shall be used: `shouldUseColors()`, `shouldInitAtStartup()`.

### 2.2 Methods & Responsibilities
- Keep methods compact with a single, clearly defined responsibility (**Single Responsibility Principle**).
- Avoid code duplication (**DRY** - Don't Repeat Yourself).
- Avoid premature or unnecessary abstractions (**KISS** / **YAGNI**).
- Prefer to composition over deep inheritance hierarchies unless extending core Swing/FX classes.

### 2.3 Block & Bracing Rules (`if`, `for`, `while`, etc.)
- **Block-separation** Put a single empty line before a block (if it is not the first statement within an enclosing block) and after a block (if it is not the last statement within an enclosing block).
- **Single-line bodies:** Do NOT wrap single-line statement blocks in curly braces `{}`.
- **Exception 1 (Nesting):** Nested control blocks MUST always use curly braces `{}`.
- **Exception 2 (Branch Consistency):** If any branch in an `if`/`else if`/`else` chain requires curly braces (e.g. because it has multiple statements), **all** branches in that chain MUST be enclosed in curly braces.
- **Reference Style:** Adhere strictly to the existing formatting found in `de.ganzer.swing.controls` and `de.ganzer.swing.dlgfw`.

```java
// Correct: Single line, unbracketed
if (hideImage)
    setIcon(null);
else if (useSmallImage)
    setIcon((Icon) action.getValue(Action.SMALL_ICON));

// Correct: Branch consistency (one branch is multi-line / block, all get braces)
if (GAction.VISIBILITY_KEY.equals(propertyName)) {
    configureVisibilityFromAction(action);
} else {
    super.actionPropertyChanged(action, propertyName);
    
    if (hideImage)
        setIcon(null);
}
```

### 2.4 Method & Constructor Parameter Wrapping
- Place the first parameter immediately after the opening parenthesis `(` both in declarations and in invocations.
- If the line exceeds **120 characters**, keep the first parameter after the opening parenthesis, and place each subsequent parameter on a new line, properly indented.

```java
// Line <= 120 chars:
public GButton(Action action, boolean hideImage, boolean useSmallImage, boolean useShortName) {
    // ...
}

// Line > 120 chars:
public CustomDialog(Frame owner, 
                    String title, 
                    boolean modal,
                    GraphicsConfiguration gc, 
                    CustomConfiguration config) {
    // ...
}
```

### 2.5 Localization & User-Facing Strings (i18n)
- **Externalize User Strings:** Never hardcode text or string literals intended for user communication (e.g. labels, button titles, tooltips, dialog messages, error descriptions, confirmation prompts) directly into Java source code.
- **Resource Bundle Files:** All user-facing strings MUST be placed in the respective module's resource bundle files:
  - Default/fallback messages: `src/main/resources/.../messages.properties`
  - German localization: `src/main/resources/.../messages_de.properties`
- **Internal Message Accessors:** Access localized strings through the module's dedicated internal message helper (e.g., `CoreMessages`, `SwingMessages`, `SwingDialogsMessages`, `SwingDVMessages`, `DVMessages`, `FXMessages`) rather than embedding raw strings in UI classes.

---

## 3. JavaDoc & Commenting Standards

### 3.1 Language & Formatting
- All JavaDoc and code comments MUST be in **US-English**.
- Wrap keywords, literals (`null`, `true`, `false`), types, and parameter names in `{@code ...}`.
- Use `{@link ...}` or `{@linkplain ...}` for cross-references to classes, methods, or fields.
- Separate paragraphs in JavaDoc with an explicit `<p>` tag on its own line or at the beginning of the paragraph.

### 3.2 In-Code Comments
- Comments within method bodies and code implementations (non-JavaDoc comments) MUST use single-line comment style (`//`).
- Do NOT use block comments (`/* ... */`) inside method bodies.

### 3.3 Column Width & Indentation
- Break JavaDoc descriptions and code comments at column **80** (strict 80-character margin).
- Indent continuation lines in JavaDoc tags (`@param`, `@return`, `@throws`, `@see`) so that the text aligns neatly, matching `de.ganzer.swing.controls`.

```java
/**
 * Creates a new GButton associated with the specified action and visual
 * configuration parameters.
 * <p>
 * If {@code hideImage} is set to {@code true}, any icon attached to the
 * action is suppressed.
 *
 * @param action The action triggering button events.
 * @param hideImage Flag indicating whether action icons should be
 *                  suppressed during rendering.
 * @since 6.3.0
 */
```

### 3.4 Documentation Scope
- Document all `public` and `protected` APIs (classes, interfaces, records, methods, constants).
- Document non-obvious business logic, concurrency constraints, or edge-case handling.
- Maintain and update technical documentation whenever modifying underlying behaviors.

---

## 4. Advanced Engineering Guidelines *(Agent Extensions)*

To maximize stability, maintainability, and runtime efficiency across this library, the following extended practices apply:

### 4.1 Swing & Thread Safety
- **Event Dispatch Thread (EDT):** All Swing component creation and mutation must happen on the EDT (`SwingUtilities.invokeLater` / `SwingUtilities.isEventDispatchThread()`).
- **Long-Running Operations:** Never block the EDT with I/O, heavy calculations, or network requests; use `SwingWorker` or asynchronous background threads.
- **Listener Management:** Always provide mechanisms to remove listeners or use weak references where appropriate to prevent memory leaks in long-lived GUI models.

### 4.2 Null-Safety & Defensive Programming
- Validate public method arguments using `Objects.requireNonNull(arg, "arg must not be null")` or explicit checks.
- Return empty collections (e.g., `Collections.emptyList()`, `List.of()`) rather than `null` to avoid `NullPointerException`s downstream.
- Favor unmodifiable collections when exposing internal state (`Collections.unmodifiableList`, `List.copyOf`).

### 4.3 Resource & Exception Management
- Always use `try-with-resources` for `AutoCloseable` instances (Streams, Channels, Readers, Writers).
- Do not catch generic `Throwable` or `Exception` and silently swallow it; preserve the stack trace or rethrow a descriptive domain exception.

### 4.4 Backward Compatibility & Library Evolution
- As a shared library, preserve binary and source backward compatibility where possible.
- When deprecating APIs, annotate with `@Deprecated(since = "...", forRemoval = ...)` and provide a clear `{@link ...}` alternative in JavaDoc.

---

## 5. Testing & Quality Assurance

- **New Features:** Write comprehensive automated unit/integration tests (JUnit 5) covering normal execution and edge cases.
- **Bug Fixes:** Always write a regression test reproducing the issue before or alongside applying the fix.
- **Execution:** Run relevant tests after changes using Maven (`mvn test -pl <module>` or `mvn test`).
- **Zero-Regression Policy:** Fix any test broken by your changes immediately. Never disable (`@Disabled`), weaken assertions, or mock out failing logic to force a green build.

---

## 6. Operational Workflow & Priorities

1. **Impact Analysis:** Analyze structural and behavioral impacts before modifying code.
2. **Planning:** Create a concise step-by-step plan for multi-file or complex tasks.
3. **Incremental Implementation:** Apply focused, minimal changes without touching unrelated files or reformating untouched lines.
4. **Verification:** Validate via compilation, inspection, and test execution.
5. **Summary:** Provide a clear summary of changes, test results, and potential side effects.

### Priority Hierarchy:
1. **Existing project architecture** and domain consistency take highest precedence.
2. **Project-specific rules** (this document and subfolder guidelines) override general conventions.
3. **Proactive clarification:** When facing ambiguous requirements, ask before executing large changes, and propose adding the decision to these guidelines.
