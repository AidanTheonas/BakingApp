package com.example.aidan.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {
    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
    private int stepId;
    private String shortDescription, fullDescription, videoUrl, thumbnailUrl;

    public Steps() {
    }

    public Steps(int stepId, String shortDescription, String fullDescription, String videoUrl, String thumbnailUrl) {
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    private Steps(Parcel in) {
        stepId = in.readInt();
        shortDescription = in.readString();
        fullDescription = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    public int getStepId() {
        return stepId;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stepId);
        dest.writeString(shortDescription);
        dest.writeString(fullDescription);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }
}
