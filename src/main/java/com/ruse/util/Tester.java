package com.ruse.util;

import io.ipgeolocation.api.IPGeolocationAPI;

public class Tester {

    private final static IPGeolocationAPI api = new IPGeolocationAPI("99ed94ea6c6242c684dcd8e699c28004");

    public static void main(String[] args) {
        System.out.println("IP: " + longToIp(3541791244L));
    }

    public static String ipToDec(String ip){
        long decimal = 0;
        String[] octets = ip.split("\\.");
        for(int i = 0; i < octets.length; i++){
            int power = 3 - i;
            decimal += Integer.parseInt(octets[i]) * Math.pow(256, power);
        }
        return String.valueOf(decimal);
    }

    public static String longToIp(long i) {

        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF);

    }
}
