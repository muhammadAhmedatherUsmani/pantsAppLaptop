package com.erlystagestudios.pantsapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.erlystagestudios.pantsapplication.FragmentTags.FragmentArgs;
import com.erlystagestudios.pantsapplication.PantsApplication;
import com.erlystagestudios.pantsapplication.R;
import com.erlystagestudios.pantsapplication.controller.ScoreCalculator;
import com.erlystagestudios.pantsapplication.model.Round;
import com.erlystagestudios.pantsapplication.model.Turn;
import com.hassan.androidutils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.erlystagestudios.pantsapplication.controller.GameConfig.SCORE_HIGH;

/**
 * Created by Trikster on 7/14/2015.
 */
public class RoundEndFragment extends Fragment {

	public static final String TAG = LogUtils.createTag( RoundEndFragment.class );
	@Inject
	ScoreCalculator scorer;
	@Inject
	Round           round;


	TextView txtPlayerOutcome;

	TextView txtPlayer1Place;
	TextView txtPlayer1PlaceScore;
	TextView txtPlayer2Place;
	TextView txtPlayer2PlaceScore;

	TextView txtPlayer1Animal;
	TextView txtPlayer1AnimalScore;
	TextView txtPlayer2Animal;
	TextView txtPlayer2AnimalScore;

	TextView txtPlayer1Name;
	TextView txtPlayer1NameScore;
	TextView txtPlayer2Name;
	TextView txtPlayer2NameScore;

	TextView txtPlayer1Thing;
	TextView txtPlayer1ThingScore;
	TextView txtPlayer2Thing;
	TextView txtPlayer2ThingScore;

	TextView txtPlayer1Song;
	TextView txtPlayer1SongScore;
	TextView txtPlayer2Song;
	TextView txtPlayer2SongScore;

	TextView txtPlayer1Score;
	TextView txtPlayer2Score;

	private GameInteractionListener gameListener;

	public static Fragment newInstance (Round round) {
		RoundEndFragment fragment = new RoundEndFragment();
		Bundle args = new Bundle();
		args.putParcelable( FragmentArgs.ARGS_ROUND, round );
		fragment.setArguments( args );
		return fragment;
	}

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );

		PantsApplication.component( getActivity() ).inject( this );

		if (savedInstanceState != null) {
			round = getArguments().getParcelable( FragmentArgs.ARGS_ROUND );
		}
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_round_end, container, false );
		ButterKnife.bind( this, view );
		txtPlayer1Place =view.findViewById(R.id.txt_player1_place);
		txtPlayer1PlaceScore=view.findViewById(R.id.txt_player1_place_score);
		txtPlayer2Place=view.findViewById(R.id.txt_player2_place);
		txtPlayer2PlaceScore=view.findViewById(R.id.txt_player2_place_score);

		txtPlayer1Animal =view.findViewById(R.id.txt_player1_animal);
		txtPlayer1AnimalScore=view.findViewById(R.id.txt_player1_animal_score);
		txtPlayer2Animal=view.findViewById(R.id.txt_player2_animal);
		txtPlayer2AnimalScore=view.findViewById(R.id.txt_player2_animal_score);

		txtPlayer1Name =view.findViewById(R.id.txt_player1_name);
		txtPlayer1NameScore=view.findViewById(R.id.txt_player1_name_score);
		txtPlayer2Name=view.findViewById(R.id.txt_player2_name);
		txtPlayer2NameScore=view.findViewById(R.id.txt_player2_name_score);

		txtPlayer1Thing =view.findViewById(R.id.txt_player1_thing);
		txtPlayer1ThingScore=view.findViewById(R.id.txt_player1_thing_score);
		txtPlayer2Thing=view.findViewById(R.id.txt_player2_thing);
		txtPlayer2ThingScore=view.findViewById(R.id.txt_player2_thing_score);

		txtPlayer1Song =view.findViewById(R.id.txt_player1_song);
		txtPlayer1SongScore=view.findViewById(R.id.txt_player1_song_score);
		txtPlayer2Song=view.findViewById(R.id.txt_player2_song);
		txtPlayer2SongScore=view.findViewById(R.id.txt_player2_song_score);

		txtPlayer1Score=view.findViewById(R.id.txt_player1_score);
		txtPlayer2Score=view.findViewById(R.id.txt_player2_score);

		txtPlayerOutcome=view.findViewById(R.id.txt_outcome);
		Button btn_new_round =view.findViewById(R.id.btn_new_round);
		btn_new_round.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int backStackEntryCount = getChildFragmentManager().getBackStackEntryCount();
				for (int i = backStackEntryCount - 1; i >= 0; i--) {
					FragmentManager.BackStackEntry backStackEntryAt = getChildFragmentManager().getBackStackEntryAt( i );
					getChildFragmentManager().popBackStackImmediate();
					if (backStackEntryAt.getName().equals( RoundStartFragment.TAG )) {
						break;
					}
				}

				getChildFragmentManager().popBackStackImmediate( RoundStartFragment.TAG,
						FragmentManager.POP_BACK_STACK_INCLUSIVE );
				if (gameListener != null) {
					gameListener.addRoundStartFragment();
				}

