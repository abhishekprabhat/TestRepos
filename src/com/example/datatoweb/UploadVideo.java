package com.example.datatoweb;

import android.os.AsyncTask;
import android.widget.Toast;
import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import android.os.ParcelFileDescriptor;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import java.net.Socket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadVideo extends AsyncTask <String, String, Boolean> {
	private Context ctx;
	private boolean hasCamera;
	private Camera mCamera;
	private MediaRecorder mMediaRecorder;
	private Socket socket;
	private CameraPreview mCameraPreview;
	private static String TAG = "Upload Video Async";


		UploadVideo (Context c){
		ctx = c;
		 Toast.makeText(ctx, "UploadVideo: Context Set."+ctx, Toast.LENGTH_SHORT).show();
		 hasCamera = checkCameraHardware(ctx);
		 Toast.makeText(ctx, "Camera Available: "+hasCamera, Toast.LENGTH_SHORT).show();
	}
	
	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try{
			//socket = new Socket ("192.168.1.4", 4444);
		}catch (Exception e){
			 Toast.makeText(ctx, "Socket Exception"+ctx, Toast.LENGTH_SHORT).show();	
			 Log.d("UploadVideo", e.toString());
		}

		startStreaming();
		return true;
	}

	public void startStreaming (){
		ParcelFileDescriptor pfd = ParcelFileDescriptor.fromSocket(socket);
		mCamera = CameraActivity.getCameraInstance();
        mCameraPreview = new CameraPreview(ctx, mCamera);
		mMediaRecorder = new MediaRecorder();
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		// this is the unofficially supported MPEG2TS format, suitable for streaming (Android 3.0+)
		mMediaRecorder.setOutputFormat(8);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
		mMediaRecorder.setOutputFile(pfd.getFileDescriptor());
		mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());
		try{
			mMediaRecorder.prepare();
			mMediaRecorder.start();
		}catch(Exception e){
			Toast.makeText(ctx, "Unable to Start Camera",Toast.LENGTH_SHORT).show();
		}
	}
}
