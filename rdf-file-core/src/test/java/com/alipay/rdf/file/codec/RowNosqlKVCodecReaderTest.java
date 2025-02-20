package com.alipay.rdf.file.codec;

import com.alipay.rdf.file.exception.RdfFileException;
import com.alipay.rdf.file.interfaces.FileFactory;
import com.alipay.rdf.file.interfaces.FileReader;
import com.alipay.rdf.file.model.FileConfig;
import com.alipay.rdf.file.model.StorageConfig;
import com.alipay.rdf.file.util.DateUtil;
import com.alipay.rdf.file.util.TemporaryFolderUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hongwei.quhw 2021/8/2 7:34 下午
 */
public class RowNosqlKVCodecReaderTest {
    TemporaryFolderUtil tf = new TemporaryFolderUtil();

    @Before
    public void setUp() throws IOException {
        tf.create();
    }

    // 模板有多余字段
    @Test
    public void test2() throws Exception {
        String filePath = File.class.getResource("/codec/kv/data/data2.txt").getPath();

        FileConfig config = new FileConfig(filePath, "/codec/kv/template/template2.json", new StorageConfig("nas"));

        FileReader fileReader = FileFactory.createReader(config);

        Map<String, Object> head = fileReader.readHead(HashMap.class);
        Assert.assertEquals(100L, head.get("totalCount"));
        Assert.assertEquals(new BigDecimal("300.03"), head.get("totalAmount"));

        Map<String, Object> row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_0", row.get("seq"));
        Assert.assertEquals("inst_seq_0", row.get("instSeq"));
        Assert.assertEquals("2013-11-09 12:34:56",
                DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131109", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131112 12:23:34",
                DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("33"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("10.22"), row.get("amount"));
        Assert.assertEquals(new Integer(22), row.get("age"));
        Assert.assertEquals(new Long(12345), row.get("longN"));
        Assert.assertEquals(Boolean.TRUE, row.get("bol"));
        Assert.assertEquals("备注1", row.get("memo"));

        row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_1", row.get("seq"));
        Assert.assertEquals("inst_seq_1", row.get("instSeq"));
        Assert.assertEquals("2013-11-10 15:56:12",
                DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131110", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131113 12:33:34",
                DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("34"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("11.88"), row.get("amount"));
        Assert.assertEquals(new Integer(33), row.get("age"));
        Assert.assertEquals(new Long(56789), row.get("longN"));
        Assert.assertEquals(Boolean.FALSE, row.get("bol"));
        Assert.assertEquals(null, row.get("memo"));

        Assert.assertNull(fileReader.readRow(HashMap.class));
    }

    // 文件有部分字段在模板中没有
    @Test
    public void test3() throws Exception {
        String filePath = File.class.getResource("/codec/kv/data/data3.txt").getPath();

        FileConfig config = new FileConfig(filePath, "/codec/kv/template/template2.json", new StorageConfig("nas"));

        FileReader fileReader = FileFactory.createReader(config);

        Map<String, Object> head = fileReader.readHead(HashMap.class);
        Assert.assertEquals(100L, head.get("totalCount"));
        Assert.assertEquals(new BigDecimal("300.03"), head.get("totalAmount"));

        Map<String, Object> row = fileReader.readRow(HashMap.class);
//        Assert.assertEquals("seq_0", row.get("seq"));
//        Assert.assertEquals("inst_seq_0", row.get("instSeq"));
//        Assert.assertEquals("2013-11-09 12:34:56", DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131109", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131112 12:23:34", DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("33"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("10.22"), row.get("amount"));
        Assert.assertEquals(new Integer(22), row.get("age"));
        Assert.assertEquals(new Long(12345), row.get("longN"));
        Assert.assertEquals(Boolean.TRUE, row.get("bol"));
        Assert.assertEquals("备注1", row.get("memo"));

        row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_1", row.get("seq"));
        Assert.assertEquals("inst_seq_1", row.get("instSeq"));
        // Assert.assertEquals("2013-11-10 15:56:12", DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131110", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131113 12:33:34",
                DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("34"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("11.88"), row.get("amount"));
        Assert.assertEquals(new Integer(33), row.get("age"));
        Assert.assertEquals(new Long(56789), row.get("longN"));
        Assert.assertEquals(Boolean.FALSE, row.get("bol"));
        Assert.assertEquals(null, row.get("memo"));

        Assert.assertNull(fileReader.readRow(HashMap.class));
    }

    // 文件字段有完全为空字段，解析忽略， 兼容关系模式写入空
    // 有空值为空
    @Test
    public void test4() throws Exception {
        String filePath = File.class.getResource("/codec/kv/data/data4.txt").getPath();

        FileConfig config = new FileConfig(filePath, "/codec/kv/template/template2.json", new StorageConfig("nas"));

        FileReader fileReader = FileFactory.createReader(config);

        Map<String, Object> head = fileReader.readHead(HashMap.class);
        Assert.assertEquals(100L, head.get("totalCount"));
        Assert.assertEquals(new BigDecimal("300.03"), head.get("totalAmount"));

        Map<String, Object> row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("20131109", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131112 12:23:34", DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("33"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("10.22"), row.get("amount"));
        Assert.assertEquals(new Integer(22), row.get("age"));
        Assert.assertEquals(new Long(12345), row.get("longN"));
        Assert.assertEquals(Boolean.TRUE, row.get("bol"));
        Assert.assertEquals("备注1", row.get("memo"));

        row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_1", row.get("seq"));
       // Assert.assertEquals("inst_seq_1", row.get("instSeq"));
        Assert.assertEquals("20131110", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131113 12:33:34", DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("34"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("11.88"), row.get("amount"));
        Assert.assertEquals(new Integer(33), row.get("age"));
        Assert.assertEquals(new Long(56789), row.get("longN"));
        Assert.assertEquals(Boolean.FALSE, row.get("bol"));
        Assert.assertEquals(null, row.get("memo"));

        Assert.assertNull(fileReader.readRow(HashMap.class));
    }

    // 配置fileconfig指定
    @Test(expected = RdfFileException.class)
    public void test5() throws Exception {
        String filePath = File.class.getResource("/codec/kv/data/data2.txt").getPath();

        FileConfig config = new FileConfig(filePath, "/codec/kv/template/template2.json", new StorageConfig("nas"));

        FileReader fileReader = FileFactory.createReader(config);
        config.setRowCodecMode("relation");

        Map<String, Object> head = fileReader.readHead(HashMap.class);
    }

    // 模板少字段
    @Test(expected = RdfFileException.class)
    public void test6() throws Exception {
        String filePath = File.class.getResource("/codec/kv/data/data4.txt").getPath();

        FileConfig config = new FileConfig(filePath, "/codec/kv/template/template3.json", new StorageConfig("nas"));

        FileReader fileReader = FileFactory.createReader(config);

        Map<String, Object> head = fileReader.readHead(HashMap.class);
        Assert.assertEquals(100L, head.get("totalCount"));
        Assert.assertEquals(new BigDecimal("300.03"), head.get("totalAmount"));

        Map<String, Object> row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("20131109", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131112 12:23:34", DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("33"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("10.22"), row.get("amount"));
        Assert.assertEquals(new Integer(22), row.get("age"));
        Assert.assertEquals(new Long(12345), row.get("longN"));
        Assert.assertEquals(Boolean.TRUE, row.get("bol"));
        Assert.assertEquals("备注1", row.get("memo"));

        row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_1", row.get("seq"));
        // Assert.assertEquals("inst_seq_1", row.get("instSeq"));
        Assert.assertEquals("20131110", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131113 12:33:34", DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("34"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("11.88"), row.get("amount"));
        Assert.assertEquals(new Integer(33), row.get("age"));
        Assert.assertEquals(new Long(56789), row.get("longN"));
        Assert.assertEquals(Boolean.FALSE, row.get("bol"));
        Assert.assertEquals(null, row.get("memo"));

        Assert.assertNull(fileReader.readRow(HashMap.class));
    }

    // 文件字段有内容，没有分隔符
    @Test(expected = RdfFileException.class)
    public void test7() throws Exception {
        String filePath = File.class.getResource("/codec/kv/data/data5.txt").getPath();

        FileConfig config = new FileConfig(filePath, "/codec/kv/template/template2.json", new StorageConfig("nas"));

        FileReader fileReader = FileFactory.createReader(config);

        Map<String, Object> head = fileReader.readHead(HashMap.class);
        Assert.assertEquals(100L, head.get("totalCount"));
        Assert.assertEquals(new BigDecimal("300.03"), head.get("totalAmount"));

        Map<String, Object> row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_0", row.get("seq"));
        Assert.assertEquals("inst_seq_0", row.get("instSeq"));
        Assert.assertEquals("2013-11-09 12:34:56",
                DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131109", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131112 12:23:34",
                DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("33"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("10.22"), row.get("amount"));
        Assert.assertEquals(new Integer(22), row.get("age"));
        Assert.assertEquals(new Long(12345), row.get("longN"));
        Assert.assertEquals(Boolean.TRUE, row.get("bol"));
        Assert.assertEquals("备注1", row.get("memo"));

        row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_1", row.get("seq"));
        Assert.assertEquals("inst_seq_1", row.get("instSeq"));
        Assert.assertEquals("2013-11-10 15:56:12",
                DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131110", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131113 12:33:34",
                DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new Integer("34"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("11.88"), row.get("amount"));
        Assert.assertEquals(new Integer(33), row.get("age"));
        Assert.assertEquals(new Long(56789), row.get("longN"));
        Assert.assertEquals(Boolean.FALSE, row.get("bol"));
        Assert.assertEquals(null, row.get("memo"));

        Assert.assertNull(fileReader.readRow(HashMap.class));
    }

    @Test
    public void test8() throws Exception {
        String filePath = File.class.getResource("/codec/kv/data/data6.txt").getPath();

        FileConfig config = new FileConfig(filePath, "/codec/kv/template/template4.json", new StorageConfig("nas"));

        FileReader fileReader = FileFactory.createReader(config);

        Map<String, Object> head = fileReader.readHead(HashMap.class);
        Assert.assertEquals(100L, head.get("totalCount"));
        Assert.assertEquals(new BigDecimal("300.03"), head.get("totalAmount"));

        Map<String, Object> row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_0", row.get("seq"));
        Assert.assertEquals("inst_seq_0", row.get("instSeq"));
        Assert.assertEquals("2013-11-09 12:34:56",
                DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131109", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131112 12:23:34",
                DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new BigDecimal("33"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("10.22"), row.get("amount"));
        Assert.assertEquals(new Integer(22), row.get("age"));
        Assert.assertEquals(new Long(12345), row.get("longN"));
        Assert.assertEquals(Boolean.TRUE, row.get("bol"));
        Assert.assertEquals("备注1", row.get("memo"));

        row = fileReader.readRow(HashMap.class);
        Assert.assertEquals("seq_1", row.get("seq"));
        Assert.assertEquals("inst_seq_1", row.get("instSeq"));
        Assert.assertEquals("2013-11-10 15:56:12",
                DateUtil.format((Date) row.get("gmtApply"), "yyyy-MM-dd HH:mm:ss"));
        Assert.assertEquals("20131110", DateUtil.format((Date) row.get("date"), "yyyyMMdd"));
        Assert.assertEquals("20131113 12:33:34",
                DateUtil.format((Date) row.get("dateTime"), "yyyyMMdd HH:mm:ss"));
        Assert.assertEquals(new BigDecimal("34"), row.get("applyNumber"));
        Assert.assertEquals(new BigDecimal("11.88"), row.get("amount"));
        Assert.assertEquals(new Integer(33), row.get("age"));
        Assert.assertEquals(new Long(56789), row.get("longN"));
        Assert.assertEquals(Boolean.FALSE, row.get("bol"));
        Assert.assertEquals(null, row.get("memo"));

        Assert.assertNull(fileReader.readRow(HashMap.class));
    }


    @After
    public void after() {
        tf.delete();
    }
}
