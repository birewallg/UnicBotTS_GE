package local.bwg.model;

import org.json.JSONObject;

abstract class AbstractUser {
    JSONObject getJSONObject() { return null; }
    void loadFromSirializeble(Object obj) { }
}
