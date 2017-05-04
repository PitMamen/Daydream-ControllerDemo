package com.Szmygt.app.vr;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import com.Szmygt.app.vr.ble.BluetoothLeService;
import com.Szmygt.app.vr.common.Constants;
import com.Szmygt.app.vr.utils.ObjectStore;
import com.unity3d.player.UnityPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UnityPlayerActivity extends Activity implements BluetoothLeService.Callback {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private int time;
    private int seq;
    private int xOri;
    private int yOri;
    private int zOri;
    private int xAcc;
    private int yAcc;
    private int zAcc;
    private int xGyro;
    private int yGyro;
    private int zGyro;
    private int xTouch;
    private int yTouch;
    private BluetoothAdapter mBluetoothAdapter;

    private float mFloat = 0.0f;

    public static final String TAG = UnityPlayerActivity.class.getSimpleName();


    private List<String> addressList = new ArrayList<String>();

    private Set<String> rigAddressList = new HashSet<String>();  //存储蓝牙设备

    private BluetoothLeService mBluetoothLeService;

    private boolean mConnected = false;


    // 管理服务的生命周期
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("haha", "Unable to initialize Bluetooth");
                finish();
            }
            connectService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private byte[] mData;
    private float[] mFloatdata = new float[4];
    private  int[] intData = new int[3];

    // 连接服务
    private void connectService() {
        if (mBluetoothLeService != null) {
            mBluetoothLeService.registerCallback(this);
            for (String address : addressList) {
                final boolean result = mBluetoothLeService.connect(address);
                //写数据
                byte[] writeBytes = new byte[20];
                writeBytes[0] = (byte) 0x00;
                mBluetoothLeService.writeCharacteristic(writeBytes);
                Log.d("haha", "Connect request result=" + result + ",address:" + address);
                if (result) {
                    rigAddressList.add(address);
//					addressView.setText(address);
                }
            }
        }
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String address = bundle.getString(Constants.EXTRA_ADDRESS);
            Log.d("TATA"," " + msg.what);
            switch (msg.what) {
                case BluetoothLeService.ACTION_GATT_CONNECTED:
                    mConnected = true;
//					addressView.setText(address);
                    rigAddressList.add(address);
                case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED:
                    rigAddressList.add(address);
                    break;
                case BluetoothLeService.ACTION_GATT_DISCONNECTED:
                    mConnected = false;
                    rigAddressList.remove(address);
                    // updateConnectionState(R.string.disconnected);
                    break;
                case BluetoothLeService.ACTION_DATA_AVAILABLE:
                    // 将数据显示在mDataField上

                    mData = bundle.getByteArray(Constants.EXTRA_DATA);


                 //   googleAnalysis();

                    selfAnalysis();
                    System.out.println(mFloatdata[0]+"  "+mFloatdata[1]+"  "+mFloatdata[2]+"  "+mFloatdata[3]);
                    //googleAnalysisLog();
                    break;
            }
        }
    };

    private void googleAnalysisLog() {
        Log.e("googleAnalysisLog", "time=="+time+
        "   " + "seq==" + seq +
        "   " + "xOri==" + xOri +
        "   " + "yOri==" + yOri +
        "   " + "zOri==" + zOri +
        "   " + "xAcc==" + xAcc +
        "   " + "yAcc==" + yAcc +
        "   " + "zAcc==" + zAcc +
        "   " + "xGyro==" + xGyro +
        "   " + "yGyro==" + yGyro +
        "   " + "zGyro==" + zGyro +
        "   " + "xTouch==" + xTouch +
        "   " + "yTouch==" + yTouch);
    }

    private void selfAnalysis() {
        for (int i = 0; i < mData.length / 4; i++) {
            mFloat = getFloat(mData[3 + i * 4], mData[2 + i * 4], mData[1 + i * 4], mData[0 + i * 4]);
            mFloatdata[i] = mFloat;

            Log.e(TAG, "floatvalue====: " + mFloat);
            System.out.println(mFloatdata.length);
        }
    }

    private void googleAnalysis() {
        time = ((mData[0] & 0xFF) << 1 | (mData[1] & 0x80) >> 7);
        seq = (mData[1] & 0x7C) >> 2;

        //欧拉角X
        xOri = (mData[1] & 0x03) << 11 | (mData[2] & 0xFF) << 3 | (mData[3] & 0x80) >> 5;
        xOri = (xOri << 19) >> 19;
        intData[0] = xOri;


        //欧拉角Y
        yOri = (mData[3] & 0x1F) << 8 | (mData[4] & 0xFF);
        yOri = (yOri << 19) >> 19;

        intData[1] = yOri;
        //欧拉角Z
        zOri = (mData[5] & 0xFF) << 5 | (mData[6] & 0xF8) >> 3;
        zOri = (zOri << 19) >> 19;
        intData[2] = zOri;
        Log.d("papa", "intData.lenght===: "+intData.length+"  "+"value1=="+intData[0]+"  "+"value2=="+intData[1]+"  "+"value3=="+intData[2]);
        xAcc = (mData[6] & 0x07) << 10 | (mData[7] & 0xFF) << 2 | (mData[8] & 0xC0) >> 6;
        xAcc = (xAcc << 19) >> 19;


        yAcc = (mData[8] & 0x3F) << 7 | (mData[9] & 0xFE) >> 1;
        yAcc = (yAcc << 19) >> 19;


        zAcc = (mData[9] & 0x01) << 12 | (mData[10] & 0xFF) << 4 | (mData[11] & 0xF0) >> 4;
        zAcc = (zAcc << 19) >> 19;


        xGyro = ((mData[11] & 0x0F) << 9 | (mData[12] & 0xFF) << 1 | (mData[13] & 0x80) >> 7);
        xGyro = (xGyro << 19) >> 19;


        yGyro = ((mData[13] & 0x7F) << 6 | (mData[14] & 0xFC) >> 2);
        yGyro = (yGyro << 19) >> 19;


        zGyro = ((mData[14] & 0x03) << 11 | (mData[15] & 0xFF) << 3 | (mData[16] & 0xE0) >> 5);
        zGyro = (zGyro << 19) >> 19;

        xTouch = ((mData[16] & 0x1F) << 3 | (mData[17] & 0xE0) >> 5);
        yTouch = ((mData[17] & 0x1F) << 3 | (mData[18] & 0xE0) >> 5);
    }

    public float[] getfloatArray() {
        if (mFloatdata != null) {
            return mFloatdata;
        }
        return null;
    }
    public  int[] getIntData(){
        if (intData!=null){
            Log.d("pipi", "intData.lenght===: "+intData.length+"  "+"value1=="+intData[0]+"  "+"value2=="+intData[1]+"  "+"value3=="+intData[2]);
            return intData;
        }
        return null;
    }


    public float getFloat(byte data1, byte data2, byte data3, byte data4) {
        byte[] mdata = {data1, data2, data3, data4};
        DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(mdata));
        float f = 0.0f;
        try {
            f = dataStream.readFloat();
            dataStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();

        if (savedInstanceState == null) {
            ObjectStore.put("UnityPlayerActivity", UnityPlayerActivity.this);
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //获取已经保存过的设备信息
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bluetoothDevice : devices) {
            Log.d("haha", "bond:" + bluetoothDevice.getBondState());

            addressList.add(bluetoothDevice.getAddress());
            Log.d("haha", "devices：" + bluetoothDevice.getName() + ", " + bluetoothDevice.getAddress());
        }

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.unRegisterCallback(this);
        }
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothLeService.unRegisterCallback(this);
        unRegister();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        register();
        connectService();
        mUnityPlayer.resume();
    }

    boolean isRegister = false;

    private void register() {
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        isRegister = true;
    }

    private void unRegister() {
        if (isRegister) {
            unregisterReceiver(mGattUpdateReceiver);
            isRegister = false;
        }
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //system
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return intentFilter;
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }


    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }


    @Override
    public void onDispatchData(int type, byte[] data, String address) {
        Log.d("测试次数DeviceControl", "onDispatchData.........");
        Message message = handler.obtainMessage(type);       //从已经得到数据的回调中  再通过handler发送
        Bundle bundle = new Bundle();
        bundle.putByteArray(Constants.EXTRA_DATA, data);
        bundle.putString(Constants.EXTRA_ADDRESS, address);
        message.setData(bundle);
        handler.sendMessage(message);
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d("haha", "action:" + action);
            String address;
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                address = device.getAddress();
                rigAddressList.add(address);
                Log.d("haha", "devices:" + device.getName() + "address:" + address);
            }
        }
    };
}
