package com.huanhong.mashineshop.activity;

import android.content.Intent;

import com.huanhong.mashineshop.BaseActivity;
import com.huanhong.mashineshop.R;

public class SuccessActivity extends BaseActivity{
    @Override
    protected int getContentViewId() {
        return R.layout.activity_success;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,GoodsActivity.class));
    }
}