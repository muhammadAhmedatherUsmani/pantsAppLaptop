package com.erlystagestudios.pantsapplication.provider;

import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by Trikster on 7/8/2015.
 */
public class TurnContract {

	public static final String CONTENT_AUTHORITY = "com.erlystagestudios.pantsapplication";

	public static final Uri BASE_CONTENT_URI = Uri.parse( "content://" + CONTENT_AUTHORITY );

	public static final class TurnEntry {

		public static final String PATH_TURN = "turn";

		public static final String TABLE_NAME = "turn";

		public static final String _ID                 = "_id";
		public static final String COLUMN_PLAYER_TYPE  = "playerType";
		public static final String COLUMN_PLACE        = "place";
		public static final String COLUMN_ANIMAL       = "animal";
		public static final String COLUMN_NAME         = "name";
		public static final String COLUMN_THING        = "thing";
		public static final String COLUMN_SONG         = "song";
		public static final String COLUMN_SCORE        = "score";
		public static final String COLUMN_ROUND_LETTER = "roundLetter";

		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
															  .appendPath( PATH_TURN ).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TURN;

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TURN;

		public static Uri buildTurnUri (long id) {
			return ContentUris.withAppendedId( CONTENT_URI, id );
		}

		private static Uri buildTurnUri (String columnName, String arg) {
			return CONTENT_URI.buildUpon().appendQueryParameter( columnName, arg ).build();
		}

		public static Uri buildTurnUri (String letter) {
			return buildTurnUri( COLUMN_ROUND_LETTER, letter );
		}

		public static Uri buildTurnUriWithPlace (String place) {
			return buildTurnUri( COLUMN_PLACE, place );
		}

		public static Uri buildTurnUriWithAnimal (String animal) {
			return buildTurnUri( COLUMN_ANIMAL, animal );
		}

		public static Uri buildTurnUriWithName (String name) {
			return buildTurnUri( COLUMN_NAME, name );
		}

		public static Uri buildTurnUriWithThing (String thing) {
			return buildTurnUri( COLUMN_THING, thing );
		}

		public static Uri buildTurnUriWithSong (String song) {
			return buildTurnUri( COLUMN_SONG, song );
		}
	}


}
