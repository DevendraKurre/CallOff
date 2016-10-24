package com.kurre.calloff;

/**
 * Created by kurre on 25-09-2016.
 */
public interface Constants {

    int port_for_msg_sending = 2225;
    int port_for_msg_receiving = 2226;
    int port_for_call_sending = 2227;
    int port_for_call_receiving = 2228;


    String LOGIN_URL = "http://192.168.1.174/calloff/login.php";
    String REGISTER_URL = "";
    String RESET_URL = "";
    String GET_CONTACTS_URL = "http://192.168.1.174/calloff/get_all_contacts.php";

    enum task {
        LOGIN, REGISTER, RESET, GET_CONTACTS
    }

    //Messages
    String LOGIN_FAILED = "Login Failed! Please try again...";
    String LOGIN_SUCCESS = "Login Successful...";

}