package cn.csu.sise.computerscience.applicationpracticeii;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

        // todo 测试用，记得取消自动填写用户名和密码
        muserName.setText("test");
        muserPwd.setText("8088");

        getActivity().requestPermissions(new String[]{Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);

        mloginBtn = v.findViewById(R.id.loginbtn);
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msharedPreferences.edit().putString("username", muserName.getText().toString()).apply();
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

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(getContext().getString(R.string.login_title));

        return v;
    }

    private class accountFetchTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject()
                        .put("username", msharedPreferences.getString("username", "null"))
                        .put("userPassword", muserPwd.getText());
                responseJson = new serverConnect(getContext()).runPost(UrlBase.BASE + "login.json", jsonObject.toString());

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return responseJson;
        }

        @Override
        protected void onPostExecute(JSONObject responseJson) {
            super.onPostExecute(responseJson);
            try {
                if (responseJson.getString("status").equals("ok")) {
                    Intent i = new Intent(getContext(), MainActivity.class);
                    i.putExtra(Intent.EXTRA_TEXT, msharedPreferences.getString("username", "null"));
                    Toast.makeText(getContext(), responseJson.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), responseJson.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
