package com.kurre.calloff;

/**
 * Created by kurre on 25-09-2016.
 */
public interface Constants {

    long phone_number = 7204457089L;
    String my_name = "Dev";

    int port_for_sending = 2225;
    int port_for_receiving = 2226;

    String LOGIN_URL = "http://192.168.1.174/calloff/login.php";
    String REGISTER_URL = "";
    String RESET_URL = "";
    String GET_CONTACTS_URL = "http://192.168.1.174/calloff/get_all_contacts.php";

    enum task {
        LOGIN, REGISTER, RESET, GET_CONTACTS
    };

    //Messages
    String LOGIN_FAILED = "Login Failed! Please try again...";
    String LOGIN_SUCCESS = "Login Successful...";

}