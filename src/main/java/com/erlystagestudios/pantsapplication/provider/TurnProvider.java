package com.erlystagestudios.pantsapplication.provider;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import com.erlystagestudios.pantsapplication.provider.TurnContract.TurnEntry;

/**
 * Created by Trikster on 7/7/2015.
 */
public class TurnProvider extends ContentProvider {

	public static final int TURN             = 100;
	public static final int TURN_WITH_LETTER = 101;
	public static final int TURN_WITH_PLACE  = 101;
	public static final int TURN_WITH_ANIMAL = 102;
	public static final int TURN_WITH_NAME   = 103;
	public static final int TURN_WITH_THING  = 104;
	public static final int TURN_WITH_SONG   = 105;

	public static final UriMatcher URI_MATCHER = buildUriMatcher();

	private PantsDbHelper dbHelper;

	private static UriMatcher buildUriMatcher () {
		UriMatcher matcher = new UriMatcher( UriMatcher.NO_MATCH );
		final String authority = TurnContract.CONTENT_AUTHORITY;

		matcher.addURI( authority, TurnEntry.PATH_TURN, TURN );

		return matcher;
	}

	@Override
	public boolean onCreate () {
		dbHelper = new PantsDbHelper( getContext() );
		return true;
	}

	@Override
	public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor result;
		switch (URI_MATCHER.match( uri )) {
			case TURN:
				result = dbHelper.getReadableDatabase().query(
						TurnEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder
				);
				break;

			default:
				throw new IllegalArgumentException( "Unknown Uri" );
		}
		result.setNotificationUri( getContext().getContentResolver(), uri );
		return result;
	}

	@Override
	public String getType (Uri uri) {
		return null;
	}

	@Override
	public Uri insert (Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete (Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update (Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
}
