package com.erlystagestudios.pantsapplication;

import android.support.test.InstrumentationRegistry;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.*;
import com.erlystagestudios.pantsapplication.controller.*;
import com.erlystagestudios.pantsapplication.ui.GameActivity;
import com.erlystagestudios.pantsapplication.ui.RoundStartFragment;
import com.hassan.androidutils.LogUtils;
import com.squareup.spoon.Spoon;
import dagger.Component;
import org.junit.Before;

import java.sql.SQLException;

import javax.inject.Singleton;

/**
 * Created by Trikster on 8/7/2015.
 */
public class RoundStartTest extends ActivityInstrumentationTestCase2<GameActivity> {

	public static final String TAG = LogUtils.createTag( RoundStartTest.class );

	private TestGameComponent gameComponent;
	private GameActivity      activity;
	private View              containerLayout;

	public RoundStartTest () {
		super( GameActivity.class );
	}

	@Before
	@Override
	public void setUp () throws Exception {
		super.setUp();

		PantsApplication application =
				(PantsApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
		TestGameComponent testComponent = DaggerRoundStartTest_TestGameComponent
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
		final RoundStartFragment fragment =
				(RoundStartFragment) fragmentManager.findFragmentByTag( RoundStartFragment.TAG );
		Thread.sleep( 3 * 1000 );
		Spoon.screenshot( activity, "round_start" );
		Thread.sleep( 1 * 1000 );
	}

	@Override
	protected void tearDown () throws Exception {
		super.tearDown();
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface TestGameComponent extends GameComponent {

		void inject (RoundStartTest test);
	}
}
