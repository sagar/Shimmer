package sagar.com.shimmer.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import sagar.com.shimmer.R;

public class PorterDuffLayout extends FrameLayout {

  public PorterDuffLayout(@NonNull Context context) {
    super(context);
  }

  public PorterDuffLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    porterDuff(canvas, Mode.DST_OUT);
  }

  private void porterDuff(Canvas canvas, Mode mode) {
    Paint paint = new Paint();

    Bitmap destinationBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flame);
    canvas.drawBitmap(destinationBitmap, 0, 0, paint);

    paint.setXfermode(new PorterDuffXfermode(mode));

    Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sagar);
    canvas.drawBitmap(originBitmap, 0, 0, paint);
  }
}
