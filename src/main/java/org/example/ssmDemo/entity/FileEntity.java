package org.example.ssmDemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.ByteArrayOutputStream;

/**
 * Description:
 * <p>
 * ClassName: FileEntity
 * date: 2021/3/28 11:19
 *
 * @author liangc
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class FileEntity {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 字节流
     */
    private ByteArrayOutputStream byteArrayOutputStream;
}
