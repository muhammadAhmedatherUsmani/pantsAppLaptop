package com.erlystagestudios.pantsapplication.controller;

import com.erlystagestudios.pantsapplication.TurnValidator;
import com.erlystagestudios.pantsapplication.model.PlayerType;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.erlystagestudios.pantsapplication.provider.TurnContract.TurnEntry;
import com.hassan.androidutils.TextUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.erlystagestudios.pantsapplication.controller.GameConfig.SCORE_HIGH;

/**
 * Created by Trikster on 7/9/2015.
 */
public class ScoreCalculator {

	PantsDbHelper dbHelper;

	@Inject
	public ScoreCalculator (PantsDbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public int getScore (Turn turn) throws SQLException {
		// TODO get all PANTS for the playertype and letter and if given is unique score will be 10 otherwise, 5

//		if (turn.getPlayerType() == PlayerType.HUMAN){	turn.setPlaceMultiple(1);
//			turn.setAnimalMultiple(1);
//			turn.setNameMultiple(1);
//			turn.setThingMultiple( 1 );
//			turn.setSongMultiple( 1 );
//			return 5 * SCORE_HIGH;
//		}

		float multipleSum = 0, temp = 0;
		temp = getWordMultiple( turn.getRoundLetter(), TurnEntry.COLUMN_PLACE, turn.getPlace() );
		turn.setPlaceMultiple( temp );
		multipleSum += temp;
		turn.setAnimalMultiple( temp = getWordMultiple( turn.getRoundLetter(), TurnEntry.COLUMN_ANIMAL,
				turn.getAnimal() ) );
		multipleSum += temp;
		turn.setNameMultiple( temp = getWordMultiple( turn.getRoundLetter(), TurnEntry.COLUMN_NAME,
				turn.getName() ) );
		multipleSum += temp;
		turn.setThingMultiple( temp = getWordMultiple( turn.getRoundLetter(), TurnEntry.COLUMN_THING,
				turn.getThing() ) );
		multipleSum += temp;
		turn.setSongMultiple( temp = getWordMultiple( turn.getRoundLetter(), TurnEntry.COLUMN_SONG,
				turn.getSong() ) );
		multipleSum += temp;

		return (int) (multipleSum * SCORE_HIGH);
	}

	public int getBotScore (Turn botTurn, Turn humanTurn) throws SQLException {
		// calculate place score
		String humanPlace = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getPlace());
		float botPlaceScore = botTurn.getPlace().equalsIgnoreCase(humanPlace) ? 0.5f : 1f;
		botTurn.setPlaceMultiple(botPlaceScore);

		// calculate animal score
		String humanAnimal = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getAnimal());
		float botAnimalScore = botTurn.getAnimal().equalsIgnoreCase(humanAnimal) ? 0.5f : 1f;
		botTurn.setAnimalMultiple(botAnimalScore);

