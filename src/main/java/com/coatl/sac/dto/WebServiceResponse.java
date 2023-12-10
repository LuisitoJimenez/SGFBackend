package com.coatl.sac.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebServiceResponse {
    
    private boolean success;
    private String message;
    private Object data;

    public WebServiceResponse(Object data) {
        this.success = true;
        this.message = "OK";
        this.data = data;
    }

    public WebServiceResponse(boolean success) {
        this.success = success;
        this.message = success ? "OK" : "Error";
    }

    public WebServiceResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public WebServiceResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public WebServiceResponse(boolean success, String message, List<?> list) {
        this.success = success;
        this.message = message;
        this.data = list;
    }

    public WebServiceResponse(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Object getData() {
        if (data == null) data = new HashMap<>();
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
