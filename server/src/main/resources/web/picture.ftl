<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>图片预览</title>
    <#include "*/commonHeader.ftl">
    <link rel="stylesheet" href="css/viewer.min.css">
    <script src="js/viewer.min.js"></script>
    <style>
        body {
            background-color: #404040;
        }
        #image { width: 800px; margin: 0 auto; font-size: 0;}
        #image li {  display: inline-block;width: 50px;height: 50px; margin-left: 1%; padding-top: 1%;}
        /*#dowebok li img { width: 200%;}*/
    </style>
</head>
<body>

<ul id="image">
    <#list imgUrls as img>
	<#if img?contains("http://") || img?contains("https://")|| img?contains("ftp://")|| img?contains("file://")>
    <#assign finalUrl="${img}">
     <#else>
    <#assign finalUrl="${baseUrl}${img}">
     </#if>
      <li><div src="${finalUrl}" style="display: none"></li>
    </#list>
</ul>
<script>
 document.addEventListener('DOMContentLoaded', function () {
        var viewer = new Viewer(document.getElementById('image'), {
        url: 'src',
        navbar: false,
        button: false,
        backdrop: false,
        loop : true,
            });
            viewer.view(0); // 0 是图片的索引，如果你想点击第一张图片，索引为 0
        });
    /*初始化水印*/
    window.onload = function() {
        initWaterMark();
    }
</script>
</body>

</html>
