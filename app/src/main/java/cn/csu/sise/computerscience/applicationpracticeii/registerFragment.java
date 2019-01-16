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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class registerFragment extends Fragment {
    private EditText muserName;
    private EditText musrPwd;
    private EditText muserTel;
    private EditText muserchecknum;
    private Button mchecknumBtn;
    private Button mregisterBtn;
    private SharedPreferences msharedPreferences;
    private JSONObject responseJson;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msharedPreferences=getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, parent, false);
        muserName=v.findViewById(R.id.username);

        msharedPreferences.edit().putString("userName",muserName.getText().toString()).apply();

        musrPwd=v.findViewById(R.id.userpwd);
        muserTel=v.findViewById(R.id.usertel);
        muserchecknum=v.findViewById(R.id.userchecknum);
        mchecknumBtn=v.findViewById(R.id.checknumbtn);

        mchecknumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new checknumFetchTask().execute();
            }
        });

        mregisterBtn=v.findViewById(R.id.registerbtn);
        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new accountFetchTask().execute();
            }
        });
        return v;
    }

    private class checknumFetchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("userTel", muserTel.getText());
                 responseJson = new serverConnect(getContext()).runPost(UrlBase.BASE+"require_validation", jsonObject.toString());
                if (responseJson.getString("status").equals("ok")) {
                    Toast.makeText(getContext(), responseJson.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), responseJson.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class accountFetchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("username", msharedPreferences.getString("username", "null"))
                        .put("userPassword", musrPwd.getText())
                        .put("userTel",muserTel.getText())
                        .put("validationCode",muserchecknum.getText());
               responseJson=new serverConnect(getContext()).runPost(UrlBase.BASE+"account_register", jsonObject.toString());
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
