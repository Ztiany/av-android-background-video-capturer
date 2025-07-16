package me.ztiany.capturer;

import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface OutputProvider {

    class Components extends HashMap<String, Object> {

        public <T> T require(String key) {
            //noinspection unchecked
            T t = (T) get(key);
            if (t == null) {
                throw new NullPointerException("no value associated with " + key);
            }
            return t;
        }
    }

    String ORIENTATION = "ORIENTATION";
    String WORKER = "WORKER";
    String PREVIEW = "PREVIEW";
    String STREAM_CONFIGURATION = "StreamConfiguration";

    void onAttach(@NonNull Camera2Operator camera2Operator, @NonNull Components components);

    void onDetach();

}
