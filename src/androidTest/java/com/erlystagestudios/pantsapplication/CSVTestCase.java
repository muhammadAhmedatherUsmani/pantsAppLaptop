package com.erlystagestudios.pantsapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import com.erlystagestudios.pantsapplication.controller.*;
import com.erlystagestudios.pantsapplication.model.PlayerType;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.erlystagestudios.pantsapplication.provider.TurnContract;
import com.hassan.androidutils.LogUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.*;
import dagger.Component;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Trikster on 7/10/2015.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class CSVTestCase extends InstrumentationTestCase {

	private static final String TAG = LogUtils.createTag( CSVTestCase.class );
	@Inject
	PantsDbHelper dbHelper;
	private TestGameComponent gameComponent;

	@Before
	@Override
	public void setUp () throws Exception {
		super.setUp();

		Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
		PantsApplication application = (PantsApplication) context;
		TestGameComponent component = DaggerCSVTestCase_TestGameComponent
				.builder()
				.gameController( new GameController() )
				.turnDbModule( new TurnDbModule( application ) )
				.build();
		application.setComponent( component );
		gameComponent = (TestGameComponent) PantsApplication.component( application );
		gameComponent.inject( this );
	}

	@Test
	public void createCSV () throws IOException, SQLException {
		dbHelper.getWritableDatabase().delete( TurnContract.TurnEntry.TABLE_NAME, "", null );
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
			dbHelper.getTurnDao().createOrUpdate( temp );
		}
		List<Turn> turns = dbHelper.getTurnDao().queryForAll();
		assertEquals( "Size of turns should be equal to records read from csv", lines, turns.size() );
//		assertEquals( "Size of turns should be equal to total records in the csv", 26, turns.size() );
		Dao<Turn, Integer> turnDao = dbHelper.getTurnDao();
		for (Turn turn : tempTurns) {
			QueryBuilder<Turn, Integer> builder = turnDao.queryBuilder();
			Where<Turn, Integer> where = builder.where();
			where.and( where.like( TurnContract.TurnEntry.COLUMN_PLACE, new SelectArg( turn.getPlace() ) ),
					where.like( TurnContract.TurnEntry.COLUMN_ANIMAL, new SelectArg( turn.getAnimal() ) ),
					where.like( TurnContract.TurnEntry.COLUMN_NAME, new SelectArg( turn.getName() ) ),
					where.like( TurnContract.TurnEntry.COLUMN_THING, new SelectArg( turn.getThing() ) ),
					where.like( TurnContract.TurnEntry.COLUMN_SONG, new SelectArg( turn.getSong() ) ) );

			List<Turn> returnedTurn = turnDao.query( builder.prepare() );
			assertTrue( "Size must be 1", returnedTurn.size() == 1 );
			assertEquals( "Both turns should be equal", turn, returnedTurn.get( 0 ) );
			LogUtils.logD( TAG, returnedTurn.toString() );
		}

//		Assert.assertTrue( expected.equals( temp ) );
		stream.close();
		reader.close();
		dbHelper.close();
	}

//	public void testDbInsertRead () throws java.sql.SQLException {
//		List<Turn> turns = dbHelper.getTurnDao().queryForAll();
//
//		Turn turn = new Turn();
//		turn.setRoundLetter( "A" );
//		turn.setPlace( "Amsterdam" );
//		turn.setAnimal( "Ant" );
//		turn.setName( "Alan" );
//		turn.setThing( "Art" );
//		turn.setSong( "Art" );
//		turn.setScore( 50 );
//
//		int id = dbHelper.getTurnDao().create( turn );
//		Turn tempTurn = dbHelper.getTurnDao().queryForId( id );
//
//		Assert.assertTrue( tempTurn.equals( turn ) );
//	}
//
//	public void testRandomAlphabets () {
//		GameController gameController = new GameController();
//		String randomAlphabet;
//
//		for (int i = 0; i < 10; i++) {
//			randomAlphabet = Round.getRandomAlphabet();
//			Assert.assertTrue( Pattern.matches( "[A-Za-z]", randomAlphabet ) );
//		}
//	}

	@After
	@Override
	public void tearDown () throws Exception {
		super.tearDown();
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface TestGameComponent extends GameComponent {

		void inject (CSVTestCase testDb);
	}
}
