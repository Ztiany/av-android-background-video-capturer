package me.ztiany.capturer;

import android.util.Size;

import androidx.annotation.NonNull;

import java.util.List;

public interface SizeSelector {

    @NonNull
    Size getBestSupportedSize(@NonNull List<Size> sizes);
}
