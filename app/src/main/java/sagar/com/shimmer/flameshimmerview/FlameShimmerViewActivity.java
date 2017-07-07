package sagar.com.shimmer.flameshimmerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.MaskTransformation;
import sagar.com.shimmer.R;

public class FlameShimmerViewActivity extends AppCompatActivity {

  private static final String TAG = FlameShimmerViewActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.shimmer_view);
    ImageView imageView = (ImageView) findViewById(R.id.shimmerView);

    //transform1(imageView);

  }

  private void transform2(ImageView imageView) {

  }

  private void transform1(ImageView imageView) {
    Glide.with(this)
        .load(R.drawable.sagar)
        .bitmapTransform(new MaskTransformation(this, R.drawable.flame))
        .into(imageView);
  }
}