package com.erlystagestudios.pantsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Trikster on 7/7/2015.
 */
public class Round implements Parcelable {

	public static final Creator<Round> CREATOR = new Creator<Round>() {
		@Override
		public Round createFromParcel (Parcel in) {
			return new Round( in );
		}

		@Override
		public Round[] newArray (int size) {
			return new Round[size];
		}
	};
	private String currentLetter;
	private Turn   humanTurn;
	private Turn   botTurn;
	private RoundOutcome player1Outcome = RoundOutcome.NONE;
	public Round () {
	}

	protected Round (Parcel in) {
		currentLetter = in.readString();
		humanTurn = in.readParcelable( Turn.class.getClassLoader() );
		botTurn = in.readParcelable( Turn.class.getClassLoader() );
		player1Outcome = RoundOutcome.valueOf( in.readString() );
	}

	public static String getRandomAlphabet () {
		int iLetter = (int) (Math.random() * 24 + 1);
		return String.valueOf( (char) (97 + iLetter) );
	}

	public String getCurrentLetter () {
		return currentLetter;
	}

	public void setCurrentLetter (String currentLetter) {
		this.currentLetter = currentLetter;
	}

	public Turn getHumanTurn () {
		return humanTurn;
	}

	public void setHumanTurn (Turn humanTurn) {
		this.humanTurn = humanTurn;
	}

	public Turn getBotTurn () {
		return botTurn;
	}

	public void setBotTurn (Turn botTurn) {
		this.botTurn = botTurn;
	}

	public RoundOutcome getPlayer1Outcome () {
		return player1Outcome;
	}

	public void setPlayer1Outcome (RoundOutcome player1Outcome) {
		this.player1Outcome = player1Outcome;
	}

	@Override
	public int describeContents () {
		return 0;
	}

	@Override
	public void writeToParcel (@NonNull Parcel dest, int flags) {
		dest.writeString( currentLetter );
		dest.writeParcelable( humanTurn, flags );
		dest.writeParcelable( botTurn, flags );
		dest.writeString( player1Outcome.name() );
	}

	public enum RoundOutcome {
		TIME_OVER, DRAW, LOST, NONE,
	}
}
