package cn.csu.sise.computerscience.applicationpracticeii;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class MainFragment extends Fragment {

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private RecyclerView mDepartmentRecyclerView;
    private RecyclerView mDoctorRecyclerView;
    private Toolbar mToolbar;
    private JSONObject mDepartmentsJson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        sharedPreferences = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mDepartmentRecyclerView = v.findViewById(R.id.rv_recyclerview_one);
        mDoctorRecyclerView = v.findViewById(R.id.rv_recyclerview_two);

        mDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDoctorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return v;
    }

    private class DepartmentsFetchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                mDepartmentsJson = new serverConnect(getContext()).runPost(UrlBase.BASE+"information/department_list", "");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if(mDepartmentsJson.getString("status").equals("ok")) {
                    ArrayList<String> departments = new ArrayList<>();
                    JSONArray extra = mDepartmentsJson.getJSONArray("extra");
                    for(int i = 0; i < extra.length(); i++){
                        departments.add(extra.getString(i));
                    }
                    mDepartmentRecyclerView.setAdapter(new DepartmentAdapter(departments));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DepartmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mDepartmentNameTextView;
        String mDepartmentName;

        public DepartmentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_department, parent, false));
            mDepartmentNameTextView = itemView.findViewById(R.id.department_name);
        }

        public void bind(String departmentName) {
            mDepartmentName = departmentName;
        }

        @Override
        public void onClick(View v) {
            //todo implement department onclick
        }
    }

    private class DepartmentAdapter extends RecyclerView.Adapter<DepartmentHolder> {

        private List<String> mDepartmentNames;

        public DepartmentAdapter(List<String> departmentNames) {
            mDepartmentNames = departmentNames;
        }

        @NonNull
        @Override
        public DepartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DepartmentHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DepartmentHolder departmentHolder, int i) {
            departmentHolder.bind(mDepartmentNames.get(i));
        }

        @Override
        public int getItemCount() {
            return mDepartmentNames.size();
        }
    }

    private class DoctorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mDoctorNameTextView;
        TextView mDoctorPositionalTitleTextView;
        TextView mDoctorJobTextView;
        Doctor mDoctor;

        public DoctorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_doctor_brief, parent, false));
            mDoctorJobTextView = itemView.findViewById(R.id.doctor_job);
            mDoctorNameTextView = itemView.findViewById(R.id.doctor_name);
            mDoctorPositionalTitleTextView = itemView.findViewById(R.id.positional_title);
        }

        public void bind(Doctor doctor) {
            mDoctor = doctor;
            mDoctorJobTextView.setText(mDoctor.doctorJob);
            mDoctorPositionalTitleTextView.setText(mDoctor.doctorPositionalTitle);
            mDoctorNameTextView.setText(mDoctor.doctorName);
        }

        @Override
        public void onClick(View v) {
            //todo implement doctor onclick
        }
    }

    private class DoctorAdapter extends RecyclerView.Adapter<DoctorHolder> {

        private List<Doctor> mDoctors;

        public DoctorAdapter(List<Doctor> doctors) {
            mDoctors = doctors;
        }

        @NonNull
        @Override
        public DoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DoctorHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorHolder doctorHolder, int i) {
            doctorHolder.bind(mDoctors.get(i));
        }

        @Override
        public int getItemCount() {
            return mDoctors.size();
        }
    }
}
