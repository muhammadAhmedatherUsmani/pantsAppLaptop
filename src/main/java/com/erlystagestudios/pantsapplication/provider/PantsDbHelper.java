package com.erlystagestudios.pantsapplication.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.erlystagestudios.pantsapplication.model.Turn;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Trikster on 7/8/2015.
 */
public class PantsDbHelper extends OrmLiteSqliteOpenHelper {

	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "pants.db";
//    public Object shadow$_klass_;

    private SQLiteHelper helper;

	private Dao<Turn, Integer>                 turnDao;
	private RuntimeExceptionDao<Turn, Integer> turnException;

	public PantsDbHelper (Context context) {
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
		helper = new SQLiteHelper( context, DATABASE_NAME, null, DATABASE_VERSION );
		getWritableDatabase();
	}

	@Override
	public void onCreate (SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		helper.onCreate( sqLiteDatabase );
		try {
			TableUtils.createTable( connectionSource, Turn.class );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade (SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
						   int newVersion) {
		helper.onUpgrade( sqLiteDatabase, oldVersion, newVersion );
		try {
			TableUtils.dropTable( connectionSource, Turn.class, true );
			onCreate( sqLiteDatabase, connectionSource );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public SQLiteDatabase getWritableDatabase () {
		return helper.getWritableDatabase( this );
	}

	@Override
	public SQLiteDatabase getReadableDatabase () {
		return helper.getReadableDatabase( this );
	}

	@Override
	public void close () {
		helper.close();
	}



	public Dao<Turn, Integer> getTurnDao () {
		if (turnDao == null) {
			try {
				turnDao = getDao( Turn.class );
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return turnDao;
	}
}
