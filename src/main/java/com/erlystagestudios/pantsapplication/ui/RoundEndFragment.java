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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

	@Bind(R.id.txt_outcome)
	TextView txtPlayerOutcome;

	@Bind(R.id.txt_player1_place)
	TextView txtPlayer1Place;
	@Bind(R.id.txt_player1_place_score)
	TextView txtPlayer1PlaceScore;
	@Bind(R.id.txt_player2_place)
	TextView txtPlayer2Place;
	@Bind(R.id.txt_player2_place_score)
	TextView txtPlayer2PlaceScore;

	@Bind(R.id.txt_player1_animal)
	TextView txtPlayer1Animal;
	@Bind(R.id.txt_player1_animal_score)
	TextView txtPlayer1AnimalScore;
	@Bind(R.id.txt_player2_animal)
	TextView txtPlayer2Animal;
	@Bind(R.id.txt_player2_animal_score)
	TextView txtPlayer2AnimalScore;

	@Bind(R.id.txt_player1_name)
	TextView txtPlayer1Name;
	@Bind(R.id.txt_player1_name_score)
	TextView txtPlayer1NameScore;
	@Bind(R.id.txt_player2_name)
	TextView txtPlayer2Name;
	@Bind(R.id.txt_player2_name_score)
	TextView txtPlayer2NameScore;

	@Bind(R.id.txt_player1_thing)
	TextView txtPlayer1Thing;
	@Bind(R.id.txt_player1_thing_score)
	TextView txtPlayer1ThingScore;
	@Bind(R.id.txt_player2_thing)
	TextView txtPlayer2Thing;
	@Bind(R.id.txt_player2_thing_score)
	TextView txtPlayer2ThingScore;

	@Bind(R.id.txt_player1_song)
	TextView txtPlayer1Song;
	@Bind(R.id.txt_player1_song_score)
	TextView txtPlayer1SongScore;
	@Bind(R.id.txt_player2_song)
	TextView txtPlayer2Song;
	@Bind(R.id.txt_player2_song_score)
	TextView txtPlayer2SongScore;

	@Bind(R.id.txt_player1_score)
	TextView txtPlayer1Score;
	@Bind(R.id.txt_player2_score)
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
		txtPlayer1Place =(TextView)view.findViewById(R.id.txt_player1_place);
		txtPlayer1PlaceScore=(TextView)view.findViewById(R.id.txt_player1_place_score);
		txtPlayer2Place=(TextView)view.findViewById(R.id.txt_player2_place);
		txtPlayer2PlaceScore=(TextView)view.findViewById(R.id.txt_player2_place_score);

		txtPlayer1Animal =(TextView)view.findViewById(R.id.txt_player1_animal);
		txtPlayer1AnimalScore=(TextView)view.findViewById(R.id.txt_player1_animal_score);
		txtPlayer2Animal=(TextView)view.findViewById(R.id.txt_player2_animal);
		txtPlayer2AnimalScore=(TextView)view.findViewById(R.id.txt_player2_animal_score);

		txtPlayer1Name =(TextView)view.findViewById(R.id.txt_player1_name);
		txtPlayer1NameScore=(TextView)view.findViewById(R.id.txt_player1_name_score);
		txtPlayer2Name=(TextView)view.findViewById(R.id.txt_player2_name);
		txtPlayer2NameScore=(TextView)view.findViewById(R.id.txt_player2_name_score);

		txtPlayer1Thing =(TextView)view.findViewById(R.id.txt_player1_thing);
		txtPlayer1ThingScore=(TextView)view.findViewById(R.id.txt_player1_thing_score);
		txtPlayer2Thing=(TextView)view.findViewById(R.id.txt_player2_thing);
		txtPlayer2ThingScore=(TextView)view.findViewById(R.id.txt_player2_thing_score);

		txtPlayer1Song =(TextView)view.findViewById(R.id.txt_player1_song);
		txtPlayer1SongScore=(TextView)view.findViewById(R.id.txt_player1_song_score);
		txtPlayer2Song=(TextView)view.findViewById(R.id.txt_player2_song);
		txtPlayer2SongScore=(TextView)view.findViewById(R.id.txt_player2_song_score);

		txtPlayer1Score=(TextView)view.findViewById(R.id.txt_player1_score);
		txtPlayer2Score=(TextView)view.findViewById(R.id.txt_player2_score);

		txtPlayerOutcome=(TextView)view.findViewById(R.id.txt_outcome);
		Button btn_new_round =(Button)view.findViewById(R.id.btn_new_round);
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

			}
		});
		return view;
	}

	@Override
	public void onViewCreated (View view, Bundle savedInstanceState) {
		super.onViewCreated( view, savedInstanceState );
		Turn player1Turn = round.getHumanTurn();
		Turn player2Turn = round.getBotTurn();
//
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

	@OnClick(R.id.btn_new_round)
	public void onNewRoundClicked () {

	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach( activity );
//		gameListener = FragmentUtils.castActivity( activity, GameInteractionListener.class );
	}

	@Override
	public void onDetach () {
		super.onDetach();
		gameListener = null;
	}
}
