// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.clj.demo;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.jce.spec.ECPublicKeySpec;
import org.spongycastle.math.ec.ECPoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

public class SpongyCastleCrypto implements Crypto {
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Override
    public byte[] sign(byte[] signedData, PrivateKey privateKey) throws U2FException {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(signedData);
            return signature.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new U2FException("Error when signing", e);
        } catch (SignatureException e) {
            throw new U2FException("Error when signing", e);
        } catch (InvalidKeyException e) {
            throw new U2FException("Error when signing", e);
        }
    }

    @Override
    public boolean verifySignature(X509Certificate attestationCertificate, byte[] signedBytes,
                                   byte[] signature) throws U2FException {
        return verifySignature(attestationCertificate.getPublicKey(), signedBytes, signature);
    }

    @Override
    public boolean verifySignature(PublicKey publicKey, byte[] signedBytes,
                                   byte[] signature) throws U2FException {
        try {
            Signature ecdsaSignature = Signature.getInstance("SHA256withECDSA");
            ecdsaSignature.initVerify(publicKey);
            ecdsaSignature.update(signedBytes);
            return ecdsaSignature.verify(signature);
        } catch (InvalidKeyException e) {
            throw new U2FException("Error when verifying signature", e);
        } catch (SignatureException e) {
            throw new U2FException("Error when verifying signature", e);
        } catch (NoSuchAlgorithmException e) {
            throw new U2FException("Error when verifying signature", e);
        }
    }

    public byte[] objectToBytes(Object obj) throws U2FException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream sOut = null;
        try {
            sOut = new ObjectOutputStream(out);
            sOut.writeObject(obj);
            sOut.flush();
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (IOException e) {
            throw new U2FException("Error when encode object", e);
        }
    }

    public Object bytesToObject(byte[] bytes) throws U2FException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = null;
        try {
            sIn = new ObjectInputStream(in);
            return sIn.readObject();
        } catch (IOException e) {
            throw new U2FException("Error when parse object", e);
        } catch (ClassNotFoundException e) {
            throw new U2FException("Error when parse object", e);
        }
    }

    @Override
    public PublicKey decodePublicKey(byte[] encodedPublicKey) throws U2FException {
        try {
            X9ECParameters curve = SECNamedCurves.getByName("secp256r1");
            ECPoint point;
            try {
                point = curve.getCurve().decodePoint(encodedPublicKey);
            } catch (RuntimeException e) {
                throw new U2FException("Couldn't parse user public key", e);
            }

            return KeyFactory.getInstance("ECDSA").generatePublic(
                    new ECPublicKeySpec(point,
                            new ECParameterSpec(
                                    curve.getCurve(),
                                    curve.getG(),
                                    curve.getN(),
                                    curve.getH())));
        } catch (InvalidKeySpecException e) {
            throw new U2FException("Error when decoding public key", e);
        } catch (NoSuchAlgorithmException e) {
            throw new U2FException("Error when decoding public key", e);
        }
    }

    @Override
    public byte[] computeSha256(byte[] bytes) throws U2FException {
        try {
            return MessageDigest.getInstance("SHA-256").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new U2FException("Error when computing SHA-256", e);
        }
    }
}
