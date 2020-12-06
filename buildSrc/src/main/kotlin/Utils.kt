object Utils {

  fun isNonStable(version: String) = listOf("alpha", "beta", "rc", "cr", "m", "preview")
    .any { version.matches(".*[.\\-]$it[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE)) }
}