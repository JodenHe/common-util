package top.joden123.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

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
}