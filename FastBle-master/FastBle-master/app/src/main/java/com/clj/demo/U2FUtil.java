package com.clj.demo;

import com.clj.fastble.utils.HexUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Creator: liuke
 * Description:CLass U2FUtil was used to parse info from hushio key
 * Date: 2016/11/20 19:32
 * Email: liuke0002@outlook.com
 */
public class U2FUtil {

    /**
     * register response hex string structure:
     * cid(4byte) + command(1byte) + appid hash(32byte) + client data hash(32byte)
     * @return
     */
    public static Map<String,byte[]> parseRegFromKey(String registerHexStr){
        Map<String,byte[]> map=new HashMap<>();
        byte []cid=HexUtil.hexStringToBytes(registerHexStr.substring(0, 8));
        map.put(U2FConsts.CHANNEL_ID,cid);
        byte []command=HexUtil.hexStringToBytes(registerHexStr.substring(8,10));
        map.put(U2FConsts.COMMAND,command);
        byte []applicationIdHash=HexUtil.hexStringToBytes(registerHexStr.substring(10, 74));
        map.put(U2FConsts.APPLICATION_ID_HASH,applicationIdHash);
        byte []clientDataHash=HexUtil.hexStringToBytes(registerHexStr.substring(74, 138));
        map.put(U2FConsts.CLIENT_DATA_HASH,clientDataHash);
        return map;
    }

    /**
     * authentication response hex string structure:
     * cid(4byte)　+　command(1byte) + appid hash(32byte)　+ client data hash(32byte)　+ keyhandle长度 + keyhandle
     * @return
     */
    public static Map<String,byte[]> parseAuthFromKey(String authHexStr){
        Map<String,byte[]> map=new HashMap<>();
        byte []cid=HexUtil.hexStringToBytes(authHexStr.substring(0, 8));
        map.put(U2FConsts.CHANNEL_ID,cid);
        byte []command=HexUtil.hexStringToBytes(authHexStr.substring(8,10));
        map.put(U2FConsts.COMMAND,command);
        byte []applicationIdHash=HexUtil.hexStringToBytes(authHexStr.substring(10, 74));
        map.put(U2FConsts.APPLICATION_ID_HASH,applicationIdHash);
        byte []clientDataHash=HexUtil.hexStringToBytes(authHexStr.substring(74, 138));
        map.put(U2FConsts.CLIENT_DATA_HASH,clientDataHash);
        byte []keyHandleLen=HexUtil.hexStringToBytes(authHexStr.substring(138,140));
        map.put(U2FConsts.KEYHANDLE_LEN,keyHandleLen);
        byte[] keyHandle=HexUtil.hexStringToBytes(authHexStr.substring(140,268));
        map.put(U2FConsts.KEYHANDLE,keyHandle);
        return map;
    }

    /**
     * according to the request from key judge weather is registeration or authentication
     * @param request
     * @return
     */
    public static boolean isRegister(String request){
        String substring = request.substring(8, 10);
        if(substring.equals("01")){
            return true;
        }else{
            return false;
        }
    }
}
