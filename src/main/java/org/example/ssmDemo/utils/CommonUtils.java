package org.example.ssmDemo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CommonUtils {
    /**
     * 转换BufferedImage 数据为byte数组
     *
     * @param image Image对象
     * @param format image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage image, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
