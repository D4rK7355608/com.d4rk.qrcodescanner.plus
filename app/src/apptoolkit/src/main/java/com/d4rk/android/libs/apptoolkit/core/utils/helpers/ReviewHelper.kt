package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory

object ReviewHelper {

    fun launchInAppReviewIfEligible(
        activity: Activity,
        sessionCount: Int,
        hasPromptedBefore: Boolean,
        onReviewLaunched: () -> Unit
    ) {
        if (sessionCount < 3 || hasPromptedBefore) return

        val reviewManager = ReviewManagerFactory.create(activity)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(activity, reviewInfo)
                    .addOnCompleteListener { onReviewLaunched() }
            }
        }
    }

    fun forceLaunchInAppReview(activity: Activity) {
        val reviewManager = ReviewManagerFactory.create(activity)
        reviewManager.requestReviewFlow()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewManager.launchReviewFlow(activity, task.result)
                }
            }
    }
}