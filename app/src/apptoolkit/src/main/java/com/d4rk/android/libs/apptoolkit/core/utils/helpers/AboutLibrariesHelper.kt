package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.content.Context
import androidx.annotation.StringRes
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.AboutLibrariesHelper.fetchMarkdownFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * `AboutLibrariesHelper` is a utility object designed to fetch, process, and prepare
 * data related to application information, such as changelogs and End User License Agreements (EULAs),
 * from a remote GitHub repository. It handles fetching Markdown files, extracting specific
 * version information, and converting Markdown to HTML for display in a user interface.
 *
 * This object centralizes the logic for retrieving and formatting data that might be used in an
 * "About" or "Settings" screen of an Android application. It leverages coroutines for asynchronous
 * network operations and handles potential errors gracefully.
 */
object AboutLibrariesHelper {

    /**
     * Fetches a Markdown file from a GitHub repository.
     *
     * This function retrieves the content of a specified Markdown file from the `master` branch
     * of a GitHub repository. It performs the network operation on a background thread using
     * `Dispatchers.IO`.
     *
     * @param packageName The name of the GitHub repository (e.g., "owner/repo-name").
     * @param fileName The name of the Markdown file to fetch (e.g., "README.md").
     * @param errorResId The string resource ID of an error message to return if the fetch fails.
     * @param context The Android Context used for accessing resources.
     * @return The content of the Markdown file as a String, or an error message String if the fetch fails.
     */
    private suspend fun fetchMarkdownFile(packageName : String , fileName : String , @StringRes errorResId : Int , context : Context) : String {
        return withContext(Dispatchers.IO) {
            val fileUrl = "${AppLinks.GITHUB_RAW}/$packageName/refs/heads/master/$fileName"
            val url = URL(fileUrl)
            (url.openConnection() as? HttpURLConnection)?.let { connection ->
                runCatching {
                    connection.requestMethod = "GET"
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                            return@withContext reader.readText()
                        }
                    }
                    else {
                        context.getString(errorResId)
                    }
                }.getOrElse {
                    context.getString(errorResId)
                }.also {
                    connection.disconnect()
                }
            } ?: context.getString(errorResId)
        }
    }

    /**
     * Retrieves the changelog Markdown content from the GitHub repository for the specified package.
     *
     * This function fetches the `CHANGELOG.md` file from the repository associated with the given package name
     * and returns its content as a String. It handles errors by providing a user-friendly message.
     *
     * @param packageName The name of the package (usually matching the repository name) for which to retrieve the changelog.
     * @param context The Android Context used to access resources, particularly for error messages.
     * @return The Markdown content of the changelog file as a String, or an empty string if the file cannot be fetched.
     */
    private suspend fun getChangelogMarkdown(
        packageName : String , context : Context
    ) : String {
        return fetchMarkdownFile(packageName = packageName , fileName = "CHANGELOG.md" , errorResId = R.string.error_loading_changelog_message , context = context)
    }

    /**
     * Retrieves the End User License Agreement (EULA) Markdown content from a GitHub repository.
     *
     * This function fetches the `EULA.md` file from the designated GitHub repository for the specified
     * package name. It uses the `fetchMarkdownFile` function to handle the actual retrieval and error handling.
     *
     * @param packageName The name of the package (also used as the repository name) for which to fetch the EULA.
     *                   This should correspond to the repository name on GitHub (e.g., "my-awesome-package").
     * @param context The Android Context, used to access resources and display error messages.
     * @return A String containing the EULA content in Markdown format, or an empty string if an error occurred.
     *
     * @see fetchMarkdownFile
     */
    private suspend fun getEulaMarkdown(packageName : String , context : Context) : String {
        return fetchMarkdownFile(packageName = packageName , fileName = "EULA.md" , errorResId = R.string.error_loading_eula_message , context = context)
    }

    /**
     * Extracts the changelog for a specific version from a markdown-formatted changelog string.
     *
     * This function parses a markdown string, typically representing a changelog, and
     * extracts the section corresponding to the provided `currentVersionName`. It expects
     * changelog entries to be demarcated by headings of the form `# Version <version_name>`.
     *
     * The extracted changelog will include the heading for the specified version and all
     * content up to the next version heading or the end of the string.
     *
     * If a changelog entry for the specified version is not found, a default message is returned.
     *
     * @param markdown The markdown-formatted changelog string.
     * @param currentVersionName The name (or number) of the version to extract the changelog for.
     *                       This should match the version name used in the changelog's headings
     *                       (e.g., "1.0.0", "v2.1", etc.)
     * @return The changelog for the specified version as a String, or a message indicating
     *         that no changelog was found if the version is absent. The extracted changelog will
     *         retain the markdown format, including the version heading.
     *
     * @sample
     *   val changelog = """
     *       # Version 1.0.0
     *       - Initial release
     *
     *       # Version 1.1.0
     *       - Added feature A
     *       - Fixed bug B
     *   """
     *   val version110Changelog = extractLatestVersionChangelog(changelog, "1.1.0")
     *   // version110Changelog will be:
     *   // "# Version 1.1.0\n- Added feature A\n- Fixed bug B"
     *
     *   val version120Changelog = extractLatestVersionChangelog(changelog, "1.2.0")
     *   // version120Changelog will be:
     *   // "No changelog available for version 1.2.0"
     */
    private fun extractLatestVersionChangelog(markdown : String , currentVersionName : String) : String {
        val regex = Regex("# Version\\s+$currentVersionName:?\\s*[\\s\\S]*?(?=# Version|$)")
        val match : MatchResult? = regex.find(markdown)
        return match?.value?.trim() ?: "No changelog available for version $currentVersionName"
    }

    /**
     * Converts a Markdown string to its HTML representation using the CommonMark library.
     *
     * This function utilizes a `Parser` to interpret the Markdown syntax and an `HtmlRenderer`
     * to generate the corresponding HTML output. It handles standard Markdown elements and attributes
     * supported by the CommonMark specification.
     *
     * @param markdown The Markdown string to be converted.
     * @return The HTML representation of the input Markdown string.
     *
     * @throws Exception If an error occurs during the parsing or rendering process.  (Optional, if exceptions are possible)
     *
     * Example:
     * ```kotlin
     *  val markdownText = "# Heading 1\nThis is a paragraph."
     *  val htmlText = convertMarkdownToHtml(markdownText)
     *  println(htmlText)
     *  // Expected Output: "<h1>Heading 1</h1>\n<p>This is a paragraph.</p>\n"
     * ```
     */
    private fun convertMarkdownToHtml(markdown : String) : String {
        val parser : Parser = Parser.builder().build()
        val renderer : HtmlRenderer = HtmlRenderer.builder().build()
        val document : Node = parser.parse(markdown)
        return renderer.render(document)
    }

    /**
     * Loads and returns a Pair containing the Changelog HTML and EULA HTML.
     *
     * This function retrieves the changelog and EULA markdown files associated with a given package,
     * extracts the changelog specific to the current version, converts both to HTML, and returns them as a Pair.
     *
     * @param packageName The package name of the application.
     * @param currentVersionName The current version name of the application.
     * @param context The Android Context.
     * @return A Pair where the first element is the Changelog HTML (String or null if not found) and the second element is the EULA HTML (String or null if not found).
     */
    suspend fun loadHtmlData(packageName : String , currentVersionName : String , context : Context) : Pair<String? , String?> {
        val changelogMarkdown : String = getChangelogMarkdown(packageName = packageName , context = context)
        val extractedChangelog : String = extractLatestVersionChangelog(markdown = changelogMarkdown , currentVersionName = currentVersionName)
        val changelogHtml : String = convertMarkdownToHtml(markdown = extractedChangelog)
        val eulaMarkdown : String = getEulaMarkdown(packageName = packageName , context = context)
        val eulaHtml : String = convertMarkdownToHtml(markdown = eulaMarkdown)

        return changelogHtml to eulaHtml
    }
}