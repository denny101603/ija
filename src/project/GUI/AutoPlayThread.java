/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * AutoPlayThread class.
 *
 * */
package project.GUI;

public class AutoPlayThread implements Runnable
{
    private volatile Thread blinker;
    private TabViewController controller;
    private int speed;

    public AutoPlayThread(TabViewController controller, int speed)
    {
        this.controller = controller;
        this.speed = speed;
    }

    public void myStop() {
        Thread moribund = blinker;
        blinker = null;
        moribund.interrupt();
    }

    public void start() {
        blinker = new Thread(this);
        blinker.start();
    }


    public void run()
    {
        Thread thisThread = Thread.currentThread();
        while (blinker == thisThread)
        {
            try
            {
                if(!controller.DoNextMove(false))
                {
                    controller.disableAutoplayButtons(false);
                    myStop();
                }
                thisThread.sleep(speed);
            } catch (InterruptedException e)
            {
            }
        }
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
        blinker.interrupt();
    }

    public int getSpeed()
    {
        return speed;
    }
}
