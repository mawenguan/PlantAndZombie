package com.mwg.plantandzombie.zombie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mwg.plantandzombie.plant.Plant;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mwg on 2018/3/5.
 */

public class Zomb extends android.support.v7.widget.AppCompatImageView {

    Bitmap zombBitmap;
    public float speed;
    public float oldSpeed;
    public int HP;

    public Zomb(Context context) {
        super(context);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(100, 150);
        setLayoutParams(lp);
    }

    public void beginAnimation() {
        AnimationDrawable ad = new AnimationDrawable();
        int w = zombBitmap.getWidth() / 8;
        //从某种僵尸的连图中，分别截取子图片，并将其添加至AnimationDrawable中
        for (int i = 0; i < 8; i++) {
            Bitmap subBitmap = Bitmap.createBitmap(zombBitmap, i * w, 0, w, zombBitmap.getHeight());
            BitmapDrawable bd = new BitmapDrawable(getResources(), subBitmap);
            ad.addFrame(bd, 100);
        }
        //让动画重复滚动播放（其中true代表8帧图片只播放一次，false代表循环播放）
        ad.setOneShot(false);
        setImageDrawable(ad);
        //开始做动画
        ad.start();
        //手动释放图片资源
        zombBitmap.recycle();
    }

    public void eatPlant(final Plant plant) {
        oldSpeed = speed;
        speed = 0;
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                plant.HP--;
                if (plant.HP <= 0) {
                    speed = oldSpeed;
                    if (plant != null) {
                        plant.deadAction();
                    }
                    t.cancel();
//                    cancel();//取消Timer即可，TimerTask可以不加（取消Timer即也取消了TimerTask）
                }
            }
        };
        t.schedule(tt, 0, 100);
    }
}
