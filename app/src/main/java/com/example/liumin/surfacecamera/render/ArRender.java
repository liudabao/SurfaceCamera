package com.example.liumin.surfacecamera.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.example.liumin.surfacecamera.entity.Model;
import com.example.liumin.surfacecamera.entity.Point;
import com.example.liumin.surfacecamera.ui.CameraInterface;
import com.example.liumin.surfacecamera.util.STLReader;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by liumin on 2016/8/24.
 */
public class ArRender implements GLSurfaceView.Renderer {

    private static final String TAG = "TAG";
    SurfaceTexture mSurface;
    int mTextureID = -1;
    DirectDrawer mDirectDrawer;
    GLSurfaceView glSurfaceView;
    Context mContext;
    //
    private Model model;
    private Point mCenterPoint;
    private Point eye=new Point(0 ,0 ,-3);
    private Point up=new Point(0, 1, 0);
    private Point center=new Point(0, 0, 0);
    private float mScalef=1;
    private float mDegree=0;

    public ArRender(GLSurfaceView glSurfaceView, Context context){
        //this.glSurfaceView=glSurfaceView;
        mContext=context;
        //ar
        try {
            model = new STLReader().parseBinStlInAssets(context, "huba.stl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   // @Override
  //  public void onFrameAvailable(SurfaceTexture surfaceTexture) {
    //    Log.e(TAG, "onFrameAvailable...");
        //glSurfaceView.requestRender();
   // }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG, "onSurfaceCreated...");
      //  mTextureID = createTextureID();
      //  mSurface = new SurfaceTexture(mTextureID);
      //  mSurface.setOnFrameAvailableListener(this);
      //  mDirectDrawer = new DirectDrawer(mTextureID);
      //  CameraInterface.getInstance().doOpenCamera(null);

        //model
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glClearDepthf(1.0f);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glShadeModel(GL10.GL_SMOOTH);
        float r=model.getR();
        mScalef=0.5f/r;
        mCenterPoint=model.getCentrePoint();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG, "onSurfaceChanged...");
        gl.glViewport(0, 0, width, height);
       // if (!CameraInterface.getInstance().isPreviewing()) {
       //     CameraInterface.getInstance().doStartPreview(mSurface, 1.33f);
        //}

        //model
        //gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, ((float)width)/height, 1f, 100f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e(TAG, "onDrawFrame...");
       // GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
       // gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //mSurface.updateTexImage();
        //float[] mtx = new float[16];
       // mSurface.getTransformMatrix(mtx);
        //mDirectDrawer.draw(mtx);

        //model
        //gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        GLU.gluLookAt(gl, eye.x, eye.y, eye.z, center.x,
                center.y, center.z, up.x, up.y, up.z);
        gl.glRotatef(mDegree , 0, 1, 0);
        gl.glScalef(mScalef, mScalef, mScalef);
        gl.glTranslatef(-mCenterPoint.x, -mCenterPoint.y, -mCenterPoint.z);

        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glNormalPointer(GL10.GL_FLOAT, 0, model.getVnormsBuffer());
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, model.getVertBuffer());
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, model.getFacetCount()*3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
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
}
