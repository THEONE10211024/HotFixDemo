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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.Button;
import android.widget.Toast;

import com.raymond.demo.andfixdemo.test.A;
import com.raymond.demo.andfixdemo.test.Fix;
import com.tencent.bugly.crashreport.CrashReport;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.net.URI;

/**
 * sample activity
 * 
 * @author luohou
 * @author sanping.li@alipay.com
 *
 */
public class MainActivity extends Activity implements DownloadStatusListener {
	private static final String TAG = "euler";

	private Button btnHook;
	private ThinDownloadManager downloadManager;
	private int downloadId;
	private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
		btnHook = (Button) findViewById(R.id.btn_hook);
		btnHook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				downLoadingFile();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void showToast(View v) {
		Fix.showToast(this);
	}

	private void downLoadingFile(){
		if(downloadManager.query(downloadId) == DownloadManager.STATUS_NOT_FOUND){
			File filesDir = getExternalFilesDir("");
			Uri downloadUri = Uri.parse("https://github.com/THEONE10211024/HotFixDemo/blob/master/app/src/main/java/patch/out.apatch");
			String patchFileString = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/out.apatch";
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
		Toast.makeText(this,"Hook Success",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDownloadFailed(int i, int i1, String s) {
		Toast.makeText(this,"Hook Failed",Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProgress(int i, long l, long l1, int i1) {

	}
}
