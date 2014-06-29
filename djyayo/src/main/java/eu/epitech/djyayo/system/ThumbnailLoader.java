package eu.epitech.djyayo.system;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Loads a thumbnail from URL in background, and changes a ImageView's drawable when it's done.
 */
public class ThumbnailLoader {

    private String dst;
    private ImageView imageView;

    public ThumbnailLoader(ImageView imageView) {
        this.imageView = imageView;
    }

    public void load(String url, String dst) {
        this.dst = dst;
        new DownloadThumbnailTask().execute(url);
    }

    private class DownloadThumbnailTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                InputStream is = (new URL(params[0])).openStream();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dst));

                byte buf[] = new byte[4096];
                int bytes_read;
                while ((bytes_read = is.read(buf)) > 0) {
                    bos.write(buf, 0, bytes_read);
                }
                is.close();
                bos.close();
                return dst;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("Loader", "Got!");
                Bitmap bitmap = BitmapFactory.decodeFile(result);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
