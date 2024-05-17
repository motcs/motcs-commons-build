package com.motcs.build.mvc;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/vnobo">Alex bob</a>
 */
public class SpecificationUtils {

    private static final List<String> SKIP_CRITERIA_KEYS = List.of("extend", "createdTime", "updatedTime");

    /**
     * 序列化对象条件
     *
     * @param object 需要序列化得实体
     * @return 拼接后得Criteria
     */
    public static <T> Specification<T> build(Object object, List<String> skipKeys) {
        return build(object, skipKeys, false);
    }

    /**
     * 构建用于查询的 Criteria 对象
     *
     * @param object           要转换为 Criteria 对象的 JavaBean
     * @param skipKeys         要忽略的属性名列表
     * @param ignoreStringLike 是否忽略字符串类型的模糊查询
     * @return 构建好的 Criteria 对象
     */
    public static <T> Specification<T> build(Object object, List<String> skipKeys, boolean ignoreStringLike) {
        // 将 JavaBean 转换为 Map
        Map<String, Object> objectMap = MapClassValue.beanToMapObject(object);
        // 移除要忽略的属性
        skipKeys.forEach(objectMap::remove);
        SKIP_CRITERIA_KEYS.forEach(objectMap::remove);
        // 根据 Map 构建 Criteria 对象
        return build(objectMap, ignoreStringLike);
    }

    /**
     * 根据给定的对象属性Map构建Criteria对象，可选择是否忽略字符串模糊匹配。
     *
     * @param objectMap        对象属性Map
     * @param ignoreStringLike 是否忽略字符串模糊匹配
     * @return Criteria对象
     */
    public static <T> Specification<T> build(Map<String, Object> objectMap, boolean ignoreStringLike) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                Object value = entry.getValue();
                String key = entry.getKey();
                if (value instanceof String v && !ignoreStringLike) {
                    // 如果值是字符串，则创建相等谓词
                    predicates.add(builder.like(root.get(key).as(String.class), v + "%"));
                } else if (value instanceof Iterable<?> v) {
                    // 如果值是整数，则创建相等谓词
                    predicates.add(root.get(key).in(v));
                } else {
                    // 否则，创建相等谓词
                    predicates.add(builder.equal(root.get(key), value));
                }
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
}