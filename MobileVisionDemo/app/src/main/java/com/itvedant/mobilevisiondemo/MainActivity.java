package com.itvedant.mobilevisiondemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class MainActivity extends AppCompatActivity {
    private Button readBarcode, read;
    private TextView barcodeValue;

    private static final int BARCODE_CAPTURE_REQUEST = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeValue = (TextView) findViewById(R.id.barcode_value);
        readBarcode = (Button) findViewById(R.id.read_barcode);
        read = (Button) findViewById(R.id.read);

        readBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AUTO_FOCUS, true);

                startActivityForResult(intent, BARCODE_CAPTURE_REQUEST);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap myQRCode = BitmapFactory.decodeResource(getResources(), R.drawable.qr);

                BarcodeDetector barcodeDetector =
                        new BarcodeDetector.Builder(MainActivity.this)
                                .setBarcodeFormats(Barcode.QR_CODE)
                                .build();

                Frame myFrame = new Frame.Builder()
                        .setBitmap(myQRCode)
                        .build();

                SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);

                // Check if at least one barcode was detected
                if(barcodes.size() != 0) {

                    // Print the QR code's message
                    Log.d("My QR Code's Data",
                            barcodes.valueAt(0).displayValue
                    );

                    barcodeValue.setText(barcodes.valueAt(0).displayValue);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_CAPTURE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BARCODE_OBJECT);
                    if (barcode.format == Barcode.QR_CODE && barcode.valueFormat == Barcode.TEXT)
                        barcodeValue.setText("Simple text: " + barcode.displayValue);
                    if (barcode.format == Barcode.QR_CODE && barcode.valueFormat == Barcode.URL)
                        barcodeValue.setText("Url: " + barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    barcodeValue.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                barcodeValue.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
