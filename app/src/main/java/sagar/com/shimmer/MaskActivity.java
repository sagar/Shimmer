package sagar.com.shimmer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.MaskTransformation;

public class MaskActivity extends AppCompatActivity {

  private static final String TAG = MaskActivity.class.getSimpleName();

  //https://stackoverflow.com/questions/12614542/maskingcrop-image-in-frame
  //https://stackoverflow.com/questions/5299452/how-can-i-crop-an-image-with-mask-and-combine-it-with-another-image-background
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mask_view);
    transform2();
  }

  private void transform1() {
    final ImageView imageView = (ImageView) findViewById(R.id.image_container);
    Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.sagar);
    Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
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

  private void transform2() {
    final ImageView imageView = (ImageView) findViewById(R.id.image_container);
    Glide.with(this)
        .load(R.drawable.sagar)
        .bitmapTransform(new MaskTransformation(this, R.drawable.mask))
        .into(imageView);
  }
}