package com.erlystagestudios.pantsapplication.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Trikster on 7/8/2015.
 */
public class DictionaryProvider extends ContentProvider {

	@Override
	public boolean onCreate () {
		return false;
	}

	@Override
	public Cursor query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return null;
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
