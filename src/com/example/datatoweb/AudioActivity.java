package com.example.datatoweb;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;

import wseemann.media.FFmpegMediaMetadataRetriever;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import android.os.ParcelFileDescriptor;


public class AudioActivity extends Activity {

   private MediaRecorder myAudioRecorder;
   private String outputFile = null;
   private Button start,stop,play;
   private Context ctx;

   FFmpegMediaMetadataRetriever mmr = null;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_audio);
      start = (Button)findViewById(R.id.button1);
      stop = (Button)findViewById(R.id.button2);
      play = (Button)findViewById(R.id.button3);

      stop.setEnabled(false);
      play.setEnabled(false);
      outputFile = Environment.getExternalStorageDirectory().
      getAbsolutePath() + "/myrecording.3gp";;

      myAudioRecorder = new MediaRecorder();
      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
      myAudioRecorder.setOutputFile(outputFile);
      mmr = new FFmpegMediaMetadataRetriever();

      ctx = getBaseContext();
      Toast.makeText(getBaseContext(), "Output File: "+outputFile, Toast.LENGTH_LONG).show();

   }

   public void start(View view){
      try {
         myAudioRecorder.prepare();
         myAudioRecorder.start();
      } catch (IllegalStateException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      start.setEnabled(false);
      stop.setEnabled(true);
      Toast.makeText(getApplicationContext(), "Recording started at "+outputFile, Toast.LENGTH_LONG).show();
      myStreaming (view);
   }
   
   public void myStreaming(View v){
	   UploadAsync async = new UploadAsync(getBaseContext(), v.getContext());
	   async.execute();
   }
   
   public void stop(View view){
      myAudioRecorder.stop();
      myAudioRecorder.release();
      myAudioRecorder  = null;
      stop.setEnabled(false);
      play.setEnabled(true);

      String fileRead = null;
      try{
    	  fileRead = Environment.getExternalStorageDirectory().getAbsolutePath() +"/myrecording.3gp"; 
 
    	  File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    	  File[] files = f.listFiles();
    	  int i=0;
    	  for (File inFile : files) {
    		  i++;
    		  System.err.println("File "+i+". "+inFile.getName());
    	      if (inFile.isDirectory()) {
    	          // is directory
    	      }
    	  }    	  
    	  AssetManager am = ctx.getAssets();
    	  //InputStream iStream = am.open(fileRead, AssetManager.ACCESS_STREAMING);
 
    	
    	  AssetFileDescriptor d = openAssetFile(fileRead);//am.openFd("myrecording.3gp");
      
          mmr.setDataSource(d.getFileDescriptor(), d.getStartOffset(), d.getLength());
          String duration = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);

          File f1 = new File (outputFile);
          long fileLength = f1.length();
      Toast.makeText(getApplicationContext(), "File Length = "+fileLength,
    		  Toast.LENGTH_SHORT).show();
      Toast.makeText(getApplicationContext(), "File Duration = "+duration,
    		  Toast.LENGTH_SHORT).show();
      }catch (Exception e){
    	  Toast.makeText(ctx, "Exception Occured: in Opening AMR File", Toast.LENGTH_SHORT).show();
    	  e.printStackTrace();
      }
      mmr.release();
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   
   public void play(View view) throws IllegalArgumentException,   
   SecurityException, IllegalStateException, IOException{
   
   MediaPlayer m = new MediaPlayer();
   m.setDataSource(outputFile);
   m.prepare();
   m.setVolume(1, 1);
   m.start();
   int dur = m.getDuration()/1000;
   
   Toast.makeText(getApplicationContext(), "Playing audio: Duration "+dur, Toast.LENGTH_LONG).show();

   }

   //@Override
   public AssetFileDescriptor openAssetFile(String assetPath) throws FileNotFoundException
   {
       //final String assetPath = uri.getLastPathSegment();  // or whatever

       try
       {
           final boolean canBeReadDirectlyFromAssets = false; // if your asset going to be compressed?
           if (canBeReadDirectlyFromAssets)
           {
               return ctx.getAssets().openFd(assetPath);
           }
           else
           {
               final File cacheFile = new File(ctx.getCacheDir(), assetPath);
               cacheFile.getParentFile().mkdirs();
               copyToCacheFile(assetPath, cacheFile);
               return new AssetFileDescriptor(ParcelFileDescriptor.open(cacheFile, ParcelFileDescriptor.MODE_READ_ONLY), 0, -1);
           }
       }
       catch (FileNotFoundException ex)
       {
           throw ex;
       }
       catch (IOException ex)
       {
           throw new FileNotFoundException(ex.getMessage());
       }
   }

   private void copyToCacheFile(final String assetPath, final File cacheFile) throws IOException
   {
       final InputStream inputStream = ctx.getAssets().open(assetPath, AssetManager.ACCESS_BUFFER);
       try
       {
           final FileOutputStream fileOutputStream = new FileOutputStream(cacheFile, false);
           try
           {
               //using Guava IO lib to copy the streams, but could also do it manually
               //ByteStreams.copy(inputStream, fileOutputStream);
        	   int data;
        	   while ((data = inputStream.read()) != -1) {  
        		    System.out.print(" " + data);  
        		    fileOutputStream.write(data);  
        		   }  
           }
           finally
           {
               fileOutputStream.close();
           }
       }
       finally
       {
           inputStream.close();
       }
   }   
   
}