// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.clj.demo;


import com.clj.demo.messages.AuthenticateRequest;
import com.clj.demo.messages.AuthenticateResponse;
import com.clj.demo.messages.RegisterRequest;
import com.clj.demo.messages.RegisterResponse;

public interface U2FKey {
  RegisterResponse register(RegisterRequest registerRequest) throws U2FException;

  AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest) throws U2FException;
}
