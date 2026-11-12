# Junie Guidelines (JetBrains IDE Agent)

This document contains specialized execution instructions for **Junie** when operating on the `ganzer` project.
Always adhere to the base standards defined in `AGENTS.md` at the project root.

---

## 1. IDE Tool & MCP Integration

- **Code Inspections & Linting:**
  - After modifying Java files, run `mcp_idea_lint_files` or `mcp_idea_get_file_problems` to verify that no new syntax errors, deprecation warnings, or style violations were introduced.
- **Refactoring & Symbol Renaming:**
  - Prefer `mcp_idea_rename_refactoring` over manual text search-and-replace for identifiers across classes and modules to guarantee semantic correctness.
- **Imports & Formatting:**
  - Use `mcp_idea_optimize_imports` to ensure clean, unused-import-free headers.
  - Follow project formatting conventions (see `AGENTS.md`) without reformatting entire untouched files.
- **Resource Bundles & i18n:**
  - Ensure all user-facing strings (UI labels, dialogs, error messages) are externalized into the corresponding `messages.properties` / `messages_de.properties` files and accessed via module message helpers (`CoreMessages`, `SwingMessages`, etc.).
- **Build & Compilation:**
  - Validate edits with `mcp_idea_build_project` or targeted Maven builds before finishing tasks.

---

## 2. Environment & CLI Execution

- **Terminal Environment:** Windows PowerShell.
  - Do NOT use Bash-specific syntax (e.g. `&&`, `mkdir -p`, forward slash paths in native CLI tools).
  - Use backslashes `\` for Windows file paths.
  - Wrap Maven arguments in single quotes: `mvn 'test' '-pl' 'swing' '-Dtest=GButtonTest'`.
- **Targeted Test Execution:**
  - Fast feedback loop: Run single module tests first: `mvn 'test' '-pl' '<module-name>'`.
  - Full suite check: `mvn 'test'`.

---

## 3. Debugging & Runtime Diagnostics

- **JVM Debugger First:** When triaging complex control-flow or UI event issues in Swing/JavaFX, utilize `ij-debugger` tools (logpoints, breakpoints, frame inspections) before altering code.
- **Logpoint Hygiene:** Prefer non-suspending logpoints over inserting ad-hoc `System.out.println` statements into production code. Ensure all agent breakpoints are removed before concluding the session.

---

## 4. Subagent Delegation & Scoping

- **When to Delegate:** Spawn subagents only for isolated, read-only exploratory tasks or parallel independent sub-tasks.
- **Scope Definition:** Every subagent call must define an explicit scope (concrete file paths, specific questions) and clear deliverables.
- **Model Tiers:** Use the `fast` tier for straightforward boilerplate and lookups; reserve higher tiers for complex architectural refactorings.

---

## 5. Changelist Management

- When working on dedicated issues, consider isolating modifications in a specific changelist via `mcp_idea_create_changelist` to avoid mixing changes with pending developer edits.
