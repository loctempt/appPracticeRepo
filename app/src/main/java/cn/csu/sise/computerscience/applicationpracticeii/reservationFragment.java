package cn.csu.sise.computerscience.applicationpracticeii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import cn.csu.sise.computerscience.applicationpracticeii.alipay.PayDemoActivity;
import cn.csu.sise.computerscience.applicationpracticeii.alipay.PayResult;

public class reservationFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "reservationFragment";
    private EditText muserAge;
    private EditText muserName;
    private EditText musrCondition;
    private RadioGroup muserSex;
    private Button mreservationBtn;
    private TextView muserSexTv;
    private SharedPreferences msharedPreferences;
    private JSONObject responseJson;
    private String mScheduleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msharedPreferences = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mScheduleId = getActivity().getIntent().getStringExtra(reservationActivity.EXTRA_SCHEDULE_ID);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation, parent, false);
        muserAge = v.findViewById(R.id.userage);
        muserName = v.findViewById(R.id.username);
        musrCondition = v.findViewById(R.id.usercondition);

        mreservationBtn = v.findViewById(R.id.reservationbtn);
        mreservationBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                PayDemoActivity.payV2(getActivity(), new Handler() {
                    @SuppressWarnings("unused")
                    public void handleMessage(Message msg) {
                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Log.d(TAG, "handleMessage: 支付成功");
                            new reserveFetchTask().execute();
                        } else {
                            Log.d(TAG, "handleMessage: 支付失败");
                        }
                    }
                });
            }
        });

        muserSex = v.findViewById(R.id.sexradioGroup);
        RadioButton rMale = v.findViewById(R.id.radio0);
        RadioButton rFemale = v.findViewById(R.id.radio1);
        muserSex.setOnCheckedChangeListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.reservation_title));

        return v;
    }

    private class reserveFetchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("patientName", muserName.getText())
                        .put("patientSex", msharedPreferences.getString("userSex", "男").toString())
                        .put("patientAge", muserAge.getText())
                        .put("patientCondition", musrCondition.getText())
                        .put("scheduleId", mScheduleId);
                responseJson = new serverConnect(getContext()).runPost(UrlBase.BASE + "data_alter/new_reservation", jsonObject.toString());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (responseJson.getString("status").equals("ok")) {
                    Intent i = new Intent(getContext(), MainActivity.class);
                    getActivity().finish();
//                    i.putExtra(Intent.EXTRA_TEXT, msharedPreferences.getString("username", "null"));
//                    startActivity(i);
//                    todo 没有写支付功能
                }
                Toast.makeText(getContext(), responseJson.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio0:
                msharedPreferences.edit().putString("userSex", "男").apply();
                break;
            case R.id.radio1:
                msharedPreferences.edit().putString("userSex", "女").apply();
                break;
        }
    }


//    private class reservationAdapter extends RecyclerView.Adapter<reservationHolder> {
//        private List<doctor> mDoctors;
//
//        public reservationAdapter(List<doctor> mDoctors) {
//            mDoctors = mDoctors;
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//
//        @NonNull
//        @Override
//        public reservationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull reservationHolder holder, int position, @NonNull List<Object> payloads) {
//            super.onBindViewHolder(holder, position, payloads);
//        }
//    }
}
