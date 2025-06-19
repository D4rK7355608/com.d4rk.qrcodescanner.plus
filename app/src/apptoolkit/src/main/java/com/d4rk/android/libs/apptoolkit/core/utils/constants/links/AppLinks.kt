package com.d4rk.android.libs.apptoolkit.core.utils.constants.links

object AppLinks {

    // Play Store
    const val PLAY_STORE_MAIN : String = "https://play.google.com/"
    const val PLAY_STORE_APP : String = "${PLAY_STORE_MAIN}store/apps/details?id="
    const val PLAY_STORE_BETA : String = "${PLAY_STORE_MAIN}apps/testing/"
    const val PLAY_STORE_APP_REVIEWS_SUFFIX : String = "&showAllReviews=true"
    const val MARKET_APP_PAGE : String = "market://details?id="

    // Legal & Policy
    private const val GOOGLE_SITES_BASE : String = "https://d4rk7355608.github.io/profile/"
    const val ADS_HELP_CENTER : String = "${GOOGLE_SITES_BASE}#ads-help-center"
    const val PRIVACY_POLICY : String = "${GOOGLE_SITES_BASE}#privacy-policy-apps"
    const val TERMS_OF_SERVICE : String = "${GOOGLE_SITES_BASE}#terms-of-service-apps"
    const val CODE_OF_CONDUCT : String = "${GOOGLE_SITES_BASE}#code-of-conduct-website"
    const val LEGAL_NOTICES : String = "${GOOGLE_SITES_BASE}#legal-notices"
    const val GPL_V3 : String = "https://www.gnu.org/licenses/gpl-3.0"

    // GitHub
    private const val GITHUB_USER : String = "D4rK7355608"
    const val GITHUB_BASE : String = "https://github.com/$GITHUB_USER/"
    const val GITHUB_ISSUES_SUFFIX : String = "/issues/new"
    const val GITHUB_RAW : String = "https://raw.githubusercontent.com/$GITHUB_USER"
    fun githubChangelog(packageName : String) : String = "$GITHUB_BASE$packageName/blob/master/CHANGELOG.md"
}