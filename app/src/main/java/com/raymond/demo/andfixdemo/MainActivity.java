/*
 * 
 * Copyright (c) 2015, alipay.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raymond.demo.andfixdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.raymond.demo.andfixdemo.test.A;
import com.raymond.demo.andfixdemo.test.Fix;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

//import com.tencent.bugly.crashreport.CrashReport;

/**
 * sample activity
 *
 * @author luohou
 * @author sanping.li@alipay.com
 *         http://blog.csdn.net/wjr2012/article/details/7993722 ndk的调试
 */
public class MainActivity extends Activity implements DownloadStatusListener {
    private static final String TAG = "euler";
    @Bind(R.id.btn_click)
    Button btnClick;
    @Bind(R.id.btn_hook)
    Button btnHook;
    @Bind(R.id.btn_action)
    Button btnClass;
    private ThinDownloadManager downloadManager;
    private int downloadId;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        btnHook = (Button) findViewById(R.id.btn_hook);
        btnHook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadingFile();
            }
        });
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(v);
            }
        });
        btnClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"fix",Toast.LENGTH_LONG).show();
              /*  EventBus.getDefault().post("Hello AndFix!");*/
//                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(Process.myPid());
    }

    public void showToast(View v) {
        Fix.showToast(this);
    }

    private void downLoadingFile() {
        if (downloadManager.query(downloadId) == DownloadManager.STATUS_NOT_FOUND) {
            File filesDir = getExternalFilesDir("");
            Uri downloadUri = Uri.parse("https://raw.githubusercontent.com/THEONE10211024/HotFixDemo/master/app/src/main/java/patch/out.apatch");
//			Uri downloadUri = Uri.parse("https://raw.githubusercontent.com/alibaba/AndFix/master/tools/apkpatch-1.0.3.zip");

            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + MainApplication.APATCH_PATH;
            Uri destinationUri = Uri.parse(patchFileString);
            final DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                    .setRetryPolicy(new DefaultRetryPolicy())
                    .setDownloadListener(this);
            downloadId = downloadManager.add(downloadRequest);
        }
    }

    @Override
    public void onDownloadComplete(int i) {
        // add patch at runtime
        try {
            // .apatch file path
            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + MainApplication.APATCH_PATH;
            /*File filesDir = getExternalFilesDir("");
			Uri destinationUri = Uri.parse(filesDir+"/out.apatch");
			mPatchManager.addPatch(destinationUri.getPath());*/
            MainApplication mp = (MainApplication) getApplication();
            mp.getPatchManager().addPatch(patchFileString);

            Log.d(TAG, "apatch:" + patchFileString + " added.");
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
        Toast.makeText(this, "Hook Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadFailed(int i, int i1, String s) {
        Toast.makeText(this, "Hook Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProgress(int i, long l, long l1, int i1) {

    }
}
