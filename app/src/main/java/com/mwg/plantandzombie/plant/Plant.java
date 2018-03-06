package com.mwg.plantandzombie.plant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;

/**
 * Created by mwg on 2018/3/5.
 */

public class Plant extends android.support.v7.widget.AppCompatImageView {

    Bitmap plantBitmap;
    public boolean isLive = false;
    Context context;
    MyCallBack myCallBack;
    public int costSunCount;
    public int HP;
    Timer fireTimer;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public Plant(Context context) {
        super(context);
        HP = 50;
        this.context = context;
        setScaleType(ScaleType.FIT_XY);
    }

    public void beginAnimation() {
        AnimationDrawable ad = new AnimationDrawable();
        int w = plantBitmap.getWidth() / 8;
        //从某种僵尸的连图中，分别截取子图片，并将其添加至AnimationDrawable中
        for (int i = 0; i < 8; i++) {
            Bitmap subBitmap = Bitmap.createBitmap(plantBitmap, i * w, 0, w, plantBitmap.getHeight());
            BitmapDrawable bd = new BitmapDrawable(getResources(), subBitmap);
            ad.addFrame(bd, 200);
        }
        //让动画重复滚动播放（其中true代表8帧图片只播放一次，false代表循环播放）
        ad.setOneShot(false);
        setImageDrawable(ad);
        //开始做动画
        ad.start();
    }

    public void beginFire() {
    }

    public void deadAction(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //断绝坑和植物的关系，否则坑里不能再次添加植物
                FrameLayout boxfl = (FrameLayout) getTag();
                boxfl.setTag(null);
                myCallBack.deletePlant(Plant.this);
            }
        });
    }

    public interface MyCallBack {
        public abstract void addSunCount(int count);
        public abstract void deletePlant(Plant plant);

    }
}
