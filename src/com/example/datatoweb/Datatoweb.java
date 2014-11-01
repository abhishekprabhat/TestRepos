package com.example.datatoweb;

import java.util.ArrayList;
import java.util.List;
  
 import org.apache.http.HttpResponse;
 import org.apache.http.NameValuePair;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.entity.UrlEncodedFormEntity;
 import org.apache.http.client.methods.HttpGet;
 import org.apache.http.client.methods.HttpPost;
 import org.apache.http.impl.client.DefaultHttpClient;
 import org.apache.http.message.BasicNameValuePair;
 import org.apache.http.params.CoreProtocolPNames;
 import org.json.JSONObject;
  
 import android.app.Activity;
 import android.content.Intent;
 import android.os.Bundle;
 import android.util.Log;
import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;
  
 public class Datatoweb extends Activity {

 	EditText name;
 	Button btnCreate;
 	String page="";
     /** Called when the activity is first created. */
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
  
         setContentView(R.layout.activity_main);
         btnCreate = (Button) findViewById(R.id.button1);
         name = (EditText) findViewById(R.id.editText1);
     
         btnCreate.setOnClickListener(new Button.OnClickListener()
         {
             public void onClick(View v)
             {
                 myStartService(v);
             }
         }); 
     }
  
     void examineJSONFile()
     {
         try
         {
             JSONObject object=new JSONObject();
             object.put("Name", name.getText());
             String str=object.toString();
             executeHttpPost(str);
         	//Log.i("JsonString :", str);
         	Toast.makeText(this, "Json Objects are : " + str,Toast.LENGTH_LONG).show();
  
  
         }
         catch (Exception je)
         {
  
         }
     }
  
     public  void executeHttpPost(String string) throws Exception 
 	{
     	//This method  for HttpConnection  
 	    try 
 	    {
 	    	HttpClient client = new DefaultHttpClient();
  
 	        HttpPost request = new HttpPost("http://10.0.2.2/online/sample.php");
  
 	        List value=new ArrayList();
  
 	        value.add(new BasicNameValuePair("Name",string));
  
 	        UrlEncodedFormEntity entity=new UrlEncodedFormEntity(value);
  
 	        request.setEntity(entity);
  
 	        client.execute(request);
  
 	       System.out.println("after sending :"+request.toString());
  
 	    } 
      catch(Exception e)	    {
 	    }
  
 	}
 
     public void moveToAudio(View v){
    	 Toast.makeText(getBaseContext(), "Trying to change Activity", Toast.LENGTH_SHORT).show();
    	 Intent i1 = new Intent (v.getContext(), AudioActivity.class);
    	 startActivity(i1);
     }
     
     // Method to start the service
      public void myStartService(View view) {
    	 Toast.makeText(getBaseContext(), "Sending Message: "+ name.getText().toString(), Toast.LENGTH_LONG).show();
    	 Intent i1 = new Intent(getBaseContext(), FTPService.class);
    	 i1.putExtra("Temp Msg", name.getText().toString());
         startService(i1);
     }
     // Method to stop the service
     public void stopService(View view) {
        stopService(new Intent(getBaseContext(), FTPService.class));
     }
 }