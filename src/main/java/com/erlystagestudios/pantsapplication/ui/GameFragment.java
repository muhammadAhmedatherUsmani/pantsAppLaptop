package com.erlystagestudios.pantsapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.erlystagestudios.pantsapplication.FragmentTags;
import com.erlystagestudios.pantsapplication.PantsApplication;
import com.erlystagestudios.pantsapplication.R;
import com.erlystagestudios.pantsapplication.controller.ScoreCalculator;
import com.erlystagestudios.pantsapplication.model.PlayerType;
import com.erlystagestudios.pantsapplication.model.Round;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.hassan.androidutils.LogUtils;
import com.hassan.androidutils.TextUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Trikster on 7/6/2015.
 */
public class GameFragment extends Fragment {

	public static final String TAG = LogUtils.createTag( GameFragment.class );
	@Bind(R.id.txt_timer)
	TextView txtTimer;
	@Bind(R.id.txt_alphabet)
	TextView        txtAlphabet;
	@Bind(R.id.edt_place)
	EditText        edtPlace;
	@Bind(R.id.edt_animal)
	EditText        edtAnimal;
	@Bind(R.id.edt_name)
	EditText        edtName;
	@Bind(R.id.edt_thing)
	EditText        edtThing;
	@Bind(R.id.edt_song)
	EditText        edtSong;
	@Inject
	Round           round;
	@Inject
	ScoreCalculator scorer;
	private CountDownTimer          timer;
	@Nullable
	private GameInteractionListener gameListener;

	public static Fragment newInstance (Round round) {
		GameFragment fragment = new GameFragment();
		Bundle args = new Bundle();
		args.putParcelable( FragmentTags.FragmentArgs.ARGS_ROUND, round );
		fragment.setArguments( args );
		return fragment;
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_game, container, false );
		ButterKnife.bind( this, view );
		return view;
	}

	@Override
	public void onViewCreated (View view, Bundle savedInstanceState) {
		super.onViewCreated( view, savedInstanceState );

		PantsApplication.component( getActivity() ).inject( this );

		txtAlphabet.setText( round.getCurrentLetter() );
		timer = new CountDownTimer( 60000, 1000 ) {
			@Override
			public void onTick (long millisUntilFinished) {
				int secs = (int) (millisUntilFinished / 1000);
				txtTimer.setText( String.format( "00:%02d", secs ) );
			}

			@Override
			public void onFinish () {
				txtTimer.setText( "Time Over!" );
				prepareTurn( true );
			}
		};
		timer.start();
	}

	@OnClick(R.id.btn_submit)
	public void submitTurn () {
		prepareTurn( false );
	}

	private void prepareTurn (boolean isTimeOver) {
		if (gameListener == null) {
			return;
		}
		Round.RoundOutcome playerOutcome = isTimeOver ? Round.RoundOutcome.TIME_OVER : Round.RoundOutcome.NONE;
		Turn humanTurn = makeUserTurn();
		this.round.setHumanTurn( humanTurn );
		this.round.setPlayer1Outcome( playerOutcome );
		gameListener.displayRoundEndFragment( this.round );
	}

	private Turn makeUserTurn () {
		Turn userTurn = new Turn();
		userTurn.setPlayerType( PlayerType.HUMAN );
		userTurn.setRoundLetter( round.getCurrentLetter() );

		userTurn.setPlace( TextUtils.getText( edtPlace ) );
		userTurn.setAnimal( TextUtils.getText( edtAnimal ) );
		userTurn.setName( TextUtils.getText( edtName ) );
		userTurn.setThing( TextUtils.getText( edtThing ) );
		userTurn.setSong( TextUtils.getText( edtSong ) );

		return userTurn;
	}

	@Override
	public void onStop () {
		super.onStop();
		timer.cancel();
	}

	@Override
	public void onDestroyView () {
		super.onDestroyView();
		ButterKnife.unbind( this );
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach( activity );
		if (activity instanceof GameInteractionListener) {
			gameListener = (GameInteractionListener) activity;
		} else {
			throw new IllegalStateException(
					"Activity must implement " + FragmentInteractionListener.class.getCanonicalName() );
		}
	}

	@Override
	public void onDetach () {
		super.onDetach();
		gameListener = null;
	}

	private class Scoring {

		void calculate () {
			// Make uri with place and letter, same for name and others, query in db
			// if row is returned score will be 5 otherwise 10

		}
	}
}
