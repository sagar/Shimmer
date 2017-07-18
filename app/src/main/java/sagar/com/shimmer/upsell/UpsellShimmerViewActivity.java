package sagar.com.shimmer.upsell;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import sagar.com.shimmer.R;

public class UpsellShimmerViewActivity extends AppCompatActivity implements SensorEventListener2 {

  private static final String TAG = UpsellShimmerViewActivity.class.getSimpleName();

  private SensorManager sensorManager;
  private Sensor accelerometer, magnetometer;
  private final float[] accelerometerReading = new float[3];
  private final float[] magnetometerReading = new float[3];

  private final float[] rotationMatrix = new float[9];
  public static final float[] orientationAngles = new float[3];

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.upsell_shimmer_view);
    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Get updates from the accelerometer and magnetometer at a constant rate.
    // To make batch operations more efficient and reduce power consumption,
    // provide support for delaying updates to the application.
    //
    // In this example, the sensor reporting delay is small enough such that
    // the application receives an update before the system checks the sensor
    // readings again.

    sensorManager
        .registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    sensorManager
        .registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
  }

  @Override
  protected void onPause() {
    super.onPause();

    // Don't receive any more updates from either sensor.
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      System.arraycopy(sensorEvent.values, 0, accelerometerReading,
          0, accelerometerReading.length);
      updateOrientationAngles();
    }
    else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
      System.arraycopy(sensorEvent.values, 0, magnetometerReading,
          0, magnetometerReading.length);
      updateOrientationAngles();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {
    // Do something here if sensor accuracy changes.
    // You must implement this callback in your code.
  }

  // Compute the three orientation angles based on the most recent readings from
  // the device's accelerometer and magnetometer.
  public void updateOrientationAngles() {
    // Update rotation matrix, which is needed to update orientation angles.
    SensorManager.getRotationMatrix(rotationMatrix, null,
        accelerometerReading, magnetometerReading);

    // "rotationMatrix" now has up-to-date information.

    SensorManager.getOrientation(rotationMatrix, orientationAngles);

    // "orientationAngles" now has up-to-date information.
    Log.e(TAG, "roll = " + orientationAngles[2]);
  }

  @Override
  public void onFlushCompleted(Sensor sensor) {

  }
}