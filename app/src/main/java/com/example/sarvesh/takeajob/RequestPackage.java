package com.example.sarvesh.takeajob;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

public class RequestPackage {

   private String method;
   private String uri;
   private HashMap<String,String> params=new HashMap<>();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> p) {
            params = p;
    }

    String getEncodedParams()
    {
        Set<String> keys=params.keySet();
        StringBuilder b=new StringBuilder();
        for (String k:keys)
        {
         String v="blank";
            if(!params.get(k).equals("blank")) {

                try {
                    v = URLEncoder.encode(params.get(k), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.getMessage();
                    Log.d("SERVER", e.getMessage());
                }

                if (b.length() > 0) {
                    b.append("&");
                }
                b.append(k + "=" + v);
            }
            else{
                //v=params.get(k);
                if (b.length() > 0) {
                    b.append("&");
                }
                b.append(k + "=" + v);
            }
        }

        Log.d("SERVER", b.toString());
       return b.toString();
    }
}
