package com.pascal.movieku_compose.presentation.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pascal.movieku_compose.R
import com.pascal.movieku_compose.presentation.screen.detail.DetailScreen
import com.pascal.movieku_compose.presentation.navigation.NavigationItem
import com.pascal.movieku_compose.presentation.navigation.Screen
import com.pascal.movieku_compose.presentation.screen.favorite.FavoriteScreen
import com.pascal.movieku_compose.presentation.screen.home.HomeScreen
import com.pascal.movieku_compose.presentation.screen.popular.PopularScreen
import com.pascal.movieku_compose.presentation.ui.theme.MovieKuComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen(onTimeout = {
                navigateToMainScreen()
            })
        }
    }

    private fun navigateToMainScreen() {
        setContent {
            MovieKuComposeTheme(darkTheme = true) {
                AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(3000)
        onTimeout.invoke()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_image),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Screen.HomeScreen.route, Screen.PopularScreen.route, Screen.FavoriteScreen.route)) {
                BottomBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    innerPadding = paddingValues,
                    onMovieClicked = { movieId ->
                        navController.navigate(Screen.DetailScreen.createRoute(movieId))
                    }
                )
            }
            composable(route = Screen.PopularScreen.route) {
                PopularScreen(
                    innerPadding = paddingValues,
                    onMovieClicked = { movieId ->
                        navController.navigate(Screen.DetailScreen.createRoute(movieId))
                    }
                )
            }
            composable(route = Screen.FavoriteScreen.route) {
                FavoriteScreen(
                    innerPadding = paddingValues,
                    onMovieClicked = { movieId ->
                        navController.navigate(Screen.DetailScreen.createRoute(movieId))
                    }
                )
            }
            composable(
                route = Screen.DetailScreen.route,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                        nullable = false
                    },
                )
            ) {
                DetailScreen(
                    movieId = it.arguments!!.getInt("id"),
                    onNavBack = {
                        navController.popBackStack()
                    })
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = "Home",
                icon = Icons.Default.Home,
                screen = Screen.HomeScreen
            ),
            NavigationItem(
                title = "Popular",
                icon = Icons.Default.Star,
                screen = Screen.PopularScreen
            ),
            NavigationItem(
                title = "Favorite",
                icon = Icons.Default.Favorite,
                screen = Screen.FavoriteScreen
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}