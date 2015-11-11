package benak.tomas.crimemonitor.client.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import benak.tomas.crimemonitor.R;

/**
 * A custom view class used to display a crime index value.
 * 
 * <p>
 * This class displays the crime index as a gauge. The gauge has a visible scale
 * and a hand showing the value on that scale.
 * 
 * <p>
 * The scale goes from the minimum crime index value to the maximum crime index
 * value. The scale is colored. It goes from red at the scale's minimum through
 * the midpoint colors in between the minimum and maximum to green at the
 * scales's maximum. All the scale's colors are stored in the color midpoints
 * array. The colors between the midpoints are interpolated.
 * 
 * <p>
 * The scale is visualized as a part of a ring. I call this ring the crime index
 * ring. The effect of a ring is created with the two circles, one for the outer
 * boundary of the ring (the outer circle) and one for the inner boundary of the
 * ring (the inner circle). The circles are drawn with the same center, the
 * crime index ring center ({@linkplain #mInnerCircleX},
 * {@linkplain #mInnerCircleY}), but with the two different radii
 * {@link #mInnerCircleRadius}, {@link #mOuterCircleRadius}.
 * 
 * <p>
 * The outer circle is drawn using the {@code SweepGradient} configured to use
 * the color midpoints array.
 * 
 * <p>
 * The inner circle is drawn atop of the outer circle to create the ring effect.
 * 
 * <p>
 * The scale begins at a certain angle {@linkplain #INDEX_START_ANGLE} within
 * the crime index ring, where the sweep gradient begins with the scale's
 * minumum color, and goes along the crime index ring center spanning an angle
 * of {@linkplain #INDEX_SWIPE_ANGLE} right to the point, where the sweep
 * gradient finishes with the scale's maximum color.
 * 
 * <p>
 * Finally, the index hand is drawn atop of the both whole drawing. The index
 * hand works as a clock hand, showing the crime index value on the crime index
 * scale. It starts at the crime index ring center.
 * 
 * @author Tom
 */
public class CrimeIndexCompactView extends View
{
	private enum DisplayMode
	{
		Normal, NaN
	}

	private DisplayMode			mDisplayMode						= DisplayMode.Normal;

	private static final double	FULL_ANGLE							= 360.0d;

	private String				CRIME_INDEX_NAN_TEXT;

	/**
	 * the crime index value
	 */
	private Float				mIndex;

	/**
	 * the inner circle center x-coordinate
	 * <p>
	 * The inner circle is just a helper circle used to render the crime scale
	 * ring.
	 */
	private float				mInnerCircleX;
	/**
	 * the inner circle center y-coordinate
	 * <p>
	 * The inner circle is just a helper circle used to render the crime scale
	 * ring.
	 */
	private float				mInnerCircleY;
	/**
	 * the inner circle radius
	 * <p>
	 * The inner circle is just a helper circle used to render the crime scale
	 * ring
	 */
	private float				mInnerCircleRadius;

	/**
	 * a ratio between the radius of the inner circle and the radius of the
	 * outer circle
	 */
	private final float			INNER_TO_OUTER_CIRCLE_RADIUS_RATIO	= 0.7f;
	/**
	 * the crime index ring bounding rectangle
	 */
	private RectF				mIndexCircleDrawingRect				= new RectF();

	/**
	 * the paint used to draw the crime index ring
	 */
	private Paint				mCrimeIndexPaint;
	/**
	 * the paint used to draw the inner circle
	 */
	private Paint				mInnerIndexPaint;
	/**
	 * the background color
	 */
	private int					BACKGROUND_COLOR;

	/**
	 * the crime index ring sweep gradient colors
	 */
	private int					INDEX_GRADIENT_COLORS[];

	/**
	 * the start angle of the crime index scale
	 */
	private final float			INDEX_START_ANGLE					= -90 - 120;

	/**
	 * the swipe angle of the crime index scale
	 */
	private final float			INDEX_SWIPE_ANGLE					= 240;

