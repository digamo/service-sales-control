package br.com.digamo.salescontrol.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtils {

    /**
     * Generate a JSON from a Java object and return the generated JSON as a string or as a byte array
     * @param obj
     * @return
     * @throws JsonProcessingException 
     */
    public static String asJsonString(final Object obj) throws JsonProcessingException {
        
    	if(obj == null)
    		throw new IllegalArgumentException();
    	
        return new ObjectMapper().writeValueAsString(obj);
    }


}
