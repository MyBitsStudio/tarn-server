package com.ruse.util.vpn;

import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;

import static com.ruse.security.tools.SecurityUtils.api;

public class VPNTool {

    public static void main(String[] args){
        checkSecurity("45.158.170.3");
    }

    public static void checkSecurity(String ip){
        GeolocationParams geoParams = new GeolocationParams();
        geoParams.setIPAddress(ip);
        geoParams.setFields("geo,time_zone,currency");

        geoParams.setIncludeSecurity(true);
        Geolocation geolocation = api.getGeolocation(geoParams);

        if (geolocation.getStatus() == 200) {

            System.out.println("Security: \nAnonymous : " + geolocation.getGeolocationSecurity().getAnonymous()
            +" \nKnown Attacker : " + geolocation.getGeolocationSecurity().getKnownAttacker()
            +" \nProxy : " + geolocation.getGeolocationSecurity().getProxy()
            +" \nProxy Type : " + geolocation.getGeolocationSecurity().getProxyType()
            +" \nCloud Provider : " + geolocation.getGeolocationSecurity().getCloudProvider()
            +" \nTor : " + geolocation.getGeolocationSecurity().getTor()
            +" \nThreat Score : " + geolocation.getGeolocationSecurity().getThreatScore()
            +" \nCity : " + geolocation.getCity()
            +" \nCountry : " + geolocation.getCountryName()
            +" \nContinent : " + geolocation.getContinentName()
            +" \nCurrency : " + geolocation.getCurrency().getName()
            +" \nTime Zone : " + geolocation.getTimezone().getName()
            +" \nTime Zone Code : " + geolocation.getTimezone().getCurrentTime());

        }
    }
}
