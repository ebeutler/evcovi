package ch.edoand.evcovi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.util.Log;

public class XMLResponseHandler implements ResponseHandler<List<DisplayEvent>> {
  private final String LOG_TAG = "XMLResponseHandler";

  @SuppressLint("SimpleDateFormat")
  DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
  
  private final String EMPTY = "empty";
  private final String EVENT = "event";
  private final String TITLE = "title";
  private final String DESCRIPTION = "description";
  private final String START_DATE = "start-date-time";
  private final String END_DATE = "end-date-time";
  private final String LOCATION = "location";
  private final String ORGANISER = "organiser";
  private final String NAME = "name";
  private final String STREET = "street";
  private final String ZIP = "zip";
  private final String CITY = "city";
  private final String PHONE = "phone";
  private final String EMAIL = "email";
  private final String WEB = "web";
  private final String IMAGE = "image";
  private final String IMAGE_ASP_RATIO = "1:1";
  
  private List<DisplayEvent> events;
  private DisplayEvent currentEvent;
  private String currentTag;
  private boolean inLocation = false;
  private boolean inOrganiser = false;
  
  @Override
  public List<DisplayEvent> handleResponse(HttpResponse response)
      throws ClientProtocolException, IOException {
    events = new ArrayList<DisplayEvent>();
    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      XmlPullParser xpp = factory.newPullParser();
      xpp.setInput(new InputStreamReader(response.getEntity().getContent())); 
      int eventType = xpp.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG) {
          startTag(xpp);
        } else if (eventType == XmlPullParser.END_TAG) {
          endTag(xpp.getName());
        } else if (eventType == XmlPullParser.TEXT) {
          text(xpp.getText());
        }
        eventType = xpp.next();
      }
    } catch (XmlPullParserException xppe) {
      Log.e(LOG_TAG, "Exception while parsing XML result", xppe);
    }
    return events;
  }

  void startTag(XmlPullParser xpp) {
    String tag = xpp.getName();
    if(EVENT.equalsIgnoreCase(tag)) {
      currentEvent = new DisplayEvent(xpp.getAttributeValue(null, "id"));
    } else if(LOCATION.equalsIgnoreCase(tag)) {
      inLocation = true;
    } else if(ORGANISER.equalsIgnoreCase(tag)) {
      inOrganiser = true;
    } else if(IMAGE.equalsIgnoreCase(tag) && IMAGE_ASP_RATIO.equalsIgnoreCase(
        xpp.getAttributeValue(null, "asp-ratio"))) {
      currentTag = IMAGE + IMAGE_ASP_RATIO;
    } else if(EMPTY.equalsIgnoreCase(tag)){
      Log.d(LOG_TAG, "Result XML empty (no events)");
    } else {
      currentTag = tag;
    }
  }

  void endTag(String tag) {
    if(EVENT.equalsIgnoreCase(tag)) {
      events.add(currentEvent);
      Log.i(LOG_TAG, "Added event: " + currentEvent.toString());
      currentEvent = null;
    } else if(LOCATION.equalsIgnoreCase(tag)) {
      inLocation = false;
    } else if(ORGANISER.equalsIgnoreCase(tag)) {
      inOrganiser = false;
    } else {
      currentTag = null;
    }
  }

  void text(String text) {
    if(TITLE.equalsIgnoreCase(currentTag)) {
      currentEvent.setTitle(text);
    } else if(DESCRIPTION.equalsIgnoreCase(currentTag)) {
      currentEvent.setDescription(text);
    } else if(START_DATE.equalsIgnoreCase(currentTag)) {
      currentEvent.setStartDate(parseDate(text));
    } else if(END_DATE.equalsIgnoreCase(currentTag)) {
      currentEvent.setEndDate(parseDate(text));
    } else if(inLocation && NAME.equalsIgnoreCase(currentTag)) {
      currentEvent.setLocName(text);
    } else if(inLocation && STREET.equalsIgnoreCase(currentTag)) {
      currentEvent.setLocStreet(text);
    } else if(inLocation && ZIP.equalsIgnoreCase(currentTag)) {
      currentEvent.setLocZip(text);
    } else if(inLocation && CITY.equalsIgnoreCase(currentTag)) {
      currentEvent.setLocCity(text);
    } else if(inOrganiser && PHONE.equalsIgnoreCase(currentTag)) {
      currentEvent.setOrgPhone(text);
    } else if(inOrganiser && EMAIL.equalsIgnoreCase(currentTag)) {
      currentEvent.setOrgEmail(text);
    } else if(inOrganiser && WEB.equalsIgnoreCase(currentTag)) {
      currentEvent.setOrgWeb(text);
    } else if((IMAGE + IMAGE_ASP_RATIO).equalsIgnoreCase(currentTag)) {
      currentEvent.setImgUrl1to1(text);
    }
  }

  Date parseDate(String text) {
    try {
      return DATE_FORMAT.parse(text);
    } catch (ParseException pe) {
      Log.e(LOG_TAG, "Exception parsing date from [" + text + "]", pe);
    }
    return null;
  }
}
