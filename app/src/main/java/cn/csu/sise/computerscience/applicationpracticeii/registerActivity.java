package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class registerActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new registerFragment();
    }
}
