package sagar.com.shimmer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ShimmerView extends View {

  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  Shader gradient;
  Paint paint = new Paint();

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
    return new int[] {gold1, gold2, gold3, gold4, gold5, gold6, gold7};
  }

  @Override
  protected void onDraw(Canvas canvas) {
    paint.setShader(gradient);
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    super.onDraw(canvas);
  }

  @Override
  protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    super.onSizeChanged(width, height, oldWidth, oldHeight);
    gradient = new LinearGradient(0, 0, width, height, getColorList(), null, TileMode.CLAMP);
  }
}
