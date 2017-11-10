package com.totalbp.mm.interfaces;

import org.json.JSONArray;

/**
 * Created by 140030 on 05/06/2017.
 */

public interface VolleyCallback {
    void onSuccess(JSONArray result);
    void onSave(String output);
}
