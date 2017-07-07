package sagar.com.shimmer.others;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import sagar.com.shimmer.R;

public class FbShimmerActivity extends AppCompatActivity{

  private static final String TAG = FbShimmerActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fb_shimmer);

    FbShimmerLayout layout = (FbShimmerLayout) findViewById(R.id.fbcontainer);
    layout.startShimmerAnimation();
  }
}