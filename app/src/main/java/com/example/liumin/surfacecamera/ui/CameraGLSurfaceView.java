package com.example.liumin.surfacecamera.ui;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.example.liumin.surfacecamera.render.DirectDrawer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by liumin on 2016/8/18.
 */
public class CameraGLSurfaceView extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "yanzi";
    Context mContext;
    SurfaceTexture mSurface;
    int mTextureID = -1;
    DirectDrawer mDirectDrawer;

    public CameraGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        setEGLContextClientVersion(2);

    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        CameraInterface.getInstance().doStopCamera();
    }

    private int createTextureID() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public SurfaceTexture _getSurfaceTexture() {
        return mSurface;
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onFrameAvailable...");
        this.requestRender();
    }
}
