package sagar.com.shimmer.flameshimmerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import sagar.com.shimmer.R;

public class FlameShimmerViewActivity extends AppCompatActivity {

  private static final String TAG = FlameShimmerViewActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.flame_shimmer_view);
  }
}