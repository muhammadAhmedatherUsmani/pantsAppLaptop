package com.erlystagestudios.pantsapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import com.erlystagestudios.pantsapplication.controller.*;
import com.erlystagestudios.pantsapplication.model.*;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import dagger.Component;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Trikster on 7/14/2015.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class GameTestCase extends InstrumentationTestCase {

	BotController   botController;
	@Inject
	Round           round;
	@Inject
	ScoreCalculator scorer;
	@Inject
	PantsDbHelper   dbHelper;
	private TestGameComponent gameComponent;
	private String            CURRENT_LETTER;

	@Before
	@Override
	public void setUp () throws Exception {
		super.setUp();

		Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
		PantsApplication application = (PantsApplication) context;
		TestGameComponent component = DaggerGameTestCase_TestGameComponent
				.builder()
				.gameController( new GameController() )
				.turnDbModule( new TurnDbModule( application ) )
				.build();
		application.setComponent( component );
		gameComponent = (TestGameComponent) PantsApplication.component( application );
		gameComponent.inject( this );

		CURRENT_LETTER = "A";
		round.setCurrentLetter( CURRENT_LETTER );
		botController = new BotController( round, dbHelper );
	}

	@Test
	public void testGame () throws SQLException {
		Turn userTurn = new Turn();
		userTurn.setPlayerType( PlayerType.HUMAN );
		userTurn.setRoundLetter( CURRENT_LETTER );

		userTurn.setPlace( "Karachi" );
		userTurn.setAnimal( " Ant" );
		userTurn.setName( "" );
		userTurn.setThing( "Amsterdam" );
		userTurn.setSong( "All of me" );

		Turn botTurn = botController.getTurn();

		int userScore = scorer.getScore( userTurn );
		int botScore = scorer.getScore( botTurn );

		assertEquals( "User score is 20", 20, userScore );
		assertTrue( "User score can not be greater than bot score", userScore <= botScore );
	}

	@Test
	public void testGame2 () throws SQLException {
		Turn userTurn = new Turn();
		userTurn.setPlayerType( PlayerType.HUMAN );
		userTurn.setRoundLetter( CURRENT_LETTER );

		userTurn.setPlace( "Ant" );
		userTurn.setAnimal( "Amsterdam" );
		userTurn.setName( "Alex" );
		userTurn.setThing( "All of me" );
		userTurn.setSong( "Apple" );

		Turn botTurn = botController.getTurn();

		int userScore = scorer.getScore( userTurn );
		int botScore = scorer.getScore( botTurn );

		assertEquals( "User score is 50", 50, userScore );
		assertTrue( "User score can not be greater than bot score", userScore <= botScore );
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface TestGameComponent extends GameComponent {

		void inject (GameTestCase gameTestCase);
	}
}
