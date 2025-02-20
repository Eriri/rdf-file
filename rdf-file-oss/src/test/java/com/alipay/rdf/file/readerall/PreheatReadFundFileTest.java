package com.alipay.rdf.file.readerall;

import com.alipay.rdf.file.interfaces.FileFactory;
import com.alipay.rdf.file.interfaces.FileOssToolContants;
import com.alipay.rdf.file.interfaces.FileReader;
import com.alipay.rdf.file.interfaces.FileStorage;
import com.alipay.rdf.file.model.FileConfig;
import com.alipay.rdf.file.model.FileDefaultConfig;
import com.alipay.rdf.file.model.StorageConfig;
import com.alipay.rdf.file.preheat.OssPreheatReaderConfig;
import com.alipay.rdf.file.spi.RdfFileReaderSpi;
import com.alipay.rdf.file.storage.OssConfig;
import com.alipay.rdf.file.util.OssTestUtil;
import com.alipay.rdf.file.util.RdfFileUtil;
import com.alipay.rdf.file.util.TemporaryFolderUtil;
import com.alipay.rdf.file.util.TestLog;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证预热读
 *
 * @author hongwei.quhw
 * @version $Id: PreheatReadTest.java, v 0.1 2017年7月17日 下午7:06:40 hongwei.quhw Exp $
 */
public class PreheatReadFundFileTest {
    private TemporaryFolderUtil        temporaryFolder = new TemporaryFolderUtil();
    private static final StorageConfig storageConfig   = OssTestUtil.geStorageConfig();
    private static String              ossPathPrefix   = "rdf/rdf-file/open/PreheatReadFundFileTest";
    private static FileStorage         fileStorage     = FileFactory.createStorage(storageConfig);
    private OssConfig                  ossConfig;

    @Before
    public void setUp() throws Exception {
        FileDefaultConfig defaultConfig = new FileDefaultConfig();
        TestLog log = new TestLog() {
            @Override
            public void debug(String msg) {
            }

            @Override
            public void warn(String msg) {
            }

            @Override
            public void info(String msg) {
                if (msg.indexOf("ReadAllProcessor") == 0) {
                    super.debug(msg);
                }
            }
        };
        defaultConfig.setCommonLog(log);
        temporaryFolder.create();
        ossConfig = (OssConfig) storageConfig.getParam(OssConfig.OSS_STORAGE_CONFIG_KEY);
        ossConfig.setOssTempRoot(temporaryFolder.getRoot().getAbsolutePath());
        System.out.println(temporaryFolder.getRoot().getAbsolutePath());
    }

    @Test
    public void testPreheatWithNormalRead() throws Exception {
        String ossFilePath = RdfFileUtil.combinePath(ossPathPrefix, "OFD_996_H0_20170427_03.txt");
        fileStorage.upload(
            File.class.getResource("/preheat/fund/all/OFD_996_H0_20170427_03.txt").getPath(),
            ossFilePath, false);

        FileConfig normalConfig = new FileConfig(ossFilePath,
            "/preheat/template_batchPurchase.json", storageConfig);
        normalConfig.setFileEncoding("GBK");
        normalConfig.setSummaryEnable(true);
        normalConfig.setReadAll(true);
        FileReader normalReader = FileFactory.createReader(normalConfig);

        FileConfig preheatConfig = new FileConfig(ossFilePath,
            "/preheat/template_batchPurchase.json", storageConfig);
        preheatConfig.setFileEncoding("GBK");
        preheatConfig.setSummaryEnable(true);
        preheatConfig.setType(FileOssToolContants.OSS_PREHEAT_PROTOCOL_READER);
        preheatConfig.setReadAll(true);

        FileReader preheatReader = FileFactory.createReader(preheatConfig);

        Assert.assertEquals(normalReader.readHead(HashMap.class),
            preheatReader.readHead(HashMap.class));

        Map<String, Object> row = null;
        while (null != (row = normalReader.readRow(HashMap.class))) {
            Assert.assertEquals(row, preheatReader.readRow(HashMap.class));
            System.out.println(row);
        }

        Assert.assertNull(preheatReader.readRow(HashMap.class));

        Map<String, Object> tail = preheatReader.readTail(HashMap.class);

        Assert.assertEquals("OFDCFEND", tail.get("fileEnd"));

        normalReader.close();
        preheatReader.close();
    }

