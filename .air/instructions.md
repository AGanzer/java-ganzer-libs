# Air-Agent Guidelines (JetBrains AI Platform)

This document provides specific instructions for **Air-Agents** operating within JetBrains IDE environments on the `ganzer` codebase.
Refer to `AGENTS.md` at the project root for project-wide coding and documentation standards.

---

## 1. IDE Context & Project Structure

- **Module Hierarchy:** Respect Maven module dependencies (`core` -> `swing` / `fx` -> `ui-tests`). Do not create circular dependencies.
- **Run Configurations:** Utilize predefined project run configurations in `.idea/runConfigurations/` for executing module suites (`core_in_de_ganzer_core`, `de_ganzer_swing_in_de_ganzer_swing`, `fx_in_de_ganzer_fx`).
- **Code Style Alignment:** Leverage IntelliJ's project code styles (`.idea/codeStyles/codeStyleConfig.xml`) to maintain uniform formatting across all modules.

---

## 2. Code Generation & Analysis Standards

- **Semantic Cohesion:** When creating new components in `de.ganzer.swing.controls` or `de.ganzer.swing.dialogs`, align with existing abstractions (e.g., `GButton`, `AbstractModifiableDialog`, `DataSupport`).
- **Control Flow & Braces:**
  - Enforce single-line body unbracketed style for `if`, `while`, `for` statements.
  - Apply curly braces `{}` if nesting occurs or if any branch in an `if`/`else` structure is multi-line.
- **JavaDoc & Comments:**
  - Strict 80-character margin wrap.
  - Wrap technical identifiers in `{@code ...}` and class references in `{@link ...}`.
  - Continuation lines in tag sections (`@param`, `@return`, `@throws`) must be indented.

---

## 3. Verification & Quality Gates

- Always verify code modifications against the project build before concluding.
- Ensure all GUI-related modifications preserve Swing thread safety rules (EDT confinement).
