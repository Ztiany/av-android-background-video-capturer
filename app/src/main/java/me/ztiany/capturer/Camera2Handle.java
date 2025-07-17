package me.ztiany.capturer;

import android.hardware.camera2.CameraCaptureSession;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Camera2Handle {

    void startCapturingCameraSession(
            @NonNull Surface surface,
            @Nullable CameraCaptureSession.StateCallback stateCallback
    );

    void stopCapturingCameraSession();

}
