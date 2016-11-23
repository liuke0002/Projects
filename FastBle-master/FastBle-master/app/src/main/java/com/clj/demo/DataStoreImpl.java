package com.clj.demo;

import android.content.SharedPreferences;

import com.clj.fastble.utils.HexUtil;

import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.encoders.Hex;

import java.security.KeyPair;

/**
 * Created by liuke on 2016/11/19
 * <p/>
 * Email liuke0002@outlook.com
 */
public class DataStoreImpl implements DataStore {

    private SharedPreferences sp;
    private Crypto crypto;

    public DataStoreImpl(SharedPreferences sp, Crypto crypto) {
        this.sp = sp;
        this.crypto = crypto;
    }

    public void storeKeyPair(byte[] keyHandle, KeyPair keyPair) {
        try {
            sp.edit().putString(HexUtil.encodeHexStr(keyHandle), Base64.toBase64String(crypto.objectToBytes(keyPair))).commit();
        } catch (U2FException e) {
            e.printStackTrace();
        }
    }

    public KeyPair getKeyPair(byte[] keyHandle) {
        String keyPairBase64 = sp.getString(HexUtil.encodeHexStr(keyHandle), null);
        byte[] decode = Base64.decode(keyPairBase64);
        Object keyPair = null;
        try {
            keyPair = crypto.bytesToObject(decode);
        } catch (U2FException e) {
            e.printStackTrace();
        }
        return (KeyPair) keyPair;
    }

    @Override
    public int incrementCounter() {
        int count = sp.getInt(U2FConsts.U2F_COUNTER, 0);
        sp.edit().putInt(U2FConsts.U2F_COUNTER, count + 1);
        return count + 1;
    }
}
