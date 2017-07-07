package sagar.com.shimmer.others;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import sagar.com.shimmer.R;

public class ShimmerLayoutActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = ShimmerLayoutActivity.class.getSimpleName();
  private boolean stopped = false;
  private ShimmerLayout container1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.shimmer_layout);
    ImageView nonShimmerImage = (ImageView) findViewById(R.id.image1);
    nonShimmerImage.setOnClickListener(this);
    container1 = (ShimmerLayout) findViewById(R.id.shimmer_view_container1);
    container1.startShimmerAnimation();
  }

  public void onClick(View view) {
    if (stopped) {
      container1.startShimmerAnimation();
      stopped = false;
    } else {
      container1.stopShimmerAnimation();
      stopped = true;
    }

  }
}