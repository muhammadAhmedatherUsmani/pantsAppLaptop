package com.erlystagestudios.pantsapplication;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.v4.app.*;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.*;
import android.widget.*;
import com.erlystagestudios.pantsapplication.controller.*;
import com.erlystagestudios.pantsapplication.model.*;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.erlystagestudios.pantsapplication.provider.TurnContract;
import com.erlystagestudios.pantsapplication.ui.*;
import com.hassan.androidutils.LogUtils;
import com.hassan.androidutils.TextUtils;
import com.squareup.spoon.Spoon;
import dagger.Component;
import org.junit.Before;

import java.sql.SQLException;
import java.util.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Trikster on 7/23/2015.
 */
public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {

	public static final String TAG = LogUtils.createTag( GameActivityTest.class );

	@Inject
	PantsDbHelper   dbHelper;
	@Inject
	Round           round;
	@Inject
	ScoreCalculator scorer;

	private String roundLetter = "M";
	private int               humanTurnScore;
	private TestGameComponent gameComponent;
	private GameActivity      activity;
	private View              containerLayout;

	public GameActivityTest () {
		super( GameActivity.class );
	}

	@Before
	@Override
	public void setUp () throws Exception {
		super.setUp();

//		Context context = activity.getApplicationContext();
		PantsApplication application =
				(PantsApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
		TestGameComponent testComponent = DaggerGameActivityTest_TestGameComponent
				.builder()
				.gameController( new GameController() )
				.turnDbModule( new TurnDbModule( application ) )
				.build();
		application.setComponent( testComponent );
		gameComponent = (TestGameComponent) PantsApplication.component( application );
		gameComponent.inject( this );

		setActivityInitialTouchMode( true );
		activity = getActivity();

		containerLayout = activity.findViewById( R.id.layout_container );
	}

	public void testGame () throws InterruptedException, SQLException {
		getInstrumentation().waitForIdleSync();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();

		getInstrumentation().waitForIdleSync();
		round.setCurrentLetter( roundLetter );
		final RoundStartFragment fragment =
				(RoundStartFragment) fragmentManager.findFragmentByTag( RoundStartFragment.TAG );
		assertEquals( fragment.getTag(), RoundStartFragment.TAG );
		round.setBotTurn( getBotTurn() );
		final Turn botTurn = round.getBotTurn();
		final Turn humanTurn = getHumanTurn();

		activity.runOnUiThread( new Runnable() {
			@Override
			public void run () {
				TextView txtAlphabet = (TextView) fragment.getView().findViewById( R.id.txt_round_alphabet );
				TextImageView imageText = (TextImageView) fragment.getView().findViewById( R.id.image_round_letter );
//				txtAlphabet.setText( roundLetter );
				imageText.setText( roundLetter );
				round.setCurrentLetter( roundLetter );
//				assertEquals( imageText.getText().toString(), round.getCurrentLetter() );
				assertEquals( roundLetter, round.getCurrentLetter() );

				assertNotNull( botTurn );
				assertNotNull( humanTurn );
			}
		} );
		Thread.sleep( 4 * 1000 );
		Spoon.screenshot( activity, "round_start" );
		Thread.sleep( 1 * 1000 );

		getInstrumentation().waitForIdleSync();
		LogUtils.logD( "Test",
				botTurn.getPlace() + "," + botTurn.getAnimal() + "," + botTurn.getName() + "," +
						botTurn.getThing() + "," + botTurn.getSong() );

		GameFragment gameFragment =
				(GameFragment) fragmentManager.findFragmentByTag( GameFragment.TAG );
		activity.runOnUiThread( new Runnable() {
			@Override
			public void run () {
				GameFragment gameFragment =
						(GameFragment) fragmentManager.findFragmentByTag( GameFragment.TAG );
				((EditText) gameFragment.getView().findViewById( R.id.edt_place )).setText( humanTurn.getPlace() );
				((EditText) gameFragment.getView().findViewById( R.id.edt_animal )).setText( humanTurn.getAnimal() );
				((EditText) gameFragment.getView().findViewById( R.id.edt_name )).setText( humanTurn.getName() );
				((EditText) gameFragment.getView().findViewById( R.id.edt_thing )).setText( humanTurn.getThing() );
				((EditText) gameFragment.getView().findViewById( R.id.edt_song )).setText( humanTurn.getSong() );
				//				((EditText) gameFragment.getView().findViewById( R.id.edt_place )).setText( "Amsterdam" );
				//				((EditText) gameFragment.getView().findViewById( R.id.edt_animal )).setText( "Ant" );
				//				((EditText) gameFragment.getView().findViewById( R.id.edt_name )).setText( "Alex" );
				//				((EditText) gameFragment.getView().findViewById( R.id.edt_thing )).setText( "Apple" );
				//				((EditText) gameFragment.getView().findViewById( R.id.edt_song )).setText( "All of me" );
			}
		} );
		Thread.sleep( 1 * 1000 );
		Spoon.screenshot( activity, "game_screen" );
		TouchUtils.clickView( this, gameFragment.getView().findViewById( R.id.btn_submit ) );

		assertNotNull( round.getHumanTurn() );
		getInstrumentation().waitForIdleSync();

		Turn turn = round.getHumanTurn();

		assertEquals( "round.getHumanTurn() should be human turn", PlayerType.HUMAN,
				round.getHumanTurn().getPlayerType() );

		Map<String, Object> map = new HashMap<>();
		map.put( TurnContract.TurnEntry.COLUMN_PLACE, turn.getPlace() );
		map.put( TurnContract.TurnEntry.COLUMN_ANIMAL, turn.getAnimal() );
		map.put( TurnContract.TurnEntry.COLUMN_NAME, turn.getName() );
		map.put( TurnContract.TurnEntry.COLUMN_THING, turn.getThing() );
		map.put( TurnContract.TurnEntry.COLUMN_SONG, turn.getSong() );
		map.put( TurnContract.TurnEntry.COLUMN_PLAYER_TYPE, turn.getPlayerType() );
		List<Turn> returnedTurn = dbHelper.getTurnDao().queryForFieldValuesArgs( map );
		assertTrue( "Human Turn should be saved & Size must be >= 1", returnedTurn.size() >= 1 );

		boolean foundTurn = false;
		for (Turn t : returnedTurn) {
			foundTurn = foundTurn || t.equals( turn );
		}
		assertEquals( "Human turn should be saved", true, foundTurn );

		RoundEndFragment endFragment =
				(RoundEndFragment) fragmentManager.findFragmentByTag( RoundEndFragment.TAG );
		TextView p1Score = (TextView) endFragment.getView().findViewById( R.id.txt_player1_score );
		TextView p2Score = (TextView) endFragment.getView().findViewById( R.id.txt_player2_score );
		Spoon.screenshot( activity, "round_end" );
		assertEquals( Integer.parseInt( TextUtils.getText( p1Score ) ), humanTurnScore );
		assertEquals( Integer.parseInt( TextUtils.getText( p2Score ) ), scorer.getScore( botTurn ) );

		Instrumentation.ActivityMonitor activityMonitor =
				getInstrumentation().addMonitor( GameActivity.class.getName(), null, false );
		TouchUtils.clickView( this, endFragment.getView().findViewById( R.id.btn_new_round ) );
		FragmentActivity gameActivity = (FragmentActivity) activityMonitor.waitForActivityWithTimeout( 10000 );
		assertNotNull( "GameActivity is null", gameActivity );
		assertEquals( "Monitor for GameActivity has not been called", 1, activityMonitor.getHits() );
		assertEquals( "Activity is of wrong type", GameActivity.class, gameActivity.getClass() );

		Fragment fragmentByTag = gameActivity.getSupportFragmentManager().findFragmentByTag( RoundStartFragment.TAG );
		assertNotNull( fragmentByTag );

		for (Fragment f : fragmentManager.getFragments()) {
			LogUtils.logD( TAG, f.getClass().getCanonicalName() + f.getTag() + " | " );
		}
		assertEquals( "Back Stack should have only 1 fragment", 1, fragmentManager.getBackStackEntryCount() );
		getInstrumentation().removeMonitor( activityMonitor );

		//		UiDevice instance = UiDevice.getInstance( getInstrumentation() );
		//		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen.png";
		//		boolean screenshot = instance.takeScreenshot( new File( path ) );
		//		assertTrue( "Failed to take screenshot", screenshot );
	}

	private Turn getBotTurn () {
		Turn botTurn = new Turn();
		botTurn.setRoundLetter( roundLetter );
		botTurn.setPlayerType( PlayerType.BOT );
		botTurn.setPlace( "Malta" );
		botTurn.setAnimal( "Magpie" );
		botTurn.setName( "Martin" );
		botTurn.setThing( "Map" );
		botTurn.setSong( "Magic" );
		botTurn.setScore( 50 );
		return botTurn;
	}

	private Turn getHumanTurn () {
		Turn humanTurn = new Turn();
		humanTurn.setRoundLetter( roundLetter );
		humanTurn.setPlace( "Mauritius" );
		humanTurn.setAnimal( "Magpie" );
		humanTurn.setName( "Mudassar" );
		humanTurn.setThing( "Mail" );
		humanTurn.setSong( "Mad World" );
		humanTurnScore = 45;
		return humanTurn;
	}

	@Override
	protected void tearDown () throws Exception {
		super.tearDown();
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface TestGameComponent extends GameComponent {

		void inject (GameActivityTest test);
	}
}