		// calculate name score
		String humanName = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getName());
		float botNameScore = botTurn.getName().equalsIgnoreCase(humanName) ? 0.5f : 1f;
		botTurn.setNameMultiple(botNameScore);


		// calculate thing score
		String humanThing = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getThing());
		float botThingScore = botTurn.getThing().equalsIgnoreCase(humanThing) ? 0.5f : 1f;
		botTurn.setThingMultiple(botThingScore);

		// calculate song score
		String humanSong = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getSong());
		float botSongScore = botTurn.getSong().equalsIgnoreCase(humanSong) ? 0.5f : 1f;
		botTurn.setSongMultiple(botSongScore);

		float totalMulitpleScore = botPlaceScore + botAnimalScore + botNameScore + botThingScore + botSongScore;
		return (int) (totalMulitpleScore * SCORE_HIGH);
	}

	public int getHumanScore (Turn botTurn, Turn humanTurn) throws SQLException {
		// calculate place score
		float humanPlaceScore = 0;
		String humanPlace = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getPlace());

		if (isWordInDb(botTurn.getRoundLetter(), TurnEntry.COLUMN_PLACE, humanPlace)) {
			humanPlaceScore = botTurn.getPlace().equalsIgnoreCase(humanPlace) ? 0.5f : 1f;
		}

		humanTurn.setPlaceMultiple(humanPlaceScore);

		// calculate animal score
		float humanAnimalScore = 0;
		String humanAnimal = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getAnimal());

		if (isWordInDb(botTurn.getRoundLetter(), TurnEntry.COLUMN_ANIMAL, humanAnimal)) {
			humanAnimalScore = botTurn.getAnimal().equalsIgnoreCase(humanAnimal) ? 0.5f : 1f;
		}
		humanTurn.setAnimalMultiple(humanAnimalScore);

		// calculate name score
		float humanNameScore = 0;
		String humanName = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getName());

		if (isWordInDb(botTurn.getRoundLetter(), TurnEntry.COLUMN_NAME, humanName)) {
			humanNameScore = botTurn.getName().equalsIgnoreCase(humanName) ? 0.5f : 1f;
		} else if ( !humanName.isEmpty() ) {
			humanNameScore = 1f;
		}
		humanTurn.setNameMultiple(humanNameScore);

		// calculate thing score
		float humanThingScore = 0;
		String humanThing = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getThing());

		if (isWordInDb(botTurn.getRoundLetter(), TurnEntry.COLUMN_THING, humanThing)) {
			humanThingScore = botTurn.getThing().equalsIgnoreCase(humanThing) ? 0.5f : 1f;
		}
		humanTurn.setThingMultiple(humanThingScore);

		// calculate song score
		float humanSongScore = 0;
		String humanSong = getTrimmedValue(botTurn.getRoundLetter() ,humanTurn.getSong());

		if (isWordInDb(botTurn.getRoundLetter(), TurnEntry.COLUMN_SONG, humanSong)) {
			humanSongScore = botTurn.getSong().equalsIgnoreCase(humanSong) ? 0.5f : 1f;
		} else if ( !humanSong.isEmpty() ) {
			humanSongScore = 1f;
		}
		humanTurn.setSongMultiple(humanSongScore);

		float totalMulitpleScore = humanPlaceScore + humanAnimalScore + humanNameScore + humanThingScore + humanSongScore;
		return (int) (totalMulitpleScore * SCORE_HIGH);
	}

	public int calculateScore (Turn humanTurn, Turn botTurn) throws SQLException {
		float multipleSum =0,temp = 0;
		humanTurn.setPlaceMultiple( temp = getWordMultiple( humanTurn.getRoundLetter(),
				TurnEntry.COLUMN_PLACE,
				humanTurn.getPlace(),
				botTurn.getPlace() ) );
		multipleSum += temp;

		humanTurn.setAnimalMultiple( temp = getWordMultiple( humanTurn.getRoundLetter(),
				TurnEntry.COLUMN_ANIMAL,
				humanTurn.getAnimal(),
				botTurn.getAnimal() ) );
		multipleSum += temp;

		humanTurn.setNameMultiple( temp = getWordMultiple( humanTurn.getRoundLetter(),
				TurnEntry.COLUMN_NAME,
				humanTurn.getName(),
				botTurn.getName() ) );
		multipleSum += temp;

		humanTurn.setThingMultiple( temp = getWordMultiple( humanTurn.getRoundLetter(),
				TurnEntry.COLUMN_THING,
				humanTurn.getThing(),
				botTurn.getThing() ) );
		multipleSum += temp;

		humanTurn.setSongMultiple( temp = getWordMultiple( humanTurn.getRoundLetter(),
				TurnEntry.COLUMN_SONG,
				humanTurn.getSong(),
				botTurn.getSong() ) );
		multipleSum += temp;

		return (int) (multipleSum * SCORE_HIGH);
	}

	private float getWordMultiple (String roundLetter, String columnName, String humanValue, String botValue) throws SQLException {
		String humanTrimmedValue = getTrimmedValue( roundLetter, humanValue );
		String botTrimmedValue = getTrimmedValue( roundLetter, botValue );

		if (humanTrimmedValue == null) {
			return 0;
		} else if (botTrimmedValue == null) {
			return 1;
		} else if (humanTrimmedValue.equalsIgnoreCase( botTrimmedValue )) {
			return 0.5f;
		} else {
			return getWordMultipleFromDbValidation( roundLetter, columnName, humanValue );
		}
	}

	private float getWordMultiple (String roundLetter, String columnName, String value) throws SQLException {
		String trimmedValue = getTrimmedValue( roundLetter, value );
		if (trimmedValue == null) return 0;
		return getWordMultipleFromDbValidation( roundLetter, columnName, trimmedValue );
	}

	private float getWordMultipleFromDbValidation (String roundLetter, String columnName, String trimmedValue) throws SQLException {
		Dao<Turn, Integer> turnDao = dbHelper.getTurnDao();
				Map<String, Object> map = new HashMap<>();
				map.put( TurnEntry.COLUMN_ROUND_LETTER, roundLetter );
				map.put( columnName, trimmedValue );

		QueryBuilder<Turn, Integer> builder = turnDao.queryBuilder();
		Where<Turn, Integer> where = builder.where();
		where.and(
				where.eq( TurnEntry.COLUMN_ROUND_LETTER, roundLetter ),
				where.like( columnName, trimmedValue ),
				where.eq( TurnEntry.COLUMN_PLAYER_TYPE, PlayerType.HUMAN )
		);

		List<Turn> turns = turnDao.query( builder.prepare() );
		turnDao.queryForFieldValuesArgs( map );

		return turns.size() == 0 ? 1 : 0.5f; // No turn found in the column with given value then multiple is 1 else 0
	}


	private boolean isWordInDb (String roundLetter, String columnName, String trimmedValue) throws SQLException {
		Dao<Turn, Integer> turnDao = dbHelper.getTurnDao();
		Map<String, Object> map = new HashMap<>();
		map.put( TurnEntry.COLUMN_ROUND_LETTER, roundLetter );
		map.put( columnName, trimmedValue );

		QueryBuilder<Turn, Integer> builder = turnDao.queryBuilder();
		Where<Turn, Integer> where = builder.where();
		where.and(
				where.eq( TurnEntry.COLUMN_ROUND_LETTER, roundLetter ),
				where.like( columnName, trimmedValue )
				// where.eq( TurnEntry.COLUMN_PLAYER_TYPE, PlayerType.BOT )
		);

		List<Turn> turns = turnDao.query( builder.prepare() );
		turnDao.queryForFieldValuesArgs( map );

		// if word in db return true else false
		return turns.size() > 0;
	}

	private String getTrimmedValue (String roundLetter, String value) {
		if (TextUtils.isEmpty( value )) return null;

		String trimmedValue = value.trim();
		return TurnValidator.isValid( roundLetter, trimmedValue ) ? trimmedValue : null;
	}
}
