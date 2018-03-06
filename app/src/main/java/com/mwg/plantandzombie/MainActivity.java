package com.mwg.plantandzombie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mwg.plantandzombie.plant.IcePea;
import com.mwg.plantandzombie.plant.Pea;
import com.mwg.plantandzombie.plant.PeaNut;
import com.mwg.plantandzombie.plant.Plant;
import com.mwg.plantandzombie.plant.SunFlower;
import com.mwg.plantandzombie.zombie.Zomb;
import com.mwg.plantandzombie.zombie.ZombA;
import com.mwg.plantandzombie.zombie.ZombB;
import com.mwg.plantandzombie.zombie.ZombC;
import com.mwg.plantandzombie.zombie.ZombD;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Plant.MyCallBack {

    @BindView(R.id.ivSunCount)
    TextView tvSunCount;
    @BindView(R.id.ivPlantRemove)
    ImageView ivPlantRemove;
    @BindViews({R.id.plant1, R.id.plant2, R.id.plant3, R.id.plant4})
    List<ImageView> ivPlants;

    @BindView(R.id.activityMain)
    AbsoluteLayout activityMain;
    @BindView(R.id.gridLayout)
    GridLayout gridLayout;

    public Plant dragPlant;

    ArrayList<Plant> plants = new ArrayList<Plant>();//用集合把所有已经种到坑里的植物保存
    ArrayList<Zomb> zombs = new ArrayList<Zomb>();//用集合把所有已经添加的僵尸保存
    int zombCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题，设置全屏
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initUI();
        addZombie();

        //创建一个移动的线程
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        //移动僵尸
                        for (Zomb zomb : zombs) {
                            zomb.setX(zomb.getX() - zomb.speed);
                            if (zomb.getX() < -zomb.getWidth()) {
                                activityMain.removeView(zomb);
                                zombs.remove(zomb);

                                break;
                            }
                            //遍历植物，判断植物的矩形和僵尸的矩形是否交叉，如果交叉，则僵尸吃植物
                            for (Plant plant : plants) {
                                Rect plantRect = new Rect();
                                Rect zombRect = new Rect();
                                plant.getGlobalVisibleRect(plantRect);
                                zomb.getGlobalVisibleRect(zombRect);
                                if (plantRect.intersect(zombRect) && zomb.speed != 0) {
                                    zomb.eatPlant(plant);
                                }
                            }
                        }


                        //让所有射手类的植物开始发射
                        for (Plant plant : plants) {
                            //找到射手类型的植物
                            if (plant instanceof Pea) {
                                Pea p = (Pea) plant;
                                for (ImageView bullet : p.bullets) {
                                    bullet.setX(bullet.getX() + 5);
                                    //如果子弹离开屏幕，则删除
                                    if (bullet.getX() > activityMain.getWidth()) {
                                        activityMain.removeView(bullet);
                                        p.bullets.remove(bullet);
                                        break;
                                    }

                                    // 遍历每一个僵尸,如果和子弹交叉，则子弹打中僵尸，僵尸掉血或者消失
                                    for (Zomb zomb : zombs) {
                                        Rect zombRect = new Rect();
                                        Rect bulletRect = new Rect();
                                        zomb.getGlobalVisibleRect(zombRect);
                                        bullet.getGlobalVisibleRect(bulletRect);

                                        if (zombRect.intersect(bulletRect)) {
                                            activityMain.removeView(bullet);
                                            p.bullets.remove(bullet);

                                            zomb.HP--;

                                            //判断如果为寒冰射手，且僵尸还未被打中时，则僵尸的速度减半
                                            if (p instanceof IcePea && zomb.getAlpha() == 1) {
                                                zomb.speed *= 0.5;
                                                zomb.setAlpha(0.5f);
                                            }

                                            if (zomb.HP <= 0) {
                                                activityMain.removeView(zomb);
                                                zombs.remove(zomb);
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }, 0, 1000 / 50);
    }

    private void addZombie() {
        //创建添加僵尸的timer
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Zomb z = null;
                        //0-29  A
                        //30-59 B
                        //60-89 C
                        //90-   D
                        int type = zombCount / 30;
                        switch (type) {
                            case 0:
                                z = new ZombA(MainActivity.this);
                                break;
                            case 1:
                                z = new ZombB(MainActivity.this);
                                break;
                            case 2:
                                z = new ZombC(MainActivity.this);
                                break;
                            default:
                                z = new ZombD(MainActivity.this);
                                break;
                        }
                        int y = (int) (Math.random() * 5) * 150;
                        z.setX(activityMain.getWidth());
                        z.setY(y);
                        activityMain.addView(z);
                        zombs.add(z);
                        z.beginAnimation();
                        zombCount++;
                    }
                });
            }
        }, 0, 1000);
    }

    private void initUI() {
        //获取资源文件里的所有植物的Bitmap；
        Bitmap plantsBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.seedpackets);
        //得到单张植物图片的宽度；
        int width = plantsBitmap.getWidth() / 18;

        //遍历存放所有植物图片的集合，得到要截取的每张图片在原始图中的坐标（因为y坐标都相同，实为得到x的坐标）
        //再以坐标分别截取集合中的各个子图片
        for (int i = 0; i < ivPlants.size(); i++) {
            ImageView ivPlant = ivPlants.get(i);
            int x = 0;
            switch (i) {
                case 1:
                    x = 2 * width;
                    break;
                case 2:
                    x = 3 * width;
                    break;
                case 3:
                    x = 5 * width;
                    break;
            }
            Bitmap plantBitmap = Bitmap.createBitmap(plantsBitmap, x, 0, width, plantsBitmap.getHeight());
            ivPlant.setImageBitmap(plantBitmap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < ivPlants.size(); i++) {
                    ImageView ivPlant = ivPlants.get(i);
                    Rect rect = new Rect();
                    ivPlant.getGlobalVisibleRect(rect);
                    if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {

                        int currentSunCount = Integer.parseInt(tvSunCount.getText().toString());

                        switch (i) {
                            case 0:
                                if (currentSunCount < 50) {
                                    return super.onTouchEvent(event);
                                }
                                dragPlant = new SunFlower(this);
                                break;
                            case 1:
                                if (currentSunCount < 100) {
                                    return super.onTouchEvent(event);
                                }
                                dragPlant = new Pea(this);
                                break;
                            case 2:
                                if (currentSunCount < 175) {
                                    return super.onTouchEvent(event);
                                }
                                dragPlant = new IcePea(this);
                                break;
                            case 3:
                                if (currentSunCount < 125) {
                                    return super.onTouchEvent(event);
                                }
                                dragPlant = new PeaNut(this);
                                break;
                        }
                        activityMain.addView(dragPlant, ivPlant.getLayoutParams());
                        dragPlant.beginAnimation();
                        dragPlant.setMyCallBack(this);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragPlant != null) {
                    dragPlant.setX(event.getRawX() - dragPlant.getWidth() / 2);
                    dragPlant.setY(event.getRawY() - dragPlant.getHeight() / 2);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (dragPlant != null) {
                    //遍历每一个坑Layout
                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        FrameLayout boxfl = (FrameLayout) gridLayout.getChildAt(i);
                        Rect rect = new Rect();
                        boxfl.getGlobalVisibleRect(rect);
                        //判断是否在坑里松手，并且坑里不能有植物
                        if (rect.contains((int) event.getRawX(), (int) event.getRawY())
                                && boxfl.getTag() == null) {
                            dragPlant.setX(rect.left + rect.width() / 2 - dragPlant.getWidth() / 2);
                            dragPlant.setY(rect.top + rect.height() / 2 - dragPlant.getHeight() / 2);
                            boxfl.setTag(dragPlant);
                            dragPlant.setTag(boxfl);
                            dragPlant.isLive = true;
                            dragPlant.beginFire();
                            //用集合把所有已经种到坑里的植物保存
                            plants.add(dragPlant);

                            //花钱
                            addSunCount(-dragPlant.costSunCount);
                            break;
                        }
                    }
                    //如果植物沒被放入坑里，则删除植物
                    if (!dragPlant.isLive) {
                        activityMain.removeView(dragPlant);
                    }
                    dragPlant = null;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void addSunCount(int count) {
        int totalCount = Integer.parseInt(tvSunCount.getText().toString()) + count;
        tvSunCount.setText(totalCount + "");
    }

    @Override
    public void deletePlant(Plant plant) {
        if (plant!=null){
            //从界面和集合中分别删除
            activityMain.removeView(plant);
            plants.remove(plant);
        }
    }
}
