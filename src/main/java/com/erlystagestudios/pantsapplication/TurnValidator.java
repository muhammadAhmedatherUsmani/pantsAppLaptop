package com.erlystagestudios.pantsapplication;

import com.erlystagestudios.pantsapplication.model.Turn;
import com.hassan.androidutils.TextUtils;

import static com.hassan.androidutils.TextUtils.EMPTY;

/**
 * Created by Trikster on 7/13/2015.
 */
public class TurnValidator {


	public static Turn cleanTurn (Turn turn) {
		Turn cleanTurn = turn;
		String roundLetter = turn.getRoundLetter();
		cleanTurn.setPlace( isValid( roundLetter, turn.getPlace() ) ? turn.getPlace() : EMPTY );
		cleanTurn.setAnimal( isValid( roundLetter, turn.getAnimal() ) ? turn.getAnimal() : EMPTY );
		cleanTurn.setName( isValid( roundLetter, turn.getName() ) ? turn.getName() : EMPTY );
		cleanTurn.setThing( isValid( roundLetter, turn.getThing() ) ? turn.getThing() : EMPTY );
		cleanTurn.setSong( isValid( roundLetter, turn.getSong() ) ? turn.getSong() : EMPTY );
		return cleanTurn;
	}

	/**
	 * Validates whether the provided word starts with given letter or not, ignoring case.
	 *
	 * @param letter Letter for the round
	 * @param word   word to validate against the letter
	 * @return True, if word starts with given letter, false otherwise.
	 */
	public static boolean isValid (String letter, String word) {
		if (TextUtils.isEmpty( letter ) || TextUtils.isEmpty( word )) {
			return false;
		}
		String firstLetter = word.trim().substring( 0, 1 );
		return firstLetter.equalsIgnoreCase( letter );
	}

	public boolean validateTurn (Turn turn) {
		// Validate word which is non-empty starts from current letter
		String roundLetter = turn.getRoundLetter();

		return isValid( roundLetter, turn.getPlace() ) &&
				isValid( roundLetter, turn.getAnimal() ) &&
				isValid( roundLetter, turn.getName() ) &&
				isValid( roundLetter, turn.getThing() ) &&
				isValid( roundLetter, turn.getSong() );
	}
}
