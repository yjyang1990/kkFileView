<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>${file.name}预览</title>
    <link rel='stylesheet' href='xlsx/plugins/css/pluginsCss.css' />
    <link rel='stylesheet' href='xlsx/plugins/plugins.css' />
    <link rel='stylesheet' href='xlsx/css/luckysheet.css' />
    <link rel='stylesheet' href='xlsx/assets/iconfont/iconfont.css' />
    <script src="xlsx/plugins/js/plugin.js"></script>
    <script src="xlsx/luckysheet.umd.js"></script>
    <script src="js/watermark.js" type="text/javascript"></script>
    <script src="js/base64.min.js" type="text/javascript"></script>
</head>
<#if pdfUrl?contains("http://") || pdfUrl?contains("https://") || pdfUrl?contains("ftp://")>
    <#assign finalUrl="${pdfUrl}">
<#else>
    <#assign finalUrl="${baseUrl}${pdfUrl}">
</#if>
<script>
    /**
     * 初始化水印
     */
    function initWaterMark() {
        let watermarkTxt = '${watermarkTxt}';
        if (watermarkTxt !== '') {
            watermark.init({
                watermark_txt: '${watermarkTxt}',
                watermark_x: 0,
                watermark_y: 0,
                watermark_rows: 0,
                watermark_cols: 0,
                watermark_x_space: ${watermarkXSpace},
                watermark_y_space: ${watermarkYSpace},
                watermark_font: '${watermarkFont}',
                watermark_fontsize: '${watermarkFontsize}',
                watermark_color: '${watermarkColor}',
                watermark_alpha: ${watermarkAlpha},
                watermark_width: ${watermarkWidth},
                watermark_height: ${watermarkHeight},
                watermark_angle: ${watermarkAngle},
            });
        }
    }

    // 添加加载状态管理
    let isLoading = false;
    let loadingTask = null;

