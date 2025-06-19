package com.d4rk.android.libs.apptoolkit.app.help.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.UiHelpQuestion

@Composable
fun rememberFaqList(): List<UiHelpQuestion> {
    val context = LocalContext.current
    return remember {
        listOf(
            R.string.question_1 to R.string.summary_preference_faq_1,
            R.string.question_2 to R.string.summary_preference_faq_2,
            R.string.question_3 to R.string.summary_preference_faq_3,
            R.string.question_4 to R.string.summary_preference_faq_4,
            R.string.question_5 to R.string.summary_preference_faq_5,
            R.string.question_6 to R.string.summary_preference_faq_6,
            R.string.question_7 to R.string.summary_preference_faq_7,
            R.string.question_8 to R.string.summary_preference_faq_8,
            R.string.question_9 to R.string.summary_preference_faq_9
        ).map { (questionRes, answerRes) ->
            UiHelpQuestion(
                question = context.getString(questionRes),
                answer = context.getString(answerRes)
            )
        }.filter { it.question.isNotBlank() && it.answer.isNotBlank() }
    }
}
