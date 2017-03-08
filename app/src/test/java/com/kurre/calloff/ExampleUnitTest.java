package com.kurre.calloff;

import android.os.Environment;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void test_socket() {
        System.out.println(new Date().toString());

        String date = "Tue Nov 01 08:52:08 IST 2016";
        String[] time = date.split(" ")[3].split(":");
        String timestamp = time[0] + ":" + time[1] + " " + (Integer.valueOf(time[0]) < 12 ? "am" : "pm");
        System.out.println(timestamp);
        /*try {
            new SimpleDateFormat("E M d H:m:s z YYYY").parse("Tue Nov 01 08:52:08 IST 2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        //Date date = new Date("Tue Nov 01 08:52:08 IST 2016");
        //System.out.println(new SimpleDateFormat("h:mm a").format(date));
    }
}