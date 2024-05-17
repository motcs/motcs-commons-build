package com.motcs.build.mvc;

import com.motcs.build.enums.DataEnu;
import com.motcs.build.enums.DataTimeEnum;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-17 星期五
 */
public class DataTime {

    /**
     * 获取当前时间
     *
     * @param dataTimeEnum 转换的类型
     * @return 返回格式化后的时间
     */
    public static String localDateTime(DataTimeEnum dataTimeEnum) {
        return localDateTime(LocalDateTime.now(), dataTimeEnum);
    }

    /**
     * 获取指定时间的格式
     *
     * @param now          传递时间
     * @param dataTimeEnum 转换的类型
     * @return 返回格式化后的时间
     */
    public static String localDateTime(LocalDateTime now, DataTimeEnum dataTimeEnum) {
        if (dataTimeEnum == DataTimeEnum.YEAR)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy"));
        else if (dataTimeEnum == DataTimeEnum.MONTH)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("MM"));
        else if (dataTimeEnum == DataTimeEnum.DAY)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("dd"));
        else if (dataTimeEnum == DataTimeEnum.HOUR)
            return now.getHour() < 10 ? "0" + now.getHour() : String.valueOf(now.getHour());
        else if (dataTimeEnum == DataTimeEnum.MINUTE)
            return now.getMinute() < 10 ? "0" + now.getMinute() : String.valueOf(now.getMinute());
        else if (dataTimeEnum == DataTimeEnum.SECOND)
            return now.getSecond() < 10 ? "0" + now.getSecond() : String.valueOf(now.getSecond());
        else if (dataTimeEnum == DataTimeEnum.MILLISECOND) {
            LocalDateTime localDateTime = LocalDateTime.now();
            return String.valueOf(localDateTime.atZone(ZoneId
                    .systemDefault()).toInstant().toEpochMilli());
        } else if (dataTimeEnum == DataTimeEnum.NANOSECOND) {
            // 获取纳秒数
            return String.valueOf(now.getNano());
        } else if (dataTimeEnum == DataTimeEnum.YM)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        else if (dataTimeEnum == DataTimeEnum.YMD)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        else if (dataTimeEnum == DataTimeEnum.MD)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd"));
        else if (dataTimeEnum == DataTimeEnum.YM_ZN) {
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy年MM月"));
        } else if (dataTimeEnum == DataTimeEnum.YMD_ZN)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        else if (dataTimeEnum == DataTimeEnum.MD_ZN)
            return now.toLocalDate().format(DateTimeFormatter.ofPattern("MM月dd日"));
        else if (dataTimeEnum == DataTimeEnum.WEEK)
            return String.valueOf(now.getDayOfWeek().getValue());
        else if (dataTimeEnum == DataTimeEnum.WHICH_WEEK) {
            WeekFields weekFields = WeekFields.of(java.util.Locale.getDefault());
            int weekOfYear = now.toLocalDate().get(weekFields.weekOfYear());
            return String.valueOf(weekOfYear);
        } else if (dataTimeEnum == DataTimeEnum.YMD_HMS)
            return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        else if (dataTimeEnum == DataTimeEnum.YMD_HMS_ZN)
            return now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH点mm分ss秒"));
        else if (dataTimeEnum == DataTimeEnum.HMS)
            return now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        else if (dataTimeEnum == DataTimeEnum.HMS_ZN)
            return now.format(DateTimeFormatter.ofPattern("HH点mm分ss秒"));
        return now.toString();
    }

    /**
     * 获取当前时间的信息
     *
     * @param dataEnu 时间类型枚举
     * @return 返回信息
     */
    public static String localDate(DataEnu dataEnu) {
        return localDate(LocalDate.now(), dataEnu);
    }

    /**
     * 获取指定时间的格式
     *
     * @param now     传递时间
     * @param dataEnu 转换的类型
     * @return 返回格式化后的时间
     */
    public static String localDate(LocalDate now, DataEnu dataEnu) {
        if (dataEnu == DataEnu.YEAR)
            return now.format(DateTimeFormatter.ofPattern("yyyy"));
        else if (dataEnu == DataEnu.MONTH)
            return now.format(DateTimeFormatter.ofPattern("MM"));
        else if (dataEnu == DataEnu.DAY)
            return now.format(DateTimeFormatter.ofPattern("dd"));
        else if (dataEnu == DataEnu.YM)
            return now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        else if (dataEnu == DataEnu.YMD)
            return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        else if (dataEnu == DataEnu.MD)
            return now.format(DateTimeFormatter.ofPattern("MM-dd"));
        else if (dataEnu == DataEnu.YM_ZN) {
            return now.format(DateTimeFormatter.ofPattern("yyyy年MM月"));
        } else if (dataEnu == DataEnu.YMD_ZN)
            return now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        else if (dataEnu == DataEnu.MD_ZN)
            return now.format(DateTimeFormatter.ofPattern("MM月dd日"));
        else if (dataEnu == DataEnu.WEEK)
            return String.valueOf(now.getDayOfWeek().getValue());
        else if (dataEnu == DataEnu.WHICH_WEEK) {
            WeekFields weekFields = WeekFields.of(java.util.Locale.getDefault());
            int weekOfYear = now.get(weekFields.weekOfYear());
            return String.valueOf(weekOfYear);
        }
        return now.toString();
    }

    public static String getLocalDate(LocalDate now, int integer) {
        if (integer == 0)
            return now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        else if (integer == 1)
            return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        else if (integer == 2)
            return now.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        else if (integer == 3)
            return now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return now.toString();
    }

    /**
     * 获取一个时间距今的毫秒数
     *
     * @param time 需要判断的时间
     * @return 时间相隔的毫秒数
     */
    public static long LocalDateTimeToSecond(LocalDateTime time) {
        Duration duration = Duration.between(LocalDateTime.now(), time);
        return duration.toSeconds();
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author motcs
     */
    public static boolean timeValid(LocalDateTime startTime, LocalDateTime endTime) {
        //获取当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        //返回判断结果
        return !nowTime.isBefore(startTime) && nowTime.compareTo(endTime) < 1;
    }

}
