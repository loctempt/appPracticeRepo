package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class reservationActivity extends SingleFragmentActivity {
    public static final String EXTRA_SCHEDULE_ID = "cn.csu.sise.computerscience.applicationpracticeii.schedule_id";

    @Override
    protected Fragment createFragment() {
        return new reservationFragment();
    }

    public static Intent getIntent(Context context, String scheduleId) {
        Intent i = new Intent(context, reservationActivity.class);
        i.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        return i;
    }
}
