package com.motcs.build.mvc;

import com.motcs.build.annotation.exception.ServerException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用mao对类的指定字段赋值
 * 类与map之间的交互
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
@Log4j2
public class MapClassValue {

    /**
     * 根据map的key为字段，value为值
     * 字段为空时设置默认值
     *
     * @param obj 类
     * @param map 需要设置的内容
     */
    public static void setValue(Object obj, Map<String, Object> map) {
        if (ObjectUtils.isEmpty(map)) {
            return;
        }
        // 获取对象的Class对象
        Class<?> clazz = obj.getClass();
        for (String string : map.keySet()) {
            try {
                // 获取对应的字段
                Field field = clazz.getDeclaredField(string);
                // 设置访问权限，允许修改私有字段
                field.setAccessible(true);
                // 检查并确保字段是可赋值的（即不是final或者static）
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    // 获取字段当前值
                    Object fieldValue = field.get(obj);
                    if (ObjectUtils.isEmpty(fieldValue)) {
                        field.set(obj, map.get(string));
                    }
                }
            } catch (Exception e) {
                log.info("MapClassValue.setDefaultValue Cannot set default value error :{}", e.getMessage());
            }
        }
    }

    /**
     * 将map内容转换为一个指定的class
     * map.key = class.filed.name
     *
     * @param clazz 需要转换的类
     * @param map   转换的mao
     * @param <T>   转换的目标类
     * @return 返回转换成功的类
     */
    public static <T> T mapToClass(Class<T> clazz, Map<String, Object> map) {
        if (ObjectUtils.isEmpty(map) || clazz == null) {
            return null;
        }
        try {
            T instance = clazz.getDeclaredConstructor().newInstance(); // 创建类的实例
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
                try {
                    // 使用FieldUtils进行更安全的操作
                    Field field = FieldUtils.getField(clazz, fieldName, true);
                    if (field != null) {
                        // 确保类型兼容
                        field.set(instance, fieldValue);
                    }
                } catch (Exception e) {
                    log.error("Cannot set field {} with value {}: {}", fieldName, fieldValue, e.getMessage(), e);
                }
            }
            return instance;
        } catch (Exception e) {
            log.error("Failed to instantiate class {}: {}", clazz.getName(), e.getMessage(), e);
            throw ServerException.withMsg("Could not create an instance of " + clazz.getName(), e);
        }
    }

    /**
     * 将bean转换为map<S,S>
     *
     * @param obj 需要转换的类
     * @return 返回转换后的map
     */
    public static Map<String, String> beanToMapString(Object obj) {
        if (obj == null) {
            return Map.of();
        }
        Map<String, String> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value;
                    try {
                        value = getter.invoke(obj);
                        if (value != null) {
                            map.put(key, value.toString());
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 将bean转换为map<S,S>
     *
     * @param obj 需要转换的类
     * @return 返回转换后的map
     */
    public static Map<String, Object> beanToMapObject(Object obj) {
        if (obj == null) {
            return Map.of();
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value;
                    try {
                        value = getter.invoke(obj);
                        if (value != null) {
                            map.put(key, value);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

}
