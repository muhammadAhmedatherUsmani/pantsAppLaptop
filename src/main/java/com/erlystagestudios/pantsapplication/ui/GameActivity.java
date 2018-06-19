package com.erlystagestudios.pantsapplication.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.erlystagestudios.pantsapplication.*;
import com.erlystagestudios.pantsapplication.controller.ScoreCalculator;
import com.erlystagestudios.pantsapplication.model.Round;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.hassan.androidutils.FragmentUtils;
import com.hassan.androidutils.LogUtils;

import java.sql.SQLException;

import javax.inject.Inject;

public class GameActivity extends BaseActivity implements GameInteractionListener {

	private static final String TAG = LogUtils.createTag( GameActivity.class );

	@Inject
	Round           round;
	@Inject
	ScoreCalculator scorer;

	@Inject
	PantsDbHelper dbHelper;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_game );

		PantsApplication.component( this ).inject( this );
		if (null == savedInstanceState) {
			addRoundStartFragment();
		}
	}

	@Override
	public void displayGameFragment (Round round) {
		this.round = round;
		Fragment fragment = GameFragment.newInstance( round );
		FragmentUtils.createBuilder( this )
				.containerId( R.id.layout_container )
				.addToBackStack( false )
				.replace( fragment, GameFragment.TAG );
	}

	@Override
	public void displayRoundEndFragment (Round round) {
		this.round = round;
		Turn humanTurn = TurnValidator.cleanTurn( round.getHumanTurn() );
		try {
			int botScore = scorer.getScore( this.round.getBotTurn() );
			this.round.getBotTurn().setScore( botScore );
			int humanScore = scorer.calculateScore( this.round.getHumanTurn(),
					this.round.getBotTurn() );
			this.round.getHumanTurn().setScore( humanScore );
			dbHelper.getTurnDao().create( humanTurn );
			if (Round.RoundOutcome.NONE == this.round.getPlayer1Outcome()) {
				this.round
						.setPlayer1Outcome( humanScore < botScore ? Round.RoundOutcome.LOST : Round.RoundOutcome.DRAW );
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			LogUtils.logW( TAG, "Unable to add human turn to db" );
		}
		Fragment fragment = RoundEndFragment.newInstance( round );
		FragmentUtils.createBuilder( this )
//				.closeKeyboard()
				.containerId( R.id.layout_container )
				.addToBackStack( false )
				.replace( fragment, RoundEndFragment.TAG );
	}
}
