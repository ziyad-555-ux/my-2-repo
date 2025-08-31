pull_request_template.md**Title:** Add run.sh script & GitHub Actions build workflow

**Description:**
- Added a `run.sh` script that prints “Hello World,” providing a simple executable entry point for the project.
- Introduced a GitHub Actions workflow to run `./gradlew build` on pull requests using Java 17, ensuring builds run via the Gradle wrapper.

**Testing:**
- ✅ `./gradlew build --console=plain`
- ✅ `./run.sh`

**Notes:**
- Auto-merge could not be enabled within this environment; please enable it manually on the PR if required.
