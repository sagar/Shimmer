package sagar.com.shimmer.customdrawable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import sagar.com.shimmer.R;

public class HeartShimmerDrawableActivity extends AppCompatActivity {

  private static final String TAG = HeartShimmerDrawableActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.heart_shimmer_drawable);

    ImageView imageView = (ImageView) findViewById(R.id.shimmer_drawable);
    //
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false;
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.paywall_heart_bg, options);
    Bitmap foreground = BitmapFactory.decodeResource(getResources(), R.drawable.paywall_heart_color, options);
    LayoutParams params = new LayoutParams(background.getWidth(), background.getHeight());
    imageView.setLayoutParams(params);
    imageView.setImageDrawable(new HeartShimmerDrawable(this, foreground, background));
  }
}