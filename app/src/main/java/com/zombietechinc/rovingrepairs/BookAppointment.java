package com.zombietechinc.rovingrepairs;

/**
 * Created by User on 9/7/2017.
 */

public class BookAppointment {
    String eventId;
    String firstName;
    String lastName;
    String phoneNumber;
    String numberOfPeople = "\"participants\": { \"numbers\": [{\"peopleCategoryId\": \"WENWLT\",\n" +
            "        \"number\": 1\n" +
            "      }] }";
    String productId;
    String emailAddress;
    String address;

    public String getPostBody (String eventId, String firstName,
                               String lastName, String emailAddress ,String phoneNumber, String productId, String address ) {
        //"numbers": [{"peopleCategoryId": "Cadults","number": 1},{"peopleCategoryId": "Cchildren","number": 1}]

        //"{\"productId\": \"" + productId + "\"" + ",\n \"startTime\":\""+ startTime + "\",\n" + "\"" + "endTime\":\"" + endTime + "\",\n" + peopleNumbers +"}";
        String postRequest = "{\"eventId\": \"" + eventId + "\"" + ",\n  \"customer\":{\n \"firstName\" : \"" + firstName + "\"," +
                "\n \"lastName\" : \" " + lastName + "\" , \n \"emailAddress\" : \"" + emailAddress + "\" , " +"\"streetAdress\": { \"address1\": \""+ address + "\"}," + " \n \"phoneNumbers\" : [{ \"number\" : \""
                + phoneNumber + "\" , \"type\" : \"mobile\" }] } , \n " + numberOfPeople + ", \n \"productId\" : \"" + productId + "\" }";


    return postRequest;
    }

    public String getPostwithID (String eventId, String productId, String customerID) {
        String postRequestwithID = "{\"eventId\": \"" + eventId + "\"" + ",\n  \"customerId\": \"" + customerID + "\", \n " + numberOfPeople + ", \n \"productId\" : \"" + productId + "\" }";

        return postRequestwithID;
    }
}
