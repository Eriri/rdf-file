package com.alipay.rdf.file.util;

import java.lang.reflect.Method;
import java.util.Map;

import com.alipay.rdf.file.exception.RdfErrorEnum;
import com.alipay.rdf.file.exception.RdfFileException;
import org.junit.Assert;
import org.junit.Test;


/**
 * 工具类测试
 * 
 * @author hongwei.quhw
 * @version $Id: RdfFileUtilTest.java, v 0.1 2017年8月8日 下午5:43:24 hongwei.quhw Exp $
 */
public class RdfFileUtilTest {
    @Test
    public void testFineMethod() {
        Method method = RdfFileUtil.findMethod(Config1.class, "hashCode");
        Assert.assertNull(method);

        method = RdfFileUtil.findMethod(Config1.class, "equals");
        Assert.assertNull(method);

        method = RdfFileUtil.findMethod(Config2.class, "equals");
        Assert.assertNotNull(method);

        method = RdfFileUtil.findMethod(Config2.class, "hashCode");
        Assert.assertNotNull(method);
    }

    /**
     * 测试分割符
     */
    @Test
    public void testSplitPreserveAllTokens() {
        String[] cols = RdfFileUtil.split("a|b|c", "|");
        Assert.assertEquals(3, cols.length);
        Assert.assertEquals("c", cols[2]);

        cols = RdfFileUtil.split("a|b|c|", "|");
        Assert.assertEquals(4, cols.length);
        Assert.assertEquals("", cols[3]);

        cols = RdfFileUtil.split("|a|b|c|", "|");
        Assert.assertEquals(5, cols.length);
        Assert.assertEquals("", cols[0]);

        cols = RdfFileUtil.split("a|@|b|@|c", "|@|");
        Assert.assertEquals(3, cols.length);
        Assert.assertEquals("a", cols[0]);

        cols = RdfFileUtil.split("a|@|b|@|c@", "|@|");
        Assert.assertEquals(3, cols.length);
        Assert.assertEquals("c@", cols[2]);

        cols = RdfFileUtil.split("a|@|b|@|c|", "|@|");
        Assert.assertEquals(3, cols.length);
        Assert.assertEquals("c|", cols[2]);
    }

    private static class Config1 {

    }

    private static class Config2 {
        private String name;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Config2 other = (Config2) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

    }

    @Test
    public void testParsePathParams() {

        Assert.assertTrue(RdfFileUtil.parsePathParams("").isEmpty());

        String path = "/aaa/bb/cc/dd.json";

        Assert.assertTrue(RdfFileUtil.parsePathParams(path).isEmpty());

        path = "/aaa/bb/cc/dd.json?xxx";

        try {
            Assert.assertTrue(RdfFileUtil.parsePathParams(path).isEmpty());
            Assert.fail();
        } catch (RdfFileException e) {
            Assert.assertEquals(RdfErrorEnum.ILLEGAL_ARGUMENT, e.getErrorEnum());
        }

        path = "/aaa/bb/cc/dd.json?xxx=ggg&kkk=ddd&hhh=";
        Map<String, String> params = RdfFileUtil.parsePathParams(path);
        Assert.assertEquals(3, params.size());
        Assert.assertEquals("ggg", params.get("xxx"));
        Assert.assertEquals("ddd", params.get("kkk"));
        Assert.assertEquals("", params.get("hhh"));
    }
}
