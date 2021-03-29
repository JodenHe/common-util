/**
 *    Copyright [2019] [https://github.com/JodenHe]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.jodenhe.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common util
 * Created by xiaofeng.he on 2019/02/21
 *
 * @author joden_he
 */
public class CommonUtil {

    // 正则表达式 用于匹配属性的第一个字母
    private static final String REGEX = "[a-zA-Z]";
    // 默认日期格式
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * if t is empty then return {@code result}, else return itself.
     * @param t
     * @param result
     * @param <T>
     * @return
     */
    public static<T> T nvl(T t, T result) {
        return isEmpty(t)? result: t;
    }

    /**
     * if t is empty then return {@code nullResult}, else return {@code result}.
     * @param t
     * @param result
     * @param <T>
     * @return
     */
    public static<T> T nvl(T t, T nullResult, T result) {
        return isEmpty(t)? nullResult: result;
    }

    /**
     * Check whether the given {@code String} is empty.
     * <p>
     *    copy by spring framework util
     * </p>
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * Check whether the given {@code String} is not empty.
     * <p>
     *    copy by spring framework util
     * </p>
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     * Check whether the given {@code String} is blank.
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || isEmpty(str.trim()));
    }

    /**
     * Check whether the given {@code String} is not blank.
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Check whether the given {@code Object} is exists.
     * @param o
     * @param objects
     * @return
     */
    public static boolean in(Object o, Object... objects) {
        return Arrays.asList(objects).indexOf(o) >= 0;
    }

    /**
     * return substring with {@code length}
     * @param str
     * @param length
     * @return
     */
    public static String substr(String str, int length) {
        if (str != null) return str.substring(0, Math.min(str.length(), length));
        return null;
    }

    /**
     * format date
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDateStr(Date date, String pattern) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * insert element in array's specific index
     * @param original
     * @param element
     * @param index
     * @param <T>
     * @return
     */
    public static String[] insertElement(String original[], String element, int index) {
        int length = original.length;
        if (length < index) {
            throw new RuntimeException("Array insert element: Array's length could not less than insert index");
        }
        String[] destination = new String[length + 1];
        System.arraycopy(original, 0, destination, 0, index);
        destination[index] = element;
        System.arraycopy(original, index, destination, index
                + 1, length - index);
        return destination;
    }

