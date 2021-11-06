package com.matthias.dreamz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.matthias.dreamz.data.model.TagType
import com.matthias.dreamz.ui.screen.Screen
import com.matthias.dreamz.ui.screen.calendar.CalendarScreen
import com.matthias.dreamz.ui.screen.calendar.CalendarViewModel
import com.matthias.dreamz.ui.screen.dreamlist.DreamListViewModel
import com.matthias.dreamz.ui.screen.dreamlist.DreamsListScreen
import com.matthias.dreamz.ui.screen.editdream.EditDreamScreen
import com.matthias.dreamz.ui.screen.editdream.EditDreamViewModel
import com.matthias.dreamz.ui.screen.graph.GraphScreen
import com.matthias.dreamz.ui.screen.graph.GraphViewModel
import com.matthias.dreamz.ui.screen.login.LoginScreen
import com.matthias.dreamz.ui.screen.login.LoginViewModel
import com.matthias.dreamz.ui.screen.tags.TagsScreen
import com.matthias.dreamz.ui.screen.tags.TagsViewModel
import com.matthias.dreamz.ui.screen.viewdream.ViewDreamScreen
import com.matthias.dreamz.ui.screen.viewdream.ViewDreamViewModel
import com.matthias.dreamz.ui.theme.DreamzTheme
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(android.R.color.system_accent1_500)
        setContent {
            val mainViewModel = hiltViewModel<MainViewModel>()
            val logged = mainViewModel.logged.collectAsState(initial = true).value
            val navController = rememberAnimatedNavController()
            DreamzTheme {
                ProvideWindowInsets {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        AnimatedNavHost(
                            navController = navController,
                            startDestination = Screen.DreamList.route
                        ) {
                            dreamzComposable(
                                Screen.DreamList.route
                            ) {
                                val dreamListViewModel: DreamListViewModel = hiltViewModel()
                                DreamsListScreen(
                                    dreamListViewModel,
                                    navController,
                                )
                            }
                            dreamzComposable(
                                Screen.ViewDream.route,
                                arguments = listOf(navArgument("dreamId") {
                                    type = NavType.LongType
                                })
                            ) {
                                val viewDreamViewModel: ViewDreamViewModel = hiltViewModel()
                                val dreamDayId = it.arguments?.getLong("dreamId")
                                requireNotNull(dreamDayId, { "dreamDayId should not be null" })
                                ViewDreamScreen(
                                    viewDreamViewModel,
                                    dreamDayId,
                                    navController
                                )
                            }
                            dreamzComposable(
                                Screen.EditDream.route,
                                arguments = listOf(navArgument("dreamId") {
                                    type = NavType.LongType
                                })
                            ) {
                                val editDreamViewModel: EditDreamViewModel = hiltViewModel()
                                val dreamDayId = it.arguments?.getLong("dreamId")
                                requireNotNull(dreamDayId, { "dreamDayId should not be null" })
                                EditDreamScreen(
                                    editDreamViewModel,
                                    dreamDayId,
                                    navController
                                )
                            }
                            dreamzComposable(Screen.Calendar.route) {
                                val calendarViewModel: CalendarViewModel = hiltViewModel()
                                CalendarScreen(calendarViewModel = calendarViewModel)
                            }
                            dreamzComposable(Screen.Peoples.route) {
                                val tagsViewModel: TagsViewModel = hiltViewModel()
                                TagsScreen(
                                    tagsViewModel = tagsViewModel,
                                    tagType = TagType.PEOPLE,
                                    navController = navController
                                )
                            }
                            dreamzComposable(Screen.Tags.route) {
                                val tagsViewModel: TagsViewModel = hiltViewModel()
                                TagsScreen(
                                    tagsViewModel = tagsViewModel,
                                    tagType = TagType.TAG,
                                    navController = navController
                                )
                            }
                            dreamzComposable(Screen.Graph.route) {
                                val graphViewModel: GraphViewModel = hiltViewModel()
                                GraphScreen(graphViewModel = graphViewModel)
                            }
                            dreamzComposable(Screen.Login.route) {
                                val loginViewModel: LoginViewModel = hiltViewModel()
                                LoginScreen(
                                    loginViewModel = loginViewModel,
                                    navController = navController
                                )
                            }

                        }
                    }

                }
            }
            if (!logged) {
                navController.navigate(Screen.Login.route)
            }
        }
    }

    fun NavGraphBuilder.dreamzComposable(
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
    ) {
        val duration = 250
        composable(
            route, arguments = arguments, enterTransition = { initial, _ ->
                fadeIn(
                    animationSpec = tween(duration)
                )
            },
            exitTransition = { _, target ->
                fadeOut(
                    animationSpec = tween(50)
                )
            },
            popEnterTransition = { initial, _ ->
                fadeIn(
                    animationSpec = tween(duration)
                )
            },
            popExitTransition = { _, target ->
                fadeOut(
                    animationSpec = tween(50)
                )
            }, content = content
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        DreamzTheme {
            Text("hi")
        }
    }
}