package com.retro.musicplayer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.retro.musicplayer.data.model.PlaylistWithSongs
import com.retro.musicplayer.data.model.Song
import com.retro.musicplayer.ui.viewmodel.PlaylistViewModel

/**
 * Dialog để thêm bài hát vào playlist.
 * 
 * Features:
 * - Hiển thị danh sách playlists
 * - Tạo playlist mới nhanh
 * - Thêm bài vào playlist đã chọn
 */
@Composable
fun AddToPlaylistDialog(
    song: Song,
    onDismiss: () -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    val playlistsWithSongs by viewModel.playlistsWithSongs.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                Text(
                    text = "Add to Playlist",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                
                Divider()
                
                // Create new playlist button
                ListItem(
                    headlineContent = { Text("Create new playlist") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.clickable {
                        showCreateDialog = true
                    }
                )
                
                Divider()
                
                // Playlist list
                if (playlistsWithSongs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No playlists yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        items(
                            items = playlistsWithSongs,
                            key = { it.playlist.id }
                        ) { playlistWithSongs ->
                            val isInPlaylist = playlistWithSongs.songs.any { it.id == song.id }
                            
                            PlaylistItemForSelection(
                                playlist = playlistWithSongs,
                                isSelected = isInPlaylist,
                                onClick = {
                                    if (isInPlaylist) {
                                        viewModel.removeSongFromPlaylist(
                                            playlistWithSongs.playlist.id,
                                            song.id
                                        )
                                    } else {
                                        viewModel.addSongToPlaylist(
                                            playlistWithSongs.playlist.id,
                                            song.id
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
                
                Divider()
                
                // Close button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
    
    // Create playlist dialog
    if (showCreateDialog) {
        CreatePlaylistQuickDialog(
            songToAdd = song,
            onDismiss = { showCreateDialog = false },
            onConfirm = { name ->
                viewModel.createPlaylist(name, null)
                // Wait a bit then add song to the newly created playlist
                // (In production, you'd want to get the ID back from createPlaylist)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun PlaylistItemForSelection(
    playlist: PlaylistWithSongs,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = playlist.playlist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text("${playlist.songs.size} songs")
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.QueueMusic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "In playlist",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun CreatePlaylistQuickDialog(
    songToAdd: Song,
    onDismiss: () -> Unit,
    onConfirm: (name: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Playlist") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Playlist name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name.trim())
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
