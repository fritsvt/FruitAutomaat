package tech.frits.fruitautomaat;

import static tech.frits.fruitautomaat.R.id.imgs;

/**
 * Created by frits on 13/10/2017.
 */

public class Wheel extends Thread {

    interface WheelListener {
        void newImage(int img);
    }

    private static int[] images = {R.drawable.fruit_1, R.drawable.fruit_2, R.drawable.fruit_3, R.drawable.fruit_4,
            R.drawable.fruit_5, R.drawable.fruit_6, R.drawable.fruit_7, R.drawable.fruit_8, R.drawable.fruit_9, R.drawable.fruit_10,
            R.drawable.fruit_11, R.drawable.fruit_12, R.drawable.fruit_13, R.drawable.fruit_14, R.drawable.fruit_15, R.drawable.fruit_16,
            R.drawable.fruit_17, R.drawable.fruit_18, R.drawable.fruit_19, R.drawable.fruit_20};

    public int currentIndex;
    private WheelListener wheelListener;
    private long frameDuration;
    private long startIn;
    private boolean isStarted;

    public Wheel(WheelListener wheelListener, long frameDuration, long startIn) {
        this.wheelListener = wheelListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        currentIndex = 0;
        isStarted = true;
    }

    public void nextImg() {
        currentIndex++;

        if (currentIndex == images.length) {
            currentIndex = 0;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }

        while(isStarted) {
            try {
                Thread.sleep(frameDuration);
                frameDuration += 1;
            } catch (InterruptedException e) {
            }

            nextImg();

            if (wheelListener != null) {
                wheelListener.newImage(images[currentIndex]);
            }
        }
    }

    public void stopWheel() {
        isStarted = false;
    }
}