<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta charset="utf-8">
    </head>
<body>
<h2>Hello World!</h2>
    <form action="/ssmDemo/v1/api/fileupload" method="post"  enctype="multipart/form-data">
        <p>选择文件:<input type="file" name="files" multiple="multiple"></p>
        <p><input type="submit" value="提交"></p>
    </form>
</body>
</html>
