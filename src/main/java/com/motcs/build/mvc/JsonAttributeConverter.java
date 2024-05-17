package com.motcs.build.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.persistence.AttributeConverter;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
public abstract class JsonAttributeConverter<T> implements AttributeConverter<T, String> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T convertToEntityAttribute(String json) {
        try {
            JsonNode value = objectMapper.readTree(json);
            return this.objectMapper.convertValue(value, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}