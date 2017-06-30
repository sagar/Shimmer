package sagar.com.shimmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  ShimmerFrameLayout container1;
  boolean stopped = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    container1 =
        (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container1);
    container1.startShimmerAnimation();

    ImageView imageView = (ImageView) findViewById(R.id.image1);
    imageView.setOnClickListener(this);
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
