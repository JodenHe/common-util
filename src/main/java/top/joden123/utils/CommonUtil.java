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
package top.joden123.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Common util
 * Created by xiaofeng.he on 2019/02/21
 *
 * @author joden_he
 */
public class CommonUtil {

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
     * Check whether the given {@code String} is blank.
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || isEmpty(str.trim()));
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
}
