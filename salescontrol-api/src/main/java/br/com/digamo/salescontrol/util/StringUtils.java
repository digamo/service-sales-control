package br.com.digamo.salescontrol.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtils {

    /**
     * Generate a JSON from a Java object and return the generated JSON as a string or as a byte array
     * @param obj
     * @return
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
