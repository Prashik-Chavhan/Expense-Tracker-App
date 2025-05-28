package com.example.expensetracker.ui.alertDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.expensetracker.R
import com.example.expensetracker.core.util.Constants

@Composable
fun ThemeDialog(
    currentTheme: String,
    onThemeChanged: (String) -> Unit,
    onDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDialogDismiss() },
        containerColor = MaterialTheme.colorScheme.primaryContainer,

        title = {
            Text(
                text = stringResource(id = R.string.theme_settings),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground
            )
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Column(modifier = Modifier.selectableGroup()) {
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.system_default),
                        selected = currentTheme == Constants.SYSTEM_DEFAULT,
                        onClick = { onThemeChanged(Constants.SYSTEM_DEFAULT) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.light),
                        selected = currentTheme == Constants.LIGHT_MODE,
                        onClick = { onThemeChanged(Constants.LIGHT_MODE) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.dark),
                        selected = currentTheme == Constants.DARK_MODE,
                        onClick = { onThemeChanged(Constants.DARK_MODE) },
                    )
                }
            }
        },
        confirmButton = {
        }
    )
}


@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}