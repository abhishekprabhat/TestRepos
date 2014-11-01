package com.example.datatoweb;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import java.net.InetAddress;
import android.util.Log;

import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;

import android.widget.Toast;

public class FTPService extends Service {

	 private String messsage="Not Set Yet!";
	 private UploadAsync async;

	@Override
	public IBinder onBind(Intent arg0) {
	      Toast.makeText(getBaseContext(), "FTPClient: onBind() Called", Toast.LENGTH_LONG).show();
		// TODO Auto-generated method stub
		return null;
	}

	   @Override
	   public int onStartCommand(Intent intent, int flags, int startId) {
	      // Let it continue running until it is stopped.
		   Bundle b1 = intent.getExtras();
		   messsage = b1.getString("Temp Msg");
		   
	      Toast.makeText(getBaseContext(), "Service Started: Sending in Background: "+messsage, Toast.LENGTH_LONG).show();
     
	      
	      async = new UploadAsync(getBaseContext(), getApplicationContext());
	      async.execute("192.168.1.4", messsage);
	      return START_STICKY;
	   }
	   @Override
	   public void onDestroy() {
	      super.onDestroy();
	      Toast.makeText(getBaseContext(), "Service Destroyed", Toast.LENGTH_LONG).show();
	   }
	   
}
