# Roadmap

This document tracks planned improvements and ideas for future FlowProbe releases.

The roadmap describes intended direction, not a commitment to specific release dates or versions. Features are available only when implemented, tested, documented, and published in a release.

## Current baseline (0.1.1 source)

FlowProbe supports declarative HTTP flows, response validation, context exports, placeholder resolution, optional Azure DevOps impediment tickets, and macOS native distribution.

Supported response expectation operators:

- `equals`
- `notEquals`
- `contains`
- `exists`
- `notExists`

The `exists` and `notExists` operators do not require `value`. They inspect whether a JSON Pointer path is present; a present JSON `null` value is **not** a missing path.

JSON extraction preserves both the extracted value and its presence through `ExtractedValue`. Comparison operators do not treat a missing actual path as a successful comparison, and exporting an absent path is an error.

The features below are **planned** unless explicitly identified as already supported.

---

## Priorities

### 1. Secure credential storage fallback

**Goal:** provide a carefully controlled alternative when an operating-system credential manager is unavailable.

Planned work:

- Complete the file-backed credential storage implementation and its integration with provider configuration.
- Define backend-selection behavior: distinguish an unsupported keyring from a supported but locked, denied, or temporarily failing keyring.
- Do not silently downgrade to file storage after an access or security failure.
- Use a private per-user location, restrictive directory/file permissions, and safe file creation and replacement. Verify platform-specific behavior, including symlink and permission handling.
- Decide whether the file store is explicitly opt-in or has a documented automatic fallback policy.
- Define the protection offered at rest. Filesystem permissions do **not** provide encryption; if encryption is added, its key must be protected separately.
- Test creation, retrieval, update, deletion, permission failures, and backend-selection scenarios.

**Acceptance criteria:** the selected backend is predictable; no silent security downgrade occurs; the CLI reports actionable failures; sensitive values are not exposed in diagnostics.

### 2. Environment variables and secret-safe execution

**Goal:** run version-controlled flows in CI/CD and containers without embedding API keys, tokens, or other secrets in YAML.

Planned work:

- Support explicit references to environment variables; a possible future syntax is `${env:API_TOKEN}` (**not currently supported**).
- Define how environment variables interact with flow-exported variables, including namespaces, precedence, missing names, and empty values.
- Avoid importing every environment variable into the flow context by default.
- Prevent sensitive resolved values from leaking into CLI output, errors, reproducible cURL commands, execution summaries, or Azure DevOps tickets.
- Assess support for container-mounted secret files separately, with explicit restrictions on what files a flow can access.
- Document the security boundary: a flow file that can send HTTP requests must be treated as trusted input when secrets are available to its process.
- Add tests for missing secrets, redaction, logging/reporting, and execution in non-interactive environments.

**Acceptance criteria:** a flow can reference a deployment-provided credential without storing it in source control or unintentionally including it in rendered diagnostics.

### 3. Expand response expectation operators

**Goal:** build on typed evaluation inputs and extraction-presence metadata to support richer response assertions without adding special cases to the dispatcher.

All new operators must define behavior for missing paths, explicit JSON `null`, incompatible types, and invalid configuration, with unit and end-to-end coverage.

#### Presence and nullability

Already supported:

- `exists`: the path must be present, even if its value is `null`.
- `notExists`: the path must be absent.

Planned:

- `isNull`: the path must exist and its value must be JSON `null`.
- `notNull`: the path must exist and its value must not be JSON `null`.

```yaml
expect:
  body:
    - path: "/user/id"
      operator: "exists"
    - path: "/error"
      operator: "notExists"
```

Future `isNull` and `notNull` are expected to be unary operators without a `value` field.

#### Numeric comparisons

Planned:

- `greaterThan`
- `greaterThanOrEquals`
- `lessThan`
- `lessThanOrEquals`

```yaml
expect:
  body:
    - path: "/total"
      operator: "greaterThan"
      value: 0
```

Number comparison must define numeric coercion and precision (for example, integer and decimal values) instead of converting values to strings.

#### String matching

Planned:

- `startsWith`
- `endsWith`
- `matches`

```yaml
expect:
  body:
    - path: "/reference"
      operator: "startsWith"
      value: "PAY-"
    - path: "/filename"
      operator: "endsWith"
      value: ".json"
    - path: "/code"
      operator: "matches"
      value: "^ORD-[0-9]+$"
```

Regular-expression validation should include explicit safeguards against excessively expensive patterns.

#### Multiple accepted values

Planned:

- `oneOf`
- `notOneOf`

```yaml
expect:
  body:
    - path: "/status"
      operator: "oneOf"
      value:
        - "READY"
        - "PROCESSING"
        - "COMPLETED"
```

Define equality semantics for scalar values and the behavior of missing or null actual values before implementation.

#### Collection and content checks

Planned:

- `empty`
- `notEmpty`
- `sizeEquals`
- `sizeGreaterThan`
- `sizeLessThan`

```yaml
expect:
  body:
    - path: "/results"
      operator: "notEmpty"
    - path: "/results"
      operator: "sizeGreaterThan"
      value: 0
```

Define which types are supported (strings, arrays, and objects), whether size means characters or elements, and how null and missing paths behave.

#### JSON type validation

Planned:

- `typeIs`

```yaml
expect:
  body:
    - path: "/items"
      operator: "typeIs"
      value: "array"
```

Candidate type names: `string`, `number`, `boolean`, `object`, `array`, and `null`. Type validation should fail on a missing path unless a separate missing-path assertion is used.

---

## CLI and automation readiness

Before committing to editor integrations or a stable `1.0.0` contract, improve FlowProbe's usability as a tool invoked by other programs.

Planned work:

- Define machine-readable execution output (for example, JSON) in addition to human-oriented CLI rendering.
- Specify exit codes, stable result fields, error categories, and compatibility rules for the machine-readable format.
- Ensure standard output and standard error are suitable for scripts and CI/CD pipelines.
- Review cancellation, non-interactive execution, and cross-platform native distribution. Platform support should only be advertised after it is implemented and tested.
- Keep credential redaction consistent across structured output, human-readable output, and ticket generation.

## VS Code integration (longer term, near 1.0.0)

**Goal:** let developers author, discover, execute, and inspect FlowProbe flows from the editor without having to type CLI commands.

Possible initial capabilities:

- Discover FlowProbe YAML files in a workspace.
- Run a flow through the existing CLI and display results per step.
- Show validation diagnostics and navigate to relevant YAML locations.
- Offer schema-based completion and validation for supported operators and fields.
- Detect CLI availability and report version or compatibility issues.

Later possibilities include templates, execution history, comparison of runs, and optional knowledge-assisted workflows.

The extension should be a **client of FlowProbe**, not a second HTTP execution engine. A stable, appropriately redacted machine-readable CLI contract should precede the extension.

---

## Suggested delivery sequence

1. **0.1.1 release hardening:** finalize documentation, add end-to-end coverage for `exists` and `notExists`, and verify native release behavior.
2. **Security and automation milestone (candidate 0.2.x):** implement credential-storage fallback policy, environment-backed variables, and secret-safe output.
3. **Validation milestone (candidate 0.3.x):** add nullability and numeric operators first, followed by further string, collection, and type operators.
4. **Pre-1.0 integration milestone:** stabilize machine-readable results and prototype the VS Code extension.
5. **1.0.0 readiness:** document compatibility, verify supported platforms and CI/CD scenarios, and maintain reliable release and regression checks.

These milestones and version numbers are tentative and may be rearranged based on technical findings and user feedback.
