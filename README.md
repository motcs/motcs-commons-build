# 妃妃的常用工具包（标题后的括号内表示功能对应的类）

## 1. 时间处理器(DataTime)

    1.1 DataEnu LocalDate操作对应的枚举类
    1.2 DataTimeEnum localDateTime操作对应的枚举类
    1.3 获取不同格式的当前时间 localDateTime(DataTimeEnum)
    1.4 获取指定时间的不同格式 localDateTime(LocalDateTime,DataTimeEnum)
    1.5 获取不同格式的当前时间 localDate(DataEnu)
    1.6 获取指定时间的不同格式 localDate(LocalDate,DataEnu)
    1.7 获取一个时间距离当前时间的毫秒数 localDateTimeToSecond(LocalDateTime)
    1.8 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致 timeValid(LocalDate,LocalDate)
    1.9 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致 timeValid(LocalDateTime,LocalDateTime)

## 2. 数据库操作包(JdbcDatabaseService)

    2.1 jdbcClient 数据库客户端连接
    2.2 jdbcTemplate 数据库模板操作
    2.3 parameterJdbcTemplate 数据库参数映射模板操作
    2.4 conversions 转换的参数

## 3. 数据库与类映射转换的Converter(JsonAttributeConverter)

## 4. Map与类相互操作的工具包(MapClassValue)

## 5. 全局异常处理(MvcExceptionHandler)

## 6. 自动拼接Specification(SpecificationUtils)

## 7. 全局Redis缓存管理(ContextCaCheFilter)

    7.1 CapturingResponseWrapper 转换响应流
    7.2 CapturingServletOutputStream 转换后的响应流内容提取
