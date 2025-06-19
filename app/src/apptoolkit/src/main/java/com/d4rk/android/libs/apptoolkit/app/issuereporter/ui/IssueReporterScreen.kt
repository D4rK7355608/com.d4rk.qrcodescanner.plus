package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.actions.IssueReporterEvent
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.ui.UiIssueReporterScreen
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab.AnimatedExtendedFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab.SmallFloatingActionButton
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar.DefaultSnackbarHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ButtonIconSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraExtraLargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueReporterScreen(activity: Activity) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isFabExtended: MutableState<Boolean> = remember { mutableStateOf(value = true) }
    val viewModel: IssueReporterViewModel = koinViewModel()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val uiStateScreen: UiStateScreen<UiIssueReporterScreen> by viewModel.uiState.collectAsState()
    val target: GithubTarget = koinInject()

    LaunchedEffect(key1 = scrollBehavior.state.contentOffset) {
        isFabExtended.value = scrollBehavior.state.contentOffset >= 0f
    }

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.bug_report),
        onBackClicked = { activity.finish() },
        snackbarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            val context = LocalContext.current
            Column(horizontalAlignment = Alignment.End) {
                SmallFloatingActionButton(
                    modifier = Modifier.padding(bottom = SizeConstants.MediumSize),
                    isVisible = true,
                    isExtended = true,
                    icon = Icons.Outlined.Link,
                    onClick = {
                        IntentsHelper.openUrl(
                            context = context,
                            url = "https://github.com/${target.username}/${target.repository}/issues"
                        )
                    })
                AnimatedExtendedFloatingActionButton(
                    visible = true,
                    onClick = { viewModel.onEvent(IssueReporterEvent.Send(context)) },
                    text = { Text(text = stringResource(id = R.string.issue_send)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.BugReport,
                            contentDescription = null
                        )
                    },
                    expanded = isFabExtended.value
                )
            }
        }) { paddingValues: PaddingValues ->
        IssueReporterScreenContent(
            paddingValues = paddingValues, viewModel = viewModel, uiStateScreen = uiStateScreen
        )
        DefaultSnackbarHandler(
            screenState = uiStateScreen,
            snackbarHostState = snackBarHostState,
            getDismissEvent = { IssueReporterEvent.DismissSnackbar },
            onEvent = { viewModel.onEvent(it) })
    }
}

@Composable
fun IssueReporterScreenContent(
    paddingValues: PaddingValues,
    viewModel: IssueReporterViewModel,
    uiStateScreen: UiStateScreen<UiIssueReporterScreen>
) {
    val data = uiStateScreen.data ?: UiIssueReporterScreen()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding(),
            start = SizeConstants.LargeSize,
            end = SizeConstants.LargeSize
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {

        item {
            if (!data.issueUrl.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SizeConstants.LargeSize),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircleOutline,
                            contentDescription = stringResource(id = R.string.issue_submitted_successfully),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        SmallVerticalSpacer()
                        Text(
                            text = stringResource(id = R.string.issue_submitted),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        SmallVerticalSpacer()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            val uriHandler = LocalUriHandler.current

                            TextButton(
                                onClick = { uriHandler.openUri(data.issueUrl) },

                                modifier = Modifier.bounceClick()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                                    contentDescription = stringResource(R.string.open_issue_in_browser),
                                    modifier = Modifier.size(SizeConstants.ButtonIconSize)
                                )
                                ButtonIconSpacer()
                                Text(stringResource(R.string.open_button_label))
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.issue_section_label),
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            Card(
                shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SizeConstants.LargeSize),
                    verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)
                ) {
                    OutlinedTextField(
                        value = data.title,
                        onValueChange = { viewModel.onEvent(IssueReporterEvent.UpdateTitle(it)) },
                        label = { Text(stringResource(id = R.string.issue_title_label)) },
                        leadingIcon = { Icon(Icons.Outlined.Title, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    OutlinedTextField(
                        value = data.description,
                        onValueChange = { viewModel.onEvent(IssueReporterEvent.UpdateDescription(it)) },
                        label = { Text(stringResource(id = R.string.issue_description_label)) },
                        leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )

                    OutlinedTextField(
                        value = data.email,
                        onValueChange = { viewModel.onEvent(IssueReporterEvent.UpdateEmail(it)) },
                        label = { Text(stringResource(id = R.string.issue_email_label)) },
                        placeholder = { Text(stringResource(id = R.string.optional_placeholder)) },
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.login_section_label),
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            Card(
                shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(SizeConstants.LargeSize)
                ) {

                    RadioOption(
                        selected = !data.anonymous,
                        text = stringResource(id = R.string.use_github_account),
                        onClick = { },
                        enabled = false
                    )
                    RadioOption(
                        selected = data.anonymous,
                        text = stringResource(id = R.string.send_anonymously),
                        onClick = { viewModel.onEvent(IssueReporterEvent.SetAnonymous(true)) }
                    )
                }
            }
        }

        item {
            val context = LocalContext.current
            var deviceExpanded by rememberSaveable { mutableStateOf(false) }

            Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    LargeHorizontalSpacer()
                    Row(
                        modifier = Modifier
                            .clickable { deviceExpanded = !deviceExpanded }
                            .fillMaxWidth()
                            .padding(vertical = SizeConstants.LargeSize),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.device_info),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = SizeConstants.LargeSize)
                        )
                        Icon(
                            modifier = Modifier.padding(end = SizeConstants.LargeSize),
                            imageVector = if (deviceExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                            contentDescription = stringResource(id = R.string.cd_expand_device_info)
                        )
                    }
                    AnimatedVisibility(visible = deviceExpanded) {
                        val info = remember { DeviceInfo(context).toString() }
                        Text(
                            text = info,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(SizeConstants.LargeSize)
                                .horizontalScroll(rememberScrollState())
                        )
                    }
                }
            }
        }

        item {
            ExtraExtraLargeVerticalSpacer()
        }
        item {
            ExtraExtraLargeVerticalSpacer()
        }
    }
}

@Composable
private fun RadioOption(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .let { base ->
                if (enabled) {
                    base
                        .bounceClick()
                        .clip(CircleShape)
                        .clickable(onClick = onClick)
                } else base
            }
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled
        )
        SmallHorizontalSpacer()
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}