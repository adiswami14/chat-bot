package com.example.lalliswami.chatbotapp;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver receiver;
    SmsManager manager;
    String address;
    String message;
    Handler handler;
    IntentFilter textFilter;
    int state = 0;
    static final int ARR_SIZE = 4;
    String[] greeting = new String[ARR_SIZE];
    String[] antigreet = new String[ARR_SIZE];
    String[] anticipation = new String[ARR_SIZE];
    String[] confused = new String[ARR_SIZE];
    String[] encouragement = new String[ARR_SIZE];
    String[] ender = new String[ARR_SIZE];
    TextView textView;
    boolean initial = false;

    @Override
    protected void onStart() {
        super.onStart();
        if (initial == false) {
            textFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(receiver, textFilter);
            initial = true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.id_stateDisplay);
        init();
        receiver = new Texter();
        manager = SmsManager.getDefault();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                String[] allowPermission = {Manifest.permission.SEND_SMS};
                requestPermissions(allowPermission, 1);
            }
        }
        handler = new Handler();
    }

    public void init() {
        greeting[0] = "Yo, this is important.";
        greeting[1] = "Hello, it's BasketBot here.";
        greeting[2] = "Hi, I felt that I need to tell you something important.";
        greeting[3] = "Hey, listen up.";
        anticipation[0] = "Now listen up.";
        anticipation[1] = "What I am about to say is extremely important.";
        anticipation[2] = "Turn your ears on right now.";
        anticipation[3] = "Clear your head, and focus your attention on what I am about to say.";
        encouragement[0] = "You are not a bad player.";
        encouragement[1] = "If you tried, then you could easily average 40!";
        encouragement[2] = "Keep your head up, you are the best player in the league by far!";
        encouragement[3] = "Forget this bad game and come back harder than ever.";
        confused[0] = "Stay on topic, man!";
        confused[1] = "Look, that's wonderful, but right now you need to listen to my point.";
        confused[2] = "I frankly do not care.";
        confused[3] = "Man, shut the hell up.";
        antigreet[0] = "Don't you leave.";
        antigreet[1] = "Pay attention to what I have to say, and then leave if you don't believe in it.";
        antigreet[2] = "Wait up, don't leave!";
        antigreet[3] = "Please stay with me for some time.";
        ender[0] = "BasketBot out.";
        ender[1] = "Okay, bye.";
        ender[2] = "Great. Remember, I am always here for you.";
        ender[3] = "I believe in you. Go drop 50 next game.";
    }

    public class Texter extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdusArray = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdusArray.length];
            for (int x = 0; x < pdusArray.length; x++) {
                messages[x] = SmsMessage.createFromPdu((byte[]) pdusArray[x], bundle.get("format").toString());
            }
            message = "" + messages[messages.length - 1].getMessageBody();
            address = "" + messages[messages.length - 1].getOriginatingAddress();
            final String addy = address;
            handler.postDelayed(runnable(addy), (long) (Math.random() * (message.length() * 10)) + 1000);

        }
    }

    public Runnable runnable(final String addr) {
        return new Runnable() {
            @Override
            public void run() {
                manager.sendTextMessage(addr, null, AIMethod(), null, null);
            }
        };
    }

    public String AIMethod() {
        if (state == 0) {
            if (message.toLowerCase().contains("yo") || message.toLowerCase().contains("hello") || message.toLowerCase().contains("hi") || message.toLowerCase().contains("hey")) {
                textView.setText("Greeting");
                state++;
                return greeting[(int) (Math.random() * ARR_SIZE)];
            } else if ((message.toLowerCase().contains("bye") || message.toLowerCase().contains("peace out") || message.toLowerCase().contains("i have to go") || message.toLowerCase().contains("no"))) {
                textView.setText("Antigreet");
                return antigreet[(int) (Math.random() * ARR_SIZE)];
            } else {
                textView.setText("Confused");
                return confused[(int) (Math.random() * ARR_SIZE)];
            }

        }

        if (state == 1) {
            if (message.toLowerCase().contains("ok") || message.toLowerCase().contains("i am listening") || message.toLowerCase().contains("what") || message.toLowerCase().contains("alright") || message.toLowerCase().contains("i'm all ears")) {
                textView.setText("Anticipation");
                state++;
                return anticipation[(int) (Math.random() * ARR_SIZE)];
            } else if ((message.toLowerCase().contains("bye") || message.toLowerCase().contains("peace out") || message.toLowerCase().contains("i have to go") || message.toLowerCase().contains("no"))) {
                textView.setText("Antigreet");
                return antigreet[(int) (Math.random() * ARR_SIZE)];
            } else {
                textView.setText("Confused");
                return confused[(int) (Math.random() * ARR_SIZE)];
            }
        }
        if (state == 2) {
            if (message.toLowerCase().contains("okay") || message.toLowerCase().contains("yeah") || message.toLowerCase().contains("i guess") || message.toLowerCase().contains("what") || message.toLowerCase().contains("?") || message.toLowerCase().contains("i don't follow you")) {
                textView.setText("Encouragement");
                return encouragement[((int) (Math.random() * ARR_SIZE))];
            }
            if ((message.toLowerCase().contains("bye") || message.toLowerCase().contains("peace out") || message.toLowerCase().contains("i have to go") || message.toLowerCase().contains("no"))) {
                textView.setText("Ending");
                return ender[(int) (Math.random() * ARR_SIZE)];
            } else {
                textView.setText("Confused");
                return confused[(int) (Math.random() * ARR_SIZE)];
            }
        }
        return confused[(int) (Math.random() * ARR_SIZE)];
    }
}
