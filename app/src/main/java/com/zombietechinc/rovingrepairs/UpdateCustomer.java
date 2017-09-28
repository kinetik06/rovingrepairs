package com.zombietechinc.rovingrepairs;

/**
 * Created by User on 9/14/2017.
 */

public class UpdateCustomer {
    User user;

    public String updateCustomerPost (User user) {
        String postRequest = "{ \"firstName\" : \"" + user.getName() + "\"," +
                "\n \"lastName\" : \" " + user.getLastName() + "\" , \n \"emailAddress\" : \"" + user.emailAddress + "\" , " +"\"streetAddress\": { \"address1\": \""+ user.getAddress() + "\"}," + " \n \"phoneNumbers\" : [{ \"number\" : \""
                + user.getContactnumber() + "\" , \"type\" : \"mobile\" }] } ";
        return postRequest;
    }

}
