package com.example.mvvmgithub.models;

public class LicenseModel {
    String mKey;
    String mName;
    String mSpdxId;
    String mUrl;
    String mNodeId;

    public LicenseModel(String mKey, String mName, String mSpdxId, String mUrl, String mNodeId) {
        this.mKey = mKey;
        this.mName = mName;
        this.mSpdxId = mSpdxId;
        this.mUrl = mUrl;
        this.mNodeId = mNodeId;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSpdxId() {
        return mSpdxId;
    }

    public void setmSpdxId(String mSpdxId) {
        this.mSpdxId = mSpdxId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmNodeId() {
        return mNodeId;
    }

    public void setmNodeId(String mNodeId) {
        this.mNodeId = mNodeId;
    }

    @Override
    public String toString() {
        return "LicenseModel{" +
                "mKey='" + mKey + '\'' +
                ", mName='" + mName + '\'' +
                ", mSpdxId='" + mSpdxId + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mNodeId='" + mNodeId + '\'' +
                '}';
    }
}
