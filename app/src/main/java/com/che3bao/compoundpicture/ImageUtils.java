package com.che3bao.compoundpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by haibo on 2015/12/25.
 */
public class ImageUtils {

    public interface OnBitmapWorkFinish{
        public void ok(String url, Bitmap bitmap);

        public void error(String error);
    }

    public static void savaPicture(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory()
                + File.separator + System.currentTimeMillis() + ".jpg";
        Log.i("ImageCompoundActivity",path);
        File file = new File(path);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBitmap(final String url,final OnBitmapWorkFinish response) {
        final Handler handler=new Handler();
        new Thread(){
            @Override
            public void run() {
                super.run();
                boolean isNetWorkOk=false;
                InputStream inputStream = null;
                try {
                    URL imageUrl = new URL(url);
                    HttpURLConnection httpURLConnections = (HttpURLConnection) imageUrl
                            .openConnection();
                    httpURLConnections.connect();
                    httpURLConnections.setConnectTimeout(5000);
                    if(httpURLConnections.getResponseCode()==HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnections.getInputStream();
                        Log.i("ImageCompoundActivity","ok");
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                response.ok(url,bitmap);
                            }
                        });
                        inputStream.close();
                        isNetWorkOk=true;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(!isNetWorkOk){
                        response.error("请检查你的网络连接......");
                    }
                }
            }
        }.start();
    }

    public static Bitmap zoomImage(Bitmap bitmap, double newWidth,
                                   double newHeight) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        return resultBitmap;
    }

    public static Bitmap compoundPicture(Bitmap backgroundBitmap,Bitmap imageBitmap){
        Bitmap resultBitmap = Bitmap.createBitmap(600, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(zoomImage(backgroundBitmap,600,800),0,0,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        if(imageBitmap!=null){
            canvas.drawBitmap(zoomImage(imageBitmap,550,390), 22, 202, paint);
        }
        return resultBitmap;
    }
}
