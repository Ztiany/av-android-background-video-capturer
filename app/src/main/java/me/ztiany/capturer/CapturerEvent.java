package me.ztiany.capturer;

import androidx.annotation.IntDef;

@IntDef({
        CapturerEvent.STARTED,
        CapturerEvent.STOPPED,
        CapturerEvent.ERROR,
})
public @interface CapturerEvent {

    int STARTED = 1;

    int STOPPED = 2;

    int ERROR = 3;

}
