// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.clj.demo;

public class U2FConsts {
    public static final String U2F_SP = "u2f_pref";
    public static final String U2F_COUNTER = "u2f_counter";
    public static final String CLIENT_DATA_HASH = "clientDataHash";
    public static final String APPLICATION_ID_HASH = "applicationIdHash";
    public static final String CHANNEL_ID = "channelId";
    public static final String CONTROL_HEX_BYTE = "controlHexByte";
    public static final String COMMAND="command";
    public static final String KEYHANDLE_LEN="keyhandle_len";
    public static final String KEYHANDLE="keyHandle";


    public static final byte FINISH_PACK_BYTES[] =
            new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

    public static final byte INITIAL_PACK_BYTES[] = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

}
