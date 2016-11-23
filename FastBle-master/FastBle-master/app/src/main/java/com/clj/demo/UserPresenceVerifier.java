// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.clj.demo;

public interface UserPresenceVerifier {
    byte USER_PRESENT_FLAG = (byte) 0x01;

    byte verifyUserPresence();
}