	/**
	 * the outer circle radius
	 */
	private float				mOuterCircleRadius;
	/**
	 * the crime index hand start x-coordinate
	 */
	private float				mIndexHandStartX;
	/**
	 * the crime index hand start y-coordinate
	 */
	private float				mIndexHandStartY;
	/**
	 * the crime index hand end x-coordinate
	 */
	private float				mIndexHandStopX;
	/**
	 * the crime index hand end y-coordinate
	 */
	private float				mIndexHandStopY;
	/**
	 * the paint used to draw the index hand
	 */
	private Paint				mIndexHandPaint;

	private float				mCrimeIndexMinValueText_x;
	private float				mCrimeIndexMinValueText_y;

	private String				mCrimeIndexValue;

	private float				mCrimeIndexValueText_x;

	private float				mCrimeIndexValueText_y;

	private Paint				mCrimeIndexValuePaint;
	private Paint				mCrimeIndexNanTextPaint;

	private String				mCrimeIndexLowerBoundValue;

	private float				mCrimeIndexLowerBoundValueText_x;

	private float				mCrimeIndexLowerBoundValueText_y;

	private float				mCrimeIndexValueTextSize;

	private float				mCrimeIndexNanText_x;

	private float				mCrimeIndexNanText_y;

	private Paint	mCrimeIndexNonePaint;

