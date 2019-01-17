package cn.csu.sise.computerscience.applicationpracticeii;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyReservationFragment extends Fragment {
    public static final String TAG = "MyReservationFragment";

    private String mUserId;
    private RecyclerView mReservationRecyclerView;
    private JSONObject responseJsonInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserId = getActivity().getIntent().getStringExtra(MyReservationActivity.EXTRA_USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_reservation, container, false);
        mReservationRecyclerView=v.findViewById(R.id.reservation_recycler_view);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.menu_item_my_reservation);
        new ReservationDetailItemFetchTask().execute();
        return v;
    }

    //information/user_reservation
    private class ReservationDetailItemFetchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                responseJsonInfo = new serverConnect(getContext()).runPost(UrlBase.BASE + "information/user_reservation", "");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (responseJsonInfo.getString("status").equals("ok") ) {
                    ArrayList<ReservationDetail> reservationDetails= new ArrayList<>();
                    JSONArray extra = responseJsonInfo.getJSONArray("extra");
                    for (int i = 0; i < extra.length(); i++) {
                        Log.d(TAG, "onPostExecute: 取出的元素 " + extra.getJSONObject(i).toString());
                        JSONObject reservationDetailJson = extra.getJSONObject(i);
                        reservationDetails.add(new ReservationDetail(
                                reservationDetailJson.getString("patientName"),
                                reservationDetailJson.getString("doctorName"),
                                reservationDetailJson.getString("doctorPositionalTitle"),
                                reservationDetailJson.getString("reservationTime"),
                                reservationDetailJson.getBoolean("overdue")
                        ));
                    }
                    mReservationRecyclerView.setAdapter(new myReservationAdapter(reservationDetails));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class myReservationHolder extends RecyclerView.ViewHolder {
        TextView mReservationTime;
        TextView mReservationName;
        TextView mReservationDoctorName;
        TextView mReservationDoctorPositionalTitle;
        ReservationDetail mReservationDetail;

        public myReservationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_my_reservation, parent, false));
            mReservationTime = itemView.findViewById(R.id.reserve_time);
            mReservationDoctorName = itemView.findViewById(R.id.doctor_name);
            mReservationDoctorPositionalTitle = itemView.findViewById(R.id.positional_title);
            mReservationName = itemView.findViewById(R.id.user_name);

        }

        public void bind(ReservationDetail reservationDetail) {
            mReservationDetail = reservationDetail;
            mReservationName.setText(mReservationDetail.mReservationName);
            mReservationDoctorName.setText(mReservationDetail.mReservationDoctorName);
            mReservationDoctorPositionalTitle.setText(mReservationDetail.mReservationDoctorPositionalTitle);
            mReservationTime.setText(mReservationDetail.mReservationTime);
//            if(!mReservationDetail.mOverDue){
//               Drawable drawable=myReservationHolder.itemView.getBackground();
//            }

        }
    }


    private class myReservationAdapter extends RecyclerView.Adapter<myReservationHolder> {

        private List<ReservationDetail> mDetails;

        public myReservationAdapter(List<ReservationDetail> details) {
            this.mDetails = details;
        }

        @NonNull
        @Override
        public myReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new myReservationHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull myReservationHolder ReservationHolder, int i) {
            ReservationHolder.bind(mDetails.get(i));
        }

        @Override
        public int getItemCount() {
            return mDetails.size();
        }
    }

}
