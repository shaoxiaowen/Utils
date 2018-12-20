package com.mmall.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @description: TODO
 * @author: xiaowen
 * @create: 2018-12-19 19:55
 **/
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        /**
         * 序列化用到的属性
         */
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        //忽略空bean转Json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDERD_FORMAT));

        /**
         * 反序列化用到的属性
         */
        //忽略在Json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象封装为Json字符串
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * 将对象封装成格式化的Json字符串
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * 将Json字符串转换为对象
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("Parse String to object  error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return  (T)(typeReference.getType().equals(String.class)?str:objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("Parse String to object  error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass,Class<?>...elementClass){
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClass);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.warn("Parse String to object  error", e);
            return null;
        }
    }

    public static void main(String[] args) {
        User u1=new User();
        u1.setId(1);
        u1.setEmail("1017041284@qq.com");

        User u2=new User();
        u2.setId(2);
        u2.setEmail("u21017041284@qq.com");

        String userJson=JsonUtil.obj2String(u1);

        String userJsonPretty=JsonUtil.obj2StringPretty(u1);

        log.info("userJson:{}",userJson);
        log.info("userJsonPretty:{}",userJsonPretty);

        User user=JsonUtil.string2Obj(userJson,User.class);

        List<User> userList=Lists.newArrayList();
        userList.add(u1);
        userList.add(u2);

        String userListStr=JsonUtil.obj2StringPretty(userList);
        log.info("=====================");
        log.info(userListStr);

        List<User> usrListObj1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
        });

        List<User> usrListObj2=JsonUtil.string2Obj(userListStr,List.class,User.class);

        System.out.println("end");
    }


}
