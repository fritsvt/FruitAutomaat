package tech.frits.fruitautomaat;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView msg, txt_credtits;
    private ImageView img1, img2, img3;
    private Wheel wheel1, wheel2, wheel3;
    private Button btn, btn_buy;
    private boolean isStarted;
    private int credits = 10;
    private boolean rigged = false;

    public static final Random RANDOM = new Random();

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        btn = (Button) findViewById(R.id.btn);
        btn_buy = (Button) findViewById(R.id.btn_buy);
        msg = (TextView) findViewById(R.id.msg);
        txt_credtits = (TextView) findViewById(R.id.txt_credits);

        btn_buy.setEnabled(false);

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credits += 10;
                txt_credtits.setText(credits + " credits");
                btn_buy.setEnabled(false);
                btn.setEnabled(true);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStarted && credits > 0) {
                    credits --;

                    wheel1 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img1.setImageResource(img);
                                }
                            });
                        }
                    }, 10, randomLong(0, 200));

                    wheel1.start();

                    wheel2 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img2.setImageResource(img);
                                }
                            });
                        }
                    }, 10, randomLong(150, 400));

                    wheel2.start();

                    wheel3 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img3.setImageResource(img);
                                }
                            });
                        }
                    }, 10, randomLong(150, 400));

                    wheel3.start();

                    msg.setText("");
                    isStarted = true;

                    // Generate random time.
                    Random r = new Random();
                    int Low = 1000;
                    int High = 4000;
                    int RandomTime = r.nextInt(High-Low) + Low;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            wheel1.stopWheel();
                            wheel2.stopWheel();
                            wheel3.stopWheel();
                            if (rigged) {
                                Message RiggedMessage = Message.obtain(SetRiggedSlots);
                                RiggedMessage.sendToTarget();
                                wheel1.currentIndex = 1;
                                wheel2.currentIndex = 1;
                                wheel3.currentIndex = 1;
                            }

                            Message stringMessage = Message.obtain(myTextHandler);
                            Message creditsMessage = Message.obtain(CreditsHandler);

                            if (wheel1.currentIndex == wheel2.currentIndex && wheel2.currentIndex == wheel3.currentIndex) {
                                stringMessage.obj = "Winner winner chicken dinner! +20 credits";
                                credits += 20;
                            } else if (wheel1.currentIndex == wheel2.currentIndex || wheel2.currentIndex == wheel3.currentIndex
                                    || wheel1.currentIndex == wheel3.currentIndex) {
                                stringMessage.obj = "Kleine prijs +4 credits";

                                credits += 4;
                            } else {
                                stringMessage.obj = "Volgende keer beter";
                            }
                            stringMessage.sendToTarget();

                            creditsMessage.obj = credits + " credits";
                            creditsMessage.sendToTarget();
                            isStarted = false;

                            Message buyBtnMessage = Message.obtain(BuyButtonHandler);
                            buyBtnMessage.sendToTarget();
                        }

                    }, RandomTime);
                }
            }
        });
    }

    private final Handler myTextHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message stringMessage) {
            msg.append((String) stringMessage.obj);
            return true;
        }
    });

    private final Handler CreditsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message creditsMessage) {
            txt_credtits.setText((String) creditsMessage.obj);
            return true;
        }
    });

    private final Handler BuyButtonHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (credits < 1) {
                btn_buy.setEnabled(true);
                btn.setEnabled(false);
            } else {
                btn_buy.setEnabled(false);
                btn.setEnabled(true);
            }
            return true;
        }
    });

    private final Handler SetRiggedSlots = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            img1.setImageResource(R.drawable.fruit_1);
            img2.setImageResource(R.drawable.fruit_1);
            img3.setImageResource(R.drawable.fruit_1);
            return true;
        }
    });

}
