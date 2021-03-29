package com.github.jodenhe.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class CommonUtilTest {

    @Test
    public void nvl() {
        Assert.assertEquals("123", CommonUtil.nvl("", "123"));
        Assert.assertEquals("456", CommonUtil.nvl("456", "789"));
        Assert.assertEquals("abc", CommonUtil.nvl("123", "null", "abc"));
        Assert.assertEquals("null", CommonUtil.nvl("", "null", "abc"));
    }


    @Test
    public void isEmpty() {
        Assert.assertTrue(CommonUtil.isEmpty(""));
        Assert.assertTrue(CommonUtil.isEmpty(null));
    }

    @Test
    public void isBlank() {
        Assert.assertTrue(CommonUtil.isBlank("  "));
        Assert.assertTrue(CommonUtil.isBlank(null));
        Assert.assertTrue(CommonUtil.isBlank(""));
    }

    @Test
    public void in() {
        Assert.assertTrue(CommonUtil.in("123", "aa", "123", "def"));
        Assert.assertFalse(CommonUtil.in("aa", "abc", "def"));
    }

    @Test
    public void substr() {
        Assert.assertEquals("abc", CommonUtil.substr("abcdef", 3));
    }

    @Test
    public void formatDateStr() {
        System.out.println(CommonUtil.formatDateStr(new Date(), "yyyy-MM-dd HH:mm:ss.SS"));
    }

    @Test
    public void insertElement() {
        String[] codeArray = {"062", "10000", "10110", "B200000", "1100", "D000", "0"};
        codeArray = CommonUtil.insertElement(codeArray, "0", 4);
        System.out.println(codeArray);
    }

    @Test
    public void xmlEncode() {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><BillOfLading>\n";
        System.out.println(CommonUtil.xmlEncode(str));
    }

    @Test
    public void xmlDecode() {
        String str = "&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&lt;BillOfLading&gt;";
        System.out.println(CommonUtil.xmlDecode(str));
    }

    @Test
    public void trim() {
        String str = "aaabbbcdeaaa";
        System.out.println(CommonUtil.trim(str, "aa")); /* return abbbcdea */
        System.out.println(CommonUtil.trim(str, "a")); /* return bbbcde */
    }

    @Test
    public void isNotEmpty() {
        Assert.assertFalse(CommonUtil.isNotEmpty(""));
        Assert.assertFalse(CommonUtil.isNotEmpty(null));
    }

    @Test
    public void isNotBlank() {
        Assert.assertFalse(CommonUtil.isNotBlank("  "));
        Assert.assertFalse(CommonUtil.isNotBlank(null));
        Assert.assertFalse(CommonUtil.isNotBlank(""));
    }

    @Test
    public void convertToMethodName() {
        Assert.assertEquals("setName", CommonUtil.convertToMethodName("name", A.class, true));
        Assert.assertEquals("getName", CommonUtil.convertToMethodName("name", A.class, false));

    }

    @Test
    public void setFieldValue() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        String name = "test";
        A a = new A(name);
        A o = A.class.newInstance();
        CommonUtil.setFieldValue(o, "name", name, null);
        Assert.assertEquals(a, o);
    }

    @Test
    public void parseDateTime() throws ParseException {
        Date date = CommonUtil.parseDateTime("2019-04-01 00:00:00", "GMT-8", "yyyy-MM-dd HH:mm:ss");
        System.out.println(date);
    }

    @Test
    public void timeZoneOffset() {
        TimeZone t1 = TimeZone.getTimeZone("GMT+0830");
        TimeZone t2 = TimeZone.getTimeZone("GMT+5");
        Assert.assertEquals((long) (3.5 * 60 * 60 * 1000), CommonUtil.timeZoneOffset(t1, t2));
    }
}

class A {
    private String name;

    public A() {
    }

    public A(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a = (A) o;
        return Objects.equals(name, a.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "A{" +
                "name='" + name + '\'' +
                '}';
    }
}