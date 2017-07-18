package sagar.com.shimmer.heartshimmerview;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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
import sagar.com.shimmer.R;

public class HeartShimmerView2 extends AppCompatImageView {

  private static final String TAG = HeartShimmerView2.class.getSimpleName();
  private final int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private final float positions[] = new float[7];
  private final float offset = 0.03f;
  private float animatorFloat = 0;
  private ValueAnimator animator;

  public HeartShimmerView2(Context context) {
    this(context, null);
  }

  public HeartShimmerView2(Context context, @Nullable AttributeSet attrs) {
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
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false;
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.paywall_heart_bg, options);
    int dstWidth = (int) (background.getWidth() * 0.83f);
    int dstHeight = (int) (background.getHeight() * 0.8375f);
    Bitmap scaledBackground = Bitmap.createScaledBitmap(background, dstWidth, dstHeight, true);

    // draws heart
    Bitmap bitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Config.ARGB_8888);
    Canvas tempCanvas = new Canvas(bitmap);
    super.onDraw(tempCanvas);

    // draws gradient
    final Paint paint = new Paint();
    final Shader gradient = new LinearGradient(0, 0, background.getWidth(), background.getWidth(), getColorList(),
        updateColorPositions(animatorFloat), TileMode.CLAMP);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
    paint.setShader(gradient);
    tempCanvas.drawRect(0, 0, background.getWidth(), background.getHeight(), paint);

    canvas.drawBitmap(scaledBackground, 0, 0, null);

    int dstWidth2 = (int) (background.getWidth() * 0.92f);
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, dstWidth2, dstHeight, true);
    canvas.drawBitmap(scaledBitmap, background.getWidth() * 0.05f, background.getHeight() * 0.0875f, null);
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
    }
    return positions;
  }
}
