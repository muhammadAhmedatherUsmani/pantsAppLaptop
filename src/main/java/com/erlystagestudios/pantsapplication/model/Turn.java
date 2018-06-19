package com.erlystagestudios.pantsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.erlystagestudios.pantsapplication.provider.TurnContract.TurnEntry;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Trikster on 7/8/2015.
 */
@DatabaseTable(tableName = TurnEntry.TABLE_NAME)
public class Turn implements Parcelable {

	public static final Parcelable.Creator<Turn> CREATOR = new Parcelable.Creator<Turn>() {
		@Override
		public Turn createFromParcel (Parcel in) {
			return new Turn( in );
		}

		@Override
		public Turn[] newArray (int size) {
			return new Turn[size];
		}
	};

	@DatabaseField(generatedId = true, columnName = TurnEntry._ID)
	int id;

	@DatabaseField(columnName = TurnEntry.COLUMN_PLACE)
	String place;

	@DatabaseField(columnName = TurnEntry.COLUMN_ANIMAL)
	String animal;

	@DatabaseField(columnName = TurnEntry.COLUMN_NAME)
	String name;

	@DatabaseField(columnName = TurnEntry.COLUMN_THING)
	String thing;

	@DatabaseField(columnName = TurnEntry.COLUMN_SONG)
	String song;

	@DatabaseField(columnName = TurnEntry.COLUMN_SCORE)
	int score;

	float placeMultiple;
	float animalMultiple;
	float nameMultiple;
	float thingMultiple;
	float songMultiple;

	@DatabaseField(columnName = TurnEntry.COLUMN_ROUND_LETTER)
	String roundLetter;

	@DatabaseField(columnName = TurnEntry.COLUMN_PLAYER_TYPE, defaultValue = "HUMAN")
	private PlayerType playerType; //Values can be bot or human, bot used to import csv.

	public Turn () {
		playerType = PlayerType.HUMAN;
	}

	protected Turn (Parcel in) {
		id = in.readInt();
		place = in.readString();
		animal = in.readString();
		name = in.readString();
		thing = in.readString();
		song = in.readString();
		score = in.readInt();
		roundLetter = in.readString();
		playerType = PlayerType.valueOf( in.readString() );
	}

	public int getId () {
		return id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public PlayerType getPlayerType () {
		return playerType;
	}

	public void setPlayerType (PlayerType playerType) {
		this.playerType = playerType;
	}

	public String getPlace () {
		return place;
	}

	public void setPlace (String place) {
		this.place = place;
	}

	public String getAnimal () {
		return animal;
	}

	public void setAnimal (String animal) {
		this.animal = animal;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getThing () {
		return thing;
	}

	public void setThing (String thing) {
		this.thing = thing;
	}

	public String getSong () {
		return song;
	}

	public void setSong (String song) {
		this.song = song;
	}

	public int getScore () {
		return score;
	}

	public void setScore (int score) {
		this.score = score;
	}

	public String getRoundLetter () {
		return roundLetter;
	}

	public void setRoundLetter (String roundLetter) {
		this.roundLetter = roundLetter;
	}

	public float getPlaceMultiple () {
		return placeMultiple;
	}

	public void setPlaceMultiple (float placeMultiple) {
		this.placeMultiple = placeMultiple;
	}

	public float getAnimalMultiple () {
		return animalMultiple;
	}

	public void setAnimalMultiple (float animalMultiple) {
		this.animalMultiple = animalMultiple;
	}

	public float getNameMultiple () {
		return nameMultiple;
	}

	public void setNameMultiple (float nameMultiple) {
		this.nameMultiple = nameMultiple;
	}

	public float getThingMultiple () {
		return thingMultiple;
	}

	public void setThingMultiple (float thingMultiple) {
		this.thingMultiple = thingMultiple;
	}

	public float getSongMultiple () {
		return songMultiple;
	}

	public void setSongMultiple (float songMultiple) {
		this.songMultiple = songMultiple;
	}

	@Override
	public boolean equals (Object o) {
		if (o == null || !(o instanceof Turn)) {
			return false;
		}

		if (o == this) return true;
		Turn turn = (Turn) o;
		return new EqualsBuilder()
				.append( roundLetter, turn.getRoundLetter() )
				.append( place, turn.getPlace() )
				.append( animal, turn.getAnimal() )
				.append( name, turn.getName() )
				.append( thing, turn.getThing() )
				.append( song, turn.getSong() )
				.isEquals();
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder( 7,11 )
				.append( roundLetter )
				.append( place )
				.append( animal )
				.append( name )
				.append( thing )
				.append( song )
				.toHashCode();
	}

	@Override
	public String toString () {
		StringBuilder builder = new StringBuilder( super.toString() );
		builder.append( "roundLetter:" ).append( roundLetter );
		builder.append( " place:" ).append( place );
		builder.append( " animal:" ).append( animal );
		builder.append( " name:" ).append( name );
		builder.append( " thing:" ).append( thing );
		builder.append( " song:" ).append( song );
		builder.append( " score:" ).append( score );

		return builder.toString();
	}

	@Override
	public int describeContents () {
		return 0;
	}

	@Override
	public void writeToParcel (@NonNull Parcel dest, int flags) {
		dest.writeInt( id );
		dest.writeString( place );
		dest.writeString( animal );
		dest.writeString( name );
		dest.writeString( thing );
		dest.writeString( song );
		dest.writeInt( score );
		dest.writeString( roundLetter );
		dest.writeString( playerType.toString() );
	}

}
