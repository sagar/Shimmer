package sagar.com.shimmer.flameshimmerview;

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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.MaskTransformation;
import sagar.com.shimmer.R;

public class FlameShimmerViewActivity extends AppCompatActivity {

  private static final String TAG = FlameShimmerViewActivity.class.getSimpleName();

  ImageView imageView;
  private static int gold1, gold2, gold3, gold4, gold5, gold6, gold7;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.shimmer_view);
    imageView = (ImageView) findViewById(R.id.shimmer_view);

    gold1 = getResources().getColor(R.color.gold1);
    gold2 = getResources().getColor(R.color.gold2);
    gold3 = getResources().getColor(R.color.gold3);
    gold4 = getResources().getColor(R.color.gold4);
    gold5 = getResources().getColor(R.color.gold5);
    gold6 = getResources().getColor(R.color.gold6);
    gold7 = getResources().getColor(R.color.gold7);

    //transform2(imageView);
  }

  public int[] getColorList() {
    return new int[] { gold1, gold2, gold3, gold4, gold5, gold6, gold7 };
  }

  private void transform5(ImageView imageView) {
    Bitmap original = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
    Shader shader = new LinearGradient(0, 0, 200, 200, getColorList(), null, TileMode.CLAMP);
    Canvas canvas1 = new Canvas();
    Paint paint1 = new Paint();
    paint1.setShader(shader);
    canvas1.drawBitmap(original, 0, 0, paint1);
    //canvas1.drawRect(0, 0, 200, 200, paint1);
    imageView.setImageBitmap(original);
    imageView.setScaleType(ScaleType.CENTER);
  }

  private void transform4(ImageView imageView) {
    Bitmap original = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
    Shader shader = new LinearGradient(0, 0, 200, 200, getColorList(), null, TileMode.CLAMP);
    Canvas canvas1 = new Canvas();
    Paint paint1 = new Paint();
    paint1.setShader(shader);
    canvas1.drawBitmap(original, 0, 0, paint1);
    //canvas1.drawRect(0, 0, 200, 200, paint1);
    imageView.setImageBitmap(original);
    imageView.setScaleType(ScaleType.CENTER);
  }

  private void transform3(ImageView imageView) {
    Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.flame);

    Bitmap original = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
    LinearGradient shader = new LinearGradient(0, 0, mask.getWidth(), mask.getHeight(), getColorList(), null,
        TileMode.CLAMP);
    Canvas canvas1 = new Canvas(original);
    Paint paint1 = new Paint();
    paint1.setShader(shader);
    canvas1.drawBitmap(original, 0, 0, paint1);

    //Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.sagar);
    Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(result);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    canvas.drawBitmap(original, 0, 0, null);
    canvas.drawBitmap(mask, 0, 0, paint);
    paint.setXfermode(null);
    imageView.setImageBitmap(result);
    imageView.setScaleType(ScaleType.CENTER);
  }

  private void transform2(ImageView imageView) {
    Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.sagar);
    Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.flame);
    Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(result);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    canvas.drawBitmap(original, 0, 0, null);
    canvas.drawBitmap(mask, 0, 0, paint);
    paint.setXfermode(null);
    imageView.setImageBitmap(result);
    imageView.setScaleType(ScaleType.CENTER);
  }

  private void transform1(ImageView imageView) {
    Glide.with(this)
        .load(R.drawable.sagar)
        .bitmapTransform(new MaskTransformation(this, R.drawable.flame))
        .into(imageView);
  }
}