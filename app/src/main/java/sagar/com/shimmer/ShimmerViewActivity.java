package sagar.com.shimmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ShimmerViewActivity extends AppCompatActivity {

  private static final String TAG = ShimmerViewActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.shimmer_view);
  }
}