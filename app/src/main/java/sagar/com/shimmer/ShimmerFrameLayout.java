package sagar.com.shimmer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class ShimmerFrameLayout extends FrameLayout {

  private static final String TAG = "ShimmerFrameLayout";
  private static final PorterDuffXfermode DST_IN_PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;

  // struct storing various mask related parameters, which are used to construct the mask bitmap
  private static class Mask {
    public float tilt;
    public float dropoff;
    public int fixedWidth;
    public int fixedHeight;
    public float intensity;
    public float relativeWidth;
    public float relativeHeight;

    public int maskWidth(int width) {
      return fixedWidth > 0 ? fixedWidth : (int) (width * relativeWidth);
    }

    public int maskHeight(int height) {
      return fixedHeight > 0 ? fixedHeight : (int) (height * relativeHeight);
    }

    /**
     * Get the array of colors to be distributed along the gradient of the mask bitmap
     *
     * @return An array of black and transparent colors
     */
    public int[] getGradientColors() {
      //return new int[]{gold1, gold2, gold3, gold4, gold5, gold6, gold7};
      return new int[]{Color.TRANSPARENT, Color.BLACK, Color.BLACK, Color.TRANSPARENT};
    }

    /**
     * Get the array of relative positions [0..1] of each corresponding color in the colors array
     *
     * @return A array of float values in the [0..1] range
     */
    public float[] getGradientPositions() {
      return new float[]{
          //Math.max((1.0f - intensity - dropoff) / 2, 0.0f),
          Math.max((1.0f - intensity - dropoff) / 2, 0.0f),
          Math.max((1.0f - intensity) / 2, 0.0f),
          //Math.max((1.0f - intensity) / 2, 0.5f),
          Math.min((1.0f + intensity) / 2, 1.0f),
          //Math.min((1.0f + intensity + dropoff) / 2, 1.0f),
          Math.min((1.0f + intensity + dropoff) / 2, 1.0f)};
    }
  }

  // struct for storing the mask translation animation values
  private static class MaskTranslation {

    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public void set(int fromX, int fromY, int toX, int toY) {
      this.fromX = fromX;
      this.fromY = fromY;
      this.toX = toX;
      this.toY = toY;
    }
  }

  private Paint mAlphaPaint;
  private Paint mMaskPaint;

  private Mask mMask;
  private MaskTranslation mMaskTranslation;

  private Bitmap mRenderMaskBitmap;
  private Bitmap mRenderUnmaskBitmap;

  private boolean mAutoStart;
  private int mDuration;
  private int mRepeatCount;
  private int mRepeatDelay;
  private int mRepeatMode;

  private int mMaskOffsetX;
  private int mMaskOffsetY;

  private boolean mAnimationStarted;
  private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

  protected ValueAnimator mAnimator;
  protected Bitmap mMaskBitmap;

  public ShimmerFrameLayout(Context context) {
    this(context, null, 0);
  }

  public ShimmerFrameLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ShimmerFrameLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    gold1 = context.getResources().getColor(R.color.gold1);
    gold2 = context.getResources().getColor(R.color.gold2);
    gold3 = context.getResources().getColor(R.color.gold3);
    gold4 = context.getResources().getColor(R.color.gold4);
    gold5 = context.getResources().getColor(R.color.gold5);
    gold6 = context.getResources().getColor(R.color.gold6);
    gold7 = context.getResources().getColor(R.color.gold7);

    setWillNotDraw(false);

    mMask = new Mask();
    mAlphaPaint = new Paint();
    mMaskPaint = new Paint();
    mMaskPaint.setAntiAlias(true);
    mMaskPaint.setDither(true);
    mMaskPaint.setFilterBitmap(true);
    mMaskPaint.setXfermode(DST_IN_PORTER_DUFF_XFERMODE);

    useDefaults();
  }



  /**
   * Resets the layout to its default state. Any parameters that were set or modified will be reverted back to their
   * original value. Also, stops the shimmer animation if it is currently playing.
   */
  public void useDefaults() {
    // Set defaults
    setAutoStart(false);
    setDuration(1000);
    setRepeatCount(ObjectAnimator.INFINITE);
    setRepeatDelay(0);
    setRepeatMode(ObjectAnimator.RESTART);

    mMask.dropoff = 0.5f;
    mMask.fixedWidth = 0;
    mMask.fixedHeight = 0;
    mMask.intensity = 0.0f;
    mMask.relativeWidth = 1.0f;
    mMask.relativeHeight = 1.0f;
    mMask.tilt = 20;

    mMaskTranslation = new MaskTranslation();

    setBaseAlpha(0.3f);

    resetAll();
  }

  /**
   * Enable or disable 'auto start' for this layout. When auto start is enabled, the layout will start animating
   * automatically whenever it is attached to the current window.
   *
   * @param autoStart Whether auto start should be enabled or not
   */
  public void setAutoStart(boolean autoStart) {
    mAutoStart = autoStart;
    resetAll();
  }

  /**
   * Set the alpha to be used to render the base view i.e. the unhighlighted view over which the highlight is drawn.
   *
   * @param alpha Alpha (opacity) of the base view
   */
  public void setBaseAlpha(float alpha) {
    mAlphaPaint.setAlpha((int) (clamp(0, 1, alpha) * 0xff));
    resetAll();
  }

  /**
   * Set the duration of the animation i.e. the time it will take for the highlight to move from one end of the layout
   * to the other.
   *
   * @param duration Duration of the animation, in milliseconds
   */
  public void setDuration(int duration) {
    mDuration = duration;
    resetAll();
  }

  /**
   * Set the number of times the animation should repeat. If the repeat count is 0, the animation stops after reaching
   * the end. If greater than 0, or -1 (for infinite), the repeat mode is taken into account.
   *
   * @param repeatCount Number of times the current animation should repeat, or -1 for indefinite.
   */
  public void setRepeatCount(int repeatCount) {
    mRepeatCount = repeatCount;
    resetAll();
  }

  /**
   * Set the delay after which the animation repeat, unless it has ended.
   *
   * @param repeatDelay Delay after which the animation should repeat, in milliseconds.
   */
  public void setRepeatDelay(int repeatDelay) {
    mRepeatDelay = repeatDelay;
    resetAll();
  }

  /**
   * Set what the animation should do after reaching the end. One of
   * <a href="http://developer.android.com/reference/android/animation/ValueAnimator.html#REVERSE">REVERSE</a> or
   * <a href="http://developer.android.com/reference/android/animation/ValueAnimator.html#RESTART">RESTART</a>
   *
   * @param repeatMode Repeat mode of the animation
   */
  public void setRepeatMode(int repeatMode) {
    mRepeatMode = repeatMode;
    resetAll();
  }

  /**
   * Start the shimmer animation. If the 'auto start' property is set, this method is called automatically when the
   * layout is attached to the current window. Calling this method has no effect if the animation is already playing.
   */
  public void startShimmerAnimation() {
    if (mAnimationStarted) {
      return;
    }
    Animator animator = getShimmerAnimation();
    animator.start();
    mAnimationStarted = true;
  }

  /**
   * Stop the shimmer animation. Calling this method has no effect if the animation hasn't been started yet.
   */
  public void stopShimmerAnimation() {
    if (mAnimator != null) {
      mAnimator.end();
      mAnimator.removeAllUpdateListeners();
      mAnimator.cancel();
    }
    mAnimator = null;
    mAnimationStarted = false;
  }

  /**
   * Translate the mask offset horizontally. Used by the animator.
   *
   * @param maskOffsetX Horizontal translation offset of the mask
   */
  private void setMaskOffsetX(int maskOffsetX) {
    if (mMaskOffsetX == maskOffsetX) {
      return;
    }
    mMaskOffsetX = maskOffsetX;
    invalidate();
  }

  /**
   * Translate the mask offset vertically. Used by the animator.
   *
   * @param maskOffsetY Vertical translation offset of the mask
   */
  private void setMaskOffsetY(int maskOffsetY) {
    if (mMaskOffsetY == maskOffsetY) {
      return;
    }
    mMaskOffsetY = maskOffsetY;
    invalidate();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (mGlobalLayoutListener == null) {
      mGlobalLayoutListener = getLayoutListener();
    }
    getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
  }

  private ViewTreeObserver.OnGlobalLayoutListener getLayoutListener() {
    return new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        boolean animationStarted = mAnimationStarted;
        resetAll();
        if (mAutoStart || animationStarted) {
          startShimmerAnimation();
        }
      }
    };
  }

  @Override
  protected void onDetachedFromWindow() {
    stopShimmerAnimation();
    if (mGlobalLayoutListener != null) {
      getViewTreeObserver().removeGlobalOnLayoutListener(mGlobalLayoutListener);
      mGlobalLayoutListener = null;
    }
    super.onDetachedFromWindow();
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    if (!mAnimationStarted || getWidth() <= 0 || getHeight() <= 0) {
      super.dispatchDraw(canvas);
      return;
    }
    dispatchDrawUsingBitmap(canvas);
  }

  private static float clamp(float min, float max, float value) {
    return Math.min(max, Math.max(min, value));
  }

  /**
   * Draws and masks the children using a Bitmap.
   *
   * @param canvas Canvas that the masked children will end up being drawn to.
   */
  private boolean dispatchDrawUsingBitmap(Canvas canvas) {
    Bitmap unmaskBitmap = tryObtainRenderUnmaskBitmap();
    Bitmap maskBitmap = tryObtainRenderMaskBitmap();
    if (unmaskBitmap == null || maskBitmap == null) {
      return false;
    }
    // First draw a desaturated version
    drawUnmasked(new Canvas(unmaskBitmap));
    canvas.drawBitmap(unmaskBitmap, 0, 0, mAlphaPaint);

    // Then draw the masked version
    drawMasked(new Canvas(maskBitmap));
    canvas.drawBitmap(maskBitmap, 0, 0, null);

    return true;
  }

  private Bitmap tryObtainRenderUnmaskBitmap() {
    if (mRenderUnmaskBitmap == null) {
      mRenderUnmaskBitmap = tryCreateRenderBitmap();
    }
    return mRenderUnmaskBitmap;
  }

  private Bitmap tryObtainRenderMaskBitmap() {
    if (mRenderMaskBitmap == null) {
      mRenderMaskBitmap = tryCreateRenderBitmap();
    }
    return mRenderMaskBitmap;
  }

  private Bitmap tryCreateRenderBitmap() {
    int width = getWidth();
    int height = getHeight();
    try {
      return createBitmapAndGcIfNecessary(width, height);
    } catch (OutOfMemoryError e) {
      String logMessage = "ShimmerFrameLayout failed to create working bitmap";
      StringBuilder logMessageStringBuilder = new StringBuilder(logMessage);
      logMessageStringBuilder.append(" (width = ");
      logMessageStringBuilder.append(width);
      logMessageStringBuilder.append(", height = ");
      logMessageStringBuilder.append(height);
      logMessageStringBuilder.append(")\n\n");
      for (StackTraceElement stackTraceElement :
          Thread.currentThread().getStackTrace()) {
        logMessageStringBuilder.append(stackTraceElement.toString());
        logMessageStringBuilder.append("\n");
      }
      logMessage = logMessageStringBuilder.toString();
      Log.d(TAG, logMessage);
    }
    return null;
  }

  // Draws the children without any mask.
  private void drawUnmasked(Canvas renderCanvas) {
    renderCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    super.dispatchDraw(renderCanvas);
  }

  // Draws the children and masks them on the given Canvas.
  private void drawMasked(Canvas renderCanvas) {
    Bitmap maskBitmap = getMaskBitmap();
    if (maskBitmap == null) {
      return;
    }

    renderCanvas.clipRect(
        mMaskOffsetX,
        mMaskOffsetY,
        mMaskOffsetX + maskBitmap.getWidth(),
        mMaskOffsetY + maskBitmap.getHeight());
    renderCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    super.dispatchDraw(renderCanvas);

    renderCanvas.drawBitmap(maskBitmap, mMaskOffsetX, mMaskOffsetY, mMaskPaint);
  }

  private void resetAll() {
    stopShimmerAnimation();
    resetMaskBitmap();
    resetRenderedView();
  }

  // If a mask bitmap was created, it's recycled and set to null so it will be recreated when needed.
  private void resetMaskBitmap() {
    if (mMaskBitmap != null) {
      mMaskBitmap.recycle();
      mMaskBitmap = null;
    }
  }

  // If a working bitmap was created, it's recycled and set to null so it will be recreated when needed.
  private void resetRenderedView() {
    if (mRenderUnmaskBitmap != null) {
      mRenderUnmaskBitmap.recycle();
      mRenderUnmaskBitmap = null;
    }

    if (mRenderMaskBitmap != null) {
      mRenderMaskBitmap.recycle();
      mRenderMaskBitmap = null;
    }
  }

  // Return the mask bitmap, creating it if necessary.
  private Bitmap getMaskBitmap() {
    if (mMaskBitmap != null) {
      return mMaskBitmap;
    }

    int width = mMask.maskWidth(getWidth());
    int height = mMask.maskHeight(getHeight());

    mMaskBitmap = createBitmapAndGcIfNecessary(width, height);
    Canvas canvas = new Canvas(mMaskBitmap);
    Shader gradient;
    int x1 = 0, y1 = 0;
    int x2 = width, y2 = 0;
    gradient =
        new LinearGradient(
            x1, y1,
            x2, y2,
            mMask.getGradientColors(),
            mMask.getGradientPositions(),
            Shader.TileMode.REPEAT);

    canvas.rotate(mMask.tilt, width / 2, height / 2);
    Paint paint = new Paint();
    paint.setShader(gradient);
    // We need to increase the rect size to account for the tilt
    int padding = (int) (Math.sqrt(2) * Math.max(width, height)) / 2;
    canvas.drawRect(-padding, -padding, width + padding, height + padding, paint);

    return mMaskBitmap;
  }

  // Get the shimmer <a href="http://developer.android.com/reference/android/animation/Animator.html">Animator</a>
  // object, which is responsible for driving the highlight mask animation.
  private Animator getShimmerAnimation() {
    if (mAnimator != null) {
      return mAnimator;
    }
    int width = getWidth();

    mMaskTranslation.set(-width, 0, width, 0);
    mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f + (float) mRepeatDelay / mDuration);
    mAnimator.setDuration(mDuration + mRepeatDelay);
    mAnimator.setRepeatCount(mRepeatCount);
    mAnimator.setRepeatMode(mRepeatMode);
    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        float value = Math.max(0.0f, Math.min(1.0f, (Float) animation.getAnimatedValue()));
        setMaskOffsetX((int) (mMaskTranslation.fromX * (1 - value) + mMaskTranslation.toX * value));
        setMaskOffsetY((int) (mMaskTranslation.fromY * (1 - value) + mMaskTranslation.toY * value));
      }
    });
    return mAnimator;
  }

  /**
   * Creates a bitmap with the given width and height.
   * <p/>
   * If it fails with an OutOfMemory error, it will force a GC and then try to create the bitmap
   * one more time.
   *
   * @param width  width of the bitmap
   * @param height height of the bitmap
   */
  protected static Bitmap createBitmapAndGcIfNecessary(int width, int height) {
    try {
      return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    } catch (OutOfMemoryError e) {
      System.gc();
      return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }
  }
}