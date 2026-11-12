# Claude Instructions (Anthropic Claude Code & Assistant)

This document outlines tailored instructions for **Claude** when operating on the `ganzer` project.
Always reference `AGENTS.md` at the project root for universal coding, JavaDoc, and testing conventions.

---

## 1. Interaction & Workflow Strategy

- **Exploration before Modification:** Use search and read tools to understand the surrounding module hierarchy and existing classes (e.g. `de.ganzer.swing.controls`, `de.ganzer.swing.dlgfw`) before drafting new components.
- **Structured Planning:** For tasks involving multiple classes or cross-module interactions (e.g., `core` to `swing`), formulate a concise step-by-step plan prior to editing.
- **Safety & Verification:** Execute targeted unit tests immediately after changes to confirm zero regressions.

---

## 2. Coding & Formatting Discipline

- **Language:** English (US) for all code identifiers, comments, and JavaDoc.
- **Braces & Control Flow:**
  - Omit braces for single-line `if`/`for`/`while` bodies.
  - Retain braces for nested statements or when any sibling branch in an `if`/`else` chain requires braces.
- **Margins & Formatting:**
  - JavaDoc and in-code comments must be strictly wrapped at column **80**.
  - Follow the 120-character parameter wrapping rule (first parameter inline, subsequent parameters on new indented lines).
- **Swing Threading:** Ensure GUI state changes remain strictly on the Event Dispatch Thread (EDT).

---

## 3. Essential Commands

```powershell
# Clean and compile
mvn 'clean' 'compile'

# Run tests in swing module
mvn 'test' '-pl' 'swing'

# Run single test
mvn 'test' '-pl' 'swing' '-Dtest=GButtonTest'

# Run all tests across modules
mvn 'test'
```
