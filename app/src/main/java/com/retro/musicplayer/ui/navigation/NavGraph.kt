package com.retro.musicplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.retro.musicplayer.ui.screens.HomeScreen
import com.retro.musicplayer.ui.screens.PlayerScreen
import com.retro.musicplayer.ui.screens.PlaylistDetailScreen
import com.retro.musicplayer.ui.screens.PlaylistsScreen

/**
 * Navigation routes.
 */
object Routes {
    const val HOME = "home"
    const val PLAYER = "player"
    const val PLAYLISTS = "playlists"
    const val PLAYLIST_DETAIL = "playlist/{playlistId}"
    const val SETTINGS = "settings"
    
    fun playlistDetail(playlistId: Long) = "playlist/$playlistId"
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
                onExitClick = onExitApp,
                onPlaylistClick = { playlistId ->
                    navController.navigate(Routes.playlistDetail(playlistId))
                }
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
        
        // Playlists screen
        composable(Routes.PLAYLISTS) {
            PlaylistsScreen(
                onPlaylistClick = { playlistId ->
                    navController.navigate(Routes.playlistDetail(playlistId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Playlist detail screen
        composable(
            route = Routes.PLAYLIST_DETAIL,
            arguments = listOf(
                navArgument("playlistId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: return@composable
            PlaylistDetailScreen(
                playlistId = playlistId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSongClick = {
                    navController.navigate(Routes.PLAYER)
                }
            )
        }
    }
}
