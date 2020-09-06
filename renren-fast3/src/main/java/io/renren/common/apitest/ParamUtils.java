package io.renren.common.apitest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParamUtils 用来剥离 HttpClient里本来需要支持的
 * 从数据库里 读取的 headers和params 是字符串的情况
 */

public class ParamUtils {
    //全局map支持多列数据提取
    static Map<String, Object> params_map = new LinkedHashMap<String, Object>();


    public static void clear() {
        //清空当前线程
        params_map.clear();
    }


    public static void addFromMap(Map<String, Object> map) {
        if (MapUtils.isNotEmpty(map)) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                params_map.put(key, map.get(key));
            }
        }

    }
    /**
     * 从json里查找 regx 中需要被替换的 oldValue(是json里的一个key)
     * 把 newVaule 和 key 一起放到 全局的params_map
     * @param json
     * @param regx
     * */
    //need: id=uid;code=code
    //provide: {"code":"1","data":{"name":"testfan0","pwd":"pwd0"}}
    //provide group: {"code":"1","data":[{"name":"testfan0","pwd":"pwd0"},{"name":"testfan1","pwd":"pwd1"},{"name":"testfan2","pwd":"pwd2"}]}
    public static void addFromJson(String json, String regx) {
        if (JSON.isValid(json)) {
            //1、 把 需要从返回值里拿数据的 String转成map
            Map<String, Object> map = StringToMapUtils.covertStringToMap2(regx);
            Set<String> set = map.keySet();
            System.out.println("需要替换的数据:");
            for (String key : set) {
                System.out.println(key + ": " + map.get(key));
            }
            if (MapUtils.isNotEmpty(map)) {

                for (String key : set) {
                    // 2、使用 JSONPath 从返回值里找 对应的 value
                    String oldValue = String.valueOf(map.get(key));
                    if (StringUtils.isNotBlank(oldValue)) {
                        Object newValue = JSONPath.read(json, oldValue);
                        System.out.println("new1:" + newValue);
                        //如果提取数据不存在，全局查找 ==> 一般是遇到多组数据的时候 [testfan0, testfan1, testfan2]
                        if (newValue == null) {
                            newValue = JSONPath.read(json, ".." + oldValue);
                            System.out.println("new2:" + newValue);
                        }
                        //如果提取数据是list group数据 _g1 _g2
                        if (newValue instanceof List) {
                            List<Object> list = (List<Object>) newValue;
                            // names_g1=testfan0, names_g2=testfan1, names_g3=testfan2
                            int count = 1;
                            for (Object o : list) {
                                params_map.put(key + "_g" + count++, o);
                            }
                        } else if (newValue != null) {
                            params_map.put(key, newValue);
                        } else {
                            params_map.put(key, "");
                        }

                    }
                }
                map.clear();
            }
        }

    }

    /**
     * url
     * map
     * 自定替换
     * ${id} 的 是可以在 全局的 params_map 进行搜索值的
     * 把对应的值给替换到 str里，然后返回
     */
    static String regx = "\\$\\{(.+?)\\}"; // ${test}
    static Pattern pattern = Pattern.compile(regx);

    public static String replace(String str) {
        if (StringUtils.isNotEmpty(str)) {
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                //System.out.println("matcher.group(): " + matcher.group()+"   "+matcher.group(1));
                // matcher.group()： ${id}
                // matcher.group(1)：id
                /**
                 *
                 * 把params_map 里的 value
                 * */
                str = str.replace(matcher.group(), MapUtils.getString(params_map, matcher.group(1), ""));
            }
        }
        return str;
    }

    public static void printlnMap() {
        System.out.println(params_map);
    }

    public static void main(String[] args) {
        addFromJson("{\"msg\":\"登录成功\",\"uid\":\"9CC972DFA2D4481F89841A46FD1B3E7B\",\"code\":\"1\"}", "id=uid;code=code");
        addFromJson("{\"code\":\"1\",\"data\":[{\"name\":\"testfan0\",\"pwd\":\"pwd0\"},{\"name\":\"testfan1\",\"pwd\":\"pwd1\"},{\"name\":\"testfan2\",\"pwd\":\"pwd2\"}]}","names=name");
        printlnMap();
        String str = "${id} ==== ${code}";
        System.out.println(replace(str));
    }

}
