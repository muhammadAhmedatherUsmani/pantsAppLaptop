package com.erlystagestudios.pantsapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erlystagestudios.pantsapplication.BotController;
import com.erlystagestudios.pantsapplication.PantsApplication;
import com.erlystagestudios.pantsapplication.R;
import com.erlystagestudios.pantsapplication.controller.GameComponent;
import com.erlystagestudios.pantsapplication.model.Round;
import com.hassan.androidutils.LogUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trikster on 7/1/2015.
 */
public class RoundStartFragment extends Fragment {

	public static final String TAG = LogUtils.createTag( RoundStartFragment.class );

	@Bind(R.id.txt_round_alphabet)
	TextView       txtAlphabet;
	@Bind(R.id.txt_timer)
	TextView       txtTimer;
	@Bind(R.id.root_layout)
	RelativeLayout rootLayout;
	@Bind(R.id.image_round_letter)
	TextImageView  imageRoundLetter;
	@Inject
	Round          round;
	private CountDownTimer          timer;
	@Nullable
	private GameInteractionListener gameListener;

	public static RoundStartFragment newInstance () {
		RoundStartFragment fragment = new RoundStartFragment();
		return fragment;
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_round_start, container, false );
		ButterKnife.bind( this, view );
		txtAlphabet =(TextView) view.findViewById(R.id.txt_round_alphabet);
		imageRoundLetter=(TextImageView) view.findViewById(R.id.image_round_letter);
		txtTimer = (TextView)view.findViewById(R.id.txt_timer);



		return view;
	}

	@Override
	public void onViewCreated (View view, Bundle savedInstanceState) {
		super.onViewCreated( view, savedInstanceState );

		GameComponent component = PantsApplication.component( getActivity() );
		component.inject( this );


		round.setCurrentLetter( Round.getRandomAlphabet().toUpperCase() );
		txtAlphabet.setText( round.getCurrentLetter() );
		imageRoundLetter.setText( round.getCurrentLetter() );
		new Thread( new BotController( round, component.dbHelper() ) ).start();
		timer = new CountDownTimer( 3000, 1000 ) {

			@Override
			public void onTick (long millisUntilFinished) {
				int secs = (int) (millisUntilFinished / 1000);
				String text = getResources().getQuantityString( R.plurals.no_of_secs, secs, secs );
				txtTimer.setText( text );
			}

			@Override
			public void onFinish () {
				txtTimer.setText( "Start!" );
				LogUtils.logD( RoundStartFragment.TAG, "Bot Turn: " + round.getBotTurn() );
				if (gameListener != null) {
					gameListener.displayGameFragment( round );
				}
			}
		};
		timer.start();
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach( activity );
		if (activity instanceof GameInteractionListener) {
			gameListener = (GameInteractionListener) activity;
		} else {
			throw new IllegalArgumentException( activity.getLocalClassName() + " must implement " +
					FragmentInteractionListener.class.getCanonicalName() );
		}
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
}
