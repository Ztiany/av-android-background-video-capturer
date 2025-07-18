package me.ztiany.capturer;

import androidx.annotation.NonNull;

import java.util.HashMap;

public interface OutputProvider {

    String ORIENTATION = "ORIENTATION";
    String WORKER = "WORKER";
    String PREVIEW_SIZE = "PREVIEW_SIZE";
    String STREAM_CONFIGURATION = "STREAM_CONFIGURATION";

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

    void onAttach(@NonNull Camera2Handle camera2Handle, @NonNull Components components);

    void onDetach();

}
