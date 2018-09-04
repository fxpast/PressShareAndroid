package com.prv.gootoor.lists;


import org.json.JSONArray;

/**
 * Created by roger on 20/08/2017.
 */

public class Users {

    private static Users  mInstance;

    private JSONArray usersArray;
    public JSONArray getUsersArray() { return usersArray;}
    public void setUsersArray(JSONArray usersArray) {this.usersArray = usersArray;}


    private Users(){

        usersArray = new JSONArray();

    }

    public static Users sharedInstance() {
        if (mInstance == null) {
            mInstance = new Users();
        }
        return mInstance;
    }



}
