package com.erlystagestudios.pantsapplication.ui;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.*;
import com.erlystagestudios.pantsapplication.R;
import com.hassan.androidutils.LogUtils;

/**
 * Created by Trikster on 8/12/2015.
 */
public class TextImageView extends ImageView {

	private static final String TAG = LogUtils.createTag( TextImageView.class );

	private final Bitmap mask;
	private final int textSize = -1;
	private final Paint paint;
	private final int strokeColor;

	public TextImageView (Context context) {
		super( context );
		mask = BitmapFactory.decodeResource( getResources(), R.drawable.square_mask );

		paint = new Paint( Paint.ANTI_ALIAS_FLAG );
		paint.setARGB( 255, 0, 0, 0 );
		measureTextSize();
		strokeColor = getResources().getColor( R.color.dark_gray );
	}

	public TextImageView (Context context, AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public TextImageView (Context context, AttributeSet attrs, int defStyleAttr) {
		super( context, attrs, defStyleAttr );
		mask = BitmapFactory.decodeResource( getResources(), R.drawable.square_mask );

		paint = new Paint( Paint.ANTI_ALIAS_FLAG );
		paint.setARGB( 255, 0, 0, 0 );
		measureTextSize();
		strokeColor = getResources().getColor( R.color.dark_gray );
	}

	public void measureTextSize () {
		paint.setTextSize( getResources().getDimension( R.dimen.font_huge ) );
	}

	public void setText (String text) {
		Rect bounds = new Rect();
		paint.getTextBounds( text, 0, text.length(), bounds );

		Bitmap result = Bitmap.createBitmap( mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888 );
		Canvas c = new Canvas( result );

		int x = (result.getWidth() - bounds.width()) / 2 - bounds.left;
		int y = (result.getHeight() - bounds.height()) / 2 + bounds.height();
		c.drawText( text, x, y, paint );
		paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

		c.drawBitmap( mask, 0, 0, paint );
		paint.setXfermode( null );

		paint.setStyle( Paint.Style.STROKE );
		paint.setStrokeWidth( 2 );
		paint.setColor( strokeColor );
		c.drawText( text, x, y, paint );

		setImageBitmap( result );
	}
}
