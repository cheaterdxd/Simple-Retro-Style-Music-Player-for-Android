package com.retro.musicplayer.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.retro.musicplayer.data.model.Playlist;
import com.retro.musicplayer.data.model.PlaylistSongCrossRef;
import com.retro.musicplayer.data.model.PlaylistWithSongs;
import com.retro.musicplayer.data.model.Song;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlaylistDao_Impl implements PlaylistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Playlist> __insertionAdapterOfPlaylist;

  private final EntityInsertionAdapter<PlaylistSongCrossRef> __insertionAdapterOfPlaylistSongCrossRef;

  private final EntityDeletionOrUpdateAdapter<Playlist> __deletionAdapterOfPlaylist;

  private final EntityDeletionOrUpdateAdapter<Playlist> __updateAdapterOfPlaylist;

  private final SharedSQLiteStatement __preparedStmtOfDeletePlaylistById;

  private final SharedSQLiteStatement __preparedStmtOfRemoveSongFromPlaylist;

  private final SharedSQLiteStatement __preparedStmtOfClearPlaylist;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSongPosition;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePlaylistSongCount;

  public PlaylistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlaylist = new EntityInsertionAdapter<Playlist>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `playlists` (`id`,`name`,`description`,`createdAt`,`updatedAt`,`songCount`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Playlist entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindLong(4, entity.getCreatedAt());
        statement.bindLong(5, entity.getUpdatedAt());
        statement.bindLong(6, entity.getSongCount());
      }
    };
    this.__insertionAdapterOfPlaylistSongCrossRef = new EntityInsertionAdapter<PlaylistSongCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `playlist_songs` (`playlistId`,`songId`,`position`,`addedAt`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaylistSongCrossRef entity) {
        statement.bindLong(1, entity.getPlaylistId());
        statement.bindLong(2, entity.getSongId());
        statement.bindLong(3, entity.getPosition());
        statement.bindLong(4, entity.getAddedAt());
      }
    };
    this.__deletionAdapterOfPlaylist = new EntityDeletionOrUpdateAdapter<Playlist>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `playlists` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Playlist entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPlaylist = new EntityDeletionOrUpdateAdapter<Playlist>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `playlists` SET `id` = ?,`name` = ?,`description` = ?,`createdAt` = ?,`updatedAt` = ?,`songCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Playlist entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindLong(4, entity.getCreatedAt());
        statement.bindLong(5, entity.getUpdatedAt());
        statement.bindLong(6, entity.getSongCount());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeletePlaylistById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlists WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveSongFromPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlist_songs WHERE playlistId = ? AND songId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM playlist_songs WHERE playlistId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSongPosition = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE playlist_songs SET position = ? WHERE playlistId = ? AND songId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePlaylistSongCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE playlists SET songCount = (SELECT COUNT(*) FROM playlist_songs WHERE playlistId = ?), updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPlaylist(final Playlist playlist,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlaylist.insertAndReturnId(playlist);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addSongToPlaylist(final PlaylistSongCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlaylistSongCrossRef.insert(crossRef);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addSongsToPlaylist(final List<PlaylistSongCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlaylistSongCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlaylist(final Playlist playlist,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlaylist.handle(playlist);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePlaylist(final Playlist playlist,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlaylist.handle(playlist);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlaylistById(final long playlistId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePlaylistById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeletePlaylistById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object removeSongFromPlaylist(final long playlistId, final long songId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveSongFromPlaylist.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, songId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRemoveSongFromPlaylist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearPlaylist(final long playlistId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearPlaylist.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearPlaylist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSongPosition(final long playlistId, final long songId, final int newPosition,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSongPosition.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, newPosition);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, playlistId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, songId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateSongPosition.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePlaylistSongCount(final long playlistId, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePlaylistSongCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, playlistId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdatePlaylistSongCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Playlist>> getAllPlaylists() {
    final String _sql = "SELECT * FROM playlists ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"playlists"}, new Callable<List<Playlist>>() {
      @Override
      @NonNull
      public List<Playlist> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "songCount");
          final List<Playlist> _result = new ArrayList<Playlist>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Playlist _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSongCount;
            _tmpSongCount = _cursor.getInt(_cursorIndexOfSongCount);
            _item = new Playlist(_tmpId,_tmpName,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpSongCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPlaylistById(final long playlistId,
      final Continuation<? super Playlist> $completion) {
    final String _sql = "SELECT * FROM playlists WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Playlist>() {
      @Override
      @Nullable
      public Playlist call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "songCount");
          final Playlist _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSongCount;
            _tmpSongCount = _cursor.getInt(_cursorIndexOfSongCount);
            _result = new Playlist(_tmpId,_tmpName,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpSongCount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<PlaylistWithSongs> getPlaylistWithSongs(final long playlistId) {
    final String _sql = "SELECT * FROM playlists WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"playlist_songs", "songs",
        "playlists"}, new Callable<PlaylistWithSongs>() {
      @Override
      @Nullable
      public PlaylistWithSongs call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfSongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "songCount");
            final LongSparseArray<ArrayList<Song>> _collectionSongs = new LongSparseArray<ArrayList<Song>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionSongs.containsKey(_tmpKey)) {
                _collectionSongs.put(_tmpKey, new ArrayList<Song>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipsongsAscomRetroMusicplayerDataModelSong(_collectionSongs);
            final PlaylistWithSongs _result;
            if (_cursor.moveToFirst()) {
              final Playlist _tmpPlaylist;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpDescription;
              if (_cursor.isNull(_cursorIndexOfDescription)) {
                _tmpDescription = null;
              } else {
                _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              final int _tmpSongCount;
              _tmpSongCount = _cursor.getInt(_cursorIndexOfSongCount);
              _tmpPlaylist = new Playlist(_tmpId,_tmpName,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpSongCount);
              final ArrayList<Song> _tmpSongsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpSongsCollection = _collectionSongs.get(_tmpKey_1);
              _result = new PlaylistWithSongs(_tmpPlaylist,_tmpSongsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PlaylistWithSongs>> getAllPlaylistsWithSongs() {
    final String _sql = "SELECT * FROM playlists ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"playlist_songs", "songs",
        "playlists"}, new Callable<List<PlaylistWithSongs>>() {
      @Override
      @NonNull
      public List<PlaylistWithSongs> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfSongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "songCount");
            final LongSparseArray<ArrayList<Song>> _collectionSongs = new LongSparseArray<ArrayList<Song>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionSongs.containsKey(_tmpKey)) {
                _collectionSongs.put(_tmpKey, new ArrayList<Song>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipsongsAscomRetroMusicplayerDataModelSong(_collectionSongs);
            final List<PlaylistWithSongs> _result = new ArrayList<PlaylistWithSongs>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final PlaylistWithSongs _item;
              final Playlist _tmpPlaylist;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final String _tmpDescription;
              if (_cursor.isNull(_cursorIndexOfDescription)) {
                _tmpDescription = null;
              } else {
                _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              final int _tmpSongCount;
              _tmpSongCount = _cursor.getInt(_cursorIndexOfSongCount);
              _tmpPlaylist = new Playlist(_tmpId,_tmpName,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpSongCount);
              final ArrayList<Song> _tmpSongsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpSongsCollection = _collectionSongs.get(_tmpKey_1);
              _item = new PlaylistWithSongs(_tmpPlaylist,_tmpSongsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Playlist>> searchPlaylists(final String query) {
    final String _sql = "SELECT * FROM playlists WHERE name LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"playlists"}, new Callable<List<Playlist>>() {
      @Override
      @NonNull
      public List<Playlist> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "songCount");
          final List<Playlist> _result = new ArrayList<Playlist>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Playlist _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpSongCount;
            _tmpSongCount = _cursor.getInt(_cursorIndexOfSongCount);
            _item = new Playlist(_tmpId,_tmpName,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpSongCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPlaylistSongCount(final long playlistId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM playlist_songs WHERE playlistId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object isSongInPlaylist(final long playlistId, final long songId,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM playlist_songs WHERE playlistId = ? AND songId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, songId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp != 0;
          } else {
            _result = false;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipsongsAscomRetroMusicplayerDataModelSong(
      @NonNull final LongSparseArray<ArrayList<Song>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipsongsAscomRetroMusicplayerDataModelSong(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `songs`.`id` AS `id`,`songs`.`title` AS `title`,`songs`.`artist` AS `artist`,`songs`.`album` AS `album`,`songs`.`duration` AS `duration`,`songs`.`path` AS `path`,`songs`.`albumArtUri` AS `albumArtUri`,`songs`.`dateAdded` AS `dateAdded`,`songs`.`trackNumber` AS `trackNumber`,`songs`.`year` AS `year`,_junction.`playlistId` FROM `playlist_songs` AS _junction INNER JOIN `songs` ON (_junction.`songId` = `songs`.`id`) WHERE _junction.`playlistId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      // _junction.playlistId;
      final int _itemKeyIndex = 10;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfTitle = 1;
      final int _cursorIndexOfArtist = 2;
      final int _cursorIndexOfAlbum = 3;
      final int _cursorIndexOfDuration = 4;
      final int _cursorIndexOfPath = 5;
      final int _cursorIndexOfAlbumArtUri = 6;
      final int _cursorIndexOfDateAdded = 7;
      final int _cursorIndexOfTrackNumber = 8;
      final int _cursorIndexOfYear = 9;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Song> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Song _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpTitle;
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
          final String _tmpArtist;
          _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
          final String _tmpAlbum;
          _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
          final long _tmpDuration;
          _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
          final String _tmpPath;
          _tmpPath = _cursor.getString(_cursorIndexOfPath);
          final String _tmpAlbumArtUri;
          if (_cursor.isNull(_cursorIndexOfAlbumArtUri)) {
            _tmpAlbumArtUri = null;
          } else {
            _tmpAlbumArtUri = _cursor.getString(_cursorIndexOfAlbumArtUri);
          }
          final long _tmpDateAdded;
          _tmpDateAdded = _cursor.getLong(_cursorIndexOfDateAdded);
          final int _tmpTrackNumber;
          _tmpTrackNumber = _cursor.getInt(_cursorIndexOfTrackNumber);
          final int _tmpYear;
          _tmpYear = _cursor.getInt(_cursorIndexOfYear);
          _item_1 = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpDuration,_tmpPath,_tmpAlbumArtUri,_tmpDateAdded,_tmpTrackNumber,_tmpYear);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
