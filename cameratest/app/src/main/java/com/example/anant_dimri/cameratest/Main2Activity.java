package com.example.shubham_gupta.cameratest;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class Main2Activity extends AppCompatActivity {
        TextView barcodeResult;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        barcodeResult=(TextView) findViewById(R.id.scan_result);
    b=(Button) findViewById(R.id.scan_barcode);

        registerForContextMenu(barcodeResult);

b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(getApplicationContext(),ScanBarcode.class);
        startActivityForResult(i,0);
    }
});

    }




public void scanBarcode(View v){


}

@Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
    if(requestCode== CommonStatusCodes.SUCCESS){
        if(data!=null)
        {
            Barcode barcode= data.getParcelableExtra("barcode");
            barcodeResult.setText(barcode.displayValue);

        }
        else{
barcodeResult.setText("no barcode found");

        }

    }
    else{




    super.onActivityResult(requestCode,resultCode,data);}


}


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Web-search");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Copy");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        String getstring = barcodeResult.getText().toString();
        if(item.getTitle()=="Copy"){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            Toast.makeText(getApplicationContext(), "Copied To Clipboard",
                    Toast.LENGTH_LONG).show();
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copied text" ,getstring);
            clipboard.setPrimaryClip(clip);


        }
        else if(item.getTitle()=="Web-search"){
            Uri uri = Uri.parse("https://www.google.com/search?q="+getstring);
            Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(gSearchIntent);

        }else{
            return false;
        }
        return true;
    }
}



