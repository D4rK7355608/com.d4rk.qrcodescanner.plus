package com.d4rk.qrcodescanner.plus.ui.settings.help
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.d4rk.qrcodescanner.plus.R
import com.d4rk.qrcodescanner.plus.databinding.ActivityHelpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import androidx.core.net.toUri

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_faq, FaqFragment()).commit()
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_feedback, FeedbackFragment()).commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    class FaqFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences_faq, rootKey)
        }
    }
    class FeedbackFragment : PreferenceFragmentCompat() {
        private lateinit var reviewManager: ReviewManager
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences_feedback, rootKey)
            reviewManager = ReviewManagerFactory.create(requireContext())
            val feedbackPreference: Preference? = findPreference(getString(R.string.key_feedback))
            feedbackPreference?.setOnPreferenceClickListener {
                reviewManager.requestReviewFlow().addOnSuccessListener { reviewInfo ->
                    reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
                }
                .addOnFailureListener {
                    val uri =
                        "https://play.google.com/store/apps/details?id=${requireContext().packageName}&showAllReviews=true".toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                        Snackbar.make(requireView(), R.string.snack_unable_to_open_google_play_store, Snackbar.LENGTH_SHORT).show()
                    }
                }
                .also {
                    Snackbar.make(requireView(), R.string.snack_feedback, Snackbar.LENGTH_SHORT).show()
                }
                true
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_feedback, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dev_mail -> {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "text/email"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("d4rk7355608@gmail.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_for) + getString(R.string.app_name))
                emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.dear_developer))
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_using)))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}