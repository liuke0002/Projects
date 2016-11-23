// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.clj.demo;

import android.util.Log;

import com.clj.demo.messages.AuthenticateRequest;
import com.clj.demo.messages.AuthenticateResponse;
import com.clj.demo.messages.RegisterRequest;
import com.clj.demo.messages.RegisterResponse;
import com.clj.fastble.utils.HexUtil;

import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.spec.ECParameterSpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;


public class U2FKeyReferenceImpl implements U2FKey {

    private  X509Certificate vendorCertificate;
    private  PrivateKey certificatePrivateKey;
    private final DataStore dataStore;
    private final UserPresenceVerifier userPresenceVerifier;
    private final Crypto crypto;

    public U2FKeyReferenceImpl(X509Certificate vendorCertificate, PrivateKey certificatePrivateKey,
                               DataStore dataStore, UserPresenceVerifier userPresenceVerifier, Crypto crypto) {
        this.vendorCertificate = vendorCertificate;
        this.certificatePrivateKey = certificatePrivateKey;
        this.dataStore = dataStore;
        this.userPresenceVerifier = userPresenceVerifier;
        this.crypto = crypto;
    }

    public U2FKeyReferenceImpl(DataStore dataStore, UserPresenceVerifier userPresenceVerifier, Crypto crypto) {
        this.dataStore = dataStore;
        this.userPresenceVerifier = userPresenceVerifier;
        this.crypto = crypto;
    }

    private byte[] stripMetaData(byte[] a) {
        ByteInputStream bis = new ByteInputStream(a);
        bis.read(3);
        bis.read(bis.readUnsigned() + 1);
        int keyLength = bis.readUnsigned();
        bis.read(1);
        return bis.read(keyLength - 1);
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws U2FException {

        byte[] applicationSha256 = registerRequest.getApplicationSha256();
        byte[] challengeSha256 = registerRequest.getChallengeSha256();

        byte userPresent = userPresenceVerifier.verifyUserPresence();
        if ((userPresent & UserPresenceVerifier.USER_PRESENT_FLAG) == 0) {
            throw new U2FException("Cannot verify user presence");
        }

        // generate ECC key
        SecureRandom random = new SecureRandom();
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        KeyPairGenerator g = null;
        try {
            g = KeyPairGenerator.getInstance("ECDSA");
            g.initialize(ecSpec, random);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        KeyPair keyPair = g.generateKeyPair();
        byte[] keyHandle = new byte[64];
        random.nextBytes(keyHandle);
        byte[] userPublicKey = stripMetaData(keyPair.getPublic().getEncoded());
        Log.d("userPrivateKey1",HexUtil.encodeHexStr(keyPair.getPrivate().getEncoded()));

        Log.d("storedKeyHandler", HexUtil.encodeHexStr(keyHandle));
        dataStore.storeKeyPair(keyHandle, keyPair);


        byte[] signedData = RawMessageCodec.encodeRegistrationSignedBytes(applicationSha256, challengeSha256,
                keyHandle, userPublicKey);


        byte[] signature = crypto.sign(signedData, certificatePrivateKey);
        return new RegisterResponse(userPublicKey, keyHandle, vendorCertificate, signature);
    }

    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest)
            throws U2FException {
//        byte control = authenticateRequest.getControl();
        byte[] applicationSha256 = authenticateRequest.getApplicationSha256();
        byte[] challengeSha256 = authenticateRequest.getChallengeSha256();
        byte[] keyHandle = authenticateRequest.getKeyHandle();

        KeyPair keyPair = dataStore.getKeyPair(keyHandle);
        PrivateKey privateKey=keyPair.getPrivate();
        Log.d("userPrivateKey2",HexUtil.encodeHexStr(privateKey.getEncoded()));

        int counter = dataStore.incrementCounter();
        byte userPresence = userPresenceVerifier.verifyUserPresence();
        byte[] signedData = RawMessageCodec.encodeAuthenticateSignedBytes(applicationSha256, userPresence,
                counter, challengeSha256);



        byte[] signature = crypto.sign(signedData, privateKey);


        return new AuthenticateResponse(userPresence, counter, signature);
    }
}
