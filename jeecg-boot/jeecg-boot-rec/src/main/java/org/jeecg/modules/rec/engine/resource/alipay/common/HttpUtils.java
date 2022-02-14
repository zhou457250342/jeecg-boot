package org.jeecg.modules.rec.engine.resource.alipay.common;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author: zhou x
 * @Date: 2022/2/14 12:17
 */
public class HttpUtils {
    /**
     * 下载文件
     *
     * @param netAddress
     * @param path
     * @throws IOException
     */
    public static void downloadNet(String netAddress, String path) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("创建存储路径失败");
            }
        }
        URL url = new URL(netAddress);
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        try {
            int bytesum = 0;
            int byteread;
            byte[] buffer = new byte[1024];
            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                fileOutputStream.write(buffer, 0, byteread);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            fileOutputStream.close();
        }
    }
}
