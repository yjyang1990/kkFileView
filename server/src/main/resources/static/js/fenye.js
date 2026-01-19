      function htmlttt (str){ 
             var s = "";
             if(str.length == 0) return "";
             s = str.replace(/&amp;/gi,"&");
             s = s.replace(/&nbsp;/gi," ");
             s = s.replace(/&#39;/gi,"\'");
             s = s.replace(/&quot;/gi,"\""); 
             s = s.replace(/javascript/g,"javascript ");
             s = s.replace(/iframe/gi, "iframe ");
             return s;  
       }
       
   function isEmptyString(str) {
  return !str || str.length === 0;
}
       
       function removeExtraNewlines(str) {
  // 替换连续的换行符为单个换行符
  return str.replace(/(?:\r\n|\r|\n){2,}/g, '\n');
}

      function DHTMLpagenation(content,kkkeyword,Length,page,txt)
        {
            if(page==0){
                page=1;  
            }
            this.content=content; // 内容
            this.contentLength=s.length; // 内容长度
            this.pageSizeCount; // 总页数
            this.perpageLength= Length; //default perpage byte length.
            this.currentPage= page; // 起始页为第1页
            this.regularExp=/\d+/; // 建立正则表达式，匹配数字型字符串。
            this.divDisplayContent;
            this.contentStyle=null;
            this.strDisplayContent="";
            this.divDisplayPagenation;
            this.strDisplayPagenation="";

            // 把第二个参数赋给perpageLength;
            arguments.length==2 ? perpageLength = arguments[1] : '';

            try {
                //创建要显示的DIV
                divExecuteTime=document.createElement("DIV");
                document.body.appendChild(divExecuteTime);
            }
            catch(e)
            {
            }

            // 得到divPagenation容器。
            if(document.getElementById("divPagenation"))
            {
                divDisplayPagenation=document.getElementById("divPagenation");
            }
            else
            {
                try
                {
                    //创建分页信息
                    divDisplayPagenation=document.createElement("DIV");
                    divDisplayPagenation.id="divPagenation";
                    document.body.appendChild(divDisplayPagenation);
                }
                catch(e)
                {
                    return false;
                }
            }

            // 得到divContent容器
            if(document.getElementById("divContent"))
            {
                divDisplayContent=document.getElementById("divContent");
            }
            else
            {
                try
                {
                    //创建每页显示内容的消息的DIV
                    divDisplayContent=document.createElement("DIV");
                    divDisplayContent.id="divContent";
                    document.body.appendChild(divDisplayContent);
                }
                catch(e)
                {
                    return false;
                }
            }
            DHTMLpagenation.initialize();
            return this;

        };

        //初始化分页；
        //包括把加入CSS，检查是否需要分页
        DHTMLpagenation.initialize=function()
        {
            divDisplayContent.className= contentStyle != null ? contentStyle : "divContent";

            if(contentLength<=perpageLength)
            {
                if(txt =="code"){
                content = htmlttt(content);
                strDisplayContent = '<pre><code>'+content+'</code></pre>';
                divDisplayContent.innerHTML=strDisplayContent;
				if (!!window.ActiveXObject || "ActiveXObject" in window){
           }else{
            	hljs.highlightAll(kkkeyword);
              } 
                }else if(txt =="js"){
                 content = htmlttt(content);
                 var result = js_beautify(content, 1, "\t");
                strDisplayContent = '<pre><code class="language-js">'+result+'</code></pre>';
                divDisplayContent.innerHTML=strDisplayContent;
				if (!!window.ActiveXObject || "ActiveXObject" in window){
           }else{
            	hljs.highlightAll(kkkeyword);
              } 
                }else{
                   content = removeExtraNewlines(content);
                    let list = content.split('\n') // 换行符分割
                    for(let i=0;i<list.length;i++) {
                        list[i] =  i + "." + list[i] // 加序号
                    }
                    let txt = list.join('\n'); 
             if (kkkeyword!==""&&kkkeyword!==null&&kkkeyword!=="null") {
            	 txt = txt.split(kkkeyword).join("<span class='highlight'>" + kkkeyword + "</span>");
              }
                divDisplayContent.innerHTML=txt;    
                }
                return null;
            }

            pageSizeCount=Math.ceil((contentLength/perpageLength));

            DHTMLpagenation.goto(currentPage);

            DHTMLpagenation.displayContent();
        };

       //显示分页栏
        DHTMLpagenation.displayPage=function()
        {
            strDisplayPagenation="";
            if(currentPage && currentPage !=1)
            {
             
             strDisplayPagenation+='<button  onclick="DHTMLpagenation.previous()">上一页 </button>';
            }
            else
            {
                strDisplayPagenation+="上一页";
            }

            if(currentPage && currentPage!=pageSizeCount)
            {
             strDisplayPagenation+='<button  onclick="DHTMLpagenation.next()">下一页 </button>';
    
             strDisplayPagenation+="<input type='number' value='"+currentPage+"' id='yemaPerpageLength' style='width:70px' /><button type='button' onclick='DHTMLpagenation.tiaozhuan()'/> 跳转</button> ";
            }
            else
            {
               strDisplayPagenation+="下一页";
            }
            if (isEmptyString(currentPage)) {
             currentPage =1;
             }
           strDisplayPagenation+=+currentPage+"/" + pageSizeCount + "页。<br>每页 " + perpageLength + " 字符，调整字符数：<input type='number' value='"+perpageLength+"' id='ctlPerpageLength'  style='width:70px'  /><button onclick='DHTMLpagenation.change()' /> 确定</button>";
          divDisplayPagenation.innerHTML=strDisplayPagenation;
         };

        //上一页
        DHTMLpagenation.previous=function()
        {
            DHTMLpagenation.goto(currentPage-1);
        };

        //下一页
        DHTMLpagenation.next=function()
        {

            DHTMLpagenation.goto(currentPage+1);
        };

        //跳转至某一页
        DHTMLpagenation.goto=function(iCurrentPage)
        {
            if (isEmptyString(iCurrentPage)) {
             iCurrentPage =1;
             }
            if(regularExp.test(iCurrentPage))
            {
                currentPage=iCurrentPage;
                //获取当前的内容 里面包含 ❈
                var currentContent = s.substr((currentPage-1)*perpageLength,perpageLength);
                //当前页是否有 ❈ 获取最后一个 ❈ 的位置
                var indexOf = currentContent.indexOf("❈");
                if(indexOf >= 0)
                {
                      //获取从开始位置到当前页位置的内容
                      var beginToEndContent = s.substr(0,currentPage*perpageLength);

                      //获取开始到当前页位置的内容 中的 * 的最后的下标
                      var reCount = beginToEndContent.split("❈").length - 1;

                      var contentArray = currentContent.split("❈");

                      currentContent = replaceStr(contentArray,reCount,matchContent);

                }

                strDisplayContent=currentContent;
            }
            else
            {
                alert("页面参数错误");
            }
            DHTMLpagenation.displayPage();
            DHTMLpagenation.displayContent();
        };
        //显示当前页内容
        DHTMLpagenation.displayContent=function()
        {
           if(txt =="code"){
              strDisplayContent = htmlttt(strDisplayContent);
             strDisplayContent = "<pre><code>"+strDisplayContent+"</code></pre>";
            divDisplayContent.innerHTML=strDisplayContent;
            	if (!!window.ActiveXObject || "ActiveXObject" in window){
            }else{
			hljs.highlightAll(kkkeyword);
                }	
                }else if(txt =="js"){
                 strDisplayContent = htmlttt(strDisplayContent);
                 var result = js_beautify(strDisplayContent, 1, "\t");
                strDisplayContent ='<pre><code class="language-js">'+result+'</code></pre>';
            divDisplayContent.innerHTML=strDisplayContent;
				if (!!window.ActiveXObject || "ActiveXObject" in window){
           }else{
            	hljs.highlightAll(kkkeyword);
              } 
                }else{
             if (kkkeyword!==""&&kkkeyword!==null&&kkkeyword!=="null") {
 
            	strDisplayContent = strDisplayContent.split(kkkeyword).join("<span class='highlight'>" + kkkeyword + "</span>");
              }
              divDisplayContent.innerHTML=strDisplayContent;
                }
        };
        //改变每页的字节数
        DHTMLpagenation.change=function()
        {

            var iPerpageLength = document.getElementById("ctlPerpageLength").value;
            if(regularExp.test(iPerpageLength))
            {

                DHTMLpagenation(s,iPerpageLength);
            }
            else
            {
                alert("请输入数字");
            }
        };
           //改变页码
        DHTMLpagenation.tiaozhuan=function()
        {
            var yema = document.getElementById("yemaPerpageLength").value;
            if(regularExp.test(yema))
            {
             DHTMLpagenation.goto(yema);
            }
            else
            {
                alert("请输入数字");
            }
        };

        /*  currentArray:当前页以 * 分割后的数组
            replaceCount:从开始内容到当前页的内容 * 的个数
            matchArray ： img标签的匹配的内容
        */
        function replaceStr(currentArray,replaceCount,matchArray)
        {
            var result = "";
            for(var i=currentArray.length -1,j = replaceCount-1 ;i>=1; i--)
            {

               var temp = (matchArray[j] + currentArray[i]);

               result = temp + result;

               j--;
            }

            result = currentArray[0] + result ;

            return result;
        }