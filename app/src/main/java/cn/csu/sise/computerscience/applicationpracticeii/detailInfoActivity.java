package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class detailInfoActivity extends SingleFragmentActivity {
    public static final String EXTRA_DOCTOR_ID = "cn.csu.sise.computerscience.applicationpracticeii.doctor_id";

    @Override
    protected Fragment createFragment(){
        return new detailInfoFragment();
    }

    public static Intent getIntent(Context context, String doctorId){
        Intent i = new Intent(context, detailInfoActivity.class);
        i.putExtra(EXTRA_DOCTOR_ID, doctorId);
        return i;
    }
}