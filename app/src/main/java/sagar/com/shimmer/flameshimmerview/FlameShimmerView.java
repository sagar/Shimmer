package sagar.com.shimmer.flameshimmerview;

import android.content.Context;
import android.graphics.Bitmap;
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

public class FlameShimmerView extends AppCompatImageView {

  private static final String TAG = FlameShimmerView.class.getSimpleName();
  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private static Shader gradient = null;
  private static Bitmap flame;
  private static final PorterDuffXfermode mode = new PorterDuffXfermode(Mode.DST_OUT);

  public FlameShimmerView(Context context) {
    super(context);
  }

  public FlameShimmerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    gold1 = context.getResources().getColor(R.color.gold1);
    gold2 = context.getResources().getColor(R.color.gold2);
    gold3 = context.getResources().getColor(R.color.gold3);
    gold4 = context.getResources().getColor(R.color.gold4);
    gold5 = context.getResources().getColor(R.color.gold5);
    gold6 = context.getResources().getColor(R.color.gold6);
    gold7 = context.getResources().getColor(R.color.gold7);
    flame = BitmapFactory.decodeResource(getResources(), R.drawable.flame);
  }

  public int[] getColorList() {
    return new int[] { gold1, gold2, gold3, gold4, gold5, gold6, gold7 };
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (gradient == null) {
      gradient = new LinearGradient(0, 0, getWidth(), getHeight(), getColorList(), null, TileMode.REPEAT);
    }
    Paint paint = new Paint();
    paint.setShader(gradient);
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

    paint.setXfermode(mode);

    canvas.drawBitmap(flame, 0, 0, paint);

    super.onDraw(canvas);
  }
}
