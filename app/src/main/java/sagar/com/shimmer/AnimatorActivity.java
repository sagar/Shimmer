package sagar.com.shimmer;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class AnimatorActivity extends AppCompatActivity {

  private static final String TAG = AnimatorActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.animator);
    final ImageView imageView = (ImageView) findViewById(R.id.animatedImage1);

    ValueAnimator animator = ValueAnimator.ofFloat(0f, 200f);
    animator.setDuration(3000);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    animator.addUpdateListener(new AnimatorUpdateListener() {
      @RequiresApi(api = VERSION_CODES.LOLLIPOP)
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        imageView.setTranslationX((float) valueAnimator.getAnimatedValue());
        imageView.setTranslationY((float) valueAnimator.getAnimatedValue());
        imageView.setTranslationZ((float) valueAnimator.getAnimatedValue());
      }
    });
    animator.start();
  }
}