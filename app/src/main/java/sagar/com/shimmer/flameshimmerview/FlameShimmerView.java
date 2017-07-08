package sagar.com.shimmer.flameshimmerview;

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

public class FlameShimmerView extends AppCompatImageView {

  private static final String TAG = FlameShimmerView.class.getSimpleName();
  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private static Shader gradient = null;
  private static Bitmap flame;


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

  @Override
  protected void onDraw(Canvas canvas) {
    Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
    Canvas canvas1 = new Canvas(bitmap);
    super.onDraw(canvas1);

    Paint paint = new Paint();
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
    paint.setShader(new LinearGradient(0, 0, getWidth(), getWidth(), getColorList(), null, TileMode.CLAMP));
    canvas1.drawRect(0, 0, getWidth(), getHeight(), paint);
    canvas.drawBitmap(bitmap, 0, 0, null);
  }

  /**
  @Override
  protected void onDraw(Canvas canvas) {
    final PorterDuffXfermode mode = new PorterDuffXfermode(Mode.DARKEN);
    super.onDraw(canvas);
    //Bitmap bitmap1 = Bitmap.createBitmap(200, 200, Config.ARGB_8888); // flame
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.outWidth = 200;
    options.outHeight = 200;
    Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.flame, options);
    Log.e(TAG, "height = " + bitmap1.getHeight());
    Log.e(TAG, "width = " + bitmap1.getWidth());

    //Canvas canvas1 = new Canvas(bitmap1);

    canvas.drawBitmap(bitmap1, 0, 0, null);
    //super.onDraw(canvas1);

    Shader shader = new LinearGradient(0, 0, 200, 200, getColorList(), null, TileMode.CLAMP);
    Paint paint = new Paint();
    paint.setShader(shader);
    paint.setXfermode(mode);

    Bitmap bitmap2 = Bitmap.createBitmap(200, 200, Config.ARGB_8888); // gradient
    //canvas1.drawBitmap(bitmap2, 0, 0, paint);
    //canvas.drawBitmap(bitmap2, 0, 0, paint);
    canvas.drawRect(0, 0, 297, 210, paint);
  }
  **/

  public int[] getColorList() {
    return new int[] { gold1, gold2, gold3, gold4, gold5, gold6, gold7 };
  }

  /**
  @Override
  protected void onDraw(Canvas canvas) {
    Paint paint = new Paint();
    canvas.drawBitmap(flame, 0, 0, paint);

    Bitmap gadientBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
    Canvas canvas1 = new Canvas(gadientBitmap);

    //Shader gradient = new LinearGradient(0, 0, getWidth(), getHeight(), getColorList(), null, TileMode.REPEAT);
    paint.setXfermode(mode);
    //paint.setShader(gradient);
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

    super.onDraw(canvas);
  }
  **/
}
