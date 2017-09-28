package com.zombietechinc.rovingrepairs;

/**
 * Created by User on 9/8/2017.
 */

public class CreateCustomer {
    User user;

    public String createCustomerPost (User user) {
        String postRequest = "{ \"firstName\" : \"" + user.getName() + "\"," +
                "\n \"lastName\" : \" " + user.getLastName() + "\" , \n \"emailAddress\" : \"" + user.emailAddress + "\" , " +"\"streetAddress\": { \"address1\": \""+ user.getAddress() + "\"}," + " \n \"phoneNumbers\" : [{ \"number\" : \""
                + user.getContactnumber() + "\" , \"type\" : \"mobile\" }] } ";
        return postRequest;
    }
}
