package com.mwg.plantandzombie.plant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import com.mwg.plantandzombie.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mwg on 2018/3/5.
 */

public class Pea extends Plant {

    public ArrayList<ImageView> bullets = new ArrayList<ImageView>();

    Bitmap bulletBitmap;

    public Pea(Context context) {
        super(context);
        costSunCount = 100;
        bulletBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bullet_0);
        plantBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.plant_2);
    }

    //豌豆射手开始发射子弹
    @Override
    public void beginFire() {

        fireTimer = new Timer();
        fireTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView bullet = new ImageView(context);
                        bullet.setImageBitmap(bulletBitmap);

                        AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(
                                30, 30, (int) getX() + 60, (int) getY() + 10);
                        AbsoluteLayout layout = (AbsoluteLayout) getParent();
                        layout.addView(bullet, lp);
                        bullets.add(bullet);
                    }
                });
            }
        }, 0, 1000);

    }

    @Override
    public void deadAction() {
        //当豌豆射手死掉的时候，发射子弹的Timer也要死掉
        fireTimer.cancel();
        //且已经发射出去的子弹要自动清除
        for (ImageView bullet : bullets) {
            ((ViewGroup)bullet.getParent()).removeView(bullet);
        }
        super.deadAction();
    }
}