</script>
<style>
    * {
        margin: 0;
        padding: 0;
    }

    html, body {
        height: 100%;
        width: 100%;
        overflow: hidden;
    }

    #loading-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.95);
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        z-index: 9999;
        transition: opacity 0.3s ease;
    }

    #loading-progress {
        width: 300px;
        height: 20px;
        background: #f0f0f0;
        border-radius: 10px;
        margin-top: 20px;
        overflow: hidden;
    }

    #loading-bar {
        width: 0%;
        height: 100%;
        background: linear-gradient(90deg, #4CAF50, #8BC34A);
        transition: width 0.3s ease;
        border-radius: 10px;
    }

    .spinner {
        width: 50px;
        height: 50px;
        border: 5px solid #f3f3f3;
        border-top: 5px solid #4CAF50;
        border-radius: 50%;
        animation: spin 1s linear infinite;
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

    .loading-text {
        margin-top: 20px;
        font-size: 16px;
        color: #666;
    }

    .error-message {
        display: none;
        background: #ffebee;
        border: 1px solid #ffcdd2;
        border-radius: 4px;
        padding: 20px;
        margin: 20px;
        text-align: center;
    }

</style>
<body>
<!-- 添加加载遮罩层 -->
<div id="loading-overlay">
    <div class="spinner"></div>
    <div class="loading-text">正在加载Excel文件...</div>
    <div id="loading-progress">
        <div id="loading-bar"></div>
    </div>
</div>

<!-- 错误提示 -->
<div id="error-message" class="error-message">
    <h3>加载失败</h3>
    <p id="error-detail"></p>
    <button onclick="retryLoad()" style="margin-top: 10px; padding: 8px 16px;">重试</button>
</div>

<div id="lucky-mask-demo" style="position: absolute;z-index: 1000000;left: 0px;top: 0px;bottom: 0px;right: 0px; background: rgba(255, 255, 255, 0.8); text-align: center;font-size: 40px;align-items:center;justify-content: center;display: none;">加载中</div>

<p style="text-align:center;">
<div id="button-area" style="display: none;">
    <label><button onclick="tiaozhuan()">跳转HTML预览</button></label>
    <button id="confirm-button" onclick="print()">打印</button>
</div>
<div id="luckysheet" style="margin:0px;padding:0px;position:absolute;width:100%;left: 0px;top: 20px;bottom: 0px;outline: none;"></div>

<script src="xlsx/luckyexcel.umd.js"></script>
<script>
    function tiaozhuan(){
        var test = window.location.href;
        test = test.replace(new RegExp("&officePreviewType=xlsx",("gm")),"");
        test = test+'&officePreviewType=html';
        window.location.href=test;
    }

    var url = '${finalUrl}';
    var baseUrl = '${baseUrl}'.endsWith('/') ? '${baseUrl}' : '${baseUrl}' + '/';
    if (!url.startsWith(baseUrl)) {
        url = baseUrl + 'getCorsFile?urlPath=' + encodeURIComponent(Base64.encode(url));
    }

    let mask = document.getElementById("lucky-mask-demo");
    let loadingOverlay = document.getElementById("loading-overlay");
    let loadingBar = document.getElementById("loading-bar");
    let errorMessage = document.getElementById("error-message");

    // 更新加载进度
    function updateProgress(percent) {
        if (loadingBar) {
            loadingBar.style.width = percent + '%';
        }
    }

    // 显示错误信息
    function showError(message) {
        hideLoading();
        errorMessage.style.display = 'block';
        document.getElementById('error-detail').textContent = message;
    }

    // 隐藏加载动画
    function hideLoading() {
        if (loadingOverlay) {
            loadingOverlay.style.opacity = '0';
            setTimeout(() => {
                loadingOverlay.style.display = 'none';
                document.getElementById('button-area').style.display = 'block';
            }, 300);
        }
    }

    // 重试加载
    function retryLoad() {
        errorMessage.style.display = 'none';
        loadingOverlay.style.display = 'flex';
        loadingOverlay.style.opacity = '1';
        loadTextAsync();
    }

    // 异步加载Excel文件
    async function loadTextAsync() {
        if (isLoading) return;
        
        isLoading = true;
        updateProgress(10);
        
        try {
            initWaterMark();
            
            const value = url;
            const name = '${file.name}';
            
            if (!value) {
                showError('文件URL为空');
                return;
            }

            updateProgress(30);
            
            // 使用异步方式加载
           await new Promise(resolve => setTimeout(resolve, 100)); // 给UI更新一点时间
            
       
            
            // 或者使用现有的同步方法，但放在setTimeout中避免阻塞
            await transformWithTimeout(value, name);
            
            updateProgress(100);
            
            // 延迟隐藏加载界面，让用户看到加载完成
            setTimeout(() => {
                hideLoading();
                isLoading = false;
            }, 500);
            
        } catch (error) {
            console.error('加载Excel失败:', error);
            showError('加载失败: ' + error.message);
            isLoading = false;
        }
    }

    // 使用setTimeout将同步任务拆分
    function transformWithTimeout(value, name) {
        return new Promise((resolve, reject) => {
            updateProgress(50);
            
            // 将转换过程放在setTimeout中，避免阻塞主线程
            setTimeout(() => {
                try {
                    LuckyExcel.transformExcelToLuckyByUrl(value, name, function(exportJson, luckysheetfile){
                        if(exportJson.sheets==null || exportJson.sheets.length==0){
                            reject(new Error("读取excel文件内容失败!"));
                            return;
                        }
                        
                        updateProgress(80);
                        
                        // 使用requestAnimationFrame来更新UI，避免阻塞
                        requestAnimationFrame(() => {
                            try {
                                window.luckysheet.destroy();
                                window.luckysheet.create({
                                    container: 'luckysheet',
                                    lang: "zh",
                                    showtoolbarConfig:{
                                        image: true,
                                        print: true,
                                        exportXlsx: true,
                                    },
                                    allowCopy: true,
                                    showtoolbar: true,
                                    showinfobar: false,
                                    showsheetbar: true,
                                    showstatisticBar: true,
                                    sheetBottomConfig: true,
                                    allowEdit: true,
                                    enableAddRow: false,
                                    enableAddCol: false,
                                    userInfo: false,
                                    showRowBar: true,
                                    showColumnBar: false,
                                    sheetFormulaBar: false,
                                    enableAddBackTop: true,
                                    forceCalculation: false,
                                    data: exportJson.sheets,
                                    title: exportJson.info.name,
                                    userInfo: exportJson.info.name.creator,
                                    // 添加加载完成的回调
                                    hook: {
                                        workbookCreateAfter: function() {
                                            resolve();
                                        }
                                    }
                                });
                                
                                updateProgress(90);
                                
                            } catch (err) {
                                reject(err);
                            }
                        });
                    }, 100);
                    
                } catch (error) {
                    reject(error);
                }
            }, 100);
        });
    }

 

    // 初始化Luckysheet
    function initializeLuckysheet(exportJson) {
        if (!exportJson.sheets || exportJson.sheets.length === 0) {
            throw new Error("读取excel文件内容失败!");
        }
        
        window.luckysheet.destroy();
        window.luckysheet.create({
            container: 'luckysheet',
            lang: "zh",
            showtoolbarConfig:{
                image: true,
                print: true,
                exportXlsx: true,
            },
            allowCopy: true,
            showtoolbar: true,
            showinfobar: false,
            showsheetbar: true,
            showstatisticBar: true,
            sheetBottomConfig: true,
            allowEdit: true,
            enableAddRow: false,
            enableAddCol: false,
            userInfo: false,
            showRowBar: true,
            showColumnBar: false,
            sheetFormulaBar: false,
            enableAddBackTop: true,
            forceCalculation: false,
            data: exportJson.sheets,
            title: exportJson.info.name,
            userInfo: exportJson.info.name.creator,
        });
    }

    // 页面加载完成后开始异步加载
    document.addEventListener('DOMContentLoaded', function() {
        // 延迟一点时间开始加载，确保DOM完全加载
        setTimeout(() => {
            loadTextAsync();
        }, 100);
    });

    // 添加取消加载的功能（按ESC键）
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && isLoading) {
            // 可以在这里添加取消加载的逻辑
            console.log('用户取消了加载');
        }
    });

    // 打印时，获取luckysheet指定区域html内容
    function to_print() {
        const html = luckysheet.getRangeHtml();
        document.querySelector('#print-html').innerHTML = html;
        document.querySelector('#print-area').style.display = 'block';
        document.querySelector('#button-area').style.display = 'none';
    }
</script>
</body>
</html>