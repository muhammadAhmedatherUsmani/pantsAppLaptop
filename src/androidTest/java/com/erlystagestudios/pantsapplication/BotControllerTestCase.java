package com.erlystagestudios.pantsapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import com.erlystagestudios.pantsapplication.controller.*;
import com.erlystagestudios.pantsapplication.model.Round;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.j256.ormlite.stmt.SelectArg;
import dagger.Component;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.erlystagestudios.pantsapplication.provider.TurnContract.TurnEntry.*;

/**
 * Created by Trikster on 7/13/2015.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class BotControllerTestCase extends InstrumentationTestCase {

	BotController botController;
	@Inject
	Round         round;
	@Inject
	PantsDbHelper dbHelper;
	private TestGameComponent gameComponent;
	private String            CURRENT_LETTER;

	@Before
	@Override
	public void setUp () throws Exception {
		super.setUp();

		Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
		PantsApplication application = (PantsApplication) context;
		TestGameComponent component = DaggerBotControllerTestCase_TestGameComponent
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
	public void testBotTurn () throws SQLException {
		for (int i = 0; i < 10000; i++) {
			CURRENT_LETTER = Round.getRandomAlphabet().toUpperCase();
			round.setCurrentLetter( CURRENT_LETTER );
			botController = new BotController( round, dbHelper );

			Turn turn = botController.getTurn();
			validateWord( CURRENT_LETTER, COLUMN_PLACE, turn.getPlace() );
			validateWord( CURRENT_LETTER, COLUMN_ANIMAL, turn.getAnimal() );
			validateWord( CURRENT_LETTER, COLUMN_NAME, turn.getName() );
			validateWord( CURRENT_LETTER, COLUMN_THING, turn.getThing() );
			validateWord( CURRENT_LETTER, COLUMN_SONG, turn.getSong() );
		}
	}

	private void validateWord (String letter, String columnName, String value) throws SQLException {
		assertTrue( columnName + ": " + value + " should start with " + letter,
				TurnValidator.isValid( letter, value ) );

		List<Turn> turns = dbHelper.getTurnDao().queryBuilder().where()
				.like( columnName, new SelectArg( value ) ).query();
		assertTrue( columnName + " should be present in the db", turns.size() > 0 );
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface TestGameComponent extends GameComponent {

		void inject (BotControllerTestCase botTestCase);
	}
}
