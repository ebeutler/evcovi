package ch.edoand.evcovi;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class EventNavigationGestureListener extends SimpleOnGestureListener {
  private MainActivity parentActivity;

  public EventNavigationGestureListener(MainActivity parentActivity) {
    this.parentActivity = parentActivity;
  }

  @Override
  public boolean onFling(MotionEvent ev1, MotionEvent ev2, float veloX, float veloY) {
    if ((veloX < -50.0) && (Math.abs(veloY) < 20)) {
      parentActivity.moveThroughEventList(1);
    } else if((veloX > 50.0) && (Math.abs(veloY) < 20)) {
      parentActivity.moveThroughEventList(-1);
    }
    return true;
  }
}
