package com.erlystagestudios.pantsapplication.ui;

import android.os.Bundle;
import com.erlystagestudios.pantsapplication.PantsApplication;
import com.erlystagestudios.pantsapplication.R;
import com.erlystagestudios.pantsapplication.model.Round;
import com.erlystagestudios.pantsapplication.provider.PantsDbHelper;
import com.hassan.androidutils.FragmentUtils;

import javax.inject.Inject;

public class StartActivity extends BaseActivity {

	@Inject
	PantsDbHelper dbHelper;

	@Inject
	Round round;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_start );

		PantsApplication.component( this ).inject( this );
		if (null == savedInstanceState) {
			addStartFragment();
		}
	}

	@Override
	public void addRoundStartFragment () {
		FragmentUtils.createBuilder( this )
					 .containerId( R.id.layout_container )
//					 .addToBackStack( true, RoundStartFragment.TAG )
					 .replace( RoundStartFragment.newInstance(), RoundStartFragment.TAG );
	}
}
