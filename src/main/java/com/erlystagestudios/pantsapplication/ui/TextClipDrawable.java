package com.erlystagestudios.pantsapplication.ui;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.widget.*;
import com.erlystagestudios.pantsapplication.R;
import com.hassan.androidutils.LogUtils;

/**
 * Created by Trikster on 8/7/2015.
 */
public class TextClipDrawable extends Drawable {

	private static final String TAG = LogUtils.createTag( TextClipDrawable.class );

	private static final float SHADE_FACTOR = 0.9f;
	private static final int LAYER_FLAGS = Canvas.ALL_SAVE_FLAG |
			Canvas.ALL_SAVE_FLAG |
			Canvas.ALL_SAVE_FLAG |
			Canvas.ALL_SAVE_FLAG |
			Canvas.ALL_SAVE_FLAG;
	private final Paint     textPaint;
	private final Paint     borderPaint;
	private final String    text;
	private final int       color;
	private final int       height;
	private final int       width;
	private final int       fontSize;
	private final float     radius;
	private final int       borderThickness;
	private final Drawable  mask;
	private final Resources resources;
	private       Paint     maskPaint;


	public TextClipDrawable (Resources res) {
		super();
		this.resources = res;
		mask = res.getDrawable( R.drawable.square_mask );
		// shape properties
		height = -1;
		width = -1;
		radius = 0;

		maskPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
		maskPaint.setColor( Color.BLACK );
		maskPaint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.DST_IN ) );

		// text and color
//		text = builder.toUpperCase ? builder.text.toUpperCase() : builder.text;
		color = Color.WHITE;

		// text paint settings
		fontSize = 64;
		textPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
		textPaint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.MULTIPLY ) );
		textPaint.setColor( Color.WHITE );
		textPaint.setTypeface( Typeface.DEFAULT );
		textPaint.setTextAlign( Paint.Align.CENTER );
		textPaint.setStrokeWidth( 0 );

		// border paint settings
		borderThickness = 0;
		borderPaint = new Paint();
		borderPaint.setColor( getDarkerShade( color ) );
		borderPaint.setStyle( Paint.Style.STROKE );
		borderPaint.setStrokeWidth( borderThickness );

		// drawable paint color
		text = "HELLO";
	}

	public static Bitmap getImage (ImageView imageView, Resources resources) {
		Bitmap mask = BitmapFactory.decodeResource( resources, R.drawable.square_mask );
		String text = "HIM";

		Bitmap result = Bitmap.createBitmap( mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888 );
		Canvas canvas = new Canvas( result );

		Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
		paint.setTextSize( 54 );
		paint.setARGB( 255, 0, 0, 0 );

		canvas.drawText( text, 10, mask.getHeight(), paint );
		paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

		canvas.drawBitmap( mask, 0, 0, paint );
		paint.setXfermode( null );
		imageView.setImageBitmap( result );
		return result;
	}

	private int getDarkerShade (int color) {
		return Color.rgb( (int) (SHADE_FACTOR * Color.red( color )),
				(int) (SHADE_FACTOR * Color.green( color )),
				(int) (SHADE_FACTOR * Color.blue( color )) );
	}

	@Override
	public void draw (Canvas canvas) {
		Rect r = getBounds();


		// draw border
//		if (borderThickness > 0) {
//			drawBorder(canvas);
//		}
//		canvas.drawColor( Color.GRAY );
//		int count = canvas.save();
//		mask.setBounds( r );
//		mask.draw( canvas );

//		int count = canvas.saveLayer( bounds.left, bounds.top, bounds.right, bounds.bottom, textPaint,
//				LAYER_FLAGS );

//		canvas.translate(r.left, r.top);

		Bitmap bitmap = BitmapFactory.decodeResource( resources, R.drawable.square_mask );
//		int x = 0;
//		float y = 0;
//
//		// draw text
//		int width = this.width < 0 ? r.width() : this.width;
//		int height = this.height < 0 ? r.height() : this.height;
////		int fontSize = this.fontSize < 0 ? (Math.min( width, height ) / 2) : this.fontSize;
//		textPaint.setTextSize( fontSize );
//		canvas.drawText( text, 100, 100, textPaint );
//		canvas.drawBitmap( bitmap, x, y, null );
//		LogUtils.logD( TAG, "x(" + x + "),y(" + y + ")" );
//		canvas.restoreToCount( count );

		Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
		paint.setTextSize( 54 );
		paint.setARGB( 255, 0, 0, 0 );

		canvas.drawText( text, 10, bitmap.getHeight(), paint );
		paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

		canvas.drawBitmap( bitmap, 0, 0, paint );
		paint.setXfermode( null );
	}

	private void drawBorder (Canvas canvas) {
		RectF rect = new RectF( getBounds() );
		rect.inset( borderThickness / 2, borderThickness / 2 );
		canvas.drawRect( rect, borderPaint );
	}

	@Override
	public void setAlpha (int alpha) {
		textPaint.setAlpha( alpha );
	}

	@Override
	public void setColorFilter (ColorFilter cf) {
		textPaint.setColorFilter( cf );
	}

	@Override
	public int getOpacity () {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public int getIntrinsicWidth () {
		return width;
	}

	@Override
	public int getIntrinsicHeight () {
		return height;
	}
}
