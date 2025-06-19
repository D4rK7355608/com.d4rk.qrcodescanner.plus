package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github

import android.os.Bundle

class ExtraInfo {
    private val extraInfo: MutableMap<String, String> = LinkedHashMap()

    fun put(key: String, value: String) {
        extraInfo[key] = value
    }

    fun put(key: String, value: Boolean) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Double) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Float) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Long) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Int) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Any?) {
        extraInfo[key] = value.toString()
    }

    fun putAll(extraInfo: ExtraInfo) {
        this.extraInfo.putAll(extraInfo.extraInfo)
    }

    fun remove(key: String) {
        extraInfo.remove(key)
    }

    fun isEmpty(): Boolean = extraInfo.isEmpty()

    fun toMarkdown(): String {
        if (extraInfo.isEmpty()) return ""
        val output = StringBuilder()
        output.append(
            "Extra info:\n" +
                "---\n" +
                "<table>\n"
        )
        for (key in extraInfo.keys) {
            output.append("<tr><td>")
                .append(key)
                .append("</td><td>")
                .append(extraInfo[key])
                .append("</td></tr>\n")
        }
        output.append("</table>\n")
        return output.toString()
    }

    fun toBundle(): Bundle {
        val bundle = Bundle(extraInfo.size)
        for (key in extraInfo.keys) {
            bundle.putString(key, extraInfo[key])
        }
        return bundle
    }

    companion object {
        fun fromBundle(bundle: Bundle?): ExtraInfo {
            val extraInfo = ExtraInfo()
            if (bundle == null || bundle.isEmpty) return extraInfo
            for (key in bundle.keySet()) {
                extraInfo.put(key, bundle.getString(key) ?: "")
            }
            return extraInfo
        }
    }
}
