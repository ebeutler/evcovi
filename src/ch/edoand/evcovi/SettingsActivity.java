package ch.edoand.evcovi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
  public static final String SETTINGS_DATA_URL = "data_url";
  public static final String SETTINGS_DATA_ID = "data_id";
  public static final String SETTINGS_DATA_TOKEN = "data_token";
  public static final String SETTINGS_DATA_LOAD_DAYS = "data_load_days";
  
  private static final Integer[] loadDaysSpinnerData = 
      new Integer[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int requestCode = getIntent().getExtras().getInt(MainActivity.REQUEST_CODE);
    
    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
    String dataUrl = preferences.getString(SETTINGS_DATA_URL, "");
    String dataId = preferences.getString(SETTINGS_DATA_ID, "");
    String dataToken = preferences.getString(SETTINGS_DATA_TOKEN, "");
    Integer dataLoadDays = preferences.getInt(SETTINGS_DATA_LOAD_DAYS, 3);
    
    if(("".equals(dataUrl)) || (MainActivity.PREFS_EDIT == requestCode)) {
      initAndDisplayUI(dataUrl, dataId, dataToken, dataLoadDays);
    } else {
      prepareResultAndFinish(dataUrl, dataId, dataToken, dataLoadDays);
    }
  }

  void initAndDisplayUI(String dataUrl, String dataId, String dataToken,
      Integer dataLoadDays) {
    setContentView(R.layout.activity_settings);
    ArrayAdapter<Integer> loadDaysSpinnerAdapter = new ArrayAdapter<Integer>(this, 
        R.layout.spinner_item, loadDaysSpinnerData);
    ((Spinner) findViewById(R.id.sttDownloadDaysEdit)).setAdapter(loadDaysSpinnerAdapter);

    ((EditText) findViewById(R.id.sttUrlEdit)).setText(dataUrl);
    ((EditText) findViewById(R.id.sttIdEdit)).setText(dataId);
    ((EditText) findViewById(R.id.sttTokenEdit)).setText(dataToken);
    ((Spinner) findViewById(R.id.sttDownloadDaysEdit)).setSelection(dataLoadDays - 1);
    
    ((Button) findViewById(R.id.sttButtonCancel)).setOnClickListener(
        new OnClickListener() {
      @Override
      public void onClick(View v) {
        setResult(RESULT_CANCELED);
        finish();
      }
    });
    
    ((Button) findViewById(R.id.sttButtonReset)).setOnClickListener(
        new OnClickListener() {
      @Override
      public void onClick(View v) {
        resetForm();
      }
    });
    
    ((Button) findViewById(R.id.sttButtonSave)).setOnClickListener(
        new OnClickListener() {
      @Override
      public void onClick(View v) {
        Editor prefEdit = getPreferences(MODE_PRIVATE).edit();
        String dataUrl = ((EditText) findViewById(R.id.sttUrlEdit)).getText().toString();
        prefEdit.putString(SETTINGS_DATA_URL, dataUrl);
        String dataId = ((EditText) findViewById(R.id.sttIdEdit)).getText().toString();
        prefEdit.putString(SETTINGS_DATA_ID, dataId);
        String dataToken = ((EditText) findViewById(R.id.sttTokenEdit)).getText(
            ).toString();
        prefEdit.putString(SETTINGS_DATA_TOKEN, dataToken);
        Integer dataLoadDays = (Integer) ((Spinner) findViewById(R.id.sttDownloadDaysEdit)
            ).getSelectedItem();
        prefEdit.putInt(SETTINGS_DATA_LOAD_DAYS, dataLoadDays);
        prefEdit.commit();

        prepareResultAndFinish(dataUrl, dataId, dataToken, dataLoadDays);
      }
    });
  }

  void prepareResultAndFinish(String dataUrl, String dataId, String dataToken,
      Integer dataLoadDays) {
    Intent data = new Intent();
    data.putExtra(SETTINGS_DATA_URL, dataUrl);
    data.putExtra(SETTINGS_DATA_ID, dataId);
    data.putExtra(SETTINGS_DATA_TOKEN, dataToken);
    data.putExtra(SETTINGS_DATA_LOAD_DAYS, dataLoadDays);
    setResult(RESULT_OK, data);
    finish();
  }

  void resetForm() {
    ((EditText) findViewById(R.id.sttUrlEdit)).setText("");
    ((EditText) findViewById(R.id.sttIdEdit)).setText("");
    ((EditText) findViewById(R.id.sttTokenEdit)).setText("");
    ((Spinner) findViewById(R.id.sttDownloadDaysEdit)).setSelection(2, true);
  }
}
