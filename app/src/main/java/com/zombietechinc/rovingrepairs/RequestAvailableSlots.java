package com.zombietechinc.rovingrepairs;

import java.lang.reflect.Array;

/**
 * Created by User on 9/6/2017.
 */

public class RequestAvailableSlots {

    String productId;
    String startTime;
    String endTime;
    String peopleNumbers = "\"peopleNumbers\": [{\"peopleCategoryId\": \"WENWLT\",\n" +
            "        \"number\": 1\n" +
            "      }] ";

    public String getavailableslots(String productId, String startTime, String endTime) {
        String postRequest = "{\"productId\": \"" + productId + "\"" + ",\n \"startTime\":\""
                + startTime + "\",\n" + "\"" + "endTime\":\"" + endTime + "\",\n" + peopleNumbers +"}";

        return  postRequest;
    }



}




