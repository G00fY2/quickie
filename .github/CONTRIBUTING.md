# Contributing to quickie

Welcome and thanks for being interested in contributing to quickie!

Quickie is a minimal barcode scanning library with an opinionated design.
Therefore there are currently no plans to allow enhanced customization of the scanner overview ui.
See this comment for further explanation [#14](https://github.com/G00fY2/quickie/pull/14#issuecomment-877804346).

## Creating Pull Requests
* This project uses Git Flow as branching strategy. Make sure to use the **develop** branch as the base branch when creating a pull request.
* For first time contributors GitHub will not run the GitHub Actions automatically. You should make sure that all tasks succeed before committing (see [Code Contributions](#code-contributions)).

## Code Contributions
Make sure to get working code on a personal branch with tests and sanity checks passing before you submit a PR:
```shell
./gradlew detektBundledDebug detektUnbundledDebug
./gradlew test
./gradlew :sample:lintBundledDebug
./gradlew :sample:lintUnbundledDebug
./gradlew :sample:assembleBundledDebug
./gradlew :sample:assembleUnbundledDebug
```
Please make every effort to follow existing conventions and style in order to keep the code as readable as possible.

Contribute code changes through GitHub by forking the repository and sending a pull request. I will squash all pull requests on merge.

## Publishing to Maven Central (Maintainers Only)
> [!NOTE]
> This section documents the release and deployment process for publishing the library to Maven Central / Sonatype. It is only relevant for project maintainers who have the necessary repository credentials.

Quickie uses the standard Gradle Maven Publish Plugin alongside the Gradle Signing Plugin.

### 1. Prerequisites
Before publishing, ensure that the following properties are set in your local `gradle.properties` (or `~/.gradle/gradle.properties`):
* **Sonatype Credentials:** `sonatypeUsername` and `sonatypePassword` (must contain a valid Sonatype user token).
* **PGP Signing:** `signing.keyId`, `signing.password`, and `signing.secretKeyRingFile`.

### 2. Upload Artifacts
Run the following Gradle tasks to build, sign, and upload the release publications to the OSSRH Staging API:
```shell
./gradlew publishBundledReleasePublicationToSonatypeRepository publishUnbundledReleasePublicationToSonatypeRepository
```

### 3. Trigger Central Publisher Portal Transfer
In order to transfer the deployment from the OSSRH API to the main Central Publisher Portal, you must manually trigger an upload via a `POST` request.

* **Note:** This request *must* be executed from the same IP address that was used to run the Gradle upload tasks.
* **Authentication:** The endpoint uses standard Basic Authentication. You can use your Sonatype user token credentials (`sonatypeUsername` and `sonatypePassword`).

Execute the following `curl` command (replace `YOUR_USERNAME` and `YOUR_PASSWORD` with your actual token credentials):
```shell
curl -X POST -u "YOUR_USERNAME:YOUR_PASSWORD" "https://ossrh-staging-api.central.sonatype.com/manual/upload/defaultRepository/io.github.g00fy2" -H "Content-Length: 0"
```

*Alternatively, you can use API clients like Postman, select "Basic Auth", and provide your credentials there to send the empty POST request to the URL above.*

### 4. Finalize the Release
Once the transfer is successful:
1. Log in to the [Central Publisher Portal](https://central.sonatype.com/publishing).
2. Locate your uploaded deployment.
3. Click the **Publish** button to finalize the release to Maven Central.
