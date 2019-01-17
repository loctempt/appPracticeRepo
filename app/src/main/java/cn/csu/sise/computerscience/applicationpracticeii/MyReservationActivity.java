package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class MyReservationActivity extends SingleFragmentActivity {
    public static final String EXTRA_USER_ID = "cn.csu.sise.computerscience.applicationpracticeii.MyReservationActivity.extra_user_id";

    @Override
    protected Fragment createFragment() {
        return new MyReservationFragment();
    }

    public static Intent getIntent(Context context, String userId) {
        Intent i = new Intent(context, MyReservationActivity.class);
        i.putExtra(EXTRA_USER_ID, userId);
        return i;
    }
}
