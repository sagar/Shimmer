package sagar.com.shimmer.textshimmerview;

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
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import sagar.com.shimmer.R;

public class TextShimmerView extends AppCompatTextView {

  private static final String TAG = TextShimmerView.class.getSimpleName();
  private final int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private final float positions[] = new float[7];
  private final float offset = 0.03f;
  private float animatorFloat = 0;
  private Canvas tempCanvas;
  private Bitmap bitmap;
  private ValueAnimator animator;

  public TextShimmerView(Context context) {
    this(context, null);
  }

  public TextShimmerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    gold1 = getResources().getColor(R.color.gold1);
    gold2 = getResources().getColor(R.color.gold2);
    gold3 = getResources().getColor(R.color.gold3);
    gold4 = getResources().getColor(R.color.gold4);
    gold5 = getResources().getColor(R.color.gold5);
    gold6 = getResources().getColor(R.color.gold6);
    gold7 = getResources().getColor(R.color.gold7);

    updateColorPositions(0);

    animator = ValueAnimator.ofFloat(0, 1);
    animator.setDuration(1000);
    animator.setStartDelay(0);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    animator.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        animatorFloat = valueAnimator.getAnimatedFraction();
        invalidate();
      }
    });
    animator.start();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    // draws flame
    bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
    tempCanvas = new Canvas(bitmap);
    super.onDraw(tempCanvas);

    // draws gradient
    final Paint paint = new Paint();
    final Shader gradient = new LinearGradient(0, 0, getWidth(), getWidth(), getColorList(),
        updateColorPositions(animatorFloat), TileMode.CLAMP);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
    paint.setShader(gradient);
    tempCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);

    canvas.drawBitmap(bitmap, 0, 0, null);
  }

  public int[] getColorList() {
    return new int[] { gold1, gold2, gold3, gold4, gold5, gold6, gold7 };
  }

  public float[] updateColorPositions(float fraction) {
    positions[0] = offset + fraction;
    for (int i = 1; i < positions.length; i++) {
      positions[i] = positions[i-1] + fraction;
      if (positions[i] >= 1) {
        positions[i] = 0;
      }
      //Log.e(TAG, "positions[" + i + "] = " + positions[i]);
    }
    return positions;
  }
}
