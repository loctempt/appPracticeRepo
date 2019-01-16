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


public class logInFragment extends Fragment {
    private EditText muserName;
    private EditText muserPwd;
    private Button mregisterBtn;
    private Button mloginBtn;
    private SharedPreferences msharedPreferences;
    private JSONObject responseJson;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msharedPreferences = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log_in_, parent, false);
        muserName = v.findViewById(R.id.username);
        muserPwd = v.findViewById(R.id.userpwd);

        msharedPreferences.edit().putString("username", muserName.getText().toString()).apply();
        mloginBtn = v.findViewById(R.id.loginbtn);
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new accountFetchTask().execute();
            }
        });


        mregisterBtn = v.findViewById(R.id.registerbtn);
        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), registerActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

    private class accountFetchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("username", msharedPreferences.getString("username", "null"))
                        .put("userPassword", muserPwd.getText());
                responseJson=new serverConnect(getContext()).runPost(UrlBase.BASE+"login.json", jsonObject.toString());
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
                if(responseJson.getString("status").equals("ok")) {
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
