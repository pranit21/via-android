package com.itvedant.sensorsdemo;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class AccelerometerActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerListener;

    private long lastUpdate;
    private boolean color = false;
    private float lastX, lastY, lastZ;
    private static final int SHAKE_THRESHOLD = 600;

    private TextView textView1, textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        textView1 = (TextView) findViewById(R.id.text_view1);
        textView2 = (TextView) findViewById(R.id.text_view2);
        textView3 = (TextView) findViewById(R.id.text_view3);

        lastUpdate = System.currentTimeMillis();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            accelerometerListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    float accelerationSquareRoot = (x * x + y * y + z * z)
                            / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
                    long currentTime = System.currentTimeMillis();

                    if (accelerationSquareRoot >= 2) {
                        if ((currentTime - lastUpdate) < 200) {
                            return;
                        }
                        lastUpdate = currentTime;

                        if (color) {
                            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                            getRandomNumber();
                        } else {
                            getWindow().getDecorView().setBackgroundColor(Color.RED);
                        }
                        color = !color;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        } else {
            Toast.makeText(this, "Accelerometer sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRandomNumber() {
        ArrayList<Integer> numbersGenerated = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Random randNumber = new Random();
            int iNumber = randNumber.nextInt(48) + 1;

            if (!numbersGenerated.contains(iNumber)) {
                numbersGenerated.add(iNumber);
            } else {
                i--;
            }
        }

        textView1.setText(numbersGenerated.get(0) + "");
        textView2.setText(numbersGenerated.get(1) + "");
        textView3.setText(numbersGenerated.get(2) + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accelerometerListener);
    }
}
