package benak.tomas.crimemonitor.client.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import benak.tomas.crimemonitor.R;
import benak.tomas.crimemonitor.library.CrimeMeasureTrend;


public class TrendView extends View
{
	private CrimeMeasureTrend mTrend = CrimeMeasureTrend.Stable;
	
	private Paint mTrendPaint;

	private int	mTrendColor;
	private float mDrawingCenter_x;
	private float mDrawingCenter_y;

	private boolean	mDoRotate;

	private float	mRotateAngle;

	private RectF	mDrawingRectangle;
	
	public TrendView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.TrendView,
		        0, 0);
		
		   try {
//			   mObjasnenostPercentTextSize = a.getDimensionPixelSize(R.styleable.ObjasnenostView_android_textSize, 5);
			   mTrendColor = a.getColor(R.styleable.TrendView_trend_color, Color.BLACK);
//			   mUnsolvedColor = a.getColor(R.styleable.ObjasnenostView_unsolved_color, Color.RED);
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
		mTrendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTrendPaint.setStyle(Paint.Style.FILL);
		mTrendPaint.setColor(mTrendColor);
		mTrendPaint.setStrokeWidth(this.getResources().getDimensionPixelOffset(R.dimen.trend_line_width));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		this.prepareDrawing();
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
		
		if(mTrend == null)
			return;
		
		if(mDoRotate)
		{
			canvas.save();
			canvas.rotate(mRotateAngle, mDrawingCenter_x, mDrawingCenter_y);
		}
		
		this.drawArrowInARectangle(canvas, mDrawingRectangle);
		
		canvas.restore();
	}
	
	private void prepareDrawing()
	{
		int drawingRectangleWidthWithPadding = 0;
		int drawingRectangleHeightWithPadding = 0;
		int leftPadding = 0, rightPadding = 0, topPadding = 0, bottomPadding = 0;
		
		switch (mTrend)
		{
			case Down:
			
				drawingRectangleWidthWithPadding = this.getHeight();
				drawingRectangleHeightWithPadding = this.getWidth();
				leftPadding = this.getPaddingTop();
				rightPadding = this.getPaddingBottom();
				topPadding = this.getPaddingRight();
				bottomPadding = this.getPaddingLeft();
			
				mRotateAngle = 90;
				
				mDoRotate = true;
				break;
			case Up:
				drawingRectangleWidthWithPadding = this.getHeight();
				drawingRectangleHeightWithPadding = this.getWidth();
				leftPadding = this.getPaddingBottom();
				rightPadding = this.getPaddingTop();
				topPadding = this.getPaddingLeft();
				bottomPadding = this.getPaddingRight();
			
				mRotateAngle = -90;
				
				mDoRotate = true;
				break;
			case Stable:
				drawingRectangleWidthWithPadding = this.getWidth();
				drawingRectangleHeightWithPadding = this.getHeight();
				leftPadding = this.getPaddingLeft();
				rightPadding = this.getPaddingRight();
				topPadding = this.getPaddingTop();
				bottomPadding = this.getPaddingBottom();
				
				mDoRotate = false;
				break;
		}
		
		int minX = leftPadding;
		int maxX = drawingRectangleWidthWithPadding - rightPadding;
		int minY = topPadding;
		int maxY = drawingRectangleHeightWithPadding - bottomPadding;
		
		mDrawingRectangle = new RectF(minX, minY, maxX, maxY);
		mDrawingCenter_x = mDrawingRectangle.centerX();
		mDrawingCenter_y = mDrawingRectangle.centerY();
	}

	private void drawArrowInARectangle(Canvas canvas, RectF drawingRect)
	{
		float centerX = drawingRect.centerX();
		float centerY = drawingRect.centerY();
		float minX = drawingRect.left;
		float minY = drawingRect.top;
		float maxX = drawingRect.right;
		float maxY = drawingRect.bottom;
		
		PointF lineStart = new PointF(minX, centerY);
		PointF lineEnd = new PointF(maxX, centerY);
		
		PointF leftPartStart = new PointF(centerX, minY);
		PointF leftPartEnd = new PointF(maxX, centerY);
		PointF rightPartStart = new PointF(centerX, maxY);
		PointF rightPartEnd = new PointF(maxX, centerY);
				
		canvas.drawLine(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y, mTrendPaint);
		canvas.drawLine(leftPartStart.x, leftPartStart.y, leftPartEnd.x, leftPartEnd.y, mTrendPaint);
		canvas.drawLine(rightPartStart.x, rightPartStart.y, rightPartEnd.x, rightPartEnd.y, mTrendPaint);
	}
	
	public void setTrend(CrimeMeasureTrend trend)
	{
		if(!trend.equals(mTrend))
		{
			mTrend = trend;
			
			this.prepareDrawing();
			
			this.invalidate();
		}
	}
}
