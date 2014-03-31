package ch.edoand.evcovi;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
  public static final int TOAST_MSG = 1;
  public static final int NEW_DATA_AVAILABLE = 2;
  public static final String REQUEST_CODE = "requestCode";
  public static final int PREFS_LOAD = 101;
  public static final int PREFS_EDIT = 102;
  
  private Map<String, Object> preferences;
  private GestureDetector gestureDetector;
  private int eventPosOnDisplay;
  private Toast slideNumberToast;
  private EventViewAdapter dataAdapter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    Intent prefsIntent = new Intent(this, SettingsActivity.class);
    prefsIntent.putExtra(REQUEST_CODE, PREFS_LOAD);
    startActivityForResult(prefsIntent, PREFS_LOAD);
    //TODO Queer Layout und / oder in Kombi mit List View
    setContentView(R.layout.activity_main);
    AdapterViewFlipper flipper = (AdapterViewFlipper) findViewById(R.id.evAdapterViewFlipper);
//    flipper.setAdapter(new ArrayAdapter<DisplayEvent>(this, R.layout.event_slide, 
//        new ArrayList<DisplayEvent>(loadedEvents.values())));
    dataAdapter = new EventViewAdapter(this);
    flipper.setAdapter(dataAdapter);
    
    gestureDetector = new GestureDetector(this, new EventNavigationGestureListener(this));
    
    eventPosOnDisplay = 0;
    //TODO load saved data if available
    
    //TODO settings verbessern siehe DataManagementPreferenceActivity
    //TODO mail und web adressen verlinken
    //TODO datum mit kalender verlinken
    //TODO adressen auf maps verlinken
    //TODO video links erlauben
    //TODO einstellungen daten automatisch laden bei start ja / nein
    //TODO gestures zum "abkreuzen" von events die man nicht mehr sehen will oder haken
    //       von favoriten
    //TODO alarm der regelmaessig Daten holt mit notification wenn inaktiv etc.
    //TODO geofence alarm wenn man in area von event kommt

//    FLIPPER EXAMPLE
//    setContentView(R.layout.event_slide);
//    AdapterViewFlipper flipper = (AdapterViewFlipper) findViewById(R.id.adapterViewFlipper1);
//    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, 
//        new String[] { "s 1", "s 2", "s 3", "s 4", "s 5" });
//    flipper.setAdapter(adapter);
//    flipper.startFlipping();
//    flipper.stopFlipping();
//    flipper.showNext();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return gestureDetector.onTouchEvent(event);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menuSettings:
        Intent prefsIntent = new Intent(this, SettingsActivity.class);
        prefsIntent.putExtra(REQUEST_CODE, PREFS_EDIT);
        startActivityForResult(prefsIntent, PREFS_EDIT);
        return true;
      case R.id.menuForceDownload:
        Toast.makeText(this, R.string.data_request_started, Toast.LENGTH_LONG).show();
        new AsyncXMLLoader(this).execute();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if((resultCode == RESULT_OK) && ((requestCode == PREFS_EDIT) 
        || (requestCode == PREFS_LOAD))) {
      if(preferences == null) {
        preferences = new HashMap<String, Object>();
      }
      preferences.put(SettingsActivity.SETTINGS_DATA_URL, data.getExtras().getString(
          SettingsActivity.SETTINGS_DATA_URL));
      preferences.put(SettingsActivity.SETTINGS_DATA_ID, data.getExtras().getString(
          SettingsActivity.SETTINGS_DATA_ID));
      preferences.put(SettingsActivity.SETTINGS_DATA_TOKEN, data.getExtras().getString(
          SettingsActivity.SETTINGS_DATA_TOKEN));
      preferences.put(SettingsActivity.SETTINGS_DATA_LOAD_DAYS, data.getExtras().getInt(
          SettingsActivity.SETTINGS_DATA_LOAD_DAYS));

      new AsyncXMLLoader(this).execute();
    }
  }
  
  @Override
  public void onResume() {
    super.onResume();
    displayEvent();
  }
  
  @Override
  public void onPause() {
    super.onPause();
    stopDisplay();
  }
  
  void moveThroughEventList(int distance) {
    eventPosOnDisplay += distance;
    displayEvent();
  }
  
  void displayEvent() {
    //TODO animate change
    if(dataAdapter.getCount() > 0) {
      validateDisplayedEventId();
      
      
      
      
      //TODO automatisch?
//      flipper.
//      display((DisplayEvent)loadedEvents.values().toArray()[eventPosOnDisplay]);
      if(slideNumberToast != null) {
        //prevent queueing of toasts when going through slides fast
        slideNumberToast.cancel();
      }
      slideNumberToast = Toast.makeText(this, (eventPosOnDisplay + 1) + " / " 
          + dataAdapter.getCount(), Toast.LENGTH_SHORT);
      slideNumberToast.show();
    }
  }
  
  void validateDisplayedEventId() {
    int eventsSize = dataAdapter.getCount();
    if(eventPosOnDisplay < 0) {
      eventPosOnDisplay = eventsSize - (Math.abs(eventPosOnDisplay) % eventsSize);
    } else if(eventPosOnDisplay >= eventsSize) {
      eventPosOnDisplay %= eventsSize;
    }
  }
  
  void stopDisplay() {
    // TODO if there is a slideshow mode stop it when pausing
    
  }
  
  void display(DisplayEvent event) {
    ((TextView) findViewById(R.id.evTitleView)).setText(event.getTitle());
    ((TextView) findViewById(R.id.evDescriptionView)).setText(event.getDescription());
    ((TextView) findViewById(R.id.evDateView)).setText(event.getDateString());
    ((TextView) findViewById(R.id.evVideoView)).setText(event.getVidUrl());
    ((TextView) findViewById(R.id.evLocNameView)).setText(event.getLocName());
    ((TextView) findViewById(R.id.evLocStreetView)).setText(event.getLocStreet());
    ((TextView) findViewById(R.id.evLocZipView)).setText(event.getLocZip());
    ((TextView) findViewById(R.id.evLocCityView)).setText(event.getLocCity());
    ((TextView) findViewById(R.id.evLocPhoneView)).setText(event.getLocPhone());
    ((TextView) findViewById(R.id.evLocEmailView)).setText(event.getLocEmail());
    ((TextView) findViewById(R.id.evLocWebView)).setText(event.getLocWeb());
    ((TextView) findViewById(R.id.evOrgNameView)).setText(event.getOrgName());
    ((TextView) findViewById(R.id.evOrgPhoneView)).setText(event.getOrgPhone());
    ((TextView) findViewById(R.id.evOrgEmailView)).setText(event.getOrgEmail());
    ((TextView) findViewById(R.id.evOrgWebView)).setText(event.getOrgWeb());
    ImageView imageView = (ImageView) findViewById(R.id.evImageView);
    imageView.setTag(event.getImgUrl1to1());;
    new AsyncImageGet().execute(imageView);
  }
  
  //Memory leak only temporary for delayed messages, until all messages are displayed.
  //So no delayed messages should mean not memory leak for more than a few seconds.
  @SuppressLint("HandlerLeak")
  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
      case TOAST_MSG: {
        Toast toast = Toast.makeText(MainActivity.this, msg.obj.toString(), 
            Toast.LENGTH_LONG);
        toast.show();
        break;
      }
      case NEW_DATA_AVAILABLE: {
        displayEvent();
        break;
      }
      }
    }
  };
  
  public Handler getHandler() {
    return handler;
  }

  public Map<String, Object> getPrefs() {
    return preferences;
  }

  public EventViewAdapter getDataAdapter() {
    return dataAdapter;
  }
}
