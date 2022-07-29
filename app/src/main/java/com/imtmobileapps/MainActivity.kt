package com.imtmobileapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.imtmobileapps.ui.theme.ArchitectureComposeTheme
import com.imtmobileapps.util.Routes
import com.imtmobileapps.view.account.AccountScreen
import com.imtmobileapps.view.holding.HoldingDetailScreen
import com.imtmobileapps.view.holding.HoldingListScreen
import com.imtmobileapps.view.holding.HoldingViewModel
import com.imtmobileapps.view.login.LoginScreen
import com.imtmobileapps.view.portfoliodetail.PortfolioDetail
import com.imtmobileapps.view.portfoliolist.PortfolioList
import com.imtmobileapps.view.portfoliolist.PortfolioListViewModel
import com.imtmobileapps.view.signup.SignUpScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: PortfolioListViewModel by viewModels()
    private val holdingViewModel: HoldingViewModel by viewModels()
   // private val accountViewModel: AccountViewModel by viewModels()

    // Animation Samples
    //https://github.com/google/accompanist/blob/main/sample/src/main/java/com/google/accompanist/sample/navigation/animation/AnimatedNavHostSample.kt
    // https://google.github.io/accompanist/navigation-animation/
    //https://medium.com/androiddevelopers/animations-in-navigation-compose-36d48870776b
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArchitectureComposeTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = Routes.LOGIN_SCREEN,
                    builder = {

                        // LOGIN
                        composable(
                            Routes.LOGIN_SCREEN,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )

                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            }

                        ) {
                            LoginScreen(
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                        // SIGN UP SCREEN
                        composable(
                            Routes.SIGN_UP_SCREEN,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Routes.LOGIN_SCREEN ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Routes.LOGIN_SCREEN ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Right,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            }

                        ){
                            SignUpScreen(
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                        // PORTFOLIO LIST
                        composable(
                            Routes.PORTFOLIO_LIST,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )
                                    Routes.PORTFOLIO_DETAIL ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Right,
                                            animationSpec = tween(durationMillis = 700)
                                        )
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Routes.LOGIN_SCREEN ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Right,
                                            animationSpec = tween(700)
                                        )
                                    Routes.PORTFOLIO_DETAIL ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(durationMillis = 700)
                                        )
                                    else -> null
                                }
                            },

                            ) {
                            PortfolioList(
                                viewModel = viewModel,
                                navController = navController
                            )
                        }

                        // ACCOUNT

                        composable(
                            Routes.ACCOUNT_SCREEN,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Right,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },

                            ) {
                            AccountScreen(
                                viewModel = viewModel,
                                navController = navController
                            )
                        }

                        // PORTFOLIO DETAIL
                        composable(
                            Routes.PORTFOLIO_DETAIL,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Right,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },

                            ) {
                            PortfolioDetail(onPopBackStack = {
                                navController.popBackStack()
                            },
                                viewModel = viewModel
                            )
                        }
                        // HOLDING LIST
                        composable(
                            Routes.HOLDING_LIST,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Routes.PORTFOLIO_LIST ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Right,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },
                        ) {
                            HoldingListScreen(
                                onNavigate = {
                                    navController.navigate(Routes.HOLDING_DETAIL)
                                },
                                onPopBackStack = { navController.popBackStack() },
                                viewModel = holdingViewModel
                            )
                        }

                        composable(
                            Routes.HOLDING_DETAIL,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    Routes.HOLDING_LIST ->
                                        slideIntoContainer(
                                            AnimatedContentScope.SlideDirection.Left,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    Routes.HOLDING_LIST ->
                                        slideOutOfContainer(
                                            AnimatedContentScope.SlideDirection.Right,
                                            animationSpec = tween(700)
                                        )
                                    else -> null
                                }
                            }

                        ) {

                            HoldingDetailScreen(
                                viewModel = holdingViewModel,
                                onPopBackStack = {
                                    navController.popBackStack()
                                }
                            )

                        }
                    })
            }
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
