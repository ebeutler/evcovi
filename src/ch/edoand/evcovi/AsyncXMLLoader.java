package ch.edoand.evcovi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class AsyncXMLLoader extends AsyncTask<Void, Void, List<DisplayEvent>> {
  private final String LOG_TAG = "MainActivity.AsyncHttpGet";
  private final String FIXED_URL_PARAMS = "xpage=celements_ajax&ajax_mode=XMLexport";
  private final long START = 0; //all starting now
  
  private MainActivity parentActivity;
  
  public AsyncXMLLoader(MainActivity parentActivity) {
    super();
    this.parentActivity = parentActivity;
  }
  
  @Override
  @SuppressLint("SimpleDateFormat")
  protected List<DisplayEvent> doInBackground(Void... params) {
    Map<String, Object> preferences = parentActivity.getPrefs();
    String username = "&username=" + preferences.get(SettingsActivity.SETTINGS_DATA_ID);
    String token = "&token=" + preferences.get(SettingsActivity.SETTINGS_DATA_TOKEN);
    long now = new Date().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String startDate = "&startDate=" + dateFormat.format(new Date(now + START));
    Integer loadDays = (Integer)preferences.get(SettingsActivity.SETTINGS_DATA_LOAD_DAYS);
    if(loadDays == null) {
      loadDays = 3;
    }
    long preloadTimeDiff = 24 * 3600 * 1000 * loadDays;
    String endDate = "&endDate=" + dateFormat.format(new Date(now + preloadTimeDiff));
    String baseUrl = preferences.get(SettingsActivity.SETTINGS_DATA_URL).toString();
    String requestUrl = ((!baseUrl.startsWith("http(s)?://"))?"http://":"") + baseUrl;
    requestUrl += (requestUrl.indexOf("?") < 0)?"?":"&";
    requestUrl += FIXED_URL_PARAMS + username + token + startDate + endDate;
    Log.d(LOG_TAG, "Request URL: [" + requestUrl + "]");
    XMLResponseHandler xmlHandler = new XMLResponseHandler();
    HttpGet request = new HttpGet(requestUrl);
    AndroidHttpClient client = AndroidHttpClient.newInstance("");
    List<DisplayEvent> events = Collections.emptyList();
    try {
      events = client.execute(request, xmlHandler);
    } catch (ClientProtocolException cpe) {
      Log.e(LOG_TAG, "ClientProtocolException trying to get data XML", cpe);
    } catch (IOException ioe) {
      Log.e(LOG_TAG, "IOException trying to get data XML", ioe);
    }
    if(events.isEmpty()) {
      parentActivity.getHandler().sendMessage(parentActivity.getHandler(
          ).obtainMessage(MainActivity.TOAST_MSG, 
              parentActivity.getString(R.string.no_network_response)));
    }
    if(client != null) {
      client.close();
    }
    return events;
  }

  @Override
  protected void onPostExecute(List<DisplayEvent> events) {
    Map<Long, DisplayEvent> loadedEvents = parentActivity.getEvents();
    boolean initialLoad = (loadedEvents == null) || (loadedEvents.isEmpty());
    if(events.size() > 0) {
      int newEvents = 0;
      for(DisplayEvent event : events) {
        if(!loadedEvents.containsKey(event.getId())) {
          loadedEvents.put(event.getId(), event);
          newEvents++;
        }
      }
      Handler handler = parentActivity.getHandler();
      if(newEvents > 0) {
        handler.sendMessage(handler.obtainMessage(MainActivity.TOAST_MSG, 
            String.format(parentActivity.getString(R.string.new_events_available), 
                Integer.toString(newEvents))));
        if(initialLoad) {
          handler.sendMessage(handler.obtainMessage(MainActivity.NEW_DATA_AVAILABLE));
        }
      } else {
        handler.sendMessage(handler.obtainMessage(MainActivity.TOAST_MSG, 
            parentActivity.getString(R.string.no_new_events_available)));
      }
    } else {
      Log.i(LOG_TAG, "Result list is empty.");
    }
  }
}