//				RoundStartFragment nextFrag= new RoundStartFragment();
//				getActivity().getSupportFragmentManager().beginTransaction()
//						.replace(view.getId(), nextFrag,"tag")
//						.addToBackStack(null)
//						.commit();

			}
		});
		return view;
	}

	@Override
	public void onViewCreated (View view, Bundle savedInstanceState) {
		super.onViewCreated( view, savedInstanceState );
		Turn player1Turn = round.getHumanTurn();
		Turn player2Turn = round.getBotTurn();

//		try {
//			player1Turn.setScore( scorer.getScore( player1Turn ) );
//			player2Turn.setScore( scorer.getScore( player2Turn ) );
//		}
//		catch (SQLException e) {
//			e.printStackTrace();
//		}

		txtPlayer1Place.setText( player1Turn.getPlace() );
		txtPlayer1PlaceScore.setText( String.valueOf( (int) (SCORE_HIGH * player1Turn.getPlaceMultiple()) ) );
		txtPlayer1Animal.setText( player1Turn.getAnimal() );
		txtPlayer1AnimalScore.setText( String.valueOf( (int) (SCORE_HIGH * player1Turn.getAnimalMultiple()) ) );
		txtPlayer1Name.setText( player1Turn.getName() );
		txtPlayer1NameScore.setText( String.valueOf( (int) (SCORE_HIGH * player1Turn.getNameMultiple()) ) );
		txtPlayer1Thing.setText( player1Turn.getThing() );
		txtPlayer1ThingScore.setText( String.valueOf( (int) (SCORE_HIGH * player1Turn.getThingMultiple()) ) );
		txtPlayer1Song.setText( player1Turn.getSong() );
		txtPlayer1SongScore.setText( String.valueOf( (int) (SCORE_HIGH * player1Turn.getSongMultiple()) ) );
		txtPlayer1Score.setText( String.valueOf( player1Turn.getScore() ) );

		txtPlayer2Place.setText( player2Turn.getPlace() );
		txtPlayer2PlaceScore.setText( String.valueOf( (int) (SCORE_HIGH * player2Turn.getPlaceMultiple()) ) );
		txtPlayer2Animal.setText( player2Turn.getAnimal() );
		txtPlayer2AnimalScore.setText( String.valueOf( (int) (SCORE_HIGH * player2Turn.getAnimalMultiple()) ) );
		txtPlayer2Name.setText( player2Turn.getName() );
		txtPlayer2NameScore.setText( String.valueOf( (int) (SCORE_HIGH * player2Turn.getNameMultiple()) ) );
		txtPlayer2Thing.setText( player2Turn.getThing() );
		txtPlayer2ThingScore.setText( String.valueOf( (int) (SCORE_HIGH * player2Turn.getThingMultiple()) ) );
		txtPlayer2Song.setText( player2Turn.getSong() );
		txtPlayer2SongScore.setText( String.valueOf( (int) (SCORE_HIGH * player2Turn.getSongMultiple()) ) );
		txtPlayer2Score.setText( String.valueOf( player2Turn.getScore() ) );

		switch (this.round.getPlayer1Outcome()) {
			case TIME_OVER:
				displayPlayerOutcome( R.array.array_time_over_outcome );
				break;
			case DRAW:
				displayPlayerOutcome( R.array.array_draw_outcome );
				break;
			case LOST:
				displayPlayerOutcome( R.array.array_lost_outcome );
				break;
			case NONE:
				txtPlayerOutcome.setText( R.string.msg_none_outcome );
				break;
		}
	}

	private void displayPlayerOutcome (@ArrayRes int arrayId) {
		String[] outcomeMessages = getResources().getStringArray( arrayId );
		int index = (int) (Math.random() * 5);
		txtPlayerOutcome.setText( outcomeMessages[index] );
	}

//	@OnClick(R.id.btn_new_round)
//	public void onNewRoundClicked () {
//
//	}

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
	public void onDetach () {
		super.onDetach();
		gameListener = null;
	}
}