	/**
	 * Constructs a new {@code CrimeIndexCompactView} using a provided context
	 * and attributes set.
	 * 
	 * @param context
	 *            the context as in {@code View#View(Context, AttributeSet)}
	 * @param attrs
	 *            the attributes set as in in
	 *            {@code View#View(Context, AttributeSet)}
	 * 
	 * @see in {@link View#View(Context, AttributeSet)}
	 */
	public CrimeIndexCompactView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CrimeIndexCompactView, 0, 0);

		try
		{
			mCrimeIndexValueTextSize = a.getDimensionPixelSize(
					R.styleable.CrimeIndexCompactView_android_textSize, -1);
		}
		finally
		{
			a.recycle();
		}

		// initialize the view
		this.init();
	}

	/**
	 * Called from the constructor to initialize the paints, colors etc.
	 */
	private void init()
	{
		// Set the background color. Use the application background color, as
		// defined in resources.
		BACKGROUND_COLOR = this.getContext().getResources()
				.getColor(R.color.app_background_color);

		CRIME_INDEX_NAN_TEXT = this.getContext().getResources()
				.getString(R.string.crime_index_nan_text);

		// initialize the outer circle (sweep gradient, index scale) paint
		mCrimeIndexPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrimeIndexPaint.setStyle(Paint.Style.FILL);

		mCrimeIndexValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrimeIndexValuePaint.setTextAlign(Paint.Align.CENTER);
		mCrimeIndexValuePaint.setStyle(Paint.Style.STROKE);
		mCrimeIndexValuePaint.setTextSize(mCrimeIndexValueTextSize);

		mCrimeIndexNanTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrimeIndexNanTextPaint.setTextAlign(Paint.Align.CENTER);
		mCrimeIndexNanTextPaint.setStyle(Paint.Style.STROKE);
		mCrimeIndexNanTextPaint.setTextSize(mCrimeIndexValueTextSize);

		// initialize the inner circle paint
		mInnerIndexPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mInnerIndexPaint.setStyle(Paint.Style.FILL);
		mInnerIndexPaint.setColor(BACKGROUND_COLOR);

		// initialize the index hand paint
		mIndexHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mIndexHandPaint.setStyle(Paint.Style.STROKE);
		mIndexHandPaint.setStrokeWidth(this.getResources().getDimensionPixelOffset(R.dimen.index_line_width));
		mIndexHandPaint.setStrokeCap(Paint.Cap.ROUND);

		// set the background color for this view
		// this.setBackgroundColor(BACKGROUND_COLOR);

		// initialize the sweep gradient colors
		INDEX_GRADIENT_COLORS = new int[] { Color.GREEN, Color.YELLOW,
				Color.rgb(255, 140, 0), Color.RED, BACKGROUND_COLOR };

		this.setMinimumWidth(this.getMinimumWidth());
		this.setMinimumHeight(this.getMinimumHeight());
		
		mCrimeIndexNonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrimeIndexNonePaint.setColor(Color.RED);
		mCrimeIndexNonePaint.setStrokeWidth(this.getResources().getDimensionPixelOffset(R.dimen.index_line_width));
		mCrimeIndexNonePaint.setStyle(Paint.Style.STROKE);
		
		this.calculateMinimumDimensions();
	}

	/**
	 * Called to draw the crime index gauge.
	 * <p>
	 * The crime index gauge is drawn using the previously calculated geometry
	 * model. The outer circle with the sweep gradient is drawn. The inner
	 * circle is drawn atop of the outer circle to create a ring effect.
	 * Finally, the index hand is drawn. It starts at the center of the index
	 * ring and ends at the inner boundary of the crime ring.
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		if (mDisplayMode == DisplayMode.Normal)
		{
			// draw the outer circle with the sweep gradient
			canvas.drawArc(mIndexCircleDrawingRect, INDEX_START_ANGLE,
					INDEX_SWIPE_ANGLE, true, mCrimeIndexPaint);

			// draw the inner circle atop of the outer circle to make an effect
			// of a
			// ring
			canvas.drawCircle(mInnerCircleX, mInnerCircleY, mInnerCircleRadius,
					mInnerIndexPaint);

			// draw the index hand
			canvas.drawLine(mIndexHandStartX, mIndexHandStartY,
					mIndexHandStopX, mIndexHandStopY, mIndexHandPaint);

			// draw the value
			canvas.drawText(mCrimeIndexValue, mCrimeIndexValueText_x,
					mCrimeIndexValueText_y, mCrimeIndexValuePaint);
		}
		else
			if (mDisplayMode == DisplayMode.NaN)
			{
				canvas.drawLine(
						this.getPaddingLeft(),
						this.getPaddingTop(),
						this.getWidth() - this.getPaddingRight(),
						this.getHeight() - this.getPaddingBottom(),
						mCrimeIndexNonePaint);
				
				canvas.drawLine(
						this.getPaddingLeft(),
						this.getHeight() - this.getPaddingBottom(),
						this.getWidth() - this.getPaddingRight(),
						this.getPaddingTop(),
						mCrimeIndexNonePaint);
			}
		// draw the value
		// canvas.drawText(mCrimeIndexLowerBoundValue,
		// mCrimeIndexLowerBoundValueText_x, mCrimeIndexLowerBoundValueText_y,
		// mCrimeIndexValuePaint);
	}

	/**
	 * Calculates the geometry model for this view.
	 * <p>
	 * The drawing boundaries are set. The centers of both outer and inner
	 * circles as well as their radii are calculated. The size and position of
	 * the index hand is calculated. The size and the position for both the min
	 * and max crime index value label is calculated. The sweep gradient for the
	 * outer circle is created and set up to be used during the drawing phase.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		// Calculates the drawing boundaries.
		int minX = this.getPaddingLeft();
		int minY = this.getPaddingTop();

		int width = w - this.getPaddingLeft() - this.getPaddingRight();
		int height = h - this.getPaddingTop() - this.getPaddingBottom();

		RectF drawingRect = new RectF(minX, minY, minX + width, minY + height);

		// Calculate the
		float centerX = drawingRect.centerX();
		float centerY = drawingRect.centerY();

		mInnerCircleX = centerX;
		mInnerCircleY = centerY;

		if (mDisplayMode == DisplayMode.Normal)
		{
			mOuterCircleRadius = Math.min(drawingRect.width(),
					drawingRect.height()) / 2;
			mIndexCircleDrawingRect.set(centerX - mOuterCircleRadius, centerY
					- mOuterCircleRadius, centerX + mOuterCircleRadius, centerY
					+ mOuterCircleRadius);

			mInnerCircleRadius = INNER_TO_OUTER_CIRCLE_RADIUS_RATIO
					* mOuterCircleRadius;

			// Create and set up the sweep gradient to color the crime index
			// ring.
			if (mOuterCircleRadius > 0)
			{
				// Calculate the relative positions of colors in the sweep
				// gradient.
				final int numberOfColorsForGradient = INDEX_GRADIENT_COLORS.length;
				final int numberOfColorsUsedInRange = numberOfColorsForGradient - 1;

				final float sweepAngleToFullAngleRatio = INDEX_SWIPE_ANGLE / 360.0f;
				final float sweepAngleRatioForOneColor = sweepAngleToFullAngleRatio
						/ numberOfColorsUsedInRange;

				float positions[] = new float[numberOfColorsForGradient];
				float currentColorPosition = 0.0f;

				for (int i = 0; i < numberOfColorsUsedInRange - 1; ++i)
				{
					positions[i] = currentColorPosition;
					currentColorPosition += sweepAngleRatioForOneColor;
				}

				positions[numberOfColorsUsedInRange - 1] = sweepAngleToFullAngleRatio;
				positions[numberOfColorsForGradient - 1] = 1.0f;

				SweepGradient indexRadialGradient = new SweepGradient(centerX,
						centerY, INDEX_GRADIENT_COLORS, positions);

				Matrix rotate = new Matrix();
				if (rotate.postRotate(INDEX_START_ANGLE, centerX, centerY))
				{
					indexRadialGradient.setLocalMatrix(rotate);
				}

				mCrimeIndexPaint.setShader(indexRadialGradient);

				// Compute the crime index hand size and position.
				this.computeIndexHand();

				// Calculate the crime index value position
				// TODO

				this.calculateCrimeIndexValuePosition();
			}
		}
	}

	private void calculateCrimeIndexValuePosition()
	{
		float scaleWidth = 5.0f;
		float crimeIndexDisplayValue = (float) Math
				.round(10 * (0.0f + scaleWidth * mIndex)) / 10;

		mCrimeIndexValue = String.format("%.1f", crimeIndexDisplayValue);
		// the x-coordinate for the text drawn is the same as that of the inner
		// circle,
		// as the center of the text is aligned with the center of the circle
		mCrimeIndexValueText_x = mInnerCircleX;

		
		double tanBeta = this.getTanBeta();
		double tanBetaSquared = tanBeta * tanBeta;
		float crimeIndexValueRectUpperSideOffset = mOuterCircleRadius
				/ (float) this.getOffsetToOuterRadiusRatio(tanBetaSquared);
		float crimeIndexValueRectHalfSide = (float) (crimeIndexValueRectUpperSideOffset * tanBeta);

		Rect crimeIndexValueTextBounds = new Rect();
		mCrimeIndexPaint.getTextBounds(mCrimeIndexValue, 0,
				mCrimeIndexValue.length(), crimeIndexValueTextBounds);

		mCrimeIndexValueText_y = mInnerCircleY
				+ crimeIndexValueRectUpperSideOffset
				+ crimeIndexValueRectHalfSide
				- crimeIndexValueTextBounds.height() / 2;
	}

	private double getOffsetToOuterRadiusRatio(double tanBeta)
	{
		double tanBetaSquared = tanBeta * tanBeta;
		return Math.sqrt(4 * tanBetaSquared + 4 * tanBeta + 2);
	}
	
	private double getBeta()
	{
		return (FULL_ANGLE - INDEX_SWIPE_ANGLE) / 2;
	}
	private double getTanBeta()
	{
		return Math.tan(this.getBeta());
	}
	
	/**
	 * Calculates the size and position of the crime index hand.
	 * <p>
	 * Basic trigonometry is used.
	 */
	private void computeIndexHand()
	{
		// The crime index hand starts at the crime index center.
		mIndexHandStartX = mInnerCircleX;
		mIndexHandStartY = mInnerCircleY;
		// It ends on the half way in between the inner and outer crime index
		// ring boundary.
		float mIndexHandRadius = (mOuterCircleRadius + mInnerCircleRadius) / 2.0f;
		// The angle of the crime index hand is calculated relatively to the
		// crime index
		// scale min value angle.
		float mIndexHandAngle = INDEX_START_ANGLE + mIndex * INDEX_SWIPE_ANGLE;
		// Then some additional calculations are performed to calculate the
		// endpoint of the
		// crime index hand.
		float mIndexHandStandardAngle = -mIndexHandAngle + 360;
		double mIndexHandAngleRad = (mIndexHandStandardAngle / 360.0)
				* (2 * Math.PI);
		mIndexHandStopX = mIndexHandStartX
				+ (float) (mIndexHandRadius * Math.cos(mIndexHandAngleRad));
		mIndexHandStopY = mIndexHandStartY
				- (float) (mIndexHandRadius * Math.sin(mIndexHandAngleRad));
	}

	/**
	 * Measures the view's dimensions.
	 * <p>
	 * The smaller of the available dimension values is used for both the width
	 * and height.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int minW = this.getPaddingLeft() + this.getSuggestedMinimumWidth()
				+ this.getPaddingRight();
		int minH = this.getPaddingTop() + this.getSuggestedMinimumHeight()
				+ this.getPaddingBottom();

		int measuredWidth = MeasureSpec.getSize(View.resolveSizeAndState(minW,
				widthMeasureSpec, 1));
		int measuredHeight = MeasureSpec.getSize(View.resolveSizeAndState(minH,
				heightMeasureSpec, 1));

		int lowerDimension = Math.min(measuredWidth, measuredHeight);
		this.setMeasuredDimension(lowerDimension, lowerDimension);
	}

	/**
	 * Gets the current crime index value.
	 * 
	 * @return the current crime index value
	 */
	public final float getIndex()
	{
		return mIndex;
	}

	/**
	 * Sets the current crime index value.
	 * <p>
	 * This invalidates the view, which may cause it to be redrawn.
	 * 
	 * @param index
	 *            the new crime index value to be set for this view
	 */
	public final void setIndex(float index)
	{
		mIndex = index;

		// detect NaN
		if (Float.isNaN(index))
		{
			mDisplayMode = DisplayMode.NaN;
		}
		else
		{
			mDisplayMode = DisplayMode.Normal;

			// recalculate the crime index hand size and position
			this.computeIndexHand();
			// recalculate the crime index value position
			this.calculateCrimeIndexValuePosition();
		}
		
		this.calculateMinimumDimensions();

		// invalidate this view
		this.invalidate();
	}

	@Override
	public int getMinimumWidth()
	{
		if(mIndex == null || mIndex.isNaN())
			return super.getMinimumWidth();
		else
		{
			return this.getPaddingLeft() + Math.round(this.getMinimumOuterDiameter()) + this.getPaddingRight();
		}
	}

	@Override
	public int getMinimumHeight()
	{
		if(mIndex == null || mIndex.isNaN())
			return super.getMinimumHeight();
		else
		{
			return this.getPaddingTop() + Math.round(this.getMinimumOuterDiameter()) + this.getPaddingBottom();
		}
	}
	
	private float getMinimumOuterDiameter()
	{
		Rect crimeIndexValueTextBounds = new Rect();
		mCrimeIndexPaint.getTextBounds(mCrimeIndexValue, 0, mCrimeIndexValue.length(), crimeIndexValueTextBounds);
		
		double maximumTextBoundsDimensionValue = Math.max(crimeIndexValueTextBounds.width(), crimeIndexValueTextBounds.height());
		double tanBeta = this.getTanBeta();
		
		return (float) ((2 * maximumTextBoundsDimensionValue * this.getOffsetToOuterRadiusRatio(tanBeta)) / tanBeta);
	}
	
	private void calculateMinimumDimensions()
	{
		this.setMinimumWidth(this.getMinimumWidth());
		this.setMinimumHeight(this.getMinimumHeight());
	}
}
