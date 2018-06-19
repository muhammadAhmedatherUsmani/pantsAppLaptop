package com.erlystagestudios.pantsapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.erlystagestudios.pantsapplication.R;
import com.hassan.androidutils.FragmentUtils;

/**
 * Created by Trikster on 7/23/2015.
 */
public abstract class BaseActivity extends AppCompatActivity implements FragmentInteractionListener {

	@Override
	public void startGameActivity () {
		Intent intent = new Intent( this, GameActivity.class );
		intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		startActivity( intent );
		finish();
	}

	@Override
	public void addStartFragment () {
		FragmentUtils.createBuilder( this )
					 .containerId( R.id.layout_container )
					 .addToBackStack( false )
					 .add( StartFragment.newInstance(), StartFragment.TAG );
	}

	@Override
	public void addRoundStartFragment () {
		FragmentUtils.createBuilder( this )
					 .containerId( R.id.layout_container )
					 .addToBackStack( false )
					 .replace( RoundStartFragment.newInstance(), RoundStartFragment.TAG );
	}
}
