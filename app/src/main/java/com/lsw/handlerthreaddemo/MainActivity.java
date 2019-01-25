package com.lsw.handlerthreaddemo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * 图片地址集合,图片来自网络.
     */
    private String url[]={
            "https://img-my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383264_8243.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383248_3693.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383243_5120.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383242_3127.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383242_9576.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383242_1721.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383219_5806.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383214_7794.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383213_4418.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383213_3557.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383210_8779.jpg",
            "https://img-my.csdn.net/uploads/201407/26/1406383172_4577.jpg"
    };
    Handler mainHandler,workHandler;
    HandlerThread mHandlerThread;
    TextView text;
    Button button1,button2,button3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // 创建与主线程关联的Handler
        mainHandler = new Handler();

        /**
         * 步骤1：创建HandlerThread实例对象
         * 传入参数 = 线程名字，作用 = 标记该线程
         */
        mHandlerThread = new HandlerThread("handlerThread");

        /**
         * 步骤2：启动线程
         */
        mHandlerThread.start();

        /**
         * 步骤3：创建工作线程Handler & 复写handleMessage（）
         * 作用：关联HandlerThread的Looper对象、实现消息处理操作 & 与其他线程进行通信
         * 注：消息处理操作（HandlerMessage（））的执行线程 = mHandlerThread所创建的工作线程中执行
         */

        workHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            // 消息处理的操作
            public void handleMessage(Message msg)
            {
                //设置了两种消息处理操作,通过msg来进行识别
                switch(msg.what){
                    // 消息1
                    case 1:
                        try {
                            //延时操作
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 通过主线程Handler.post方法进行在主线程的UI更新操作
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run () {
                                text.setText("我爱学习");
                            }
                        });
                        break;

                    // 消息2
                    case 2:
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run () {
                                text.setText("我不喜欢学习");
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };

    }

    private void initView(){

        // 显示文本
        text = (TextView) findViewById(R.id.text1);

        /**
         * 步骤4：使用工作线程Handler向工作线程的消息队列发送消息
         * 在工作线程中，当消息循环时取出对应消息 & 在工作线程执行相关操作
         */
        // 点击Button1
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 通过sendMessage（）发送
                // a. 定义要发送的消息
                Message msg = Message.obtain();
                msg.what = 1; //消息的标识
                msg.obj = "A"; // 消息的存放
                // b. 通过Handler发送消息到其绑定的消息队列
                workHandler.sendMessage(msg);
            }
        });

        // 点击Button2
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 通过sendMessage（）发送
                // a. 定义要发送的消息
                Message msg = Message.obtain();
                msg.what = 2; //消息的标识
                msg.obj = "B"; // 消息的存放
                // b. 通过Handler发送消息到其绑定的消息队列
                workHandler.sendMessage(msg);
            }
        });

        // 点击Button3
        // 作用：退出消息循环
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandlerThread.quit();
            }
        });
    }
}
