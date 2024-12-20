package modelo.dominio;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Integer> {
    
    public Integer unmarshal(String s) {
        return Integer.parseInt(s);
    }
 
    public String marshal(Integer number) {
        if (number == null) return "";
         
        return number.toString();
    }
}