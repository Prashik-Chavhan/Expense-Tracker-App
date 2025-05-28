package com.example.expensetracker.ui.screens.onboarding

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.PagerIndicator
import com.example.expensetracker.ui.screens.onboarding.pages.SelectExpenseCategoryScreen
import com.example.expensetracker.ui.screens.onboarding.pages.SelectTransactionCategoryScreen
import com.example.expensetracker.ui.screens.onboarding.pages.WelcomeScreen
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingScreenViewModel = hiltViewModel(),
    onFinishClicked: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    OnboardingScreenContent(
        viewModel = viewModel,
        pagerState = pagerState,
        proceed = {
            coroutineScope.launch {
                when (pagerState.currentPage) {
                    0 -> {
                        pagerState.scrollToPage(1)
                    }

                    1 -> {
                        pagerState.scrollToPage(2)
                    }

                    2 -> {
                        viewModel.finishOnboarding(
                            onFinishClicked = { onFinishClicked() }
                        )
                    }
                }
            }

        },
        back = {
            coroutineScope.launch {
                when (pagerState.currentPage) {
                    0 -> {
                    }

                    1 -> {
                        pagerState.scrollToPage(0)
                    }

                    2 -> {
                        pagerState.scrollToPage(1)

                    }
                }
            }
        },
        onClickPageIndicator = {
            coroutineScope.launch {
                pagerState.scrollToPage(it)
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreenContent(
    pagerState: PagerState,
    proceed: () -> Unit,
    back: () -> Unit,
    onClickPageIndicator: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingScreenViewModel,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            AnimatedContent(targetState = page, label = "page") { page ->
                when (page) {
                    0 -> {
                        WelcomeScreen()
                    }

                    1 -> {
                        SelectTransactionCategoryScreen(
                            viewModel = viewModel
                        )
                    }

                    2 -> {
                        SelectExpenseCategoryScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(
                visible = pagerState.currentPage != 0
            ) {
                OutlinedButton(
                    modifier = Modifier.width(180.dp),
                    onClick = { back() },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.Black,
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.back),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }

            Button(
                modifier = Modifier
                    .width(180.dp)
                    .testTag("next"),
                onClick = { proceed() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            ) {
                Text(
                    text = if (pagerState.currentPage == 2)
                        stringResource(id = R.string.finish)
                    else
                        stringResource(id = R.string.next),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            PagerIndicator(
                indicatorCount = 3,
                pagerState = pagerState,
                onClick = {
                    onClickPageIndicator(it)
                }
            )
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
//    ExpenseTrackerTheme {
//        OnboardingScreen {
//            {}
//        }
//    }
}