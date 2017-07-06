package sagar.com.shimmer;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

  private static final String TAG = MainActivity.class.getSimpleName();
  private boolean stopped = false;
  private TextView sensorInfo;
  private ShimmerFrameLayout container1;
  private SensorManager sensorManager;
  private Sensor accelerometer;
  private Sensor magnetometer;
  private float prevX, prevY, prevZ;
  private ImageView imageView;
  private ShimmerView shimmerView;
  private String magnitudeX = "+ve", magnitudeY = "+ve", magnitudeZ = "+ve";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main_shimmer_view);
    // setContentView(R.layout.activity_main_animated_view);
    /**
    setContentView(R.layout.activity_main);
    ImageView nonShimmerImage = (ImageView) findViewById(R.id.image1);
    //sensorInfo = (TextView) findViewById(R.id.sensor_info);
    nonShimmerImage.setOnClickListener(this);
    container1 = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container1);
    container1.startShimmerAnimation();
    **/
    //imageView = (ImageView) findViewById(R.id.animatedImage1);
    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    //translate();
    shimmerView = (ShimmerView) findViewById(R.id.shimmerView);
    shimmerView.startAnimation();
  }

  private void translate() {
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
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

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    final float x = sensorEvent.values[0];
    final float y = sensorEvent.values[1];
    final float z = sensorEvent.values[2];

    if (compare(x, prevX) > 0) {
      // x > prevX
      magnitudeX = "+ve";
    } else if (compare(x, prevX) < 0) {
      // x < prevX
      magnitudeX = "-ve";
    }

    if (compare(y, prevY) > 0) {
      // y > prevY
      magnitudeX = "+ve";
    } else if (compare(y, prevY) < 0) {
      // y < prevY
      magnitudeX = "-ve";
    }

    if (compare(z, prevZ) > 0) {
      // z > prevZ
      magnitudeX = "+ve";
    } else if (compare(z, prevZ) < 0) {
      // z < prevZ
      magnitudeX = "-ve";
    }

    String text = String.format(Locale.US, "x: %.1f, y: %.1f, z: %.1f\n\nmagX = %s, magY = %s, magZ = %s", x, y, z, magnitudeX, magnitudeY, magnitudeZ);
    //sensorInfo.setText(text);

    prevX = x;
    prevY = y;
    prevZ = z;
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {
    Log.e(TAG, "onAccuracyChanged();");
  }

  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    Log.e(TAG, "onPointerCaptureChanged();");
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

  private int compare(Float x, Float y) {
    if (x > y) {
      return 1;
    } else if (x < y) {
      return -1;
    }
    return 0;
  }
}

/**

 <TextView
 android:id="@+id/sensor_info"
 android:textStyle="bold"
 android:textSize="18sp"
 android:paddingLeft="25dp"
 android:text="Accelerometer Data"
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"/>

 */