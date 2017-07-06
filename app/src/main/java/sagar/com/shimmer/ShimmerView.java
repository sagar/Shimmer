package sagar.com.shimmer;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class ShimmerView extends View {

  private static final String TAG = ShimmerView.class.getSimpleName();
  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private static float[] positions = new float[] {0, 0, 0, 0, 0, 0, 0}; // = new float[] { 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f };
  private static Shader gradient = null;
  private Paint paint = new Paint();
  private ValueAnimator animator;
  private boolean mAnimationStarted;
  private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;


  public ShimmerView(Context context) {
    super(context);
  }

  public ShimmerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    gold1 = context.getResources().getColor(R.color.gold1);
    gold2 = context.getResources().getColor(R.color.gold2);
    gold3 = context.getResources().getColor(R.color.gold3);
    gold4 = context.getResources().getColor(R.color.gold4);
    gold5 = context.getResources().getColor(R.color.gold5);
    gold6 = context.getResources().getColor(R.color.gold6);
    gold7 = context.getResources().getColor(R.color.gold7);
  }

  public int[] getColorList() {
    return new int[] { gold1, gold2, gold3, gold4, gold5, gold6, gold7 };
  }

  @Override
  protected void onDraw(Canvas canvas) {
    paint.setShader(gradient);
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    super.onDraw(canvas);
  }

  public void startAnimation() {
    animator = ValueAnimator.ofFloat(0, 1);
    animator.setDuration(3000);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    animator.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        gradient = getGradient(valueAnimator.getAnimatedFraction());
        invalidate();
      }
    });
    animator.start();
  }

  public void stopAnimation() {
    if (animator == null) {
      return;
    }
    animator.end();
    animator.removeAllUpdateListeners();
    animator.cancel();
  }

  private Shader getGradient(float fraction) {
    gradient = new LinearGradient(0, 0, getWidth(), getHeight(), getColorList(), getPositions(fraction), TileMode.REPEAT);
    return gradient;
  }

  private float[] getPositions(float fraction) {
    for (int i = 0; i < 7; i++) {
      positions[i] += fraction;
      Log.e(TAG, "positions[" + i + "] = " + positions[i]);
    }
    return positions;
  }
}
