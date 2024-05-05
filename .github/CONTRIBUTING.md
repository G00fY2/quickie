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
