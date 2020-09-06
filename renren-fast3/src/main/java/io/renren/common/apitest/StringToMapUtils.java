package io.renren.common.apitest;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * 统一字符串转map封装
 *
 */
public class StringToMapUtils {
    static String regx1="&";
    static String regx2=";";

    private static Map<String,Object> covertStringToMap(String params, String regx) {
        // "method=loginMobile&loginname=test1&loginpass=test1&testloginpass=您好"
        if(!StringUtils.isEmpty(params)) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            String[] paStrings= params.split(regx);
            //userId=""
            for (int i = 0; i < paStrings.length; i++) {
                String param = paStrings[i];
                String[] params_array=param.split("=");
                if(params_array.length>1) {
                    map.put(params_array[0], params_array[1]);
                }
            }
            return map;
        }
        return null;
    }


    public static Map<String,Object> covertStringToMap1(String params) {
        return covertStringToMap(params,regx1);
    }

    public static Map<String,Object> covertStringToMap2(String params) {
        return covertStringToMap(params,regx2);
    }

}
