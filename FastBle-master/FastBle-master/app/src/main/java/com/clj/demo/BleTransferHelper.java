package com.clj.demo;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;

import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuke on 2016/11/20
 * Email liuke0002@outlook.com
 */
public class BleTransferHelper {

    private Activity context;
    private BleManager bleManager;

    public BleTransferHelper(Activity context, BleManager bleManager) {
        this.context = context;
        this.bleManager = bleManager;
    }

    private ScheduledExecutorService scheduler;

    private int numWrite = 0;

    private boolean flag = true;



    private byte[][] divideLongBytes(byte[] encodeRegisterResponse) {
        int registerResponseLen = encodeRegisterResponse.length;
        int pkgNum = (registerResponseLen % 19 == 0) ? registerResponseLen / 19 : registerResponseLen / 19 + 1;
        final byte[][] dataToSend = new byte[pkgNum + 1][20];

        for (int i = 0; i < pkgNum; i++) {
            byte[] dest = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            if ((i + 1) * 19 > registerResponseLen) {
                dest[0] = (byte) i;
                System.arraycopy(encodeRegisterResponse, i * 19, dest, 1, registerResponseLen - i * 19);
                System.arraycopy(dest, 0, dataToSend[i], 0, 20);
            } else {
                dest[0] = (byte) i;
                System.arraycopy(encodeRegisterResponse, i * 19, dest, 1, 19);
                System.arraycopy(dest, 0, dataToSend[i], 0, 20);
            }
        }
        dataToSend[pkgNum] = U2FConsts.FINISH_PACK_BYTES;
        return dataToSend;
    }

    private void startWrite(String serviceUUID, final String characterUUID, byte[] dataTosend) {
        boolean suc = bleManager.writeDevice(
                serviceUUID,
                characterUUID,
                dataTosend,
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        numWrite++;
                        flag = true;
                    }
                    @Override
                    public void onFailure(BleException exception) {
                        bleManager.handleException(exception);
                    }
                });
        if (!suc) {
            Toast.makeText(context, "Write Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void loopToSend(final byte[][] dataToSend, final String serviceUUID, final String characteristicUUID) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (flag == true && numWrite < dataToSend.length) {
                    flag = false;
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startWrite(serviceUUID, characteristicUUID, dataToSend[numWrite]);
                        }
                    });
                }
                if (numWrite == dataToSend.length) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"Write Success",Toast.LENGTH_SHORT).show();
                        }
                    });
                    scheduler.shutdown();
                    flag = false;
                    bleManager.stopListenCharacterCallback(characteristicUUID);
                }
            }
        }, 5, 5, TimeUnit.MILLISECONDS);
    }

    public void writeBytes(byte[] data, final String serviceUUID, final String characteristicUUID) {
        numWrite = 0;
        flag = true;
        bleManager=BleManager.getInstance();
        bleManager.init(context);
        if (data.length > 20) {
            byte[][] dataToSend = divideLongBytes(data);
            loopToSend(dataToSend, serviceUUID, characteristicUUID);
        } else {
            startWrite(serviceUUID, characteristicUUID, data);
        }
    }

}
