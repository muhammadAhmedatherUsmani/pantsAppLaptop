package com.erlystagestudios.pantsapplication;

import android.app.Application;
import android.content.Context;
import com.erlystagestudios.pantsapplication.controller.*;

/**
 * Created by Trikster on 7/7/2015.
 */
public class PantsApplication extends Application {

	private GameComponent component;

	@Override
	public void onCreate () {
		super.onCreate();
		setComponent( initComponent() );
	}

	protected GameComponent initComponent () {
		return DaggerComponentInitializer.init( this );
	}

	public void setComponent (GameComponent component) {
		this.component = component;
	}

	public static GameComponent component (Context context) {
		return ((PantsApplication) context.getApplicationContext()).component;
	}

	private static final class DaggerComponentInitializer {

		public static GameComponent init (Context context) {
			return DaggerGameComponent.builder()
									  .gameController( new GameController() )
									  .turnDbModule( new TurnDbModule( context ) )
									  .build();
		}
	}
}
