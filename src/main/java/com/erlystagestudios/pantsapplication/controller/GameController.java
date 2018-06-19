package com.erlystagestudios.pantsapplication.controller;

import com.erlystagestudios.pantsapplication.TurnValidator;
import com.erlystagestudios.pantsapplication.model.Round;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Trikster on 7/1/2015.
 */
@Module
@Singleton
public class GameController {

	private Round round;

	@Provides
	@Singleton
	Round provideGame () {
		round = new Round();
		return round;
	}

	@Provides
	@Singleton
	TurnValidator provideValidator () {
		return new TurnValidator();
	}

	@Provides
	@Singleton
	ScoreCalculator provideScoreCalculator (PantsDbHelper dbHelper) {
		return new ScoreCalculator( dbHelper );
	}
}
