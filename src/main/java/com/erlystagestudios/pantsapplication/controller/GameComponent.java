package com.erlystagestudios.pantsapplication.controller;

import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.erlystagestudios.pantsapplication.ui.*;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by Trikster on 7/7/2015.
 */
@Component(modules = {GameController.class,TurnDbModule.class})
@Singleton
public interface GameComponent {

	void inject (StartActivity activity);

	void inject (GameActivity activity);

	void inject (GameFragment fragment);

	void inject (RoundStartFragment fragment);

	void inject (RoundEndFragment fragment);

	PantsDbHelper dbHelper ();

	void inject (StartFragment fragment);
}
