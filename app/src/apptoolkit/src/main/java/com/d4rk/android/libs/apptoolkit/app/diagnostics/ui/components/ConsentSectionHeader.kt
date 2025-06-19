package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ConsentSectionHeader(title : String) {
    Text(
        text = title , style = MaterialTheme.typography.titleMedium , fontWeight = FontWeight.Bold , color = MaterialTheme.colorScheme.primary , modifier = Modifier.padding(
            top = SizeConstants.LargeSize ,
            bottom = SizeConstants.SmallSize ,
            start = SizeConstants.SmallSize ,
            end = SizeConstants.SmallSize
        )
    )
}