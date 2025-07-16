package me.ztiany.capturer;

import androidx.annotation.StringDef;

@StringDef({
        CameraId.BACK,
        CameraId.FRONT
})
public @interface CameraId {

    String FRONT = "1";

    String BACK = "0";

}
