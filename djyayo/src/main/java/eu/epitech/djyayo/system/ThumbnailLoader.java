package eu.epitech.djyayo.system;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

    private HttpURLConnection followRedirects(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
        int code;
        while ((code = connection.getResponseCode()) / 100 == 3) { // While redirect
            url = connection.getHeaderField("location");
            connection = (HttpURLConnection) (new URL(url)).openConnection();
        }
        return connection;
    }

    private class DownloadThumbnailTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection connection = followRedirects(params[0]);
                InputStream is = connection.getInputStream();
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
                Bitmap bitmap = BitmapFactory.decodeFile(result);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
