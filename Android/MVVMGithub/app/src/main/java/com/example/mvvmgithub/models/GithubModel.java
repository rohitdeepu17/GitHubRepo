package com.example.mvvmgithub.models;

public class GithubModel {
    int mOpenIssuesCount;
    String mName;
    String mDescription;
    PermissionsModel mPermissionsModel;
    LicenseModel mLicenseModel;

    public GithubModel(int mOpenIssuesCount, String mName, String mDescription, PermissionsModel mPermissionsModel, LicenseModel mLicenseModel) {
        this.mOpenIssuesCount = mOpenIssuesCount;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mPermissionsModel = mPermissionsModel;
        this.mLicenseModel = mLicenseModel;
    }

    public int getmOpenIssuesCount() {
        return mOpenIssuesCount;
    }

    public void setmOpenIssuesCount(int mOpenIssuesCount) {
        this.mOpenIssuesCount = mOpenIssuesCount;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public PermissionsModel getmPermissionsModel() {
        return mPermissionsModel;
    }

    public void setmPermissionsModel(PermissionsModel mPermissionsModel) {
        this.mPermissionsModel = mPermissionsModel;
    }

    public LicenseModel getmLicenseModel() {
        return mLicenseModel;
    }

    public void setmLicenseModel(LicenseModel mLicenseModel) {
        this.mLicenseModel = mLicenseModel;
    }

    @Override
    public String toString() {
        return "GithubModel{" +
                "mOpenIssuesCount=" + mOpenIssuesCount +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mPermissionsModel=" + mPermissionsModel.toString() +
                ", mLicenseModel=" + mLicenseModel.toString() +
                '}';
    }
}
