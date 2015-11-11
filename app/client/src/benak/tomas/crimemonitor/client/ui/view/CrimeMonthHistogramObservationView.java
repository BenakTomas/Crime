package benak.tomas.crimemonitor.client.ui.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.client.ui.fragment.CrimeMonthHistogramFragment;
import benak.tomas.crimemonitor.library.CrimeMonthHistogramObservation;
import benak.tomas.crimemonitor.library.utils.CalendarUtils;

/**
 * A class used to display a single observation in a crime count monthly histogram.
 * <p>
 * The observation is displayed using a colored bar, a text label with the month description
 * and a text label with the observed value for that month.
 * The height of the bar is in proportion with the observed value.
 * See the {@link CrimeMonthHistogramFragment} documentation for the details about how the bar height is
 * calculated and how the color is assigned to the bar. 
 * 
 * @author Tom
 *
 *@see CrimeMonthHistogramObservation
 *@see CrimeMonthHistogramFragment
 */
public class CrimeMonthHistogramObservationView extends View
{
	/**
	 * the textual representation of the month associated with the observation
	 * <p>This is displayed in month label.
	 */
	private final String	mMonthLabelText;
	/**
	 * the month label x-coordinate
	 */
	private float	mMonthLabelText_x;
	/**
	 * the month label y-coordinate
	 */
	private float	mMonthLabelText_y;
	/**
	 * the paint used to draw the month label with
	 */
	private Paint	mMonthLabelTextPaint;
	
	/**
	 * the rectangle where the bar is drawn
	 */
	private RectF	mBarRect = new RectF();
	/**
	 * the paint used to draw the bar
	 */
	private Paint	mBarRectPaint;
	
	/**
	 * the textual representation of the observed value
	 */
	private final String	mCrimeCountText;
	/**
	 * the observed value label x-coordinate
	 */
	private float	mCrimeCountText_x;
	/**
	 * the observed value label y-coordinate
	 */
	private float	mCrimeCountText_y;
	/**
	 * the paint used to draw the observed value label with
	 */
	private Paint	mCrimeCountTextPaint;
	
	/**
	 * the margin between the month label and the bar
	 */
	private int	mIntervalToBarMargin = 10;			// 10 pixels between the legend and the bar
	/**
	 * the relative height of the bar with respect to the total view height
	 */
	private final float	mBarWidthPercent = 0.9f;	// the bar occupies 90% of the total drawing width
	/**
	 * the margin between the bar and the observed value label
	 */
	private int	mBarToCrimeCountMargin = 10;				// 10 pixels between the bar and the crime count
	/**
	 * the minimum bar width
	 */
	private final float	mMinBarWidth = 30;			// 30 pixels
	/**
	 * the minimum bar height
	 */
	private float	mMinBarHeight = 10;				// 10 pixels
	
	/**
	 * the month label text size
	 */
	private float	mMonthLabelTextSize;
	/**
	 * the month label text color
	 */
	private final int	mMonthLabelTextColor = Color.BLACK;
	/**
	 * the observed value label text size
	 */
	private float	mCrimeCountTextSize;
	/**
	 * the observed value label text color
	 */
	private final int	mCrimeCountTextColor = Color.BLACK;
	/**
	 * the bar color
	 */
	private int	mBarRectPaintColor;
	
	/**
	 * the factor used to scale the bar's height with respect to the other view's bar heights
	 */
	private final float mBarHeightRelativeHeight;
	
	/**
	 * the background color
	 */
	private int BACKGROUND_COLOR;

