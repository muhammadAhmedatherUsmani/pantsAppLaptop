package com.erlystagestudios.pantsapplication;

import android.os.Process;
import android.support.annotation.NonNull;
import com.erlystagestudios.pantsapplication.model.*;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.erlystagestudios.pantsapplication.provider.TurnContract;
import com.hassan.androidutils.LogUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Trikster on 7/13/2015.
 */
public class BotController implements Runnable {

	private final Round round;
	private final PantsDbHelper dbHelper;

	public BotController (Round round, PantsDbHelper dbHelper) {
		this.round = round;
		this.dbHelper = dbHelper;
	}

	public Turn getTurn () throws SQLException {
		String currentLetter = round.getCurrentLetter();
		Dao<Turn, Integer> turnDao = dbHelper.getTurnDao();
		QueryBuilder<Turn, Integer> builder = turnDao.queryBuilder();
		List<Turn> turns = builder.where().like( TurnContract.TurnEntry.COLUMN_ROUND_LETTER, currentLetter ).query();

		if (turns.size() <= 0) {
			throw new IllegalStateException( "Bot unable to make word with the given letter" );
		}
		if (turns.size() == 1) {
			return turns.get( 0 );
		}
		return getRandomTurn( currentLetter, turns );
	}

	@NonNull
	private Turn getRandomTurn (String currentLetter, List<Turn> turns) {
		Turn temp = new Turn();
		temp.setPlayerType( PlayerType.BOT );
		temp.setRoundLetter( currentLetter );

		int randomIndex = (int) (Math.random() * turns.size());
		temp.setPlace( turns.get( randomIndex ).getPlace() );
		randomIndex = (int) (Math.random() * turns.size());
		temp.setAnimal( turns.get( randomIndex ).getAnimal() );
		randomIndex = (int) (Math.random() * turns.size());
		temp.setName( turns.get( randomIndex ).getName() );
		randomIndex = (int) (Math.random() * turns.size());
		temp.setThing( turns.get( randomIndex ).getThing() );
		randomIndex = (int) (Math.random() * turns.size());
		temp.setSong( turns.get( randomIndex ).getSong() );

		return temp;
	}

	@Override
	public void run () {
		try {
			android.os.Process.setThreadPriority( Process.THREAD_PRIORITY_BACKGROUND );
			round.setBotTurn( getTurn() );
			LogUtils.logD( BotController.class.getCanonicalName(),
					"Loaded bot turn for letter " + round.getCurrentLetter() );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
