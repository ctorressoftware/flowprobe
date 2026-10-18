# Development guide

This document covers local verification, tests, Native Image maintenance, and the tagged release workflow.

For contribution etiquette and pull-request guidance, see [`CONTRIBUTING.md`](../CONTRIBUTING.md).

## Requirements

For JVM development:

- Java 21
- Gradle Wrapper

For Native Image:

- GraalVM for JDK 21
- Native Image support in the selected GraalVM distribution

## Run the application

Help:

```bash
./gradlew run --args="--help"
```

Version:

```bash
./gradlew run --args="--version"
```

Run an example:

```bash
./gradlew run --args="run -f examples/normal-flow.yaml"
```

The Gradle `run` task is configured to forward standard input because provider configuration is interactive.

## Unit and integration tests

Run the standard test suite:

```bash
./gradlew test
```

The normal `test` task excludes tests tagged:

```text
os-keystore
```

Run the operating-system keystore integration test separately:

```bash
./gradlew osKeystoreTest
```

This test requires an environment with a supported operating-system credential store.

## Full JVM verification

For full JVM verification:

```bash
./gradlew clean check
```

`clean` removes generated Gradle output under `build/`.

The committed Native Image metadata under `src/main/resources/META-INF/native-image` is not removed by `clean`.

`check` includes the standard tests and JaCoCo coverage verification.

Current coverage thresholds:

```text
LINE   >= 60%
BRANCH >= 40%
```

## JaCoCo reports

The test task finalizes with the JaCoCo report.

HTML reports are generated under:

```text
build/reports/jacoco
```

## Native build

Build the native executable:

```bash
./gradlew nativeCompile
```

Generated executable:

```text
build/native/nativeCompile/flowprobe
```

For a completely fresh native build:

```bash
./gradlew clean nativeCompile
```

Remember that `clean` removes the previously generated binary because it lives under `build/`.

## Native smoke checks

Useful checks:

```bash
./build/native/nativeCompile/flowprobe --version
./build/native/nativeCompile/flowprobe --help
./build/native/nativeCompile/flowprobe run --help
./build/native/nativeCompile/flowprobe configure --help
./build/native/nativeCompile/flowprobe run -f examples/normal-flow.yaml
```

An intentionally unsupported provider is useful for validating CLI argument handling:

```bash
./build/native/nativeCompile/flowprobe configure jira
echo $?
```

The expected exit code for invalid CLI arguments is `2`.

## Native Image metadata

See [Native Image](native-image.md) for the detailed tracing workflow.

Metadata is committed under:

```text
src/main/resources/META-INF/native-image/io.github.ctorressoftware/flowprobe
```

The tracing agent merges new observations directly into that directory.

After tracing:

```bash
git diff -- src/main/resources/META-INF/native-image
```

Review every metadata change before committing it.

Changes involving reflection, serialization, proxies, JNI, or native integrations should be validated through representative native execution.

The tracing agent does not need to run after every code change. Use it when new or modified behavior requires additional reachability metadata.

## Versioning

The project version is declared in:

```text
build.gradle.kts
```

The Gradle project version is the single source of truth for the FlowProbe version.

During the build, that value is written to a generated:

```text
version.properties
```

resource.

The CLI version provider reads this resource, avoiding a separate hardcoded version in the Picocli command.

Release tags must use the same version prefixed with `v`.

For example:

```text
Gradle version: 0.1.0
Git tag:        v0.1.0
```

Pre-release versions follow the same rule:

```text
Gradle version: 0.2.0-rc.1
Git tag:        v0.2.0-rc.1
```

## Release workflow

The release workflow is defined in:

```text
.github/workflows/macos-native-release.yml
```

It runs when a tag matching `v*` is pushed.

Before building the release, the workflow verifies that:

```text
tag == "v" + Gradle project version
```

The workflow then:

1. Runs the JVM verification suite.
2. Runs the operating-system keystore integration test.
3. Builds Native Image executables for macOS x64 (Intel) and macOS arm64 (Apple Silicon).
4. Smoke-tests the generated native executables.
5. Executes a real HTTP flow against a temporary local server.
6. Packages both native executables.
7. Creates the GitHub release using the corresponding `CHANGELOG.md` entry.
8. Updates the `ctorressoftware/homebrew-tap` formula with the release artifact URLs and SHA256 values.

Versions containing a pre-release suffix, such as `0.2.0-rc.1`, are published as GitHub pre-releases.

Stable versions, such as `0.1.0`, are published as regular GitHub releases.

## Release preparation checklist

Before pushing a release tag, verify that:

- The version in `build.gradle.kts` matches the intended release.
- `CHANGELOG.md` contains an entry for that exact version.
- `README.md` and the relevant documentation reflect the release's features and limitations.
- The release changes have been committed.
- The repository references the canonical FlowProbe repository.

Run the JVM verification suite:

```bash
./gradlew clean check
```

Run the operating-system credential-store integration test when the current environment supports it:

```bash
./gradlew osKeystoreTest
```

Build the native executable:

```bash
./gradlew nativeCompile
```

Verify the generated executable:

```bash
./build/native/nativeCompile/flowprobe --version
./build/native/nativeCompile/flowprobe --help
./build/native/nativeCompile/flowprobe run --help
./build/native/nativeCompile/flowprobe configure --help
./build/native/nativeCompile/flowprobe run -f examples/normal-flow.yaml
```

The reported version must match the intended release version.

If the release changes Native Image metadata, credential storage, serialization, or provider configuration, also exercise the affected native path directly.

Before tagging, confirm the repository state:

```bash
git status
git log -5 --oneline
git remote -v
```

The working tree should be clean and `origin` should point to:

```text
https://github.com/ctorressoftware/flowprobe.git
```

### Create and push the release tag

Set the version being released:

```bash
VERSION="0.1.0"
```

Create and push an annotated tag:

```bash
git tag -a "v${VERSION}" -m "FlowProbe v${VERSION}"
git push origin "v${VERSION}"
```

For the first stable release, this creates:

```text
v0.1.0
```

Pushing the tag triggers the release workflow.

### Failed release workflows

If a workflow fails because of a temporary infrastructure or CI issue, rerun the failed jobs without modifying the tag.

A failed workflow does not automatically mean that the tag should be deleted.

Do not move or recreate a tag after a release has been publicly published.

If the workflow fails before publication and requires a new commit, decide manually whether to recreate the unpublished tag or publish a new version.

## Homebrew verification

After the tagged workflow publishes the GitHub release and updates the Homebrew tap:

```bash
brew update
brew upgrade ctorressoftware/tap/flowprobe
flowprobe --version
```

If necessary during local troubleshooting:

```bash
brew reinstall ctorressoftware/tap/flowprobe
```

Verify that the installed executable reports the expected version.

For a stable release such as `v0.1.0`, the expected output is:

```text
flowprobe 0.1.0
```

## Release references

- [GitHub Releases](https://github.com/ctorressoftware/flowprobe/releases)
- [Changelog](../CHANGELOG.md)
- [Native Image documentation](native-image.md)
- [Homebrew tap](https://github.com/ctorressoftware/homebrew-tap)
