package com.mycompany.asms.utils.query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Value<T> {

    private T data;

    public Value(T data) {
        this.data = data;
    }

    public String get() {
        if (data != null){
            String output = String.valueOf(data).strip();
            output = trimQuotes(output);
            if (data instanceof Date)
                return String.format("'%s'", new SimpleDateFormat("yyyy-MM-dd").format(data));
            if(data instanceof Boolean)
                return ((boolean) data )? "1" : "0";
            return String.format("'%s'", output);
        }
        return null;
    }


    private String trimQuotes(String str){
        if(str == null || str.isBlank())
            throw new IllegalArgumentException("passed string must not be blank");

        if(str.length() > 2) {
            if(str.charAt(0) == '\'')
                str = str.substring(1);

            if(str.charAt(str.length()-1) == '\'')
                str = str.substring(0, str.length()-1);
        }

        return str;
    }
}
