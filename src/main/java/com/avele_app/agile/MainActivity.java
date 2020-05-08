package com.avele_app.agile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.SurfaceView;
import android.widget.ToggleButton;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import java.util.UUID;
import java.io.OutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private OutputStream outStream = null;
    private static String address = null;  //Вместо “00:00” Нужно нудет ввести MAC нашего bluetooth
    private long backPressedTime;
    private Toast backToast;


    private  String     HOST      = "192.168.0.200";
    private  int        PORT      = 9091;
    private Connection  mConnect  = null;
    RelativeLayout joystick_left;
    RelativeLayout joystick_right;
    ImageView image_joystick, image_border;
    Joystick js_l;
    Joystick js_r;
    int joy_l_X, joy_l_Y, joy_r_X, joy_r_Y;

    //private BluetoothSocket btSocket = null;
    ListView listView;
    Window window;
    ImageButton buttonUp;
    ImageButton buttonDown;
    ImageButton buttonLeft;
    ImageButton buttonRight;
    ImageButton buttonA;
    ImageButton buttonB;
    ImageButton buttonX;
    ImageButton buttonY;
    ToggleButton buttonT1;
    ToggleButton buttonT2;
    EditText fieldIP;
    EditText fieldPort;
    Button buttonConnect;
    Button buttonRefresh;
    Button buttonDevices;
    SeekBar seekBar1;
    SeekBar seekBar2;
    LinearLayout devicesListLayout;
    LinearLayout devicesOpenLayout;
    LinearLayout startLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new graphics(this));
        window = getWindow();

        joy_l_X = 127; joy_l_Y = 127; joy_r_X = 127; joy_r_Y = 127;
        //mArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        startLayout = findViewById(R.id.startLayout);
        devicesListLayout = findViewById(R.id.devices_layout);
        devicesOpenLayout = findViewById(R.id.devices_open_layout);
        buttonUp = findViewById(R.id.buttonUp);
        buttonDown = findViewById(R.id.buttonDown);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonY = findViewById(R.id.buttonY);
        buttonX = findViewById(R.id.buttonX);
        buttonT1 = findViewById(R.id.toggleButton1);
        buttonT2 = findViewById(R.id.toggleButton2);
        buttonDevices = findViewById(R.id.buttonDevices);
        buttonConnect = findViewById(R.id.buttonConnect);
        fieldIP = findViewById(R.id.ip);
        fieldPort = findViewById(R.id.port);
        seekBar1 = findViewById(R.id.seekBar_1);
        seekBar2 = findViewById(R.id.seekBar_2);
        joystick_left = (RelativeLayout)findViewById(R.id.joystick_left);
        joystick_right = (RelativeLayout)findViewById(R.id.joystick_right);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Toast.makeText(getApplicationContext(), btAdapter.getName() + "  " + btAdapter.getAddress(), Toast.LENGTH_SHORT).show();
        js_l = new Joystick(getApplicationContext(), joystick_left, R.drawable.joy_touch);
        js_r = new Joystick(getApplicationContext(), joystick_right, R.drawable.joy_touch);
        js_l.setStickSize(150, 150);
        js_l.setLayoutSize(600, 600);
        js_l.setLayoutAlpha(200);
        js_l.setStickAlpha(400);
        js_l.setOffset(90);
        js_l.setMinimumDistance(10);
        js_r.setStickSize(150, 150);
        js_r.setLayoutSize(600, 600);
        js_r.setLayoutAlpha(200);
        js_r.setStickAlpha(400);
        js_r.setOffset(90);
        js_r.setMinimumDistance(10);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                sendData("22 " + progressValue);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                sendData("23 " + progressValue);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        joystick_left.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js_l.drawStick(arg1);
                    joy_l_X = js_l.getX();
                    joy_l_Y = js_l.getY();
                sendData("20 " + joy_l_X + " " + joy_l_Y);

                return true;
            }
        });

        joystick_right.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js_r.drawStick(arg1);
                joy_r_X = js_r.getX();
                joy_r_Y = js_r.getY();
                sendData("21 " + joy_r_X + " " + joy_r_Y);
                return true;
            }
        });
        buttonA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 4 " + event.getAction());
                return true;
            }
        });
        buttonB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 5 " + event.getAction());
                return true;
            }
        });
        buttonX.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 6 " + event.getAction());

                return true;
            }
        });
        buttonY.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 7 " + event.getAction());

                return true;
            }
        });
        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 0 " + event.getAction());

                return true;
            }
        });
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 1 " + event.getAction());

                return true;
            }
        });
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 2 " + event.getAction());

                return true;
            }
        });
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendData("24 3 " + event.getAction());

                return true;
            }
        });
        buttonT1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            sendData("24 8 " + (isChecked ? 1 : 0));
            }
        });
        buttonT2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            sendData("24 9 " + (isChecked ? 1 : 0));
        }
        });
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSocket();
            }
        });
        buttonDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mConnect != null)
                    mConnect.closeConnection();
                Log.d(Connection.LOG_TAG, "Соединение закрыто");
                visibleBtButtons();
            }
        });

        //visibleControlButtons();
    }
/*
    class DrawView extends View {

        public DrawView(Context context) {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.draw(canvas);
            //joystick.draw(canvas);
        }

    }*/

    public void connectSocket(){
        PORT = Integer.parseInt(fieldPort.getText().toString());
        HOST = fieldIP.getText().toString();
        mConnect = new Connection(HOST, PORT);
        // Открытие сокета в отдельном потоке
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mConnect.openConnection();
                    // Разблокирование кнопок в UI потоке
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            visibleControlButtons();
                        }
                    });
                    Log.d(Connection.LOG_TAG, "Соединение установлено");
                    Log.d(Connection.LOG_TAG, "(mConnect != null) = " + (mConnect != null));
                } catch (Exception e) {
                    Log.e(Connection.LOG_TAG, e.getMessage());
                    mConnect = null;
                }
            }
        }).start();

    }
    private void visibleControlButtons(){
        startLayout.setVisibility(View.VISIBLE);
        devicesOpenLayout.setVisibility(View.VISIBLE);
        devicesListLayout.setVisibility(View.GONE);
    }
    private void visibleBtButtons(){
        startLayout.setVisibility(View.GONE);
        devicesOpenLayout.setVisibility(View.GONE);
        devicesListLayout.setVisibility(View.VISIBLE);
    }
    private void sendData(final String msgBuffer)
    {
        if (mConnect == null) {
            Log.d(Connection.LOG_TAG, "Соединение не установлено");
        }  else {
            Log.d(Connection.LOG_TAG, "Отправка сообщения");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String text = msgBuffer+ " eofl";
                        if (text.trim().length() == 0)
                            text = "Test message";
                        /* отправляем на сервер данные */
                        if(!mConnect.sendData(text.getBytes()))
                        {
                            mConnect.closeConnection();
                            visibleBtButtons();
                        }
                    } catch (Exception e) {

                        Log.e(Connection.LOG_TAG, e.getMessage());
                    }
                }
            }).start();
        }
    }
    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        //finish();
    }
}
