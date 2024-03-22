package cn.wang.chatgpt.data.types.sdk.weixin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureUtil {
    /**
     * 签名验证
     */
    public static boolean check(String token,String signature,String timestamp,String nonce)  {
        String arr[]=new String[]{token,timestamp,nonce};
        //将token timestamp,nonce三个参数进行字典序排血
        sort(arr);
        StringBuilder content = new StringBuilder();
        for (String s : arr)
        {
            content.append(s);
        }
        MessageDigest md;
        String tmpStr=null;
        try{
            md= MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = bytetoStr(digest);

        }catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }


    /**
     * 将字节数组转换为十六进制字符串
     */
    private static String bytetoStr(byte[] byteArray)
    {
        StringBuilder strDigest = new StringBuilder();
        for(byte b:byteArray)
        {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }
    /**
     * 将字节转换为十六进制字符串
     */
    private static String byteToHexStr(byte mByte)
    {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0x0F];
        tempArr[1] =Digit[mByte & 0x0F];
        return new String(tempArr);
    }

    /**
     * 进行字典排序
     */
    private static void sort(String[] str) {
        for (int i = 0; i < str.length - 1; i++) {
            for (int j = i + 1; j < str.length; j++) {
                if (str[j].compareTo(str[i]) < 0) {
                    String temp = str[i];
                    str[i] = str[j];
                    str[j] = temp;
                }
            }
        }
    }

}
