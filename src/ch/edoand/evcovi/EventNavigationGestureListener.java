package ch.edoand.evcovi;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class EventNavigationGestureListener extends SimpleOnGestureListener {
  private MainActivity parentActivity;

  public EventNavigationGestureListener(MainActivity parentActivity) {
    this.parentActivity = parentActivity;
  }

  @Override
  public boolean onFling(MotionEvent ev1, MotionEvent ev2, float veloX, float veloY) {
    if ((veloX < -500.0) && (Math.abs(veloY) < (Math.abs(veloX) - 1000))) {
      parentActivity.moveThroughEventList(1);
    } else if((veloX > 500.0) && (Math.abs(veloY) < (Math.abs(veloX) - 1000))) {
      parentActivity.moveThroughEventList(-1);
    }
    Log.i("BLATEST", veloX + " : " + veloY);
    return true;
  }
}
