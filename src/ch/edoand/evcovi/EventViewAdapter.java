package ch.edoand.evcovi;

import java.util.TreeMap;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class EventViewAdapter extends TreeMap<String, DisplayEvent> implements Adapter {
  private static final long serialVersionUID = 5802572328586299351L;
  
  private int ID_TITLE = 0;
  private int ID_DATE = 1;
  private int ID_DESCRIPTION = 2;
  private int ID_IMAGE = 3;
  private int ID_VIDEO = 4;
  //ID_TEXT_LOCATION = 5;
  //ID_TEXT_ORGANISER = 6;
  private int ID_LOC_NAME = 7;
  private int ID_ORG_NAME = 8;
  private int ID_LOC_STREET = 9;
  private int ID_ORG_PHONE = 10;
  private int ID_LOC_ZIP = 11;
  private int ID_LOC_CITY = 12;
  private int ID_ORG_EMAIL = 13;
  private int ID_LOC_PHONE = 14;
  private int ID_ORG_WEB = 15;
  private int ID_LOC_EMAIL = 16;
  private int ID_LOC_WEB = 17;

  private MainActivity parentActivity;
  
  public EventViewAdapter(MainActivity parent) {
    parentActivity = parent;
  }
  
  @Override
  public int getCount() {
    return size();
  }

  @Override
  public DisplayEvent getItem(int position) {
    for(String key : navigableKeySet()) {
      if(position == 0) {
        return get(key);
      }
      position--;
    }
    return null;
  }

  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getItemViewType(int position) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    GridView slideView = (GridView) parentActivity.getLayoutInflater().inflate(
        R.layout.event_slide, parent);
    DisplayEvent item = getItem(position);
    ((TextView) slideView.getChildAt(ID_TITLE)).setText(item.getTitle());
    ((TextView) slideView.getChildAt(ID_DATE)).setText(item.getDateString());
    ((TextView) slideView.getChildAt(ID_DESCRIPTION)).setText(item.getDescription());
    ImageView imageView = (ImageView) slideView.getChildAt(ID_IMAGE);
    imageView.setTag(item.getImgUrl1to1());
    new AsyncImageGet().execute(imageView);
    ((TextView) slideView.getChildAt(ID_VIDEO)).setText(item.getVidUrl());
    ((TextView) slideView.getChildAt(ID_LOC_NAME)).setText(item.getLocName());
    ((TextView) slideView.getChildAt(ID_ORG_NAME)).setText(item.getOrgName());
    ((TextView) slideView.getChildAt(ID_LOC_STREET)).setText(item.getLocStreet());
    ((TextView) slideView.getChildAt(ID_ORG_PHONE)).setText(item.getOrgPhone());
    ((TextView) slideView.getChildAt(ID_LOC_ZIP)).setText(item.getLocZip());
    ((TextView) slideView.getChildAt(ID_LOC_CITY)).setText(item.getLocCity());
    ((TextView) slideView.getChildAt(ID_ORG_EMAIL)).setText(item.getOrgEmail());
    ((TextView) slideView.getChildAt(ID_LOC_PHONE)).setText(item.getLocPhone());
    ((TextView) slideView.getChildAt(ID_ORG_WEB)).setText(item.getOrgWeb());
    ((TextView) slideView.getChildAt(ID_LOC_EMAIL)).setText(item.getLocEmail());
    ((TextView) slideView.getChildAt(ID_LOC_WEB)).setText(item.getLocWeb());
    return slideView;
  }

  @Override
  public int getViewTypeCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean hasStableIds() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty();
  }

  @Override
  public void registerDataSetObserver(DataSetObserver observer) {
    // TODO Auto-generated method stub

  }

  @Override
  public void unregisterDataSetObserver(DataSetObserver observer) {
    // TODO Auto-generated method stub

  } 
}
