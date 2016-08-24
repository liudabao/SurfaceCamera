package com.example.liumin.surfacecamera;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.liumin.surfacecamera.render.CameraRender;
import com.example.liumin.surfacecamera.ui.CameraGLSurfaceView;
import com.example.liumin.surfacecamera.ui.CameraInterface;
import com.example.liumin.surfacecamera.util.DisplayUtil;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final String TAG = "yanzi";
    CameraGLSurfaceView glSurfaceView = null;
    ImageButton shutterBtn;
    float previewRate = -1f;

    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPersimmions();
        initUI();
        initViewParams();

        shutterBtn.setOnClickListener(new BtnListeners());
    }

    private void initUI(){
        glSurfaceView = (CameraGLSurfaceView)findViewById(R.id.camera_textureview);
        CameraRender cameraRender=new CameraRender(glSurfaceView);
        glSurfaceView.setRenderer(cameraRender);
        shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
    }
    private void initViewParams(){
        ViewGroup.LayoutParams params = glSurfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //
        glSurfaceView.setLayoutParams(params);

        //
        ViewGroup.LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 80);
        p2.height = DisplayUtil.dip2px(this, 80);;
        shutterBtn.setLayoutParams(p2);

    }

    private class BtnListeners implements View.OnClickListener {


        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_shutter:
                    CameraInterface.getInstance().doTakePicture();
                    break;
                default:break;
            }
        }

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        glSurfaceView.bringToFront();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        glSurfaceView.onPause();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();

            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            else if (addPermission(permissions, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)) {
                permissionInfo += "Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS Deny \n";
            }
            else if (addPermission(permissions, Manifest.permission.CAMERA)) {
                permissionInfo += "Manifest.permission.CAMERA Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }

    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("permission", "success");


                } else {
                    Log.e("permission", "fail");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
