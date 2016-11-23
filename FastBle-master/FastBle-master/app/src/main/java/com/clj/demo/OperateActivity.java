package com.clj.demo;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.demo.messages.AuthenticateRequest;
import com.clj.demo.messages.AuthenticateResponse;
import com.clj.demo.messages.RegisterRequest;
import com.clj.demo.messages.RegisterResponse;
import com.clj.fastble.BleManager;
import com.clj.fastble.bluetooth.BleGattCallback;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.ListScanCallback;
import com.clj.fastble.utils.HexUtil;

import org.spongycastle.util.encoders.Base64;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;


public class OperateActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = OperateActivity.class.getSimpleName();

    private LinearLayout layout_item_connect;
    private LinearLayout layout_device_list;
    private EditText et_device_name;
    private Crypto crypto;
    private LinearLayout layout_item_state;
    private TextView txt_device_name;
    private LinearLayout layout_character_list;

    private BleManager bleManager;
    private ProgressDialog progressDialog;

    private ScheduledExecutorService scheduler;
    private SharedPreferences sp;
    private DataStoreImpl dataStore;
    private UserPresenceVerifierImpl userPresenceVerifier;
    private BleTransferHelper bleTransferHelper;
    private String string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        findViewById(R.id.btn_scan).setOnClickListener(this);
        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.btn_disconnect).setOnClickListener(this);

        sp = getSharedPreferences(U2FConsts.U2F_SP, MODE_PRIVATE);
        crypto = new SpongyCastleCrypto();
        dataStore = new DataStoreImpl(sp, crypto);
        userPresenceVerifier = new UserPresenceVerifierImpl();
        bleTransferHelper = new BleTransferHelper(OperateActivity.this, bleManager);
        layout_item_connect = (LinearLayout) findViewById(R.id.layout_item_connect);
        layout_device_list = (LinearLayout) findViewById(R.id.layout_device_list);
        et_device_name = (EditText) findViewById(R.id.et_device_name);

        layout_item_state = (LinearLayout) findViewById(R.id.layout_item_state);
        txt_device_name = (TextView) findViewById(R.id.txt_device_name);
        layout_character_list = (LinearLayout) findViewById(R.id.layout_character_list);

        bleManager = BleManager.getInstance();
        bleManager.init(this);

        showDisConnectState();
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleManager.closeBluetoothGatt();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                scanDevice();
                break;

            case R.id.btn_connect:
                String deviceName = et_device_name.getText().toString().trim();
                if (!TextUtils.isEmpty(deviceName)) {
                    connectNameDevice(deviceName);
                }
                break;

            case R.id.btn_disconnect:
                showDisConnectState();
                break;
        }
    }

    /**
     * 搜索周围蓝牙设备
     */
    private void scanDevice() {
        if (bleManager.isInScanning())
            return;

        progressDialog.show();

        bleManager.scanDevice(new ListScanCallback(10000) {
            @Override
            public void onDeviceFound(final BluetoothDevice[] devices) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDeviceList(devices);
                    }
                });
            }

            @Override
            public void onScanTimeout() {
                super.onScanTimeout();
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 显示蓝牙设备列表
     */
    private void showDeviceList(final BluetoothDevice[] devices) {
        layout_device_list.removeAllViews();

        for (int i = 0; devices != null && i < devices.length; i++) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.layout_list_item_device, null);

            RelativeLayout layout_item_device = (RelativeLayout) itemView.findViewById(R.id.layout_list_item_device);
            TextView txt_item_name = (TextView) itemView.findViewById(R.id.txt_item_name);

            final BluetoothDevice device = devices[i];
            txt_item_name.setText(device.getName());
            layout_item_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    connectSpecialDevice(device);
                }
            });

            layout_device_list.addView(itemView);
        }
    }

    /**
     * 连接设备
     */
    private void connectSpecialDevice(final BluetoothDevice device) {
        progressDialog.show();
        bleManager.connectDevice(device, new BleGattCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                gatt.discoverServices();
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        showConnectState(device.getName(), gatt);
                    }
                });
            }

            @Override
            public void onConnectFailure(BleException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        showDisConnectState();
                    }
                });
                bleManager.handleException(exception);
            }
        });
    }

    /**
     * 直连某一蓝牙设备
     */
    private void connectNameDevice(final String deviceName) {
        progressDialog.show();
        bleManager.connectDevice(deviceName, 10000, new BleGattCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                gatt.discoverServices();
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        showConnectState(deviceName, gatt);
                    }
                });
            }

            @Override
            public void onConnectFailure(BleException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        showDisConnectState();
                    }
                });
                bleManager.handleException(exception);
            }
        });
    }


    /**
     * 显示未连接状态
     */
    private void showDisConnectState() {
        bleManager.closeBluetoothGatt();

        layout_item_connect.setVisibility(View.VISIBLE);
        layout_item_state.setVisibility(View.GONE);

        layout_device_list.removeAllViews();
    }


    /**
     * 显示连接状态
     */
    private void showConnectState(String deviceName, BluetoothGatt gatt) {
        bleManager.getBluetoothState();

        layout_item_connect.setVisibility(View.GONE);
        layout_item_state.setVisibility(View.VISIBLE);
        txt_device_name.setText(deviceName);

        layout_character_list.removeAllViews();
        if (gatt != null) {
            for (final BluetoothGattService service : gatt.getServices()) {
                View serviceView = LayoutInflater.from(this).inflate(R.layout.layout_list_item_service, null);
                TextView txt_service = (TextView) serviceView.findViewById(R.id.txt_service);
                txt_service.setText(service.getUuid().toString());

                layout_character_list.addView(serviceView);

                for (final BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    View characterView = LayoutInflater.from(this).inflate(R.layout.layout_list_item_character, null);
                    characterView.setTag(characteristic.getUuid().toString());
                    TextView txt_character = (TextView) characterView.findViewById(R.id.txt_character);
                    final Button btn_properties = (Button) characterView.findViewById(R.id.btn_properties);
                    TextView txt_value = (TextView) characterView.findViewById(R.id.txt_value);

                    txt_character.setText(characteristic.getUuid().toString());
                    txt_value.setText(Arrays.toString(characteristic.getValue()));
                    switch (characteristic.getProperties()) {
                        case 2:
                            btn_properties.setText(String.valueOf("read"));
                            btn_properties.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (btn_properties.getText().toString().equals("read")) {
                                        startRead(service.getUuid().toString(), characteristic.getUuid().toString());
                                    } else if (btn_properties.getText().toString().equals("stopListen")) {
                                        stopListen(characteristic.getUuid().toString());
                                        btn_properties.setText(String.valueOf("read"));
                                    }
                                }
                            });
                            break;

                        case 8:
                            btn_properties.setText(String.valueOf("write"));
                            btn_properties.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (btn_properties.getText().toString().equals("write")) {
                                        if (!TextUtils.isEmpty(notifyString)) {
                                            if (U2FUtil.isRegister(notifyString)) {
                                                Map<String, byte[]> regFromKey = U2FUtil.parseRegFromKey(notifyString);
                                                notifyString="";
                                                appIdHushByte = regFromKey.get(U2FConsts.APPLICATION_ID_HASH);
                                                clientDataHushByte = regFromKey.get(U2FConsts.CLIENT_DATA_HASH);
                                                RegisterRequest registerRequest = new RegisterRequest(appIdHushByte, clientDataHushByte);
                                                X509Certificate vendorCertificate = null;
                                                PrivateKey certificatePrivateKey = null;
                                                try {
                                                    CertAndKeyGen keyPair = new CertAndKeyGen("ECDSA", "SHA256withECDSA", "SC");
                                                    keyPair.generate(256);
                                                    certificatePrivateKey = keyPair.getPrivateKey();
                                                    vendorCertificate = keyPair.getSelfCertificate(new X500Name("CN=ROOT"), new Date(), (long) 1096 * 24 * 60 * 60);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                U2FKey u2FKey = new U2FKeyReferenceImpl(vendorCertificate, certificatePrivateKey, dataStore, userPresenceVerifier, crypto);
                                                byte[] encodeRegisterResponse = null;
                                                try {
                                                    RegisterResponse registerResponse = u2FKey.register(registerRequest);
                                                    encodeRegisterResponse = RawMessageCodec.encodeRegisterResponse(registerResponse);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                bleTransferHelper.writeBytes(encodeRegisterResponse, service.getUuid().toString(), characteristic.getUuid().toString());
                                            }else{
                                                Map<String, byte[]> authFromKey = U2FUtil.parseAuthFromKey(notifyString);
                                                notifyString="";
                                                clientDataHushByte=authFromKey.get(U2FConsts.CLIENT_DATA_HASH);
                                                appIdHushByte=authFromKey.get(U2FConsts.APPLICATION_ID_HASH);
                                                keyHandle=authFromKey.get(U2FConsts.KEYHANDLE);
                                                Log.d("keyHandlerFromServer",HexUtil.encodeHexStr(keyHandle));
                                                Log.d("client")
                                                AuthenticateRequest authenticateRequest = new AuthenticateRequest(AuthenticateRequest.USER_PRESENCE_SIGN, clientDataHushByte, appIdHushByte, keyHandle);
                                                U2FKey u2FKey = new U2FKeyReferenceImpl(dataStore, userPresenceVerifier, crypto);
                                                byte[] encodeAuthenticateResponse = null;
                                                try {
                                                    AuthenticateResponse authenticate = u2FKey.authenticate(authenticateRequest);
                                                    encodeAuthenticateResponse = RawMessageCodec.encodeAuthenticateResponse(authenticate);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                bleTransferHelper.writeBytes(encodeAuthenticateResponse, service.getUuid().toString(), characteristic.getUuid().toString());
                                            }
                                        }
                                    } else if (btn_properties.getText().toString().equals("stopListen")) {
                                        stopListen(characteristic.getUuid().toString());
                                        btn_properties.setText(String.valueOf("write"));
                                    }
                                }
                            });
                            break;

                        case 16:
                            btn_properties.setText(String.valueOf("notify"));
                            btn_properties.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (btn_properties.getText().toString().equals("notify")) {
                                        startNotify(service.getUuid().toString(), characteristic.getUuid().toString());

                                    } else if (btn_properties.getText().toString().equals("stopListen")) {
                                        stopListen(characteristic.getUuid().toString());
                                        bleManager.stopNotify(service.getUuid().toString(), characteristic.getUuid().toString());
                                        btn_properties.setText(String.valueOf("notify"));
                                    }
                                }
                            });
                            break;

                        case 32:
                            btn_properties.setText(String.valueOf("indicate"));
                            btn_properties.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (btn_properties.getText().toString().equals("indicate")) {
                                        startIndicate(service.getUuid().toString(), characteristic.getUuid().toString());
                                    } else if (btn_properties.getText().toString().equals("stopListen")) {
                                        stopListen(characteristic.getUuid().toString());
                                        bleManager.stopIndicate(service.getUuid().toString(), characteristic.getUuid().toString());
                                        btn_properties.setText(String.valueOf("indicate"));
                                    }
                                }
                            });
                            break;
                    }
                    layout_character_list.addView(characterView);


                }
            }
        }
    }

    int count = 0;
    byte[] appIdHushByte = new byte[32];
    byte[] clientDataHushByte = new byte[32];
    String notifyString = "";
    byte[] keyHandle;

    StringBuffer sb = new StringBuffer();
    public void startNotify(final String serviceUUID, final String characterUUID) {
        count = 0;
        notifyString="";
        boolean suc = false;
        if (null == bleManager) {
            bleManager = BleManager.getInstance();
            bleManager.init(this);
        }
        if (null != bleManager) {
            suc = bleManager.notifyDevice(
                    serviceUUID,
                    characterUUID,
                    new BleCharacterCallback() {
                        @Override
                        public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                            String str = HexUtil.encodeHexStr(characteristic.getValue());
                            Log.d("str",str);
                            if (str.substring(0, 2).equals("ff")) {
                                notifyString = sb.toString();
                                sb.setLength(0);
                                count=0;
                                Log.d("notifyString",notifyString);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        View characterView = layout_character_list.findViewWithTag(characterUUID);
                                        if (characterView != null) {
                                            TextView txt_value = (TextView) characterView.findViewById(R.id.txt_value);
                                            if (txt_value != null) {
                                                txt_value.setText(String.valueOf(HexUtil.encodeHex(characteristic.getValue())));
                                            }
                                        }
                                    }
                                });
                            } else {
                                int i = Integer.parseInt(str.substring(0, 2));
                                if (count == i) {
                                    sb.append(str.substring(2));
                                    count++;
                                }
                            }
                        }

                        @Override
                        public void onFailure(BleException exception) {
                            bleManager.handleException(exception);
                        }
                    });
        }
        if (!suc) {
            Toast.makeText(this, "Notify Error", Toast.LENGTH_SHORT).show();
        }
    }


    private void startIndicate(String serviceUUID, final String characterUUID) {
        Log.i(TAG, "startIndicate");
        boolean suc = bleManager.indicateDevice(
                serviceUUID,
                characterUUID,
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        Log.d(TAG, "indicate success： " + '\n' + Arrays.toString(characteristic.getValue()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View characterView = layout_character_list.findViewWithTag(characterUUID);
                                if (characterView != null) {
                                    TextView txt_value = (TextView) characterView.findViewById(R.id.txt_value);
                                    if (txt_value != null) {
                                        txt_value.setText(Arrays.toString(characteristic.getValue()));
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        bleManager.handleException(exception);
                    }
                });

        if (suc) {
            View characterView = layout_character_list.findViewWithTag(characterUUID);
            if (characterView != null) {
                Button btn_properties = (Button) characterView.findViewById(R.id.btn_properties);
                btn_properties.setText(String.valueOf("stopListen"));
            }
        }
    }

    private void startRead(String serviceUUID, final String characterUUID) {
        Log.i(TAG, "startRead");
        boolean suc = bleManager.readDevice(
                serviceUUID,
                characterUUID,
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        Log.d(TAG, "read success: " + '\n' + Arrays.toString(characteristic.getValue()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View characterView = layout_character_list.findViewWithTag(characterUUID);
                                if (characterView != null) {
                                    TextView txt_value = (TextView) characterView.findViewById(R.id.txt_value);
                                    if (txt_value != null) {
                                        txt_value.setText(Arrays.toString(characteristic.getValue()));
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        bleManager.handleException(exception);
                    }
                });

        if (suc) {
            View characterView = layout_character_list.findViewWithTag(characterUUID);
            if (characterView != null) {
                Button btn_properties = (Button) characterView.findViewById(R.id.btn_properties);
                btn_properties.setText(String.valueOf("stopListen"));
            }
        }
    }

    private void stopListen(String characterUUID) {
        Log.i(TAG, "stopListen");
        bleManager.stopListenCharacterCallback(characterUUID);
    }


}