    @Test
    public void testPreheatWithNormalReadLine() throws Exception {
        String ossFilePath = RdfFileUtil.combinePath(ossPathPrefix, "OFD_996_H0_20170427_03.txt");
        fileStorage.upload(
            File.class.getResource("/preheat/fund/all/OFD_996_H0_20170427_03.txt").getPath(),
            ossFilePath, false);

        FileConfig normalConfig = new FileConfig(ossFilePath,
            "/preheat/template_batchPurchase.json", storageConfig);
        normalConfig.setFileEncoding("GBK");
        normalConfig.setSummaryEnable(true);
        normalConfig.setReadAll(true);
        FileReader normalReader = FileFactory.createReader(normalConfig);

        FileConfig preheatConfig = new FileConfig(ossFilePath,
            "/preheat/template_batchPurchase.json", storageConfig);
        preheatConfig.setFileEncoding("GBK");
        preheatConfig.setSummaryEnable(true);
        preheatConfig.setReadAll(true);
        preheatConfig.setType(FileOssToolContants.OSS_PREHEAT_PROTOCOL_READER);

        FileReader preheatReader = FileFactory.createReader(preheatConfig);

        String line = null;
        while (null != (line = normalReader.readLine())) {
            Assert.assertEquals(line, preheatReader.readLine());
        }

        Assert.assertNull(preheatReader.readLine());

        normalReader.close();
        preheatReader.close();

        RdfFileReaderSpi preheatReader2 = (RdfFileReaderSpi) FileFactory
            .createReader(preheatConfig);
        RdfFileReaderSpi normalReader2 = (RdfFileReaderSpi) FileFactory.createReader(normalConfig);

        while (null != (line = normalReader2.readBodyLine())) {
            Assert.assertEquals(line, preheatReader2.readBodyLine());
        }

        Assert.assertNull(preheatReader.readLine());

        normalReader.close();
        preheatReader.close();

    }

    @Test
    public void testPreheatWithNormalRead2() throws Exception {
        String ossPath = RdfFileUtil.combinePath(ossPathPrefix, "testPreheatWithNormalRead2");

        fileStorage.upload(File.class.getResource("/preheat/fund/all/").getPath(), ossPath, false);

        List<String> paths = fileStorage.listAllFiles(ossPath);
        Collections.sort(paths);
        System.out.println(paths);

        FileConfig preheatConfig = new FileConfig("/preheat/template_batchPurchase.json",
            storageConfig);
        preheatConfig.setReadAll(true);
        preheatConfig.setFileEncoding("GBK");
        preheatConfig.setType(FileOssToolContants.OSS_PREHEAT_PROTOCOL_READER);
        OssPreheatReaderConfig preheatReaderConfig = new OssPreheatReaderConfig();
        preheatReaderConfig.setPaths(paths);
        preheatReaderConfig.setSliceBlockSize(512 * 1024);
        preheatConfig.addParam(OssPreheatReaderConfig.OSS_PREHEAT_READER_CONFIG_KEY,
            preheatReaderConfig);

        FileReader preheatReader = FileFactory.createReader(preheatConfig);

        for (String path : paths) {
            System.out.println("normal read path=" + path);
            FileConfig normalConfig = new FileConfig(path, "/preheat/template_batchPurchase.json",
                storageConfig);
            normalConfig.setFileEncoding("GBK");
            FileReader normalReader = FileFactory.createReader(normalConfig);
            Map<String, Object> row = null;
            while (null != (row = normalReader.readRow(HashMap.class))) {
                System.out.println(row);
                Assert.assertEquals(row, preheatReader.readRow(HashMap.class));
            }

            normalReader.close();
        }

        Assert.assertNull(preheatReader.readRow(HashMap.class));

        preheatReader.close();

    }

    @Test
    public void testPreheatWithNormalEmpty() throws Exception {
        String ossPath = RdfFileUtil.combinePath(ossPathPrefix, "testPreheatWithNormalEmpty");

        fileStorage.upload(File.class.getResource("/preheat/fund/empty/").getPath(), ossPath,
            false);

        List<String> paths = fileStorage.listAllFiles(ossPath);
        Collections.sort(paths);
        System.out.println(paths);

        FileConfig preheatConfig = new FileConfig("/preheat/template_batchPurchase.json",
            storageConfig);
        preheatConfig.setFileEncoding("GBK");
        preheatConfig.setSummaryEnable(true);
        preheatConfig.setReadAll(true);
        preheatConfig.setType(FileOssToolContants.OSS_PREHEAT_PROTOCOL_READER);
        OssPreheatReaderConfig preheatReaderConfig = new OssPreheatReaderConfig();
        preheatReaderConfig.setPaths(paths);
        preheatReaderConfig.setSliceBlockSize(512 * 1024);
        preheatConfig.addParam(OssPreheatReaderConfig.OSS_PREHEAT_READER_CONFIG_KEY,
            preheatReaderConfig);

        FileReader preheatReader = FileFactory.createReader(preheatConfig);

        Assert.assertNull(preheatReader.readRow(HashMap.class));

        preheatReader.close();

    }

    @After
    public void after() {
        temporaryFolder.delete();
    }
}
