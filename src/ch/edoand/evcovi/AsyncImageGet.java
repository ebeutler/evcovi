package ch.edoand.evcovi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AsyncImageGet extends AsyncTask<ImageView, Void, byte[]> {
  private final String LOG_TAG = "MainActivity.AsyncImageGet";
  
  ImageView imageView;
  
  @Override
  protected byte[] doInBackground(ImageView... imgViews) {
    if((imgViews != null) && imgViews.length > 0) {
      imageView = imgViews[0];
      InputStream in = null;
      BufferedOutputStream out = null;
      try {
        in = new BufferedInputStream(new URL(imageView.getTag().toString()).openStream());
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        out = new BufferedOutputStream(dataStream);
        IOUtils.copy(in, out);
        out.flush();
        return dataStream.toByteArray();
      } catch (MalformedURLException mue) {
        Log.e(LOG_TAG, "Malformed URL: [" + imageView + "]", mue);
      } catch (IOException ioe) {
        Log.e(LOG_TAG, "Could not download image: [" + imageView + "]", ioe);
      } finally {
        if(in != null) {
          try {
            in.close();
          } catch (IOException ioe) {
            Log.e(LOG_TAG, "Could not close in stream", ioe);
          }
        }
        if(out != null) {
          try {
            out.close();
          } catch (IOException ioe) {
            Log.e(LOG_TAG, "Could not close out stream", ioe);
          }
        }
      }
    }
    return null;
  }
  

  @Override
  protected void onPostExecute(byte[] data) {
    if(data != null) {
      Bitmap bitmap = null;
      bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, 
          new BitmapFactory.Options());
      imageView.setImageBitmap(bitmap);
    }
  }
}
