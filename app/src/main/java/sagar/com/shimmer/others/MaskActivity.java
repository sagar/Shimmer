package sagar.com.shimmer.others;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import sagar.com.shimmer.R;

public class MaskActivity extends AppCompatActivity {

  private static final String TAG = MaskActivity.class.getSimpleName();
  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mask_view);

    gold1 = getResources().getColor(R.color.gold1);
    gold2 = getResources().getColor(R.color.gold2);
    gold3 = getResources().getColor(R.color.gold3);
    gold4 = getResources().getColor(R.color.gold4);
    gold5 = getResources().getColor(R.color.gold5);
    gold6 = getResources().getColor(R.color.gold6);
    gold7 = getResources().getColor(R.color.gold7);

    //transform1();
  }

  private void transform1() {
    final float alpha = 0.9f;
    final int width = 200;
    final int height = 200;

    Paint alphaPaint = new Paint();
    alphaPaint.setAlpha((int) (clamp(0, 1, alpha) * 0xff));

    Canvas clearCanvas = new Canvas();
    Bitmap clearBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
    clearCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
    clearCanvas.drawBitmap(clearBitmap, 0, 0, alphaPaint);

    Paint maskPaint = new Paint();
    maskPaint.setAntiAlias(true);
    maskPaint.setDither(true);
    maskPaint.setFilterBitmap(true);
    maskPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

    Bitmap maskBitmap = getMaskBitmap(width, height);
    Canvas maskCanvas = new Canvas();
    int maskOffsetX = 50;
    int maskOffsetY = 0;
    maskCanvas.clipRect(maskOffsetX, maskOffsetY, 50, height);
    maskCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
    maskCanvas.drawBitmap(maskBitmap, maskOffsetX, maskOffsetY, maskPaint);
    maskCanvas.drawBitmap(maskBitmap, 0, 0, null);
  }

  private Bitmap getMaskBitmap(int width, int height) {
    Bitmap maskBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
    Canvas maskCanvas = new Canvas(maskBitmap);
    Shader gradient = new LinearGradient(0, 0, 100, 100, getGradientColors(), null, TileMode.CLAMP);
    Paint paint = new Paint();
    paint.setShader(gradient);
    maskCanvas.drawRect(10, 10, 20, 20, paint);
    return maskBitmap;
  }

  private static float clamp(float min, float max, float value) {
    return Math.min(max, Math.max(min, value));
  }

  public int[] getGradientColors() {
    return new int[]{gold1, gold2, gold3, gold4, gold5, gold6, gold7};
  }
}