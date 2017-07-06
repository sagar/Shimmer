package sagar.com.shimmer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import java.util.Locale;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

  private static final String TAG = SensorActivity.class.getSimpleName();
  private TextView sensorInfo;
  private SensorManager sensorManager;
  private Sensor accelerometer;
  private float prevX, prevY, prevZ;
  private String magnitudeX = "+ve", magnitudeY = "+ve", magnitudeZ = "+ve";

  // attitude.roll - constant updates
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.sensor);
    sensorInfo = (TextView) findViewById(R.id.sensor_info);
    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
    sensorInfo.setText(text);

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

  private int compare(Float x, Float y) {
    if (x > y) {
      return 1;
    } else if (x < y) {
      return -1;
    }
    return 0;
  }
}
