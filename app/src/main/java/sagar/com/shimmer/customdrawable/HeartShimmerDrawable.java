package sagar.com.shimmer.customdrawable;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import sagar.com.shimmer.R;

public class HeartShimmerDrawable extends Drawable {

  private static final String TAG = HeartShimmerDrawable.class.getSimpleName();
  private final int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private final float positions[] = new float[7];
  private final float offset = 0.03f;
  private float animatorFloat = 0;
  private ValueAnimator animator;
  private final Bitmap background;
  private final Bitmap foreground;
  private final Paint paint;

  public HeartShimmerDrawable(final Context context, Bitmap foreground, Bitmap background) {
    gold1 = context.getResources().getColor(R.color.gold1);
    gold2 = context.getResources().getColor(R.color.gold2);
    gold3 = context.getResources().getColor(R.color.gold3);
    gold4 = context.getResources().getColor(R.color.gold4);
    gold5 = context.getResources().getColor(R.color.gold5);
    gold6 = context.getResources().getColor(R.color.gold6);
    gold7 = context.getResources().getColor(R.color.gold7);

    updateColorPositions(0);
    paint = new Paint();

    this.background = background;
    this.foreground = foreground;

    animator = ValueAnimator.ofFloat(0, 1);
    animator.setDuration(1000);
    animator.setStartDelay(0);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    animator.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        animatorFloat = valueAnimator.getAnimatedFraction();
        invalidateSelf();
      }
    });
    animator.start();
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

  @Override
  public void draw(@NonNull Canvas canvas) {
    int backgroundX = 0, backgroundY = 0;
    int foregroundX = 6 + (backgroundX * 6), foregroundY = 9 + (backgroundY * 9);

    // background
    canvas.drawBitmap(background, backgroundX, backgroundY, null);

    // shimmer
    Bitmap shimmerBitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Config.ARGB_8888);
    Canvas tempCanvas = new Canvas(shimmerBitmap);
    tempCanvas.drawBitmap(foreground, backgroundX, backgroundY, null);
    final Shader gradient = new LinearGradient(backgroundX, backgroundY, canvas.getWidth(), canvas.getHeight(), getColorList(), updateColorPositions(animatorFloat), TileMode.CLAMP);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
    paint.setShader(gradient);
    tempCanvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

    Bitmap upscaledBitmap = Bitmap.createScaledBitmap(shimmerBitmap, (int) (shimmerBitmap.getWidth() * 1.15f), (int) (shimmerBitmap.getHeight() * 1.1f), true);

    // foreground
    canvas.drawBitmap(upscaledBitmap, foregroundX, foregroundY, null);
  }

  @Override
  public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    paint.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(@Nullable ColorFilter colorFilter) {
    paint.setColorFilter(colorFilter);
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSPARENT;
  }
}
