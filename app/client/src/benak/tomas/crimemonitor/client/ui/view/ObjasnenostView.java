package benak.tomas.crimemonitor.client.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import benak.tomas.crimemonitor.R;

/**
 * A class visualizing the numbers of solved and unsolved pie chart.
 * <p>
 * A donut chart is used.
 * There are two pie slices.
 * The first pie slice visualizes the crimes solved, the other visualizes the unsolved crimes.
 * The sizes of the two slices are proportionate.
 * In the pie hole, the crime solved ratio value is displayed.
 * 
 * @author Tom
 */
public class ObjasnenostView extends View
{
	/**
	 * the crime solved ratio
	 */
	private Float mObjasneno;
	
	/**
	 * the paint used to draw the crime solved ratio value 
	 */
	private Paint mObjasnenostPercentTextPaint;
	/**
	 * the paint used to draw the "solved" pie slice 
	 */
	private Paint mPieChartPaint_Objasneno;
	/**
	 * the paint used to draw the "unsolved" pie slice 
	 */
	private Paint mPieChartPaint_Neobjasneno;
	/**
	 * the paint used to draw the contents of the inner circle, which creates an effect of a donut chart 
	 */
	private Paint mPieHolePaint;
	/**
	 * the text color of a drawn crime solved ratio value
	 */
	private final int mObjasnenostPercentTextColor = Color.BLACK;
	/**
	 * the text size of a drawn crime solved ratio value
	 */
	private int mObjasnenostPercentTextSize;
	
	/**
	 * the pie chart center x-coordinate
	 */
	private float mCenterX;
	/**
	 * the pie chart center y-coordinate
	 */
	private float mCenterY;
	/**
	 * the pie hole (the content of the inner circle) radius
	 */
	private float mPieHoleRadius;
	/**
	 * the pie chart outer boundary (outer circle) radius
	 */
	private float mPieChartRadius;
	/**
	 * the ratio between the inner circle radius and the outer circle radius
	 */
	private final float mPieHoleRadiusToPieChartRadiusRatio = 0.6f;
	
	/**
	 * the background color of this view
	 */
	private int BACKGROUND_COLOR;

	/**
	 * the rectangle boundary of the pie chart
	 */
	private RectF	mPieChartDrawingRect;

	private int	mSolvedColor;

	private int	mUnsolvedColor;

	private float	mLabel_x;

	private float	mLabel_y;

	private String	mObjasnenoValueText;

	private float	mObjasnenoSweepAngle;

	private float	mNeobjasnenoSweepAngle;
	