    /**
     * xml encode
     * @param input
     * @return
     */
    public static String xmlEncode(String input) {
        if (input == null) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0, c = input.length(); i < c; i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#x27;");
                    break;
                case '/':
                    sb.append("&#x2F;");
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * xml decode
     * @param input
     * @return
     */
    public static String xmlDecode(String input) {
        if (input != null) {
            input = input.replace("&amp;", "&");
            input = input.replace("&lt;", "<");
            input = input.replace("&gt;", ">");
            input = input.replace("&quot;", "\"");
            input = input.replace("&#x27;", "'");
            input = input.replace("&#x2F;", "/");
        }

        return input;
    }

    /**
     * Returns a string whose value is this string, with any leading and trailing
     * {@code element} removed.
     * @param source source string.
     * @param element need to removed.
     * @return String.
     */
    public static String trim(String source,String element){
        if (source == null || source.length() <= 0) {
            return source;
        }
        boolean beginIndexFlag;
        boolean endIndexFlag;
        do{
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex = source.lastIndexOf(element) + element.length() == source.length() ? source.lastIndexOf(element) : source.length();
            if (endIndex < 0) {
                endIndex = 0;
            }
            source = source.substring(beginIndex, endIndex);
            beginIndexFlag = (source.indexOf(element) == 0);
            endIndexFlag = (source.lastIndexOf(element) + element.length() == source.length());
        } while (beginIndexFlag || endIndexFlag);
        return source;
    }

    /**
     * 根据属性名转换成对应的 getter/setter 方法
     * @param fieldName 属性名
     * @param clazz 类对象
     * @param isSet 是否指定为 set
     * @return setter/getter
     */
    public static String convertToMethodName(String fieldName, Class<?> clazz, boolean isSet) {
        // 通过正则表达式匹配第一个字符
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(fieldName);
        StringBuffer sb = new StringBuffer();
        // 如果是 set 方法名称
        if (isSet) {
            sb.append("set");
        } else {
            // get 方法名称
            try {
                Field field = clazz.getDeclaredField(fieldName);
                // 如果类型为 Boolean
                if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    sb.append("is");
                } else {
                    sb.append("get");
                }

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        // 针对以下划线开头的属性
        if (fieldName.charAt(0) != '_' && matcher.find()) {
            sb.append(matcher.replaceFirst(matcher.group().toUpperCase()));
        } else {
            sb.append(fieldName);
        }

        return sb.toString();
    }

    /**
     * 给指定对象的指定属性赋值
     * @param obj 对象
     * @param fieldName 属性名
     * @param value 值
     * @param datePattern 日期格式
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Object obj, String fieldName, String value, String datePattern)
            throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        // 得到该属性的 set 方法名
        String methodName = convertToMethodName(fieldName, obj.getClass(), Boolean.TRUE);
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            /**
             * 因为这里只是调用 bean 中属性的 set 方法，属性名称不能重复
             * 所以 set 方法也不会重复，所以就直接用方法名称去锁定一个方法
             * （注：在java中，锁定一个方法的条件是方法名及参数）
             */
            if (method.getName().equals(methodName)) {
                Class<?>[] parameterTypes =method.getParameterTypes();
                /**
                 * 如果是(整型,浮点型,布尔型,字节型,时间类型),
                 * 按照各自的规则把 value 值转换成各自的类型
                 * 否则一律按类型强制转换(比如: String 类型)
                 */
                try {
                    /**
                     * 如果是(整型,浮点型,布尔型,字节型,时间类型),
                     * 按照各自的规则把 value 值转换成各自的类型
                     * 否则一律按类型强制转换(比如: String 类型)
                     */
                    if(parameterTypes[0] == int.class || parameterTypes[0] == Integer.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Integer.valueOf(value));
                        }
                        break;
                    } else if(parameterTypes[0] == long.class || parameterTypes[0] == Long.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Long.valueOf(value));
                        }
                        break;
                    } else if(parameterTypes[0] == float.class || parameterTypes[0] == Float.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Float.valueOf(value));
                        }
                        break;
                    } else if(parameterTypes[0] == double.class || parameterTypes[0] == Double.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Double.valueOf(value));
                        }
                        break;
                    } else if(parameterTypes[0] == byte.class || parameterTypes[0] == Byte.class) {
                        if(value != null && value.length() > 0) {
                            method.invoke(obj, Byte.valueOf(value));
                        }
                        break;
                    } else if(parameterTypes[0] == boolean.class|| parameterTypes[0] == Boolean.class) {
                        if (value != null && value.length() > 0) {
                            method.invoke(obj, Boolean.valueOf(value));
                        }
                        break;
                    } else if(parameterTypes[0] == Date.class) {
                        if(value != null && value.length() > 0) {
                            if (datePattern == null) {
                                datePattern = DEFAULT_DATE_PATTERN;
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                            try {
                                Date date=sdf.parse(value);
                                method.invoke(obj,date);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException("The value could not convert to date, value: " + value);
                            }
                        }
                        break;
                    } else if(parameterTypes[0] == BigDecimal.class) {
                        if (value != null && value.length() > 0) {
                            method.invoke(obj, new BigDecimal(value));
                        }
                        break;
                    } else {
                        if (value != null && value.length() > 0) {
                            method.invoke(obj, parameterTypes[0].cast(value));
                        }
                        break;
                    }
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | SecurityException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    /**
     * 解析日期
     * @param value 值
     * @param timeZone 时区，没有给 null
     * @param pattern 日期格式
     * @return 日期
     * @throws ParseException
     */
    public static Date parseDateTime(String value, String timeZone, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        value = CommonUtil.substr(value, pattern.length());
        if (isNotEmpty(timeZone)) {
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return sdf.parse(value);
    }

    /**
     * 两个时区的毫秒数差
     * @param t1
     * @param t2
     * @return
     */
    public static long timeZoneOffset(TimeZone t1, TimeZone t2) {
        long currentTimeMillis = System.currentTimeMillis();
        return t1.getOffset(currentTimeMillis) - t2.getOffset(currentTimeMillis);
    }
}
