
package com.codepath.nytimessearch.rest.models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Headline {

    private String main;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The main
     */
    public String getMain() {
        return main;
    }

    /**
     *
     * @param main
     *     The main
     */
    public void setMain(String main) {
        this.main = main;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
