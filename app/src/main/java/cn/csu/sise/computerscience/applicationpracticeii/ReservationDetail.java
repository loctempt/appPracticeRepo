package cn.csu.sise.computerscience.applicationpracticeii;

public class ReservationDetail {
    public String mReservationName;
    public String mReservationDoctorName;
    public String mReservationDoctorPositionalTitle;
    public String mReservationTime;
    public boolean mOverDue;
    public ReservationDetail( String ReservationName,String ReservationDoctorName, String ReservationDoctorPositionalTitle,String ReservationTime,boolean overDue){
        mReservationDoctorName=ReservationDoctorName;
        mReservationDoctorPositionalTitle=ReservationDoctorPositionalTitle;
        mReservationTime=ReservationTime;
        mReservationName=ReservationName;
        mOverDue=overDue;
    }
}
