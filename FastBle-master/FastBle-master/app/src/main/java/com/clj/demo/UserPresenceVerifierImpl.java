package com.clj.demo;

/**
 * Created by liuke on 2016/11/20
 * <p>
 * Email liuke0002@outlook.com
 */
public class UserPresenceVerifierImpl implements UserPresenceVerifier {
    @Override
    public byte verifyUserPresence() {
        return USER_PRESENT_FLAG;
    }
}
