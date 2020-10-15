package com.example.mvvmgithub.models;

public class PermissionsModel {
    boolean mAdmin;
    boolean mPush;
    boolean mPull;

    public PermissionsModel(boolean mAdmin, boolean mPush, boolean mPull) {
        this.mAdmin = mAdmin;
        this.mPush = mPush;
        this.mPull = mPull;
    }

    public boolean ismAdmin() {
        return mAdmin;
    }

    public void setmAdmin(boolean mAdmin) {
        this.mAdmin = mAdmin;
    }

    public boolean ismPush() {
        return mPush;
    }

    public void setmPush(boolean mPush) {
        this.mPush = mPush;
    }

    public boolean ismPull() {
        return mPull;
    }

    public void setmPull(boolean mPull) {
        this.mPull = mPull;
    }

    @Override
    public String toString() {
        return "PermissionsModel{" +
                "mAdmin=" + mAdmin +
                ", mPush=" + mPush +
                ", mPull=" + mPull +
                '}';
    }
}
