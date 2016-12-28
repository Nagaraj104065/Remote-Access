package com.example.nagaraj.touchevents;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
 TextView t,t2;
    private float x=0;
    private float y=0;
    private float dx=0;
    private float dy=0;
   private boolean motion=false;
    PrintWriter out;
    Socket socket;
    Button b,b2;
    Context context;
    private boolean isConnected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView2);
        b=(Button) findViewById(R.id.leftclick);
        b2=(Button) findViewById(R.id.rightclick);
        context=this;
        t2.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isConnected&&(out!=null)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = event.getX();
                            y = event.getY();
                            motion = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            t.setText("DX:"+event.getX()+",DY:"+event.getY()+",X:"+x+",Y"+y);
                            dx = event.getX() - x;
                            dy = event.getY() - y;
                            x = event.getX();
                            y = event.getY();
                            if(dx !=0|| dy !=0) {

                                out.println(dx +","+dy);
                            }
                            motion = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!motion) {
                                t.setText("left");
                                out.println("left");
                            }


                    }
                }
                return true;
            }
        });
    }
    public void connect(View v){
try {
    String ip = "0.0.0.0";
    sockets conn = new sockets();
    t.setText("connected");
    conn.execute(ip);
}catch (Exception e){
    t.setText(e.toString());
}

    }
    public void space(View v){
        try {

            if (isConnected && (out != null)) {
                t.setText("space");
                out.println("space");
            }
        }catch (Exception e){
            t.setText(e.toString());
        }
    }
    public void right(View v){
      try{
          if(isConnected&&(out !=null)) {
              t.setText("right");
            out.println("right");
        }
    }catch (Exception e){
        t.setText(e.toString());
    }
}

    public void discon(View v){
       try{

           if(isConnected&&(out !=null)) {
               t.setText("tab");
            out.println("tab");
        }
    }catch (Exception e){
        t.setText(e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(isConnected&&(out !=null)) {
                out.println("exit");
                t.setText("disconnected");
                socket.close();
            }
        }catch (Exception e){
            t.setText(e.toString());
        }
    }

    public class sockets extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result=true;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            try{
                InetAddress addres=InetAddress.getByName(params[0]);
                socket=new Socket(addres,3362);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                result=false;
            } catch (IOException e) {
                e.printStackTrace();
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isConnected=aBoolean;
            Toast.makeText(context,isConnected?"Connected to server!":"Error while connecting", Toast.LENGTH_LONG).show();
            try{
                if(isConnected){
                    out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
