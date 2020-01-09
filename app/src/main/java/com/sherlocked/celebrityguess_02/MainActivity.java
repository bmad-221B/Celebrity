package com.sherlocked.celebrityguess_02;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    Button button1 ;
    Button button2 ;
    Button button3 ;
    Button button4 ;
    ImageView imageView ;
    Random rand = new Random();
    int current=0;
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }
               //Log.i("Result", result);
                return result;

            } catch (Exception e) {
                e.printStackTrace();

                return "Failed";
            }
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        imageView = findViewById(R.id.imageView);
        DownloadTask task = new DownloadTask();
        String result = null ;
        try {
            result =  task.execute("http://www.posh24.se/kandisar").get();
            //Log.i("Result",result);
            String[] splitResult = result.split("<div class=\"listedArticles\">");
            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);
            while (m.find()){
                celebURLs.add(m.group(1));
            }
            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);
            while (m.find()){
                celebNames.add(m.group(1));
            }
            //Log.i("Names", String.valueOf(celebNames.size()));
            newQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void proceed(View view){
        Object t = view.getTag() ;
        if(button1.getTag()==t){
            if(button1.getText()==celebNames.get(current)){
                Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Incorrect",Toast.LENGTH_SHORT).show();
            }
        }
        if(button2.getTag()==t){
            if(button2.getText()==celebNames.get(current)){
                Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Incorrect",Toast.LENGTH_SHORT).show();
            }
        }
        if(button3.getTag()==t){
            if(button3.getText()==celebNames.get(current)){
                Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Incorrect",Toast.LENGTH_SHORT).show();
            }
        }
        if(button4.getTag()==t){
            if(button4.getText()==celebNames.get(current)){
                Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Incorrect",Toast.LENGTH_SHORT).show();
            }
        }
        current = current + 1 ;
        newQuestion();
    }
    public void newQuestion(){

        try {
            ImageDownloader imageTask = new ImageDownloader();

            Bitmap celebImage = null;
            celebImage = imageTask.execute(celebURLs.get(current)).get();
            imageView.setImageBitmap(celebImage);
            int answer = rand.nextInt(4) + 1;
            //Log.i("Answer", String.valueOf(answer));
            int a,b,c ;
            /*a = rand.nextInt(100) ;
           while(!celebNames.get(a).equals(celebNames.get(current)))
            {
                a = rand.nextInt(100) ;
            }
            b = rand.nextInt(100) ;
            while(!celebNames.get(b).equals(celebNames.get(current)) || !celebNames.get(b).equals(celebNames.get(a)))
            {
                b = rand.nextInt(100) ;
            }
            c = rand.nextInt(100) ;
           while(!celebNames.get(c).equals(celebNames.get(current)) || !celebNames.get(c).equals(celebNames.get(a)) ||  !celebNames.get(c).equals(celebNames.get(b)))
            {
                c = rand.nextInt(100) ;
            }*/
            int n = celebNames.size() ;
            a = (current + 13)%n ;
            b = (current + 5)%n ;
            c = (current + 7)%n ;
            if(answer==1)
            {
                button1.setText(celebNames.get(current));
                button2.setText(celebNames.get(a));
                button3.setText(celebNames.get(b));
                button4.setText(celebNames.get(c));
            }
            else if(answer==2)
            {
                button2.setText(celebNames.get(current));
                button1.setText(celebNames.get(a));
                button3.setText(celebNames.get(b));
                button4.setText(celebNames.get(c));
            }
            else if(answer==3)
            {
                button3.setText(celebNames.get(current));
                button2.setText(celebNames.get(a));
                button1.setText(celebNames.get(b));
                button4.setText(celebNames.get(c));
            }
            else if(answer==4)
            {
                button4.setText(celebNames.get(current));
                button2.setText(celebNames.get(a));
                button3.setText(celebNames.get(b));
                button1.setText(celebNames.get(c));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
