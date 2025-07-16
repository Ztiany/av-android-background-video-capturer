package me.ztiany.capturer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Size;

import androidx.annotation.NonNull;


public class VideoSpec implements Parcelable {

    public final int videoWidth;
    public final int videoHeight;
    public final int frameRate;

    public VideoSpec(Builder builder) {
        videoWidth = builder.videoWidth;
        videoHeight = builder.videoHeight;
        frameRate = builder.frameRate;
    }

    protected VideoSpec(Parcel in) {
        videoWidth = in.readInt();
        videoHeight = in.readInt();
        frameRate = in.readInt();
    }

    public static final Creator<VideoSpec> CREATOR = new Creator<>() {
        @Override
        public VideoSpec createFromParcel(Parcel in) {
            return new VideoSpec(in);
        }

        @Override
        public VideoSpec[] newArray(int size) {
            return new VideoSpec[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(videoWidth);
        dest.writeInt(videoHeight);
        dest.writeInt(frameRate);
    }

    public static class Builder {
        private int videoWidth;
        private int videoHeight;
        private int frameRate;

        public Builder setVideoSize(Size size) {
            this.videoWidth = size.getWidth();
            this.videoHeight = size.getHeight();
            return this;
        }

        public Builder setFrameRate(int frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        public VideoSpec build() {
            return new VideoSpec(this);
        }

    }

}