package com.erlystagestudios.pantsapplication.ui;

import com.erlystagestudios.pantsapplication.model.Round;

/**
 * Created by Trikster on 7/23/2015.
 */
public interface GameInteractionListener extends FragmentInteractionListener {

	void addRoundStartFragment ();

	void displayGameFragment (Round round);

	void displayRoundEndFragment (Round round);
}
