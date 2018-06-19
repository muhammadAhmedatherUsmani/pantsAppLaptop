package com.erlystagestudios.pantsapplication;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.*;
import com.erlystagestudios.pantsapplication.controller.*;
import com.erlystagestudios.pantsapplication.model.Round;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.erlystagestudios.pantsapplication.ui.StartActivity;
import com.erlystagestudios.pantsapplication.ui.StartFragment;
import com.hassan.androidutils.LogUtils;
import com.squareup.spoon.Spoon;
import dagger.Component;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class StartActivityTest extends ActivityInstrumentationTestCase2<StartActivity> {

	public static final String TAG = LogUtils.createTag( StartActivityTest.class );

	@Inject
	PantsDbHelper   dbHelper;
	@Inject
	Round           round;
	@Inject
	ScoreCalculator scorer;

	private TestGameComponent gameComponent;
	private View              containerLayout;
	private StartActivity     activity;

	public StartActivityTest () {
		super( StartActivity.class );
	}

	@Override
	protected void setUp () throws Exception {
		super.setUp();
		setActivityInitialTouchMode( true );
		activity = getActivity();

		Context context = getActivity().getApplicationContext();
		PantsApplication application = (PantsApplication) context;
		TestGameComponent testComponent = DaggerStartActivityTest_TestGameComponent
				.builder()
				.gameController( new GameController() )
				.turnDbModule( new TurnDbModule( application ) )
				.build();
		application.setComponent( testComponent );
		gameComponent = (TestGameComponent) PantsApplication.component( application );
		gameComponent.inject( this );

		containerLayout = activity.findViewById( R.id.layout_container );
	}

	public void testGame () throws InterruptedException {
		getInstrumentation().waitForIdleSync();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		StartFragment startFragment =
				(StartFragment) fragmentManager.findFragmentByTag( StartFragment.TAG );
		Spoon.screenshot( activity, "start_screen" );
		TouchUtils.clickView( this, startFragment.getView().findViewById( R.id.btn_play ) );
		getInstrumentation().waitForIdleSync();

//		RoundStartFragment fragment =
//				(RoundStartFragment) fragmentManager.findFragmentByTag( RoundStartFragment.TAG );
//		TextView txtAlphabet = (TextView) fragment.getView().findViewById( R.id.txt_round_alphabet );
//		assertEquals( txtAlphabet.getText().toString(), round.getCurrentLetter() );
//
//
//		Thread.sleep( 5 * 1000 );
//		final Turn botTurn = round.getBotTurn();
//		assertNotNull( botTurn );
//		getInstrumentation().waitForIdleSync();
//		LogUtils.logD( "Test",
//				botTurn.getPlace() + "," + botTurn.getAnimal() + "," + botTurn.getName() + "," +
//						botTurn.getThing() + "," + botTurn.getSong() );
//
//		GameFragment gameFragment =
//				(GameFragment) fragmentManager.findFragmentByTag( GameFragment.TAG );
//		activity.runOnUiThread( new Runnable() {
//			@Override
//			public void run () {
//				GameFragment gameFragment =
//						(GameFragment) fragmentManager.findFragmentByTag( GameFragment.TAG );
//				((EditText) gameFragment.getView().findViewById( R.id.edt_place )).setText( botTurn.getPlace() );
//				((EditText) gameFragment.getView().findViewById( R.id.edt_animal )).setText( botTurn.getAnimal() );
//				((EditText) gameFragment.getView().findViewById( R.id.edt_name )).setText( botTurn.getName() );
//				((EditText) gameFragment.getView().findViewById( R.id.edt_thing )).setText( botTurn.getThing() );
//				((EditText) gameFragment.getView().findViewById( R.id.edt_song )).setText( botTurn.getSong() );
////				((EditText) gameFragment.getView().findViewById( R.id.edt_place )).setText( "Amsterdam" );
////				((EditText) gameFragment.getView().findViewById( R.id.edt_animal )).setText( "Ant" );
////				((EditText) gameFragment.getView().findViewById( R.id.edt_name )).setText( "Alex" );
////				((EditText) gameFragment.getView().findViewById( R.id.edt_thing )).setText( "Apple" );
////				((EditText) gameFragment.getView().findViewById( R.id.edt_song )).setText( "All of me" );
//			}
//		} );
//		TouchUtils.clickView( this, gameFragment.getView().findViewById( R.id.btn_submit ) );
//
//		assertNotNull( round.getHumanTurn() );
//		getInstrumentation().waitForIdleSync();
//
//		RoundEndFragment endFragment =
//				(RoundEndFragment) fragmentManager.findFragmentByTag( RoundEndFragment.TAG );
//		TouchUtils.clickView( this, endFragment.getView().findViewById( R.id.btn_new_round ) );
//		getInstrumentation().waitForIdleSync();
//
//		Fragment fragmentByTag = fragmentManager.findFragmentByTag( RoundStartFragment.TAG );
//		assertNotNull( fragmentByTag );
//
//		for (Fragment f : fragmentManager.getFragments()) {
//			LogUtils.logD( TAG, f.getClass().getCanonicalName() + f.getTag() + " | " );
//		}
//		assertEquals( "Back Stack should have only 1 fragment", 1, fragmentManager.getBackStackEntryCount() );

//		UiDevice instance = UiDevice.getInstance( getInstrumentation() );
//		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen.png";
//		boolean screenshot = instance.takeScreenshot( new File( path ) );
//		assertTrue( "Failed to take screenshot", screenshot );
	}

	public Turn getBotTurn () throws SQLException {
		BotController botController = new BotController( round, dbHelper );
		return botController.getTurn();
	}

	@Override
	protected void tearDown () throws Exception {
		super.tearDown();
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface TestGameComponent extends GameComponent {

		void inject (StartActivityTest testDb);
	}
}
