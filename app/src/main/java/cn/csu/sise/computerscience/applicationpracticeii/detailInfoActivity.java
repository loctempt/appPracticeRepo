package cn.csu.sise.computerscience.applicationpracticeii;

import android.support.v4.app.Fragment;

public class detailInfoActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new detailInfoFragment();
    }
}