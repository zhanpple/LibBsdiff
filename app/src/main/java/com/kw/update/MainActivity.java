package com.kw.update;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kw.lib.bsdiff.BsdiffUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //旧版本
    String old = getsdpath() + "oldfile.txt";
    //新版本
    String newp = getsdpath() + "newfile.txt";
    //差分包
    String patch = getsdpath() + "file.patch";
    //旧版apk和差分包合并生成的新版apk
    String tmp = getsdpath() + "new.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.bt_diff:
                Log.d("Thread", "bt_diff Thread ID is " + Thread.currentThread().getId());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long s = System.currentTimeMillis();
                        Log.d("Thread", "Thread ID is " + Thread.currentThread().getId());
                        BsdiffUtils.diff(old, newp, patch);
                        long s1 = System.currentTimeMillis();
                        Log.d("bsdiff", "生成差分包成功，用时:" + (s1 - s) + "ms");
                    }
                }).start();

                break;
            case R.id.bt_patch:
                Log.d("Thread", "bt_patch Thread ID is " + Thread.currentThread().getId());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long s = System.currentTimeMillis();
                        Log.d("Thread", "Thread ID is " + Thread.currentThread().getId());
                        long s2 = System.currentTimeMillis();
                        BsdiffUtils.patch(old, tmp, patch);
                        long s3 = System.currentTimeMillis();
                        Log.d("bsdiff", "差分包合并成功，用时:" + (s3 - s2) + "ms");
                    }
                }).start();

                break;
            case R.id.bt_bzip2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BsdiffUtils.executeBZ2Command("bzip2 -k -f /sdcard/new.txt");
                    }
                }).start();
                break;
            case R.id.bt_bunzip2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BsdiffUtils.executeBZ2Command("bunzip2 -f -k /sdcard/new.txt.bz2");
                    }
                }).start();
                break;
        }
    }

    private String getsdpath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

}
