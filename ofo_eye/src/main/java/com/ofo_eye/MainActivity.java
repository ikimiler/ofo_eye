package com.ofo_eye;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private SensorManager sensorManager;
    private Sensor defaultSensor;

    private View lefteye,righteye;
    private float normalSpace ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        lefteye = findViewById(R.id.lefteye);
        righteye = findViewById(R.id.righteye);

        normalSpace = getResources().getDimension(R.dimen.dimen20);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        defaultSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //完美做法，需要对dimens进行不同屏幕的适配即可

       /*
        传感器类型说明如下：
        #define SENSOR_TYPE_ACCELEROMETER 1 //加速度
        #define SENSOR_TYPE_MAGNETIC_FIELD 2 //磁力
        #define SENSOR_TYPE_ORIENTATION 3 //方向
        #define SENSOR_TYPE_GYROSCOPE 4 //陀螺仪
        #define SENSOR_TYPE_LIGHT 5 //光线感应
        #define SENSOR_TYPE_PRESSURE 6 //压力
        #define SENSOR_TYPE_TEMPERATURE 7 //温度
        #define SENSOR_TYPE_PROXIMITY 8 //接近
        #define SENSOR_TYPE_GRAVITY 9 //重力
        #define SENSOR_TYPE_LINEAR_ACCELERATION 10//线性加速度
        #define SENSOR_TYPE_ROTATION_VECTOR 11//旋转矢量
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listerner, defaultSensor, SensorManager.SENSOR_DELAY_UI);
        /*
         第三个参数如下:
         * get sensor data as fast as possible
         public static final int SENSOR_DELAY_FASTEST = 0;
         * rate suitable for games
         public static final int SENSOR_DELAY_GAME = 1;
         * rate suitable for the user interface
         public static final int SENSOR_DELAY_UI = 2;
         * rate (default) suitable for screen orientation changes
         public static final int SENSOR_DELAY_NORMAL = 3;
         */
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listerner);
    }

    private SensorEventListener listerner = new SensorEventListener(){

        private float x,y;

        @Override
        public void onSensorChanged(SensorEvent event) {

            /*
            加速度传感器说明:
            加速度传感器又叫G-sensor，返回x、y、z三轴的加速度数值。
            该数值包含地心引力的影响，单位是m/s^2。
            将手机平放在桌面上，x轴默认为0，y轴默认0，z轴默认9.81。
            将手机朝下放在桌面上，z轴为-9.81。
            将手机向左倾斜，x轴为正值。
            将手机向右倾斜，x轴为负值。
            将手机向上倾斜，y轴为负值。
            将手机向下倾斜，y轴为正值。
            加速度传感器可能是最为成熟的一种mems产品，市场上的加速度传感器种类很多。
            手机中常用的加速度传感器有BOSCH（博世）的BMA系列，AMK的897X系列，ST的LIS3X系列等。
            这些传感器一般提供±2G至±16G的加速度测量范围，采用I2C或SPI接口和MCU相连，数据精度小于16bit。
            */

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

                x -= 7.0f * event.values[0];
                y += 7.0f * event.values[1];

                //越界处理
                if(x < -normalSpace ){
                    x = -normalSpace;
                }
                if(x > 0){
                    x = 0;
                }
                if(y > 0){
                    y = 0;
                }
                if(y < -normalSpace){
                    y = -normalSpace;
                }

                lefteye.setTranslationY(y);
                lefteye.setTranslationX(x);
                lefteye.setRotation(x);
                righteye.setTranslationX(x);
                righteye.setTranslationY(y);
                righteye.setRotation(x);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
