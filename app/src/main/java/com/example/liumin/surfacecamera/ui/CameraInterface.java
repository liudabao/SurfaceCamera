package com.example.liumin.surfacecamera.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.liumin.surfacecamera.util.CamParaUtil;
import com.example.liumin.surfacecamera.util.FileUtil;
import com.example.liumin.surfacecamera.util.ImageUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by liumin on 2016/8/18.
 */
public class CameraInterface {
    private static final String TAG = "yanzi";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;

    public interface CamOpenOverCallback{
        public void cameraHasOpened();
    }

    private CameraInterface(){

    }
    public static synchronized CameraInterface getInstance(){
        if(mCameraInterface == null){
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }
    /**??Camera
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback){
        Log.i(TAG, "Camera open....");
        if(mCamera == null){
            mCamera = Camera.open();
            Log.i(TAG, "Camera open over....");
            if(callback != null){
                callback.cameraHasOpened();
            }
        }else{
            Log.i(TAG, "Camera open ??!!!");
            doStopCamera();
        }


    }
    /**???Surfaceview???????
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate){
        Log.i(TAG, "doStartPreview...");
        if(isPreviewing){
            mCamera.stopPreview();
            return;
        }
        if(mCamera != null){
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }


    }
    /**???TextureView???Camera
     * @param surface
     * @param previewRate
     */
    public void doStartPreview(SurfaceTexture surface, float previewRate){
        Log.i(TAG, "doStartPreview...");
        if(isPreviewing){
            mCamera.stopPreview();
            return;
        }
        if(mCamera != null){
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }

    }

    /**
     * ????#????Camera
     */
    public void doStopCamera(){
        if(null != mCamera)
        {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }
    /**
     * ????
     */
    public void doTakePicture(){
        if(isPreviewing && (mCamera != null)){
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }
    public boolean isPreviewing(){
        return isPreviewing;
    }



    private void initCamera(float previewRate){
        if(mCamera != null){

            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//?????????��???????
//			CamParaUtil.getInstance().printSupportPictureSize(mParams);
//			CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //????PreviewSize??PictureSize
            Camera.Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(),previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Camera.Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setDisplayOrientation(90);

//			CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")){
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);
            mCamera.startPreview();//???????



            isPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCamera.getParameters(); //????get???
            Log.i(TAG, "????????:PreviewSize--With = " + mParams.getPreviewSize().width
                    + "Height = " + mParams.getPreviewSize().height);
            Log.i(TAG, "????????:PictureSize--With = " + mParams.getPictureSize().width
                    + "Height = " + mParams.getPictureSize().height);
        }
    }



    /*?????????????????????????????????????????*/
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
            //???????????????????????????????????????????????????????????
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    Camera.PictureCallback mRawCallback = new Camera.PictureCallback()
            // ?????��??????????,?????null
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };
    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback()
            //??jpeg?????????,????????????
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if(null != data){
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data?????????????????��?
                mCamera.stopPreview();
                isPreviewing = false;
            }
            //

            if(null != b)
            {
                //
                //
                Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
                FileUtil.saveBitmap(rotaBitmap);
            }
            //
            mCamera.startPreview();
            isPreviewing = true;
        }
    };


}
