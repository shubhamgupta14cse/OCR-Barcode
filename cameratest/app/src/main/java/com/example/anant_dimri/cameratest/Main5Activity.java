package com.example.shubham_gupta.cameratest;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Main5Activity extends Activity {
    private ArrayList<MyImage> images;
    private ImageAdapter imageAdapter;
    public Button clickpic, exitpic;
    private ListView listView;
    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private RelativeLayout layout;
    Button b,b2,b3;



    private LinearLayout dotsLayout;
    private TextView[] dots;
    public int color1,color2,red1,red2,blue1,blue2,green1,green2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main5);
        //setDragEdge(SwipeBackLayout.SwipeBackListener);
        ///for swipe
        // layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        // layout.setBackgroundColor(Color.RED);

        //Timer timer = new Timer();

        //Create a task which the timer will execute.  This should be an implementation of the TimerTask interface.
        //I have created an inner class below which fits the bill.
        //MyTimer mt = new MyTimer();

        //We schedule the timer task to run after 1000 ms and continue to run every 1000 ms.
        // timer.schedule(mt, 1000, 1000);











        red1 = (int)(Math.random() * 128 + 127);
        green1 = (int)(Math.random() * 128 + 127);
        blue1 = (int)(Math.random() * 128 + 127);
        color1 = 0xff << 24 | (red1 << 16) |
                (green1 << 8) | blue1;


        new Thread() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(2300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Main5Activity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            //generate color 2

                            red2 = (int)(Math.random() * 128 + 127);
                            green2 = (int)(Math.random() * 128 + 127);
                            blue2 = (int)(Math.random() * 128 + 127);
                            color2 = 0xff << 24 | (red2 << 10) |
                                    (green2 << 8) | blue2;

                            //start animation
                            View v = findViewById(R.id.relativeLayout);
                            ObjectAnimator anim = ObjectAnimator.ofInt(v, "backgroundColor", color1, color2);


                            anim.setEvaluator(new ArgbEvaluator());
                            anim.setRepeatCount(ValueAnimator.INFINITE);
                            anim.setRepeatMode(ValueAnimator.REVERSE);
                            anim.setDuration(3000);
                            anim.start();

                            // Now set color1 to color2
                            // This way, the background will go from
                            // the previous color to the next color
                            // smoothly
                            color1 = color2;

                        }
                    });
                }
            }
        }.start();







        b2=(Button)findViewById(R.id.OCR);
        b3=(Button)findViewById(R.id.button2);

        b=(Button)findViewById(R.id.button);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Main4Activity.class);
                startActivity(i);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),Main3Activity.class);
                startActivity(i);
            }
        });

        clickpic=(Button)findViewById(R.id.btnTakePhoto) ;
        exitpic=(Button)findViewById(R.id.btnExit) ;




        exitpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        // Construct the data source
        images = new ArrayList();
        // Create the adapter to convert the array to views
        imageAdapter = new ImageAdapter(this, images);
        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.main_list_view);
        listView.setAdapter((ListAdapter) imageAdapter);

        clickpic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                activeTakePhoto();


            }
        });


    }













    public void btn(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.setTitle("Alert Dialog View");
        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnChoosePath)
                .setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        activeGallery();
                    }
                });
        dialog.findViewById(R.id.btnTakePhoto)
                .setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {

                    }
                });

        // show dialog on screen
        dialog.show();
    }




//////////////////////////make this actual comment(for only swipe test)




    /**
     * take a photo
     */

    private void activeTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
            takePictureIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * to gallery
     */
    private void activeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE &&
                        resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver()
                            .query(selectedImage, filePathColumn, null, null,
                                    null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    MyImage image = new MyImage();
                    image.setTitle("Test");
                    image.setDescription(
                            "Picture Clicked...!!");
                    image.setDatetime(System.currentTimeMillis());
                    image.setPath(picturePath);
                    images.add(image);
                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE &&
                        resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor =
                            managedQuery(mCapturedImageURI, projection, null,
                                    null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(
                            MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    MyImage image = new MyImage();
                    image.setTitle("Test");
                    image.setDescription(
                            "test take a photo and add it to list view");
                    image.setDatetime(System.currentTimeMillis());
                    image.setPath(picturePath);
                    images.add(image);


                }
        }
    }


   /* class MyTimer extends TimerTask {

        public void run() {

            //This runs in a background thread.
            //We cannot call the UI from this thread, so we must call the main UI thread and pass a runnable
            runOnUiThread(new Runnable() {

                public void run() {
                    Random rand = new Random();
                    //The random generator creates values between [0,256) for use as RGB values used below to create a random color
                    //We call the RelativeLayout object and we change the color.  The first parameter in argb() is the alpha.
                    layout.setBackgroundColor(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256) ));
                }
            });
        }/*/

}











