package org.framework.common.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public abstract class JsonUtils {

    /**
     * json字符串按照字典格式排序
     *
     * @param json
     * @return
     */
    public static String sortJson(String json) {
        if (StringUtils.isEmpty(json)) {
            return "";
        }

        Map<String, Object> map = JSONObject.parseObject(json.trim(), TreeMap.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if ( value instanceof JSONArray ) {
                JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(value));
                JSONArray array = new JSONArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    Map<String, Object> treeMap = JSONObject.parseObject(JSONObject.toJSONString(jsonArray.get(i)), TreeMap.class);
                    array.add(treeMap);
                    map.put(entry.getKey().toString(), array);
                }
            }
        }
        return JSONObject.toJSONString(map);
    }


    public static void main(String[] args){
        String jsonStr = "{\"a\":\"100\",\"b\":[{\"b1\":\"b_value1\",\"b2\":\"b_value2\"}, {\"b1\":\"b_value1\",\"b2\":\"b_value2\"}],\"c\": {\"c1\":\"c_value1\",\"c2\":\"c_value2\"}} ";
        String s = sortJson(jsonStr);
        System.out.println(s);
    }
}
