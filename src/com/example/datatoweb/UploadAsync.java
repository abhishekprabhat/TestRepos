package com.example.datatoweb;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

import android.net.rtp.AudioStream;
import android.net.rtp.AudioGroup;
import android.net.rtp.RtpStream;
import android.net.rtp.AudioCodec;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UploadAsync extends AsyncTask <String, String, Boolean> {
    boolean status = false; 
    FTPClient mFtpClient;
	 private Socket client;
	 private PrintWriter printwriter;
	 int port = 4444;
	 Context ctx;
    
	 private AudioStream audioStream;
	 private AudioGroup audioGroup;
	 private AudioCodec audioCodec;
	 
	 UploadAsync (Context c, Context c1){
		 ctx = c;
		 Toast.makeText(ctx, "UploadAsync: Context Set."+ctx, Toast.LENGTH_SHORT).show();
	 }
	 
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		//connnectingwithFTP ("192.168.1.4", "aprabhat", "admin");
		//transferMsg(arg0[0], arg0[1]);
		startStreaming();
		return true;
	}
	
	   public void startStreaming(){
		   try {
			   audioGroup = new AudioGroup();
			   audioGroup.setMode(AudioGroup.MODE_NORMAL);        
			   audioStream = new AudioStream(InetAddress.getLocalHost());
			   audioCodec = audioStream.getCodec();
			   audioStream.setCodec(AudioCodec.AMR);
			   audioStream.setMode(audioStream.MODE_SEND_ONLY);
			   
			   //	Create InetAddress of the server
			   InetAddress server = InetAddress.getByName("192.168.1.4");
			   audioStream.associate(server, 4444);
			   //audioStream.associate(InetAddress.getByAddress(new byte[] {(byte)192, (byte)168, (byte)1, (byte)2 }), 5004);
			   audioStream.join(audioGroup);
			   AudioManager Audio =  (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE); 
			   Audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
			} 
			catch (SocketException e) { e.printStackTrace();} 
			catch (UnknownHostException e) { e.printStackTrace();} 
			catch (Exception ex) { ex.printStackTrace();}
			
	   }

	public void transferMsg (String ip, String msg){
	      try {
	    	  
	    	     client = new Socket(ip, port);  //connect to server
	    	     printwriter = new PrintWriter(client.getOutputStream(),true);
	    	     printwriter.write(msg);  //write the message to output stream
	    	 
	    	     printwriter.flush();
	    	     printwriter.close();
	    	     client.close();   //closing the connection
	    	 
	    	    } catch (UnknownHostException e) {
	    	     e.printStackTrace();
	    	    } catch (IOException e) {
	    	     e.printStackTrace();
	    	    }

	}
	public void connnectingwithFTP(String ip, String userName, String pass) {  
 
        try {  
             mFtpClient = new FTPClient();  
             mFtpClient.setConnectTimeout(10 * 1000);  
             mFtpClient.connect(InetAddress.getByName(ip));  
             status = mFtpClient.login(userName, pass);  
             Log.e("isFTPConnected", String.valueOf(status));  
             if (FTPReply.isPositiveCompletion(mFtpClient.getReplyCode())) {  
                  mFtpClient.setFileType(FTP.ASCII_FILE_TYPE);  
                  mFtpClient.enterLocalPassiveMode();  
                  FTPFile[] mFileArray = mFtpClient.listFiles();  
                  Log.e("Size", String.valueOf(mFileArray.length));  
             }  
        } catch (SocketException e) {  
             e.printStackTrace();  
        } catch (UnknownHostException e) {  
             e.printStackTrace();  
        } catch (IOException e) {  
             e.printStackTrace();  
        }  
   }  
	/**  
     * @param ftpClient FTPclient object  
     * @param remoteFilePath  FTP server file path  
     * @param downloadFile   local file path where you want to save after download  
     * @return status of downloaded file  
     */  
    public boolean downloadSingleFile(FTPClient ftpClient,  
              String remoteFilePath, File downloadFile) {  
         File parentDir = downloadFile.getParentFile();  
         if (!parentDir.exists())  
              parentDir.mkdir();  
         OutputStream outputStream = null;  
         try {  
              outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));  
              ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
              return ftpClient.retrieveFile(remoteFilePath, outputStream);  
         } catch (Exception ex) {  
              ex.printStackTrace();  
         } finally {  
              if (outputStream != null) {  
                   try {  
                        outputStream.close();  
                   } catch (IOException e) {  
                        e.printStackTrace();  
                   }  
              }  
         }  
         return false;  
    } 
    
    /**  
     *   
     * @param ftpClient FTPclient object  
     * @param downloadFile local file which need to be uploaded.  
     */  
    public void uploadFile(FTPClient ftpClient, File downloadFile,String serverfilePath) {  
         try {  
              FileInputStream srcFileStream = new FileInputStream(downloadFile);  
              boolean status = ftpClient.storeFile("remote ftp path",  
                        srcFileStream);  
              Log.e("Status", String.valueOf(status));  
              srcFileStream.close();  
         } catch (Exception e) {  
              e.printStackTrace();  
         }  
    } 

    @Override
    protected void onPostExecute(Boolean result) {
	     if (ctx != null) Toast.makeText(ctx, "Msg Sent Successfully", Toast.LENGTH_LONG).show();
    }
}
