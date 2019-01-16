package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class reservationFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private EditText muserAge;
    private EditText muserName;
    private EditText musrCondition;
    private RadioGroup muserSex;
    private Button mreservationBtn;
    private TextView muserSexTv;
    private SharedPreferences msharedPreferences;
    private JSONObject responseJson;
    private String scheduleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msharedPreferences=getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        scheduleId=getActivity().getIntent().getStringExtra("scheduleId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation, parent, false);
        muserAge=v.findViewById(R.id.userage);
        muserName=v.findViewById(R.id.username);
        musrCondition=v.findViewById(R.id.usercondition);

        mreservationBtn=v.findViewById(R.id.reservationbtn);
        mreservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new reserveFetchTask().execute();
            }
        });

        muserSex=v.findViewById(R.id.sexradioGroup);
        RadioButton rMale=v.findViewById(R.id.radio0);
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
                        .put("patientSex", msharedPreferences.getString("userSex","男").toString())
                        .put("patientAge",muserAge.getText())
                        .put("patientCondition",musrCondition.getText())
                        .put("scheduleId",scheduleId);
                responseJson=new serverConnect(getContext()).runPost(UrlBase.BASE+"data_alter/new_reservation", jsonObject.toString());
                if(responseJson.getString("status").equals("ok")){
                    Toast.makeText(getContext(),responseJson.getString("message"),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),responseJson.getString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if(responseJson.getString("status").equals("ok")){
                    Toast.makeText(getContext(),responseJson.getString("message"),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), MainActivity.class);
                    i.putExtra(Intent.EXTRA_TEXT, msharedPreferences.getString("username", "null"));
                    startActivity(i);
//                    todo 没有写支付功能
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio0:
                msharedPreferences.edit().putString("userSex","男").apply();
                break;
            case R.id.radio1:
                msharedPreferences.edit().putString("userSex","女").apply();
                break;
        }
    }
//
//    private class reservationHolder extends RecyclerView.ViewHolder{
//        public reservationHolder(LayoutInflater inflater,ViewGroup parent) {
//            super(inflater.inflate(R.layout.select_reservation_item,parent,false));
//        }
//    }
//    private class reservationAdapter extends RecyclerView.Adapter<doctor>{
//        private List<doctor> mDoctors;
//        public reservationAdapter(List<doctor> doctors){
//            mDoctors=doctors;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull doctor doctor, int i) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//
//        @NonNull
//        @Override
//        public doctor onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            return null;
//        }
//    }
}
