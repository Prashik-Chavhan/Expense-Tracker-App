package com.example.expensetracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.expensetracker.core.util.Constants

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryContainer,
    onPrimary = Color.Black,
    primaryContainer = GreenPrimary,
    onPrimaryContainer = Color.White,

    secondary = TealSecondaryContainer,
    onSecondary = Color.Black,
    secondaryContainer = TealSecondary,
    onSecondaryContainer = OnTealSecondaryContainer,

    background = DarkBackground,
    onBackground = OnDarkBackground,
    surface = DarkSurface,
    onSurface = Color.White,

    tertiary = TopAppBarDark,
    onTertiary = OnTopAppBarDark,

    surfaceVariant = BottomSheetDark,
    onSurfaceVariant = OnBottomSheetDark,
)


private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = OnGreenPrimary,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = OnGreenPrimaryContainer,

    secondary = TealSecondary,
    onSecondary = OnTealSecondary,
    secondaryContainer = TealSecondaryContainer,
    onSecondaryContainer = OnTealSecondaryContainer,

    background = LightBackground,
    onBackground = OnLightBackground,
    surface = LightSurface,
    onSurface = Color.Black,

    tertiary = TopAppBarLight,
    onTertiary = OnTopAppBarLight,

    surfaceVariant = BottomSheetLight,
    onSurfaceVariant = OnBottomSheetLight,
)


@Composable
fun ExpenseTrackerTheme(
    themeMode: String,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when(themeMode) {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
        Constants.SYSTEM_DEFAULT -> { if (darkTheme) DarkColorScheme else LightColorScheme }
        Constants.LIGHT_MODE -> { LightColorScheme }
        Constants.DARK_MODE -> { DarkColorScheme }
        else -> { LightColorScheme }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}