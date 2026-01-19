<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>${file.name}代码预览</title>
    <#include  "*/commonHeader.ftl">
    <script src="js/jquery-3.6.1.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="highlight/default.min.css">
    <link rel="stylesheet" href="highlight/highlight.css">
    <script src="highlight/highlight.min.js" type="text/javascript"></script>
    <script src="js/fenye.js" type="text/javascript"></script>
    <#if "${file.suffix?lower_case}" == "js" > 
    <script src="js/jsformat.js" type="text/javascript"></script>
	</#if>
    <script src="js/base64.min.js" type="text/javascript"></script>
</head>
<body>
<input hidden id="textData" value="${textData}"/>
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
        var Length= 20000;
        var page= '${page}';
       <#if "${file.suffix?lower_case}" == "js" > var txt = "js";<#else>var txt = "code";</#if>
         DHTMLpagenation(s,kkkeyword,Length,page,txt);
		   /**
     * 初始化
     */
    window.onload = function () {
        initWaterMark();
    }
</script>
</body>

</html>
