package me.ztiany.capturer;

import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.ByteBuffer;

import timber.log.Timber;

public class FrameReader implements OutputProvider {

    private ImageReader mImageReader;

    private Size mFrameSize;

    @Nullable
    private FrameListener mFrameListener;

    private Camera2Operator mCamera2Operator;

    @Override
    public void onAttach(@NonNull Camera2Operator camera2Operator, @NonNull Components components) {
        mCamera2Operator = camera2Operator;

        mFrameSize = components.require(PREVIEW);

        mImageReader = ImageReader.newInstance(
                mFrameSize.getWidth(),
                mFrameSize.getHeight(),
                ImageFormat.YUV_420_888,
                2
        );
        Handler backgroundHandler = components.require(WORKER);
        mImageReader.setOnImageAvailableListener(new OnImageAvailableListenerImpl(), backgroundHandler);
    }

    @Override
    public void onDetach() {
        release();
    }

    private class OnImageAvailableListenerImpl implements ImageReader.OnImageAvailableListener {

        private byte[] y;
        private byte[] u;
        private byte[] v;

        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireNextImage();
            // Y:U:V == 4:2:2
            if (mFrameListener != null && image.getFormat() == ImageFormat.YUV_420_888) {
                Image.Plane[] planes = image.getPlanes();
                // 重复使用同一批byte数组，减少gc频率
                if (y == null) {
                    ByteBuffer bufferY = planes[0].getBuffer();
                    ByteBuffer bufferU = planes[1].getBuffer();
                    ByteBuffer bufferV = planes[2].getBuffer();
                    Timber.i("Y limit = %d, position = %d, capacity = %d", bufferY.limit(), bufferY.position(), bufferY.capacity());
                    Timber.i("u limit = %d, position = %d, capacity = %d", bufferU.limit(), bufferU.position(), bufferU.capacity());
                    Timber.i("v limit = %d, position = %d, capacity = %d", bufferV.limit(), bufferV.position(), bufferV.capacity());
                    Timber.i("planesY: row-stride = %d, pixel-stride = %d", planes[0].getRowStride(), planes[0].getPixelStride());
                    Timber.i("planesU: row-stride = %d, pixel-stride = %d", planes[1].getRowStride(), planes[1].getPixelStride());
                    Timber.i("planesV: row-stride = %d, pixel-stride = %d", planes[2].getRowStride(), planes[2].getPixelStride());
                    Timber.i("preview-size = %dx%d", mFrameSize.getWidth(), mFrameSize.getHeight());
                    y = new byte[bufferY.limit() - bufferY.position()];
                    u = new byte[bufferU.limit() - bufferU.position()];
                    v = new byte[bufferV.limit() - bufferV.position()];
                }

                if (image.getPlanes()[0].getBuffer().remaining() == y.length) {
                    planes[0].getBuffer().get(y);
                    planes[1].getBuffer().get(u);
                    planes[2].getBuffer().get(v);
                    mFrameListener.onPreview(y, u, v, mFrameSize, planes[0].getRowStride());
                }
            }
            image.close();
        }
    }

    public interface FrameListener {

        /**
         * 预览数据回调。
         *
         * @param y           预览数据，Y 分量。
         * @param u           预览数据，U 分量。
         * @param v           预览数据，V 分量。
         * @param previewSize 预览尺寸。
         * @param stride      步长。
         */
        void onPreview(byte[] y, byte[] u, byte[] v, Size previewSize, int stride);

    }

    public void setFrameListener(@Nullable FrameListener frameListener) {
        mFrameListener = frameListener;
    }

    public void release() {
        try {
            mImageReader.close();
        } catch (Exception exception) {
            Timber.e(exception, "FrameReader.release()");
        }
    }

}