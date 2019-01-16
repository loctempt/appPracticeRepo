package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class serverConnect {
    private Context context;
    private final OkHttpClient client=new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public serverConnect(Context context) {
        this.context = context;
    }

    String runGet(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    JSONObject runPost(String url, String json) throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.e("body: ",body.toString());
        Response response = client.newCall(request).execute();
        Log.e("response: ",response.toString());

        if (response.isSuccessful()) {
            String responseStr=response.body().toString();
            JSONObject responseJson=new JSONObject(responseStr);
            return responseJson;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

}