	/**
	 * Constructs a new {@code CrimeMonthHistogramObservationView} using a provided context,
	 * an observation, the maximum observed value and a bar color. 
	 *  
	 * @param context the context for this view
	 * @param observation the observation to display
	 * @param topCrimeCount the maximum observed value across the whole crime count monthly histogram
	 * @param barRectPaintColor the bar color
	 */
	@SuppressLint("SimpleDateFormat")
	public CrimeMonthHistogramObservationView(Context context,
			CrimeMonthHistogramObservation observation, int topCrimeCount, int barRectPaintColor)
	{
		super(context);
		
		if(observation == null)
			throw new NullPointerException("observation");
		
		// calculate the relative height of the bar as a ratio between the observed value
		// and the maximum observed value across all of the observations of th histogram
		mBarHeightRelativeHeight = (float)observation.getCrimeCount() / topCrimeCount;
		
		// assign the final fields: interval and value legends
		Calendar cal = Calendar.getInstance();
		CalendarUtils.setYearAndMonthForJavaCalendar(observation.getYear(), observation.getMonth(), cal);
		mMonthLabelText = String.format("%s %s",
				new SimpleDateFormat("MMM").format(cal.getTime()),
				new SimpleDateFormat("yyyy").format(cal.getTime()));
		mCrimeCountText = String.format("%d", observation.getCrimeCount());
		
		mBarRectPaintColor = barRectPaintColor;
		
		// initialize for painting
		this.init();
	}
	
	/**
	 * Initializes the view to be prepared to measure and draw itself.
	 */
	private void init()
	{
		float contentTextSize = this.getResources().getDimensionPixelSize(R.dimen.content_text_size);
		mMonthLabelTextSize = contentTextSize;
		mCrimeCountTextSize = contentTextSize;
		
		// initialize the month label paint
		mMonthLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mMonthLabelTextPaint.setTextSize(mMonthLabelTextSize);
		mMonthLabelTextPaint.setColor(mMonthLabelTextColor);
		
		// initialize the observed value label paint
		mCrimeCountTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCrimeCountTextPaint.setTextSize(mCrimeCountTextSize);
		mCrimeCountTextPaint.setColor(mCrimeCountTextColor);
		
		// initialize the bar paint
		mBarRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBarRectPaint.setColor(mBarRectPaintColor);
		mBarRectPaint.setStyle(Paint.Style.FILL);
		
		// set the suggested minimum width and height
		this.setMinimumWidth(this.getMinimumWidth());
		this.setMinimumHeight(this.getMinimumHeight());
		
		// load the background color from the application resources
		//BACKGROUND_COLOR = this.getContext().getResources().getColor(R.color.app_background_color);
		// set the loaded background color for this view
		//this.setBackgroundColor(BACKGROUND_COLOR);
	}
	
	/**
	 * Calculates the minimum width for this view.
	 * <p>
	 * The minimum width calculation takes the label and bar minimum widths into account.
	 */
	@Override
	public int getMinimumWidth()
	{
		// hranice pro text s popisem casoveho intervalu
		Rect mMonthLabelText_Bounds = new Rect();
		mMonthLabelTextPaint.getTextBounds(mMonthLabelText, 0, mMonthLabelText.length(), mMonthLabelText_Bounds);
		
		// hranice pro text se yobrayovanou hodnotou
		Rect mCrimeCountText_Bounds = new Rect();
		mCrimeCountTextPaint.getTextBounds(mCrimeCountText, 0, mCrimeCountText.length(), mCrimeCountText_Bounds);
		
		// sirka sloupecku se odviji od minimalni sirky a faktu, ze sloupecek
		// zabira z dostupne sirky pouze dane procento
		float minBarWidth = mMinBarWidth;
		float minBarWidthPlusSurrounding = Math.round(minBarWidth / mBarWidthPercent);
		
		return
				Math.max(
						(int)minBarWidthPlusSurrounding,
						Math.max(mMonthLabelText_Bounds.width(), mCrimeCountText_Bounds.width()));
	}
	
	/**
	 * Calculates the minimum height for this view.
	 * <p>
	 * The minimum height calculation takes the label and bar minimum heights into account,
	 * along with additional margins between them.
	 */
	@Override
	public int getMinimumHeight()
	{
		// hranice pro text s popisem casoveho intervalu
		Rect mMonthLabelText_Bounds = new Rect();
		mMonthLabelTextPaint.getTextBounds(mMonthLabelText, 0, mMonthLabelText.length(), mMonthLabelText_Bounds);
		
		// hranice pro text se yobrayovanou hodnotou
		Rect mCrimeCountText_Bounds = new Rect();
		mCrimeCountTextPaint.getTextBounds(mCrimeCountText, 0, mCrimeCountText.length(), mCrimeCountText_Bounds);
		
		return
				this.getPaddingBottom()
				+ mMonthLabelText_Bounds.height()
				+ mIntervalToBarMargin
				+ (int)mMinBarHeight
				+ mBarToCrimeCountMargin
				+ mCrimeCountText_Bounds.height()
				+ this.getPaddingTop();
	}
	
