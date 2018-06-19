package com.erlystagestudios.pantsapplication;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;
import com.erlystagestudios.pantsapplication.controller.CSVReader;
import com.erlystagestudios.pantsapplication.model.PlayerType;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.erlystagestudios.pantsapplication.provider.TurnContract;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Trikster on 7/24/2015.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class PreloadedDbTest extends BaseInstrumentationTestCase {

	@Override
	public void setUp () throws Exception {
		super.setUp();
	}

	@Test
	public void verifyPreloadedDb () throws IOException, SQLException {
//		dbHelper.getWritableDatabase().dele	te( TurnContract.TurnEntry.TABLE_NAME, "", null );
		AssetManager assets = InstrumentationRegistry.getTargetContext().getAssets();
		InputStream stream = assets.open( "pants.csv" );
		InputStreamReader reader = new InputStreamReader( stream );
		CSVReader csvReader =
				new CSVReader( reader, CSVReader.DEFAULT_SEPARATOR, CSVReader.DEFAULT_QUOTE_CHARACTER, 1 );

		Turn temp;
		String[] words;
		int lines = 0;
		List<Turn> tempTurns = new ArrayList<>();
		while ((words = csvReader.readNext()) != null) {
			lines++;
			temp = new Turn();
			temp.setRoundLetter( words[0] );
			temp.setPlace( words[1] );
			temp.setAnimal( words[2] );
			temp.setName( words[3] );
			temp.setThing( words[4] );
			temp.setSong( words[5] );
			temp.setPlayerType( PlayerType.BOT );
			tempTurns.add( temp );
		}
		List<Turn> turns = dbHelper.getTurnDao().queryForAll();
		assertEquals( "Size of turns should be equal to records read from csv", lines, turns.size() );
//		assertEquals( "Size of turns should be equal to total records in the csv", 26, turns.size() );

		for (Turn turn : tempTurns) {
			Map<String, Object> map = new HashMap<>();
			map.put( TurnContract.TurnEntry.COLUMN_PLACE, turn.getPlace() );
			map.put( TurnContract.TurnEntry.COLUMN_ANIMAL, turn.getAnimal() );
			map.put( TurnContract.TurnEntry.COLUMN_NAME, turn.getName() );
			map.put( TurnContract.TurnEntry.COLUMN_THING, turn.getThing() );
			map.put( TurnContract.TurnEntry.COLUMN_SONG, turn.getSong() );
			List<Turn> returnedTurn = dbHelper.getTurnDao().queryForFieldValuesArgs( map );
			assertTrue( "Size must be 1", returnedTurn.size() == 1 );
			assertEquals( "Both turns should be equal", turn, returnedTurn.get( 0 ) );
		}

//		Assert.assertTrue( expected.equals( temp ) );
		stream.close();
		reader.close();
		dbHelper.close();
	}

	@Override
	public void tearDown () throws Exception {
		super.tearDown();
	}
}
