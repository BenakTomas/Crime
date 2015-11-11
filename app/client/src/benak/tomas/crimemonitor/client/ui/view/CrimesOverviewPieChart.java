package benak.tomas.crimemonitor.client.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.activity.CrimeToplistItem;


/**
 * A class used to draw a pie chart of crimes.
 * <p>
 * The pie chart is rendered as a series of pie slices.
 * Each pie slice is drawn using the information from a particular crime toplist item.
 * 
 * @author Tom
 *
 * @see CrimeToplistItem
 */
public class CrimesOverviewPieChart extends View
{
	/**
	 * the crime items to render as pie slices
	 */
	private CrimeToplistItem[] mCrimeItems;
	
	/**
	 * the rectangle boundary of a drawn pie chart
	 */
	private RectF mPieChartRect;
	/**
	 * the angle at where to start drawing the first pie slice
	 */
	private final float START_ANGLE = 0;
	
	/**
	 * the paint used to draw a pie slice
	 */
	private Paint mCrimeItemPaint;
	
	/**
	 * the background color of this view
	 */
	private int BACKGROUND_COLOR;

	/**
	 * Constructs a new {@code CrimesOverviewPieChart} using a provided
	 * context and attributes for this view.
	 * 
	 * @param context the context as in {@link View#View(Context, AttributeSet)
	 * @param attrs the attribute set as in {@link View#View(Context, AttributeSet)
	 */
	public CrimesOverviewPieChart(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// initialize the view for painting
		this.init();
	}

	/**
	 * Initializes the view to be able to measure and draw itself.
	 */
	private void init()
	{
		// initialize the paint used to draw pie slices with
		mCrimeItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrimeItemPaint.setStyle(Paint.Style.FILL);
		
		// initialize the boundary rectangle of a pie chart
		mPieChartRect = new RectF();
		
		// load the background color from the resources
		BACKGROUND_COLOR = this.getContext().getResources().getColor(R.color.app_background_color);
		// set the loaded background color as a background color for this view
		this.setBackgroundColor(BACKGROUND_COLOR);
	}
	
	/**
	 * Sets the crime toplist items collection to be rendered in the pie chart.
	 * <p>
	 * The collection is stored and the view is invalidated and possibly redrawn.
	 * 
	 * @param crimeItems the collection of crime toplist items to render the pie chart for
	 */
	public final void setCrimeItems(CrimeToplistItem[] crimeItems)
	{
		if(crimeItems == null)
			throw new IllegalArgumentException("crimeItems cannot be null");
		
		this.mCrimeItems = crimeItems;
		
		// prekresli, protoze se zmenila data
		this.invalidate();
	}
	
	/**
	 * Draws the pie chart.
	 * <p>
	 * For each crime toplist item a pie slice is drawn.
	 * The size of each slice is in proportion to the crime count of the crime toplist item.
	 * The color of each slice is taken from the crime toplist item.
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if(mCrimeItems == null)
			return;
		
		// spocitej soucet hodnot
		int valueSum = 0;
		for(int i = 0; i < mCrimeItems.length; valueSum += mCrimeItems[i++].getValue());
		
		// projdi vsechna
		float currentItemStartAngle = START_ANGLE;
		float restOfFullAngle = 360.0f;
		int restOfValueSum = valueSum;
		
		for(int i = 0; i < (mCrimeItems.length - 1); ++i)
		{
			CrimeToplistItem item = mCrimeItems[i];
			int itemValue = item.getValue();
			// Calculate the ratio of the current item value to the sum of values of items, that have not been
			// assigned an angle yet. Using the calculated value, take an appropriate amount of an angle, that
			// have not been assigned to items yet.
			float currentItemSweepAngle = (float)Math.floor(restOfFullAngle * ((float)itemValue / restOfValueSum));
			// draws arc: set the appropriate pie slice color first
			mCrimeItemPaint.setColor(item.getColor());
			canvas.drawArc(mPieChartRect, currentItemStartAngle, currentItemSweepAngle, true, mCrimeItemPaint);
			// advance start angle
			currentItemStartAngle += currentItemSweepAngle;
			// subtract assigned angle from the rest of a full angle
			restOfFullAngle -= currentItemSweepAngle;
			// subtract the value of a currently drawn item from the value sum of the rest of the not drawn items.
			restOfValueSum -= itemValue;
		}
		
		// deal with the last item
		mCrimeItemPaint.setColor(mCrimeItems[mCrimeItems.length - 1].getColor());
		canvas.drawArc(mPieChartRect, currentItemStartAngle, restOfFullAngle, true, mCrimeItemPaint);
	}
	
	/**
	 * Calculates the bounding rectangle of the pie chart.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		// vypocti obdelnik, kam se bude kreslit
		mPieChartRect.set(this.getPaddingLeft(), this.getPaddingTop(), w - this.getPaddingRight(), h - this.getPaddingBottom());
	}
	
	/**
	 * Measures the dimensions of this view.
	 * <p>
	 *  The smaller of the dimension values is used for both the width and height.  
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// pozaduj vzdy stejne rozmery pro vysku a sirku
		int minW = this.getPaddingLeft() + this.getSuggestedMinimumWidth() + this.getPaddingRight();
		int minH = this.getPaddingTop() + this.getSuggestedMinimumHeight() + this.getPaddingBottom();
		
		int measuredWidth = MeasureSpec.getSize(View.resolveSize(minW, widthMeasureSpec));
		int measuredHeight = MeasureSpec.getSize(View.resolveSize(minH, heightMeasureSpec));
		
		int lowerDimension = Math.min(measuredWidth, measuredHeight);
		this.setMeasuredDimension(lowerDimension, lowerDimension);
	}
}



















