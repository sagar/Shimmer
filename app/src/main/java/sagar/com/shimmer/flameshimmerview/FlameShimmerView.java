package sagar.com.shimmer.flameshimmerview;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import sagar.com.shimmer.R;

public class FlameShimmerView extends AppCompatImageView {

  private static final String TAG = FlameShimmerView.class.getSimpleName();
  private final int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private float animatorFloat = 0;
  private Canvas tempCanvas;
  private Bitmap bitmap;
  private ValueAnimator animator;
  private float endX, endY;
  private float initPosition = -50;

  public FlameShimmerView(Context context) {
    this(context, null);
  }

  public FlameShimmerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    gold1 = getResources().getColor(R.color.gold1);
    gold2 = getResources().getColor(R.color.gold2);
    gold3 = getResources().getColor(R.color.gold3);
    gold4 = getResources().getColor(R.color.gold4);
    gold5 = getResources().getColor(R.color.gold5);
    gold6 = getResources().getColor(R.color.gold6);
    gold7 = getResources().getColor(R.color.gold7);

    animator = ValueAnimator.ofFloat((float) -Math.PI, (float) Math.PI);
    animator.setDuration(2000);
    animator.setStartDelay(0);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    animator.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Log.e(TAG, "fraction = " + valueAnimator.getAnimatedValue());
        float val = ((float) valueAnimator.getAnimatedValue()) * getWidth();
        animatorFloat = val;
        invalidate();
      }
    });
    animator.start();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    Log.e(TAG, String.format("xyz onSizeChanged(): w = %d, h = %d, oldW = %d, oldH = %d", w, h, oldw, oldh));

  }

  @Override
  protected void onDraw(Canvas canvas) {
    Log.e(TAG, "xyz onDraw()");
    // draws flame
    bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
    tempCanvas = new Canvas(bitmap);
    super.onDraw(tempCanvas);

    computeAngle();

    // draws gradient
    final Paint paint = new Paint();
    //Log.e(TAG, "width = " + getWidth());
    final Shader gradient = new LinearGradient(animatorFloat, 0, endX + animatorFloat, endY, getColorListOld(), null, TileMode.CLAMP);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
    paint.setShader(gradient);
    tempCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    canvas.drawBitmap(bitmap, 0, 0, null);
  }

  private void computeAngle() {
    double angleInRadians = Math.toRadians(50.0f);
    endX = (float) (Math.cos(angleInRadians) * getWidth());
    endY = (float) (Math.sin(angleInRadians) * getWidth());
  }

  public int[] getColorList() {
    return new int[] { gold1, gold1, gold2, gold1, gold3, gold1, gold2, gold1, gold1 };
  }

  public int[] getColorListOld() {
    return new int[] { gold1, gold2, gold3, gold4, gold5, gold6, gold7 };
  }
}
