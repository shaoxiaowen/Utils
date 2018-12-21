package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: xiaowen
 * @create: 2018-11-29 08:16
 **/
@Slf4j
public class TokenCache {

    public static final String TOKEN_PRIFIX="token_";

    //LRU算法
    //设置对本地缓存操作的参数
    private static LoadingCache<String,String> loadingCache=CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }
    public static String getKey(String key){
        String value=null;
        try{
            value=loadingCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            log.error("localCache get error",e);
        }
        return null;
    }
}
