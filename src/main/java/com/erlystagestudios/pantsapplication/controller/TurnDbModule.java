package com.erlystagestudios.pantsapplication.controller;

import android.content.Context;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Trikster on 7/8/2015.
 */
@Module
@Singleton
public class TurnDbModule {

	private Context appContext;

	public TurnDbModule (Context appContext) {
		this.appContext = appContext;
	}

	@Provides
	Context provideContext () {
		return appContext;
	}

	@Provides
	@Singleton
	PantsDbHelper provideDbHelper (Context context) {
		return new PantsDbHelper( context );
	}
}
