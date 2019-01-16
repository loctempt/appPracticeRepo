package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class detailInfoFragment extends Fragment {
    private ImageView mimg;
    private TextView mname;
    private TextView mdepartment;
    private TextView mpositionalTitle;
    private TextView mjob;
    private TextView mtel;
    private TextView mintroduction;
    private String doctorId;
    private JSONObject responseJsonInfo;
    private JSONObject responseJsonSchedule;

    //    少写了RecycleView
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorId = getActivity().getIntent().getStringExtra("doctorId");
        new detailInfoFetchTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_info, parent, false);

        mimg = v.findViewById(R.id.img);
        mname = v.findViewById(R.id.name);
        mdepartment = v.findViewById(R.id.department);
        mpositionalTitle = v.findViewById(R.id.positionalTitle);
        mjob = v.findViewById(R.id.job);
        mtel = v.findViewById(R.id.tel);
        mintroduction = v.findViewById(R.id.introduction);
        return v;
    }



    private class detailInfoFetchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("doctorId", doctorId);
                responseJsonInfo = new serverConnect(getContext()).runPost(UrlBase.BASE+"information/doctor_information", jsonObject.toString());
                responseJsonSchedule = new serverConnect(getContext()).runPost(UrlBase.BASE+"information/doctor_schedule", jsonObject.toString());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if(responseJsonInfo.getString("status").equals("ok")&&responseJsonSchedule.getString("status").equals("ok")){
                    JSONObject docPersonnalInfo = responseJsonInfo.getJSONObject("extra");
                    mjob.setText(docPersonnalInfo.getString("doctorJob"));
                    mdepartment.setText(docPersonnalInfo.getString("doctorDepartment"));
                    mname.setText(docPersonnalInfo.getString("doctorName"));
                    mpositionalTitle.setText(docPersonnalInfo.getString("doctorPositionalTitle"));
                    mtel.setText(docPersonnalInfo.getString("doctorTel"));
                    mintroduction.setText(docPersonnalInfo.getString("doctorIntroduction"));
//                todo 没写预约时间列表 imageview 医生头像没有处理
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
