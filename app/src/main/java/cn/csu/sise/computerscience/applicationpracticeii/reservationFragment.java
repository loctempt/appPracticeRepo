package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class reservationFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation, parent, false);
        muserAge = v.findViewById(R.id.userage);
        muserName = v.findViewById(R.id.username);
        musrCondition = v.findViewById(R.id.usercondition);

        mreservationBtn = v.findViewById(R.id.reservationbtn);
        mreservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new reserveFetchTask().execute();
            }
        });

        muserSex = v.findViewById(R.id.sexradioGroup);
        RadioButton rMale = v.findViewById(R.id.radio0);
        RadioButton rFemale = v.findViewById(R.id.radio1);
        muserSex.setOnCheckedChangeListener(this);
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