	/**
	 * Constructs a new {@code ObjasnenostView} using a provided context and an attribute set.
	 * 
	 * @param context the context as in {@code View#View(Context, AttributeSet)}
	 * @param attrs the attribute set as in {@code View#View(Context, AttributeSet)}
	 * 
	 * @see View#View(Context, AttributeSet)
	 */
	public ObjasnenostView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.ObjasnenostView,
		        0, 0);
		
		   try {
			   mObjasnenostPercentTextSize = a.getDimensionPixelSize(R.styleable.ObjasnenostView_android_textSize, 5);
			   mSolvedColor = a.getColor(R.styleable.ObjasnenostView_solved_color, Color.GREEN);
			   mUnsolvedColor = a.getColor(R.styleable.ObjasnenostView_unsolved_color, Color.RED);
		   } finally {
		       a.recycle();
		   }
		
		// initialize the view for painting
		this.init();
	}

	/**
	 * Initialize the view to be able to measure and draw itself.
	 */
	private void init()
	{
		// load the background color from the application resources
		BACKGROUND_COLOR = this.getContext().getResources().getColor(R.color.app_background_color);
		
		// initialize the crime solved ratio value paint
		mObjasnenostPercentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mObjasnenostPercentTextPaint.setTextSize(mObjasnenostPercentTextSize);
		mObjasnenostPercentTextPaint.setTextAlign(Paint.Align.CENTER);
		
		// initialize the "solved" slice paint
		mPieChartPaint_Objasneno = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPieChartPaint_Objasneno.setStyle(Paint.Style.FILL);
		mPieChartPaint_Objasneno.setTextSize(mObjasnenostPercentTextSize);
		mPieChartPaint_Objasneno.setColor(mSolvedColor);
		
		// initialize the "unsolved" slice paint
		mPieChartPaint_Neobjasneno = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPieChartPaint_Neobjasneno.setStyle(Paint.Style.FILL);
		mPieChartPaint_Neobjasneno.setTextSize(mObjasnenostPercentTextSize);
		mPieChartPaint_Neobjasneno.setColor(mUnsolvedColor);
		
		// initialize the pie hole paint
		mPieHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPieHolePaint.setStyle(Paint.Style.FILL);
		mPieHolePaint.setColor(BACKGROUND_COLOR);
		
		// set the background color for this view
		//this.setBackgroundColor(BACKGROUND_COLOR);
		this.calculateMinimumDimensions();
	}

	private void calculateMinimumDimensions()
	{
		this.setMinimumWidth(this.getMinimumWidth());
		this.setMinimumHeight(this.getMinimumHeight());
	}

	/**
	 * Calculates the pie chart geometry.
	 * <p>
	 * The bounding rectangle, the pie chart center and the pie chart circle boundaries are calculated. 
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		// calculate the bounding rectangle for the pie chart
		mPieChartDrawingRect = new RectF(this.getPaddingLeft(), this.getPaddingTop(), w - this.getPaddingRight(), h - this.getPaddingBottom());
		
		// calculate the center of the pie chart
		mCenterX = mPieChartDrawingRect.centerX();
		mCenterY = mPieChartDrawingRect.centerY();
		// calculate the radius of a pie chart (the outer circle boundary)
		mPieChartRadius = (float)Math.floor(Math.min(mPieChartDrawingRect.width(), mPieChartDrawingRect.height()) / 2);
		// calculate the radius of a pie hole (the inner circle boundary)
		mPieHoleRadius = mPieHoleRadiusToPieChartRadiusRatio * mPieChartRadius;
		
		Rect labelTextRect = new Rect();
		mObjasnenostPercentTextPaint.getTextBounds(mObjasnenoValueText, 0, mObjasnenoValueText.length(), labelTextRect);
		mLabel_x = mCenterX;
		mLabel_y = mCenterY + labelTextRect.height() / 2;
	}
	
	/**
	 * Measures the dimensions of this view.
	 * <p>
	 *  The smaller of the dimension values is used for both the width and height.  
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int minW = this.getPaddingLeft() + this.getSuggestedMinimumWidth() + this.getPaddingRight();
		int minH = this.getPaddingTop() + this.getSuggestedMinimumHeight() + this.getPaddingBottom();
		
		int measuredWidth = MeasureSpec.getSize(View.resolveSizeAndState(minW, widthMeasureSpec, 1));
		int measuredHeight = MeasureSpec.getSize(View.resolveSizeAndState(minH, heightMeasureSpec, 1));
		
		int lowerDimension = Math.min(measuredWidth, measuredHeight);
		this.setMeasuredDimension(lowerDimension, lowerDimension);
	}
	
	/**
	 * Draws the pie chart.
	 * <p>
	 * Both the "solved" and "unsolved" pie slices are drawn, the pie hole is drawn atop of them,
	 * and finally the crime solved ratio value is drawn atop of everything.
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		// Draw the "solved" pie slice.
		canvas.drawArc(mPieChartDrawingRect, -90, mObjasnenoSweepAngle, true, mPieChartPaint_Objasneno);
		// Draw the "unsolved" pie slice.
		canvas.drawArc(mPieChartDrawingRect, -90 + mObjasnenoSweepAngle, mNeobjasnenoSweepAngle, true, mPieChartPaint_Neobjasneno);
		
		// Draw the pie hole.
		canvas.drawCircle(mCenterX, mCenterY, mPieHoleRadius, mPieHolePaint);
		
		// Draw the crime solved ratio value text.
		canvas.drawText(mObjasnenoValueText, mLabel_x, mLabel_y, mObjasnenostPercentTextPaint);
	}
	
	/**
	 * Sets the crime solved ratio value.
	 * <p>
	 * This invalidates this view, which is possibly followed by the view redrawal.
	 * 
	 * @param objasneno the crime solved ratio value to be set
	 */
	public void setObjasneno(float objasneno)
	{
		// set the crime solved ratio value
		this.mObjasneno = objasneno;
		
		// Prepare the crime solved value to be displayed by rounding it to the whole percents.
		final float objasnenoPercentRounded = (float)Math.round(mObjasneno * 100);
		final float objasnenoRounded = objasnenoPercentRounded / 100;
		// Calculate the sizes of angles used for the "solved" pie slice and for the "unsolved" pie slice
		mObjasnenoSweepAngle = Math.round(objasnenoRounded * 360);
		mNeobjasnenoSweepAngle = 360 - mObjasnenoSweepAngle;
		
		// create the label for a solved ratio value
		mObjasnenoValueText = String.format("%.0f%%", objasnenoPercentRounded);
		
		this.calculateMinimumDimensions();
		
		// request invalidation of this view, possibly followed by the view redrawal
		this.invalidate();
	}
	
	private float getMinimumInnerDiameter()
	{
		Rect objasnenoValueTextBounds = new Rect();
		mObjasnenostPercentTextPaint.getTextBounds(mObjasnenoValueText, 0, mObjasnenoValueText.length(), objasnenoValueTextBounds);
		float diameterNeeded = (float) (this.getHypotenuse(objasnenoValueTextBounds.width(), objasnenoValueTextBounds.height()));
		return diameterNeeded;
	}
	
	private float getMinimumOuterDiameter(float innerRadius)
	{
		return innerRadius / mPieHoleRadiusToPieChartRadiusRatio;
	}
	
	private double getHypotenuse(double a, double b)
	{
		return Math.sqrt(a * a + b * b);
	}
	
	@Override
	public int getMinimumWidth()
	{
		if(mObjasneno == null)
			return super.getMinimumWidth();
		else
			return this.getPaddingLeft() + Math.round(this.getMinimumOuterDiameter(this.getMinimumInnerDiameter())) + this.getPaddingRight();
	}
	
	@Override
	public int getMinimumHeight()
	{
		if(mObjasneno == null)
			return super.getMinimumHeight();
		else
			return this.getPaddingTop() + Math.round(this.getMinimumOuterDiameter(this.getMinimumInnerDiameter())) + this.getPaddingBottom();
	}
}
