package com.effective.common.base.json.fastjson;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.*;

/**
 * 通过fastJson对string和对象之间的转化
 * @author yanweiqi
 *
 */
public final class BeanJsonUtil {

    private static final Logger log = LoggerFactory.getLogger(BeanJsonUtil.class);

    private BeanJsonUtil() {
    }

    /**
     * 实现将bean转换为JSON字符串
     *
     * @param bean
     * @return
     */
    public static String bean2Json(Object bean) {
        return bean2Json(bean, new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect});
    }

    /**
     * 实现将bean转换为JSON字符串
     * SerializerFeature 具体定义见 http://code.alibabatech.com/wiki/display/FastJSON/Serial+Features
     *
     * @param bean
     * @return
     * @throws com.alibaba.fastjson.JSONException
     */
    public static String bean2Json(Object bean, SerializerFeature[] features) {
        if (null == bean) {
            throw new IllegalArgumentException("target bean is null!");
        }
        try {
            String json = null;
            if (null == features) {
                json = JSON.toJSONString(bean);
            } else {
                json = JSON.toJSONString(bean, features);
            }
            return json;
        } catch (Exception ex) {
            throw new JSONException("Met error in converting bean to json!Error:" + ex.getMessage(), ex);
        }
    }

    /**
     * 将json字符串转化为JSON
     * 注意：
     * 转换时仅对bean与JSON中对应的key进行赋值，其他无法映射的值均为空
     *
     * @param json
     * @param targertClass
     * @return
     * @throws com.alibaba.fastjson.JSONException
     */
    public static <T> T json2Object(String json, Class<T> targertClass) {
        if (null == json || json.length() == 0) {
            throw new IllegalArgumentException("input json string is blank!");
        }
        try {
            return JSON.parseObject(json, targertClass);
        } catch (Exception ex) {
            throw new JSONException("Met error in converting json string to bean!Error:" + ex.getMessage(), ex);
        }
    }

    /**
     * 将json字符串转化为JSON
     * 注意：
     * 转换时仅对bean与JSON中对应的key进行赋值，其他无法映射的值均为空
     *
     * @param json
     * @param targertClass
     * @param features
     * @return
     * @throws com.alibaba.fastjson.JSONException
     */
    public static Object json2Object(String json, Class<?> targertClass, Feature... features) {
        if (null == json || json.length() == 0) {
            throw new IllegalArgumentException("input json string is blank!");
        }
        try {
            return JSON.parseObject(json, targertClass, features);
        } catch (Exception ex) {
            throw new JSONException("Met error in converting json string to bean!Error:" + ex.getMessage(), ex);
        }
    }

    public static <T> T json2Object(String json, TypeReference<T> typeReference) {
        if (null == json || json.length() == 0) {
            throw new IllegalArgumentException("input json string is blank!");
        }
        try {
            return JSON.parseObject(json, typeReference);
        } catch (Exception ex) {
            throw new JSONException("Met error in converting json string to bean!Error:" + ex.getMessage(), ex);
        }
    }

    /**
     * 将json转换为JSONObject
     *
     * @param json
     * @return
     * @throws com.alibaba.fastjson.JSONException
     */
    public static JSONObject toJsonObject(String json) {
        if (null == json || json.length() == 0) {
            throw new IllegalArgumentException("input json string is blank!");
        }
        try {
            return JSON.parseObject(json);
        } catch (Exception ex) {
            throw new JSONException("Met error in converting json string to bean!Error:" + ex.getMessage(), ex);
        }
    }

    /**
     * 将json转换为JSONArray
     *
     * @param json
     * @return
     * @throws com.alibaba.fastjson.JSONException
     */
    public JSONArray toJsonArray(String json) {
        if (null == json || json.length() == 0) {
            throw new IllegalArgumentException("input json string is blank!");
        }
        try {
            return JSON.parseArray(json);
        } catch (Exception ex) {
            throw new JSONException("Met error in converting json string to bean!Error:" + ex.getMessage(), ex);
        }
    }
}
