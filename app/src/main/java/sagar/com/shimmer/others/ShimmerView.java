package sagar.com.shimmer.others;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import sagar.com.shimmer.R;

public class ShimmerView extends View {

  private static final String TAG = ShimmerView.class.getSimpleName();
  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;

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
    Paint paint = new Paint();
    Shader gradient = new LinearGradient(0, 0, getWidth(), getHeight(), getColorList(), null, TileMode.REPEAT);
    paint.setShader(gradient);
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    super.onDraw(canvas);
  }
}
