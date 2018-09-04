package com.prv.gootoor.utils;

import org.json.JSONObject;

/**
 * Created by roger on 30/08/2017.
 */

public interface GeoLocInterface {
    public void completionHandlerGeo(Boolean success, JSONObject jsonLocation, String errorString);
}
