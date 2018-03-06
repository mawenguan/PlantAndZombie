package com.mwg.plantandzombie.zombie;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.mwg.plantandzombie.R;

/**
 * Created by mwg on 2018/3/5.
 */

public class ZombD extends Zomb {
    public ZombD(Context context) {
        super(context);

        speed = 4;
        HP = 10;
        zombBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.zomb_3);
    }
}
