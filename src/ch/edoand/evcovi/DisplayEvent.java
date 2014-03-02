package ch.edoand.evcovi;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class DisplayEvent {
  private long id;
  private String title;
  private String description;
  private Date startDate;
  private Date endDate;
  private String locName;
  private String locStreet;
  private String locZip;
  private String locCity;
  private String orgPhone;
  private String orgEmail;
  private String orgWeb;
  private String imgUrl1to1;
  
  public DisplayEvent(String id) {
    this.id = Long.parseLong(id);
  }
  
  public DisplayEvent(int id) {
    this.id = id;
  }
  
  public long getId() {
    return id;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Date getStartDate() {
    return startDate;
  }
  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
  public Date getEndDate() {
    return endDate;
  }
  
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  
  //TODO can't show events ending on different day than starting properly.
  @SuppressLint("SimpleDateFormat")
  public CharSequence getDateString() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE d. MMMM yyyy");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    String timeAndEndDate = "";
    if(!"00:00".equals(sdfTime.format(getStartDate()))) {
      timeAndEndDate += sdfTime.format(getStartDate());
    }
    if(getEndDate() != null) {
      timeAndEndDate += " - ";
//      if(!sdfDate.format(getStartDate()).equals(sdfDate.format(getEndDate()))) {
//        timeAndEndDate += sdfDate.format(getEndDate());
//      }
      if(!"00:00".equals(sdfTime.format(getEndDate()))) {
        timeAndEndDate += sdfTime.format(getEndDate());
      }
    }
    return timeAndEndDate + ", " + sdfDate.format(getStartDate());
  }
  
  public String getLocName() {
    return locName;
  }
  
  public void setLocName(String locName) {
    this.locName = locName;
  }
  
  public String getLocStreet() {
    return locStreet;
  }
  
  public void setLocStreet(String locAddress) {
    this.locStreet = locAddress;
  }
  
  public String getLocZip() {
    return locZip;
  }
  
  public void setLocZip(String locZip) {
    this.locZip = locZip;
  }
  
  public String getLocCity() {
    return locCity;
  }
  
  public void setLocCity(String locCity) {
    this.locCity = locCity;
  }
  
  public String getOrgPhone() {
    return orgPhone;
  }
  
  public void setOrgPhone(String orgPhone) {
    this.orgPhone = orgPhone;
  }
  
  public String getOrgEmail() {
    return orgEmail;
  }
  
  public void setOrgEmail(String orgEmail) {
    this.orgEmail = orgEmail;
  }
  
  public String getOrgWeb() {
    return orgWeb;
  }
  
  public void setOrgWeb(String orgWeb) {
    this.orgWeb = orgWeb;
  }
  
  public String getImgUrl1to1() {
    return imgUrl1to1;
  }
  
  public void setImgUrl1to1(String imgUrl1to1) {
    this.imgUrl1to1 = imgUrl1to1;
  }
  
  @Override
  public String toString() {
    return "[" + getTitle() + "] [" + getDescription() + "] [" + getStartDate() + "] [" 
        + getEndDate() + "] [" + getLocName() + "] [" + getLocStreet() + "] [" 
        + getLocZip() + "] [" + getLocCity() + "] [" + getOrgPhone()  + "] [" 
        + getOrgEmail() + "] [" + getOrgWeb() + "] [" + getImgUrl1to1() + "]";
  }
}
