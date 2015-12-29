package com.che3bao.compoundpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mCarImage;
    private ImageView mResultImage;
    private FrameLayout mFrameLayout;
    private LinearLayout mLinearLayout;
    private Bitmap compoundPicture;
    private  Bitmap carBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initImageView();
        Bitmap backgroundBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.template13);
        compoundPicture = getCompoundPicture(backgroundBitmap);
        mResultImage.setImageBitmap(compoundPicture);
    }

    private void getCarBitmap(String url) {
        ImageUtils.getBitmap(url, new ImageUtils.OnBitmapWorkFinish() {
            @Override
            public void ok(String url, Bitmap bitmap) {
                //carBitmap = bitmap;
            }
            @Override
            public void error(String error) {
                Log.i("ImageCompoundActivity", error);
            }
        });
    }

    private void initView() {
        mCarImage= (ImageView) findViewById(R.id.image);
        mResultImage= (ImageView) findViewById(R.id.resultImage);
        mFrameLayout= (FrameLayout) findViewById(R.id.frameLayout);
        mLinearLayout= (LinearLayout) findViewById(R.id.linearLayout);
    }

    private Bitmap getCompoundPicture(Bitmap backgroundBitmap){
        carBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.car_iamge);
        mCarImage.setImageBitmap(carBitmap);

        Bitmap bitmap =ImageUtils.compoundPicture(backgroundBitmap, carBitmap);
        return bitmap;
    }

    private void saveImages(Bitmap resultBitmap){
        if(resultBitmap!=null){
            ImageUtils.savaPicture(resultBitmap);
            Toast.makeText(this, "图片已保存", Toast.LENGTH_SHORT).show();
        }
    }

    private void initImageView(){

        int [] images=new int[]{R.drawable.tmeplate10,R.drawable.template11,
                R.drawable.template12,R.drawable.template13};
        for (int i=0;i<images.length;i++){
            ImageView imageView=new ImageView(this);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(dipToPx(100),dipToPx(80));
            params.setMargins(10,10,10,10);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(images[i]);
            imageView.setOnClickListener(this);
            mLinearLayout.addView(imageView);
        }
    }

    public void onClick(View v) {
        ImageView imageView= (ImageView) v;
        Drawable drawable = imageView.getDrawable();
        mFrameLayout.setBackground(drawable);
        BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
        Bitmap bitmap=bitmapDrawable.getBitmap();
        mResultImage.setImageBitmap(getCompoundPicture(bitmap));

    }

    public int dipToPx( float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void savePicture(View v){
        saveImages(compoundPicture);
    }
}

