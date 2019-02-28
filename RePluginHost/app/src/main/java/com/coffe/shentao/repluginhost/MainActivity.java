package com.coffe.shentao.repluginhost;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void InstallApp(View view){
        //安装插件
        Log.v("TanRong",""+Environment.getExternalStorageDirectory().getPath().toString() + "/replugin.apk");
        PluginInfo pluginInfo = RePlugin.install(Environment.getExternalStorageDirectory().getPath().toString() + "/replugin.apk");
        System.out.println(pluginInfo);
        //跳转到插件中去
        Intent intent = RePlugin.createIntent("com.coffe.shentao.replugianapp", "com.coffe.shentao.replugianapp.MainActivity");
        if (!RePlugin.startActivity(MainActivity.this, intent)) {
            Toast.makeText(this, "启动失败", Toast.LENGTH_LONG).show();
        }
       // RePlugin.uninstall("");//插件的名称   包名也可以  区分是否继承RePlauginApplication
    }


    //权限申请
    private String[] cameraPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
    private boolean hasRequestPermission=false;
    @Override
    protected void onResume() {
        super.onResume();
        if (checkDangerousPermissions(this, cameraPermissions)){

        }else {
            if (!hasRequestPermission){
                showScanCodeTip();
            }
        }
    }
    private void showScanCodeTip() {
        ScanCodeTipDialog dialog = new ScanCodeTipDialog();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hasRequestPermission = true;
                requestDangerousPermissions(cameraPermissions, REQUEST_CODE_CAMERA);
            }
        });
        dialog.show(getSupportFragmentManager(), MainActivity.class.getSimpleName());
    }

    @Override
    public boolean handlePermissionResult(int requestCode, boolean granted) {
        if (requestCode == REQUEST_CODE_CAMERA){
            if (!granted){
                finish();
            }
            return true;
        }
        return super.handlePermissionResult(requestCode, granted);
    }

}
