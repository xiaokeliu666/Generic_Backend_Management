package com.xliu.qch.baseadmin.common.pojo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Override requestWrapper, so the value of parameter can be changed
 *
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private Map<String , String[]> params = new HashMap<String, String[]>();

    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }


    public void addParameter(String name, Object value) {
        if(value != null) {
            if(value instanceof String[]) {
                params.put(name , (String[])value);
            }else if(value instanceof String) {
                params.put(name , new String[] {(String)value});
            }else {
                params.put(name , new String[] {String.valueOf(value)});
            }
        }
    }
}
