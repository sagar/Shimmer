package sagar.com.shimmer.textshimmerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import sagar.com.shimmer.R;

public class TextShimmerViewActivity extends AppCompatActivity {

  private static final String TAG = TextShimmerViewActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.text_shimmer_view);
  }
}