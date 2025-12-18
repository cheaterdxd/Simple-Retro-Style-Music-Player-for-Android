package com.retro.musicplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.retro.musicplayer.ui.screens.HomeScreen
import com.retro.musicplayer.ui.screens.PlayerScreen

/**
 * Navigation routes.
 */
object Routes {
    const val HOME = "home"
    const val PLAYER = "player"
    const val PLAYLIST = "playlist/{playlistId}"
    const val SETTINGS = "settings"
    
    fun playlist(playlistId: Long) = "playlist/$playlistId"
}

/**
 * App Navigation Graph.
 * 
 * Defines navigation between screens.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    onExitApp: () -> Unit,
    startDestination: String = Routes.HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Home screen
        composable(Routes.HOME) {
            HomeScreen(
                onSongClick = { song ->
                    navController.navigate(Routes.PLAYER)
                },
                onPlayerClick = {
                    navController.navigate(Routes.PLAYER)
                },
                onExitClick = onExitApp
            )
        }
        
        // Player screen
        composable(Routes.PLAYER) {
            PlayerScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
