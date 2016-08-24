package com.example.liumin.surfacecamera.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.liumin.surfacecamera.ui.CameraInterface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by liumin on 2016/8/24.
 */
public class CameraRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "yanzi";
    //Context mContext;
    SurfaceTexture mSurface;
    int mTextureID = -1;
    DirectDrawer mDirectDrawer;
    GLSurfaceView glSurfaceView;

    public CameraRender(GLSurfaceView glSurfaceView){
        this.glSurfaceView=glSurfaceView;
    }



    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated...");
        mTextureID = createTextureID();
        mSurface = new SurfaceTexture(mTextureID);
        mSurface.setOnFrameAvailableListener(this);
        mDirectDrawer = new DirectDrawer(mTextureID);
        CameraInterface.getInstance().doOpenCamera(null);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "onSurfaceChanged...");
        GLES20.glViewport(0, 0, width, height);
        if (!CameraInterface.getInstance().isPreviewing()) {
            CameraInterface.getInstance().doStartPreview(mSurface, 1.33f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "onDrawFrame...");
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage();
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);
        mDirectDrawer.draw(mtx);
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

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "onFrameAvailable...");
        glSurfaceView.requestRender();
    }
}
