package org.example.ssmDemo.model;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class RequestFormModel {

    public RequestFormModel() {
        paramMap = new HashMap<>();
        fileMap = new HashMap<>();
    }

    /**
     * 普通表单MAP
     */
    private Map<String, String> paramMap;

    /**
     * file表单MAP
     */
    private Map<String, FileItem> fileMap;
}
