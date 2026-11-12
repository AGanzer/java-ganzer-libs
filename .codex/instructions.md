# Codex Instructions (OpenAI Codex & CLI Agents)

This document contains specialized operational rules for **OpenAI Codex** and LLM-based coding engines working on the `ganzer` repository.
Always read and respect the primary guidelines in `AGENTS.md`.

---

## 1. Code Generation & Patching Standards

- **Exact Pattern Matching:** When producing code modifications, ensure surrounding context lines match the exact indentation (4 spaces) and syntax of the target file.
- **Diff Hygiene:** Produce minimal diffs. Do not introduce extraneous whitespace changes, line ending modifications (keep consistent CRLF/LF as in file), or bulk reformats.
- **Adherence to Syntactic Rules:**
  - Enforce single-line block unbracketed formatting for `if`/`for`/`while` unless nested or mixed with braced branches.
  - Hard-wrap all JavaDoc and comments at **80 columns**.
  - Wrap method/constructor parameters exceeding **120 characters** onto subsequent indented lines while keeping the first parameter on the first line.
- **Resource Bundle Externalization:**
  - Never hardcode user-facing texts in generated code. Outsource all user communication strings into the module's `messages.properties` / `messages_de.properties` and resolve them via the module's internal `*Messages` class.

---

## 2. Token Efficiency & Output Structure

- **Concise Execution:** Avoid conversational preamble; present concise rationales followed by the exact code changes or tool calls.
- **Verification First:** Before returning a completed task, run a compilation and test check via Maven CLI.
- **Self-Correction:** If compilation fails or tests break, analyze the error output directly and apply targeted fixes without reverting unaffected changes.

---

## 3. Maven Command Reference (Windows / PowerShell)

```powershell
# Compile the entire project
mvn 'compile'

# Run unit tests for a specific module
mvn 'test' '-pl' 'core'
mvn 'test' '-pl' 'swing'

# Run a single specific test class
mvn 'test' '-pl' 'swing' '-Dtest=GButtonTest'

# Run full project test suite
mvn 'test'
```
