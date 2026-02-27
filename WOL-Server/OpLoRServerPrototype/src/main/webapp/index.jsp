<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- JSPページのコンテンツタイプを設定し、文字エンコーディングをUTF-8に指定 -->

<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <!-- ページのタイトルを設定 -->
</head>
<body>
    <!-- ページの本体 -->

    <h1><%= "Hello World!" %></h1>
    <!-- JSPの式（<%= ... %>）を使用して、"Hello World!"という文字列を表示 -->
    <br/>
    <!-- 改行タグ -->

    <a href="hello-servlet">Hello Servlet</a>
    <!-- "Hello Servlet"というリンクを表示 -->
    <!-- クリックすると、"hello-servlet"というサーブレットのURLに遷移する -->
</body>
</html>

<!-- メモ：webappディレクトリが性的ファイルの公開用ディレクトリ，サーブレットは配置しなくても動作する -->