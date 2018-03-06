package com.mwg.plantandzombie.plant;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import com.mwg.plantandzombie.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mwg on 2018/3/5.
 */

public class SunFlower extends Plant {

    public SunFlower(Context context) {
        super(context);
        costSunCount = 50;
        plantBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.plant_0);
    }

    @Override
    public void beginFire() {
        //让向日葵实现各5秒产生阳光的功能
        fireTimer = new Timer();
        fireTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //由于添加阳光修改界面，所以要回到主线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //1、每隔5秒后添加阳光
                        final ImageView ivSunFlower = new SunFlower(context);
                        ivSunFlower.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sun));
                        //设置位置大小
                        AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(
                                70, 70, (int) getX(), (int) getY() + 60);
                        final AbsoluteLayout layout = (AbsoluteLayout) getParent();
                        layout.addView(ivSunFlower, lp);

                        //2、给阳光添加点击事件
                        ivSunFlower.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layout.removeView(ivSunFlower);
                                //通过回调，把MainActivity自己传过来，实现阳光数量的增加
                                myCallBack.addSunCount(25);
                            }
                        });

                        //3、让阳光在3秒后，未被收取时自动消除
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        layout.removeView(ivSunFlower);
                                    }
                                });
                            }
                        }, 3000);
                    }
                });
            }
        }, 5000, 5000);
    }

    @Override
    public void deadAction() {
        //当植物死掉的时候，阳光Timer也要停止
        fireTimer.cancel();
        super.deadAction();
    }
}
