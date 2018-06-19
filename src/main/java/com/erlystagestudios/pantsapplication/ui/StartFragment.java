package com.erlystagestudios.pantsapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ImageView;
import android.widget.VideoView;

import com.erlystagestudios.pantsapplication.PantsApplication;
import com.erlystagestudios.pantsapplication.R;
import com.hassan.androidutils.LogUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Trikster on 6/30/2015.
 */
public class StartFragment extends Fragment {

	public static final String TAG = LogUtils.createTag( StartFragment.class );
	@Bind(R.id.imageView)
	ImageView imgLogo;
	@Bind(R.id.video_view)
	VideoView videoView;
	@Inject
	Context   appContext;
	@Nullable
	private FragmentInteractionListener interactionListener;

	public static StartFragment newInstance () {
		StartFragment fragment = new StartFragment();
		return fragment;
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_start, container, false );
		ButterKnife.bind( this, view );
		imgLogo = (ImageView) view.findViewById(R.id.imgg);
		return view;
	}

	@Override
	public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated( view, savedInstanceState );
		PantsApplication.component( getActivity() ).inject( this );
		/*Add in Oncreate() funtion after setContentView()*/
//
//		AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator( view.getContext(), R.animator.wave );
//		set.setTarget( imgLogo );
//		imgLogo.setPivotX( 0 );
//		imgLogo.setPivotY( 0 );

//		Uri uri = Uri.parse( "android.resource://" + appContext.getPackageName() + "/" + R.raw.pants );
//		videoView.setVideoURI( uri );
//		videoView.start();
//		videoView.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
//			@Override
//			public void onPrepared (MediaPlayer mp) {
//				videoView.setVisibility( View.VISIBLE );
////				mp.setLooping( true );
////			}
//		} );
	}

	@OnClick(R.id.btn_play)
	public void onPlayClicked (Button btnPlay) {
		if (interactionListener != null) {
			interactionListener.startGameActivity();
		}
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach( activity );
		if (activity instanceof FragmentInteractionListener) {
			interactionListener = (FragmentInteractionListener) activity;
		}
	}

	@Override
	public void onDetach () {
		super.onDetach();
		interactionListener = null;
	}

	@Override
	public void onDestroyView () {
		super.onDestroyView();
		ButterKnife.unbind( this );
	}
}
