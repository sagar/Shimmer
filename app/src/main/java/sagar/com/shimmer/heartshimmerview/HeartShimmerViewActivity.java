package sagar.com.shimmer.heartshimmerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import sagar.com.shimmer.R;

public class HeartShimmerViewActivity extends AppCompatActivity {

  private static final String TAG = HeartShimmerViewActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.heart_shimmer_view);
  }
}