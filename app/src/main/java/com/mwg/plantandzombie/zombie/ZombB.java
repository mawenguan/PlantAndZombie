package com.mwg.plantandzombie.zombie;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.mwg.plantandzombie.R;

/**
 * Created by mwg on 2018/3/5.
 */

public class ZombB extends Zomb {
    public ZombB(Context context) {
        super(context);

        speed = 2;
        HP = 6;
        zombBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zomb_1);
    }
}
