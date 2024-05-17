package com.motcs.build.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
@Component
public abstract class JdbcDatabaseService implements InitializingBean {

    protected JdbcClient jdbcClient;
    protected ObjectMapper objectMapper;
    protected JdbcTemplate jdbcTemplate;
    protected ConversionService conversions;
    protected NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Override
    public void afterPropertiesSet() {
    }

    @Autowired
    public void setJdbcClient(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversions = conversionService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}