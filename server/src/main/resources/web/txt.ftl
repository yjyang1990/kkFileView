<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>${file.name}普通文本预览</title>
    <#include "*/commonHeader.ftl">
    <script src="js/jquery-3.6.1.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/base64.min.js" type="text/javascript"></script>
</head>
<body>
<input hidden id="textData" value="${textData}"/>
<#if "${file.suffix?html}" == "htm" || "${file.suffix?html}" == "html"  || "${file.suffix?html}" == "shtml" >
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading"> 
            <h4 class="panel-title"> <strong><font color="red"><input class="GLOkBtn" type="button" value="运行html" onclick="loadXmlData();" /></font></strong>
                <a data-toggle="collapse" data-parent="#accordion" onclick="loadText();">
                    ${file.name}   
                </a>
            </h4> 
        </div>
        <div class="panel-body">
            <div id="text"></div>
        </div>
    </div>
</div>
<script>
    // 将Freemarker的布尔值传递给JavaScript
    var scriptjs = ${scriptjs?c}; // ?c 将布尔值转换为字符串true/false
    
    /**
     *加载普通文本
     */
    function loadText() {
        var base64data = $("#textData").val()
       var div = document.getElementById("text");
        div.innerHTML = ""; //
        var textData = Base64.decode(base64data);
         textData = htmlttt(textData,1);
        var textPreData = "<xmp style='background-color: #FFFFFF;overflow-y: scroll;border:none'>" + textData + "</xmp>";
        $("#text").append(textPreData);
    }
    
     function htmlttt (str,txt){ 
             var s = "";
             if(str.length == 0) return "";
             s = str.replace(/&amp;/gi,"&");
             s = s.replace(/&lt;/gi,"<");
             s = s.replace(/&gt;/gi,">");
             s = s.replace(/&nbsp;/gi," ");
             s = s.replace(/&#39;/gi,"\'");
             s = s.replace(/&quot;/gi,"\""); 
             s = s.replace(/javascript/g,"javascript ");
             if (txt === 2){
              s = s.replace(/<script/gi, "&lt;script ");
              s = s.replace(/javascript/g,"javascript ");
              s = s.replace(/<\/script/gi, "&lt;/script ");
              s = s.replace(/<iframe/gi, "&lt;iframe ");
              s = s.replace(/<\/iframe/gi, "&lt;/iframe ");
              s = s.replace(/confirm/gi, "c&onfirm");
              s = s.replace(/alert/gi, "a&lert");
              s = s.replace(/eval/gi, "e&val");
             }
             return s;  
       } 
    
      /**
     *加载运行
     */
    function loadXmlData() {
        var base64data = $("#textData").val();
        var textData = Base64.decode(base64data);
        
        // 直接使用JavaScript变量进行判断
        if (scriptjs) {
            textData = htmlttt(textData, 1);
        } else {
            textData = htmlttt(textData, 2);
        }
        
        $('#text').html(textData);
    }
    
    /**
     * 初始化
     */
    window.onload = function () {
        initWaterMark();
        loadText();
    }
</script>
<#else/>
<link rel="stylesheet" href="highlight/highlight.css">
<script src="js/fenye.js" type="text/javascript"></script>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                    ${file.name}
                </a>
            </h4>
        </div>
      	<div id="divPagenation" class="black" >
        </div>
        <div id="divContent" class="panel-body">
        </div>
		<div id="divPagenationx" class="black" >
        </div>
    </div>
</div>
<script type="text/javascript">
    var base64data = $("#textData").val()
    var s = Base64.decode(base64data);
    var kkkeyword = '${highlightall}';
    var Length = 20000;
    var page = '${page}';
    var txt = "txt";
    DHTMLpagenation(s, kkkeyword, Length, page, txt);
    
    /**
     * 初始化
     */
    window.onload = function () {
        initWaterMark();
    }
</script>
</#if>
</body>
</html>