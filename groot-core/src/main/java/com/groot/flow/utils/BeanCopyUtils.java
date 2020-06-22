package com.groot.flow.utils;



import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : chenhaitao934
 * @date : 7:27 下午 2020/5/14
 */
public class BeanCopyUtils {
    public static final Map<String, Object> copyierMap = new ConcurrentHashMap<>();
    public static void copy(Object src, Object target){
        copy(src, target, false);
    }

    public static void copy(Object src, Object target, boolean flag) {
        String key = src.getClass().getName() + target.getClass().getName();
        BeanCopier beanCopier;
        if(copyierMap.get(key) == null){
            synchronized (BeanCopyUtils.class){
                beanCopier = BeanCopier.create(src.getClass(), target.getClass(), flag);
                copyierMap.put(key, beanCopier);
            }
        }else {
            beanCopier = (BeanCopier) copyierMap.get(key);
        }
        if(false == flag){
            beanCopier.copy(src, target, null);
        }else if(true == flag){
            beanCopier.copy(src, target, new Converter() {
                @Override
                public Object convert(Object o, Class aClass, Object o1) {
                    return null;
                }
            });
        }
    }
}
