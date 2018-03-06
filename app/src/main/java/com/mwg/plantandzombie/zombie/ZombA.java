package com.mwg.plantandzombie.zombie;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.mwg.plantandzombie.R;

/**
 * Created by mwg on 2018/3/5.
 */

public class ZombA extends Zomb {

    public ZombA(Context context) {
        super(context);

        speed = 1;
        HP = 4;
        zombBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zomb_0);
    }
}