	/**
	 * Measures the dimensions of this view.
	 * <p>
	 * The view proposes to be as big as its minimum dimensions requirements are.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = View.resolveSize(this.getMinimumWidth(), widthMeasureSpec);
		int height = View.resolveSize(this.getMinimumHeight(), heightMeasureSpec);
		
		this.setMeasuredDimension(width, height);
	}
	
	/**
	 * Calculates the drawing boundaries, sizes and positions of the view elements to be drawn.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		// calculate the drawing boundaries
		int minX = this.getPaddingLeft();
		int maxX = w - this.getPaddingRight();
		int minY = this.getPaddingTop();
		int maxY = h - this.getPaddingBottom();
		
		int width = maxX - minX;
		
		// zmer popisek intervalu (mesic a rok)
		Rect mMonthLabelText_Bounds = new Rect();
		mMonthLabelTextPaint.getTextBounds(mMonthLabelText, 0, mMonthLabelText.length(), mMonthLabelText_Bounds);
		
		// stred kreslici plochy
		float drawingCenterX = minX + (float)width / 2;
		
		// popisek vycentruji na stred a dam dolu
		mMonthLabelText_x = drawingCenterX - (float)mMonthLabelText_Bounds.width() / 2;
		mMonthLabelText_y = maxY;
		
		// vykreslovana hodnota: zmer jeji velikost
		Rect mCrimeCountText_Bounds = new Rect();
		mCrimeCountTextPaint.getTextBounds(mCrimeCountText, 0, mCrimeCountText.length(), mCrimeCountText_Bounds);
		
		mCrimeCountText_x = drawingCenterX - (float)mCrimeCountText_Bounds.width() / 2;
		final float crimeCountText_MinY = minY + mCrimeCountText_Bounds.height();
		
		// mezera mezi popiskem a sloupeckem
		int intervalToBarMargin = mIntervalToBarMargin;
		
		// mezera mezi sloupeckem a hodnotou sloupecku
		int barToCrimeCountMargin = mBarToCrimeCountMargin;
		
		// sloupecek: zabere pomernou cast dostupne sirky
		float barWidth = width * mBarWidthPercent;
		// vycentruje se kolem stredu
		float barLeft = drawingCenterX - barWidth / 2;
		float barRight = barLeft + barWidth;
		float barBottom = mMonthLabelText_y - mMonthLabelText_Bounds.height() - intervalToBarMargin;
		// horni okraj sloupecku zavisi na velikosti vykreslovane hodnoty
		final float barMaximumTop = crimeCountText_MinY + barToCrimeCountMargin;
		final float barHeight = (barBottom - barMaximumTop) * mBarHeightRelativeHeight;
		float barTop = barBottom - barHeight;
		
		mCrimeCountText_y = barTop - barToCrimeCountMargin;
		
		mBarRect.set(barLeft, barTop, barRight, barBottom);
	}
	
	/**
	 * Draws the content of this view.
	 * <p>
	 * Uses the previously calculated sizes and positions of the content elements to be drawn.
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		// nakresli text: predtim si spocti jeho bounds kvuli pozicovani
		canvas.drawText(mMonthLabelText, mMonthLabelText_x, mMonthLabelText_y, mMonthLabelTextPaint);
		
		// nakresli sloupecek
		canvas.drawRect(mBarRect, mBarRectPaint);
		
		// nakresli hodnotu (pocet trestnych cinu)
		canvas.drawText(mCrimeCountText, mCrimeCountText_x, mCrimeCountText_y, mCrimeCountTextPaint);
	}
}
