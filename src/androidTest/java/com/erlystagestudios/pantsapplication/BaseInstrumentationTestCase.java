package com.erlystagestudios.pantsapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import com.erlystagestudios.pantsapplication.controller.*;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import dagger.Component;
import org.junit.After;
import org.junit.Before;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Trikster on 7/24/2015.
 */
public class BaseInstrumentationTestCase extends InstrumentationTestCase {

	protected BaseTestGameComponent gameComponent;
	@Inject
	PantsDbHelper dbHelper;

	@Before
	@Override
	public void setUp () throws Exception {
		super.setUp();

		Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
		PantsApplication application = (PantsApplication) context;
		BaseTestGameComponent component = DaggerBaseInstrumentationTestCase_BaseTestGameComponent
				.builder()
				.gameController( new GameController() )
				.turnDbModule( new TurnDbModule( application ) )
				.build();
		application.setComponent( component );
		gameComponent = (BaseTestGameComponent) PantsApplication.component( application );
		gameComponent.inject( this );
	}


	@After
	@Override
	public void tearDown () throws Exception {
		super.tearDown();
	}

	@Component(modules = {GameController.class, TurnDbModule.class})
	@Singleton
	public interface BaseTestGameComponent extends GameComponent {

		void inject (BaseInstrumentationTestCase testCase);
	}
}
