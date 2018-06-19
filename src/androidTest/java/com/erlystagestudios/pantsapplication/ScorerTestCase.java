package com.erlystagestudios.pantsapplication;

import android.content.Context;
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
import dagger.Component;
import org.junit.*;
import org.junit.runner.RunWith;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.hassan.androidutils.TextUtils.EMPTY;

/**
 * Created by Trikster on 7/13/2015.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class ScorerTestCase extends InstrumentationTestCase {

	public static final String TAG = LogUtils.createTag( ScorerTestCase.class );

	@Inject
	ScoreCalculator scorer;
	@Inject
	PantsDbHelper dbHelper;
	private TestGameComponent gameComponent;

	@Before
	@Override
	public void setUp () throws Exception {
		super.setUp();

		Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
		PantsApplication application = (PantsApplication) context;
		TestGameComponent component = DaggerScorerTestCase_TestGameComponent
				.builder()
				.gameController( new GameController() )
				.turnDbModule( new TurnDbModule( application ) )
				.build();
		application.setComponent( component );
		gameComponent = (TestGameComponent) PantsApplication.component( application );
		gameComponent.inject( this );
	}

	@After
	public void tearDown () {
		dbHelper.getWritableDatabase().delete( TurnContract.TurnEntry.TABLE_NAME, "", null );
	}

	@Test
	public void testEmptyTurn () throws SQLException {
		Turn emptyTurn = new Turn();
		emptyTurn.setRoundLetter( "A" );
		int score = scorer.getScore( emptyTurn );
		assertEquals( "Empty turn should have 0 score", 0, score );

		emptyTurn.setPlace( EMPTY );
		emptyTurn.setAnimal( EMPTY );
		emptyTurn.setName( EMPTY );
		emptyTurn.setThing( EMPTY );
		emptyTurn.setSong( EMPTY );
		score = scorer.getScore( emptyTurn );
		assertEquals( "Empty turn should have 0 score", 0, score );
	}

	@Test
	public void testHighScoreTurn () throws SQLException {
		Turn highScoreTurn = new Turn();
		highScoreTurn.setRoundLetter( "A" );
		highScoreTurn.setPlace( "Argentina" );
		highScoreTurn.setAnimal( "African Penguin" );
		highScoreTurn.setName( "Alex" );
		highScoreTurn.setThing( "Arm" );
		highScoreTurn.setSong( "A place with no name" );
		int score = scorer.getScore( highScoreTurn );
		assertEquals( "Non db + non matching turn should have 50 score", 50, score );
	}

	@Test
	public void testTurns () throws SQLException {
		Turn botTurn = getBotTurn();
		Turn existingTurn = new Turn();
		int score;

		// Same turn as Bot
		existingTurn = new Turn();
		existingTurn.setRoundLetter( "M" );
		existingTurn.setPlace( "Malta" );
		existingTurn.setAnimal( "Magpie" );
		existingTurn.setName( "Martin" );
		existingTurn.setThing( "Map" );
		existingTurn.setSong( "Magic" );
		score = scorer.calculateScore( existingTurn, botTurn );
		existingTurn.setScore( score );
		saveTurn( existingTurn );
		assertEquals( "Same turn as bot", 25, score );

		existingTurn = new Turn();
		existingTurn.setRoundLetter( "M" );
		existingTurn.setPlace( "Malta" );
		existingTurn.setAnimal( "Magpie" );
		existingTurn.setName( "Martin" );
		existingTurn.setThing( "Map" );
		existingTurn.setSong( "Magic" );
		score = scorer.calculateScore( existingTurn, botTurn );
		existingTurn.setScore( score );
		saveTurn( existingTurn );
		assertEquals( "Same turn as bot and human last turn", 25, score );

		existingTurn = new Turn();
		existingTurn.setRoundLetter( "M" );
		existingTurn.setPlace( "Maldives" );
		existingTurn.setAnimal( "Monkey" );
		existingTurn.setName( "Mick" );
		existingTurn.setThing( "Mug" );
		existingTurn.setSong( "M83" );
		score = scorer.calculateScore( existingTurn, botTurn );
		existingTurn.setScore( score );
		saveTurn( existingTurn );
		assertEquals( "Exist in db but different than bot turn", 50, score );

		existingTurn = new Turn();
		existingTurn.setRoundLetter( "M" );
		existingTurn.setPlace( "Mauritius" );
		existingTurn.setAnimal( "Manta Ray" );
		existingTurn.setName( "Magda" );
		existingTurn.setThing( "Mail" );
		existingTurn.setSong( "Mad World" );
		score = scorer.calculateScore( existingTurn, botTurn );
		existingTurn.setScore( score );
		saveTurn( existingTurn );
		assertTrue( "New turn by human player", score == 50 );

		existingTurn = new Turn();
		existingTurn.setRoundLetter( "M" );
		existingTurn.setPlace( "Mauritius" );
		existingTurn.setAnimal( "Magpie" );
		existingTurn.setName( "Mudassar" );
		existingTurn.setThing( "Mail" );
		existingTurn.setSong( "Mad World" );
		score = scorer.calculateScore( existingTurn, botTurn );
		existingTurn.setScore( score );
		saveTurn( existingTurn );
		assertEquals( "3 existing human turn, 1 bot match and 1 new", 30, score );
	}

	private Turn getBotTurn () {
		Turn botTurn = new Turn();
		botTurn.setRoundLetter( "M" );
		botTurn.setPlayerType( PlayerType.BOT );
		botTurn.setPlace( "Malta" );
		botTurn.setAnimal( "Magpie" );
		botTurn.setName( "Martin" );
		botTurn.setThing( "Map" );
		botTurn.setSong( "Magic" );
		botTurn.setScore( 50 );
		return botTurn;
	}

	public void saveTurn (Turn humanTurn) {
		humanTurn = TurnValidator.cleanTurn( humanTurn );
		try {
			dbHelper.getTurnDao().create( humanTurn );
		}
		catch (SQLException e) {
			e.printStackTrace();
			LogUtils.logW( TAG, "Unable to add human turn to db" );
		}
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface TestGameComponent extends GameComponent {

		void inject (ScorerTestCase scorerTestCase);
	}
}
