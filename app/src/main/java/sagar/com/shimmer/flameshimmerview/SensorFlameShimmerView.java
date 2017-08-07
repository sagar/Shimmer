package sagar.com.shimmer.flameshimmerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import sagar.com.shimmer.R;

public class SensorFlameShimmerView extends AppCompatImageView implements SensorEventListener {

  private static final String TAG = SensorFlameShimmerView.class.getSimpleName();
  private final int gold1, gold2, gold3, gold4, gold5, gold6, gold7;
  private final float positions[] = new float[7];
  private float rollAngle = 0;
  private Canvas tempCanvas;
  private Bitmap bitmap;
  private float[] rotationMatrix = new float[9];
  private float[] accelerometerReading = new float[3];
  private float[] magnetometerReading = new float[3];
  private float[] orientationAngles = new float[3];
  private final float angle = 50.0f;
  private float endX;
  private float endY;

  public SensorFlameShimmerView(Context context) {
    this(context, null);
  }

  public SensorFlameShimmerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    gold1 = getResources().getColor(R.color.gold1);
    gold2 = getResources().getColor(R.color.gold2);
    gold3 = getResources().getColor(R.color.gold3);
    gold4 = getResources().getColor(R.color.gold4);
    gold5 = getResources().getColor(R.color.gold5);
    gold6 = getResources().getColor(R.color.gold6);
    gold7 = getResources().getColor(R.color.gold7);

    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    // draws flame
    bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
    tempCanvas = new Canvas(bitmap);
    super.onDraw(tempCanvas);

    computeAngle(getWidth());

    // draws gradient
    final Paint paint = new Paint();
    final Shader gradient = new LinearGradient(0, 0, endX, endY, getColorList(), null, TileMode.CLAMP);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
    paint.setShader(gradient);
    tempCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    canvas.drawBitmap(bitmap, 0, 0, null);
  }

  private void computeAngle(int width) {
    double angleInRadians = Math.toRadians(angle);
    endX = (float) (Math.cos(angleInRadians) * width);
    endY = (float) (Math.sin(angleInRadians) * width);
  }

  public int[] getColorList() {
    return new int[] { gold1, gold2, gold3, gold4, gold5, gold6, gold7 };
  }

  public float[] updateColorPositions() {
    positions[0] += rollAngle;
    for (int i = 1; i < positions.length; i++) {
      positions[i] = positions[i-1] + 0.2f;
      if (positions[i] >= 1) {
        positions[i] = 0;
      }
    }
    return positions;
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    switch (sensorEvent.sensor.getType()) {
      case Sensor.TYPE_ACCELEROMETER: System.arraycopy(sensorEvent.values, 0, accelerometerReading, 0, accelerometerReading.length); break;
      case Sensor.TYPE_MAGNETIC_FIELD: System.arraycopy(sensorEvent.values, 0, magnetometerReading, 0, magnetometerReading.length); break;
    }
    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer and update rotation matrix,
    // which is needed to update orientation angles.
    SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);

    // "rotationMatrix" now has up-to-date information.
    SensorManager.getOrientation(rotationMatrix, orientationAngles);

    rollAngle = orientationAngles[2];
    Log.e(TAG, "rollAngle: " + rollAngle);
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }
}
