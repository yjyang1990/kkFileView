package cn.keking.config;

import cn.keking.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * 配置刷新组件 - 动态配置管理
 * 功能：监听配置文件变化，实现热更新配置
 */
@Component
public class ConfigRefreshComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigRefreshComponent.class);

    // 线程池和任务调度器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService watchServiceExecutor = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    // 防抖延迟时间（秒）
    private static final long DEBOUNCE_DELAY_SECONDS = 5;

    // 任务和状态管理
    private ScheduledFuture<?> scheduledReloadTask;
    private WatchService watchService;
    private volatile boolean running = true;

    /**
     * 初始化方法 - 启动配置监听
     */
    @PostConstruct
    void init() {
        loadConfig();
        watchServiceExecutor.submit(this::watchConfigFile);
    }

    /**
     * 销毁方法 - 清理资源
     */
    @PreDestroy
    void destroy() {
        running = false;
        watchServiceExecutor.shutdownNow();
        scheduler.shutdownNow();

        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                LOGGER.warn("关闭 WatchService 时发生异常", e);
            }
        }
    }

    /**
     * 监听配置文件变化
     */
    private void watchConfigFile() {
        try {
            String configFilePath = ConfigUtils.getCustomizedConfigPath();
            Path configPath = Paths.get(configFilePath);
            Path configDir = configPath.getParent();

            if (configDir == null) {
                LOGGER.error("配置文件路径无效，无法获取父目录: {}", configFilePath);
                return;
            }

            watchService = FileSystems.getDefault().newWatchService();
            configDir.register(watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE);

            LOGGER.info("开始监听配置文件变化，配置文件: {}", configFilePath);

            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path changedPath = (Path) event.context();

                        if (changedPath.equals(configPath.getFileName())) {
                            handleConfigChange(kind);
                        }
                    }

                    if (!key.reset()) {
                        LOGGER.warn("WatchKey 无法重置，监听可能已失效");
                        break;
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.info("配置文件监听线程被中断");
                    break;
                } catch (ClosedWatchServiceException e) {
                    LOGGER.info("WatchService 已关闭");
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("初始化配置文件监听失败", e);
        } finally {
            LOGGER.info("配置文件监听线程结束");
        }
    }

    /**
     * 处理配置文件变化事件
     */
    private void handleConfigChange(WatchEvent.Kind<?> kind) {
        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            LOGGER.warn("配置文件被删除，停止监听");
            running = false;
            return;
        }

        if (kind == StandardWatchEventKinds.ENTRY_MODIFY ||
                kind == StandardWatchEventKinds.ENTRY_CREATE) {

            synchronized (lock) {
                if (scheduledReloadTask != null && !scheduledReloadTask.isDone()) {
                    scheduledReloadTask.cancel(false);
                }

                scheduledReloadTask = scheduler.schedule(() -> {
                    try {
                        LOGGER.info("检测到配置文件变化，重新加载配置");
                        loadConfig();
                    } catch (Exception e) {
                        LOGGER.error("重新加载配置失败", e);
                    }
                }, DEBOUNCE_DELAY_SECONDS, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 加载配置文件
     */
    private void loadConfig() {
        synchronized (lock) {
            try {
                Properties properties = new Properties();
                String configFilePath = ConfigUtils.getCustomizedConfigPath();

                Path configPath = Paths.get(configFilePath);
                if (!Files.exists(configPath)) {
                    LOGGER.warn("配置文件不存在: {}", configFilePath);
                    return;
                }

                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configFilePath))) {
                    properties.load(bufferedReader);
                    ConfigUtils.restorePropertiesFromEnvFormat(properties);

                    updateConfigConstants(properties);
                    setWatermarkConfig(properties);

                    LOGGER.info("配置文件重新加载完成");
                }

            } catch (IOException e) {
                LOGGER.error("读取配置文件异常", e);
            }
        }
    }

    /**
     * 更新配置常量
     */
    private void updateConfigConstants(Properties properties) {
        // 1. 缓存配置
        boolean cacheEnabled = Boolean.parseBoolean(properties.getProperty("cache.enabled", ConfigConstants.DEFAULT_CACHE_ENABLED));

        // 2. 文件类型配置
        String text = properties.getProperty("simText", ConfigConstants.DEFAULT_TXT_TYPE);
        String media = properties.getProperty("media", ConfigConstants.DEFAULT_MEDIA_TYPE);
        String tifPreviewType = properties.getProperty("tif.preview.type", ConfigConstants.DEFAULT_TIF_PREVIEW_TYPE);
        String cadPreviewType = properties.getProperty("cad.preview.type", ConfigConstants.DEFAULT_CAD_PREVIEW_TYPE);
        String prohibit = properties.getProperty("prohibit", ConfigConstants.DEFAULT_PROHIBIT);

        String[] textArray = text.split(",");
        String[] mediaArray = media.split(",");
        String[] prohibitArray = prohibit.split(",");

        // 3. Office配置
        String officePreviewType = properties.getProperty("office.preview.type", ConfigConstants.DEFAULT_OFFICE_PREVIEW_TYPE);
        String officePreviewSwitchDisabled = properties.getProperty("office.preview.switch.disabled", ConfigConstants.DEFAULT_OFFICE_PREVIEW_SWITCH_DISABLED);
        String officeTypeWeb = properties.getProperty("office.type.web", ConfigConstants.DEFAULT_OFFICE_TYPE_WEB);
        String officPageRange = properties.getProperty("office.pagerange", ConfigConstants.DEFAULT_OFFICE_PAQERANQE);
        String officWatermark = properties.getProperty("office.watermark", ConfigConstants.DEFAULT_OFFICE_WATERMARK);
        String officQuality = properties.getProperty("office.quality", ConfigConstants.DEFAULT_OFFICE_QUALITY);
        String officMaxImageResolution = properties.getProperty("office.maximageresolution", ConfigConstants.DEFAULT_OFFICE_MAXIMAQERESOLUTION);
        boolean officExportBookmarks = Boolean.parseBoolean(properties.getProperty("office.exportbookmarks", ConfigConstants.DEFAULT_OFFICE_EXPORTBOOKMARKS));
        boolean officeExportNotes = Boolean.parseBoolean(properties.getProperty("office.exportnotes", ConfigConstants.DEFAULT_OFFICE_EXPORTNOTES));
        boolean officeDocumentOpenPasswords = Boolean.parseBoolean(properties.getProperty("office.documentopenpasswords", ConfigConstants.DEFAULT_OFFICE_EOCUMENTOPENPASSWORDS));

        // 4. FTP配置
        String ftpUsername = properties.getProperty("ftp.username", ConfigConstants.DEFAULT_FTP_USERNAME);

        // 5. 路径配置
        String baseUrl = properties.getProperty("base.url", ConfigConstants.DEFAULT_VALUE);

        // 6. 安全配置
        String trustHost = properties.getProperty("trust.host", ConfigConstants.DEFAULT_VALUE);
        String notTrustHost = properties.getProperty("not.trust.host", ConfigConstants.DEFAULT_VALUE);

        // 7. PDF配置
        String pdfPresentationModeDisable = properties.getProperty("pdf.presentationMode.disable", ConfigConstants.DEFAULT_PDF_PRESENTATION_MODE_DISABLE);
        String pdfOpenFileDisable = properties.getProperty("pdf.openFile.disable", ConfigConstants.DEFAULT_PDF_OPEN_FILE_DISABLE);
        String pdfPrintDisable = properties.getProperty("pdf.print.disable", ConfigConstants.DEFAULT_PDF_PRINT_DISABLE);
        String pdfDownloadDisable = properties.getProperty("pdf.download.disable", ConfigConstants.DEFAULT_PDF_DOWNLOAD_DISABLE);
        String pdfBookmarkDisable = properties.getProperty("pdf.bookmark.disable", ConfigConstants.DEFAULT_PDF_BOOKMARK_DISABLE);
        String pdfDisableEditing = properties.getProperty("pdf.disable.editing", ConfigConstants.DEFAULT_PDF_DISABLE_EDITING);
        int pdf2JpgDpi = Integer.parseInt(properties.getProperty("pdf2jpg.dpi", ConfigConstants.DEFAULT_PDF2_JPG_DPI));

        // 8. CAD配置
        String cadTimeout = properties.getProperty("cad.timeout", ConfigConstants.DEFAULT_CAD_TIMEOUT);
        int cadThread = Integer.parseInt(properties.getProperty("cad.thread", ConfigConstants.DEFAULT_CAD_THREAD));

        // 9. 文件操作配置
        boolean fileUploadDisable = Boolean.parseBoolean(properties.getProperty("file.upload.disable", ConfigConstants.DEFAULT_FILE_UPLOAD_DISABLE));
        String size = properties.getProperty("spring.servlet.multipart.max-file-size", ConfigConstants.DEFAULT_SIZE);
        String password = properties.getProperty("delete.password", ConfigConstants.DEFAULT_PASSWORD);
        boolean deleteSourceFile = Boolean.parseBoolean(properties.getProperty("delete.source.file", ConfigConstants.DEFAULT_DELETE_SOURCE_FILE));
        boolean deleteCaptcha = Boolean.parseBoolean(properties.getProperty("delete.captcha", ConfigConstants.DEFAULT_DELETE_CAPTCHA));

        // 10. TIF配置
        String tifTimeout = properties.getProperty("tif.timeout", ConfigConstants.DEFAULT_TIF_TIMEOUT);
        int tifThread = Integer.parseInt(properties.getProperty("tif.thread", ConfigConstants.DEFAULT_TIF_THREAD));

        // 11. 首页配置
        String beian = properties.getProperty("beian", ConfigConstants.DEFAULT_BEIAN);
        String homePageNumber = properties.getProperty("home.pagenumber", ConfigConstants.DEFAULT_HOME_PAGENUMBER);
        String homePagination = properties.getProperty("home.pagination", ConfigConstants.DEFAULT_HOME_PAGINATION);
        String homePageSize = properties.getProperty("home.pagesize", ConfigConstants.DEFAULT_HOME_PAGSIZE);
        String homeSearch = properties.getProperty("home.search", ConfigConstants.DEFAULT_HOME_SEARCH);

        // 12. 权限配置
        String key = properties.getProperty("kk.key=", ConfigConstants.DEFAULT_KEY);
        boolean picturesPreview = Boolean.parseBoolean(properties.getProperty("kk.Picturespreview", ConfigConstants.DEFAULT_PICTURES_PREVIEW));
        boolean getCorsFile = Boolean.parseBoolean(properties.getProperty("kk.Getcorsfile", ConfigConstants.DEFAULT_GET_CORS_FILE));
        boolean addTask = Boolean.parseBoolean(properties.getProperty("kk.addTask", ConfigConstants.DEFAULT_ADD_TASK));
        String aesKey = properties.getProperty("aes.key", ConfigConstants.DEFAULT_AES_KEY);

        // 13. UserAgent配置
        String userAgent = properties.getProperty("useragent", ConfigConstants.DEFAULT_USER_AGENT);

        // 14. Basic认证配置
        String basicName = properties.getProperty("basic.name", ConfigConstants.DEFAULT_BASIC_NAME);

        // 15. 视频转换配置
        int mediaConvertMaxSize = Integer.parseInt(properties.getProperty("media.convert.max.size", ConfigConstants.DEFAULT_MEDIA_CONVERT_MAX_SIZE));
        boolean mediaTimeoutEnabled = Boolean.parseBoolean(properties.getProperty("media.timeout.enabled", ConfigConstants.DEFAULT_MEDIA_TIMEOUT_ENABLED));
        int mediaSmallFileTimeout = Integer.parseInt(properties.getProperty("media.small.file.timeout", ConfigConstants.DEFAULT_MEDIA_SMALL_FILE_TIMEOUT));
        int mediaMediumFileTimeout = Integer.parseInt(properties.getProperty("media.medium.file.timeout", ConfigConstants.DEFAULT_MEDIA_MEDIUM_FILE_TIMEOUT));
        int mediaLargeFileTimeout = Integer.parseInt(properties.getProperty("media.large.file.timeout", ConfigConstants.DEFAULT_MEDIA_LARGE_FILE_TIMEOUT));
        int mediaXLFileTimeout = Integer.parseInt(properties.getProperty("media.xl.file.timeout", ConfigConstants.DEFAULT_MEDIA_XL_FILE_TIMEOUT));
        int mediaXXLFileTimeout = Integer.parseInt(properties.getProperty("media.xxl.file.timeout", ConfigConstants.DEFAULT_MEDIA_XXL_FILE_TIMEOUT));
        int mediaXXXLFileTimeout = Integer.parseInt(properties.getProperty("media.xxxl.file.timeout", ConfigConstants.DEFAULT_MEDIA_XXXL_FILE_TIMEOUT));

        // 16. PDF DPI配置
        boolean pdfDpiEnabled = Boolean.parseBoolean(properties.getProperty("pdf.dpi.enabled", ConfigConstants.DEFAULT_PDF_DPI_ENABLED).trim());
        int pdfSmallDpi = Integer.parseInt(properties.getProperty("pdf.dpi.small", ConfigConstants.DEFAULT_PDF_SMALL_DTI).trim());
        int pdfMediumDpi = Integer.parseInt(properties.getProperty("pdf.dpi.medium", ConfigConstants.DEFAULT_PDF_MEDIUM_DPI).trim());
        int pdfLargeDpi = Integer.parseInt(properties.getProperty("pdf.dpi.large", ConfigConstants.DEFAULT_PDF_LARGE_DPI).trim());
        int pdfXLargeDpi = Integer.parseInt(properties.getProperty("pdf.dpi.xlarge", ConfigConstants.DEFAULT_PDF_XLARGE_DPI).trim());
        int pdfXXLargeDpi = Integer.parseInt(properties.getProperty("pdf.dpi.xxlarge", ConfigConstants.DEFAULT_PDF_XXLARGE_DPI).trim());

        // 17. PDF超时配置（新）
        int pdfTimeoutSmall = Integer.parseInt(properties.getProperty("pdf.timeout.small", ConfigConstants.DEFAULT_PDF_TIMEOUT_SMALL).trim());
        int pdfTimeoutMedium = Integer.parseInt(properties.getProperty("pdf.timeout.medium", ConfigConstants.DEFAULT_PDF_TIMEOUT_MEDIUM).trim());
        int pdfTimeoutLarge = Integer.parseInt(properties.getProperty("pdf.timeout.large", ConfigConstants.DEFAULT_PDF_TIMEOUT_LARGE).trim());
        int pdfTimeoutXLarge = Integer.parseInt(properties.getProperty("pdf.timeout.xlarge", ConfigConstants.DEFAULT_PDF_TIMEOUT_XLARGE).trim());

        // 18. PDF线程配置
        int pdfMaxThreads = Integer.parseInt(properties.getProperty("pdf.max.threads", ConfigConstants.DEFAULT_PDF_MAX_THREADS).trim());

        // 19. CAD水印配置
        boolean cadwatermark = Boolean.parseBoolean(properties.getProperty("cad.watermark", ConfigConstants.DEFAULT_CAD_WATERMARK));

        // 20. SSL忽略配置
        boolean ignoreSSL = Boolean.parseBoolean(properties.getProperty("kk.ignore.ssl", ConfigConstants.DEFAULT_IGNORE_SSL));

        // 21. 重定向启用配置
        boolean enableRedirect = Boolean.parseBoolean(properties.getProperty("kk.enable.redirect", ConfigConstants.DEFAULT_ENABLE_REDIRECT));

        // 22. 重定向启用配置
        int refreshSchedule = Integer.parseInt(properties.getProperty("kk.refreshSchedule ", ConfigConstants.DEFAULT_ENABLE_REFRECSHSCHEDULE).trim());

        // 23. 其他配置
        boolean isShowaesKey = Boolean.parseBoolean(properties.getProperty("kk.isshowaeskey", ConfigConstants.DEFAULT_SHOW_AES_KEY));
        boolean isJavaScript = Boolean.parseBoolean(properties.getProperty("kk.isjavascript", ConfigConstants.DEFAULT_IS_JAVASCRIPT));
        boolean xlsxAllowEdit = Boolean.parseBoolean(properties.getProperty("kk.xlsxallowedit", ConfigConstants.DEFAULT_XLSX_ALLOW_EDIT));
        boolean xlsxShowtoolbar = Boolean.parseBoolean(properties.getProperty("kk.xlsxshowtoolbar", ConfigConstants.DEFAULT_XLSX_SHOW_TOOLBAR));
        boolean isShowKey = Boolean.parseBoolean(properties.getProperty("kk.isshowkey", ConfigConstants.DEFAULT_IS_SHOW_KEY));
        boolean scriptJs  = Boolean.parseBoolean(properties.getProperty("kk.scriptjs", ConfigConstants.DEFAULT_SCRIPT_JS));

        // 设置配置值
        // 1. 缓存配置
        ConfigConstants.setCacheEnabledValueValue(cacheEnabled);

        // 2. 文件类型配置
        ConfigConstants.setSimTextValue(textArray);
        ConfigConstants.setMediaValue(mediaArray);
        ConfigConstants.setTifPreviewTypeValue(tifPreviewType);
        ConfigConstants.setCadPreviewTypeValue(cadPreviewType);
        ConfigConstants.setProhibitValue(prohibitArray);

        // 3. Office配置
        ConfigConstants.setOfficePreviewTypeValue(officePreviewType);
        ConfigConstants.setOfficePreviewSwitchDisabledValue(officePreviewSwitchDisabled);
        ConfigConstants.setOfficeTypeWebValue(officeTypeWeb);
        ConfigConstants.setOfficePageRangeValue(officPageRange);
        ConfigConstants.setOfficeWatermarkValue(officWatermark);
        ConfigConstants.setOfficeQualityValue(officQuality);
        ConfigConstants.setOfficeMaxImageResolutionValue(officMaxImageResolution);
        ConfigConstants.setOfficeExportBookmarksValue(officExportBookmarks);
        ConfigConstants.setOfficeExportNotesValue(officeExportNotes);
        ConfigConstants.setOfficeDocumentOpenPasswordsValue(officeDocumentOpenPasswords);

        // 4. FTP配置
        ConfigConstants.setFtpUsernameValue(ftpUsername);

        // 5. 路径配置
        ConfigConstants.setBaseUrlValue(baseUrl);

        // 6. 安全配置
        ConfigConstants.setTrustHostValue(trustHost);
        ConfigConstants.setNotTrustHostValue(notTrustHost);

        // 7. PDF配置
        ConfigConstants.setPdfPresentationModeDisableValue(pdfPresentationModeDisable);
        ConfigConstants.setPdfOpenFileDisableValue(pdfOpenFileDisable);
        ConfigConstants.setPdfPrintDisableValue(pdfPrintDisable);
        ConfigConstants.setPdfDownloadDisableValue(pdfDownloadDisable);
        ConfigConstants.setPdfBookmarkDisableValue(pdfBookmarkDisable);
        ConfigConstants.setPdfDisableEditingValue(pdfDisableEditing);
        ConfigConstants.setPdf2JpgDpiValue(pdf2JpgDpi);

        // 8. CAD配置
        ConfigConstants.setCadTimeoutValue(cadTimeout);
        ConfigConstants.setCadThreadValue(cadThread);

        // 9. 文件操作配置
        ConfigConstants.setFileUploadDisableValue(fileUploadDisable);
        ConfigConstants.setSizeValue(size);
        ConfigConstants.setPasswordValue(password);
        ConfigConstants.setDeleteSourceFileValue(deleteSourceFile);
        ConfigConstants.setDeleteCaptchaValue(deleteCaptcha);

        // 10. TIF配置
        ConfigConstants.setTifTimeoutValue(tifTimeout);
        ConfigConstants.setTifThreadValue(tifThread);

        // 11. 首页配置
        ConfigConstants.setBeianValue(beian);
        ConfigConstants.setHomePageNumberValue(homePageNumber);
        ConfigConstants.setHomePaginationValue(homePagination);
        ConfigConstants.setHomePageSizeValue(homePageSize);
        ConfigConstants.setHomeSearchValue(homeSearch);

        // 12. 权限配置
        ConfigConstants.setKeyValue(key);
        ConfigConstants.setPicturesPreviewValue(picturesPreview);
        ConfigConstants.setGetCorsFileValue(getCorsFile);
        ConfigConstants.setAddTaskValue(addTask);
        ConfigConstants.setaesKeyValue(aesKey);

        // 13. UserAgent配置
        ConfigConstants.setUserAgentValue(userAgent);

        // 14. Basic认证配置
        ConfigConstants.setBasicNameValue(basicName);

        // 15. 视频转换配置
        ConfigConstants.setMediaConvertMaxSizeValue(mediaConvertMaxSize);
        ConfigConstants.setMediaTimeoutEnabledValue(mediaTimeoutEnabled);
        ConfigConstants.setMediaSmallFileTimeoutValue(mediaSmallFileTimeout);
        ConfigConstants.setMediaMediumFileTimeoutValue(mediaMediumFileTimeout);
        ConfigConstants.setMediaLargeFileTimeoutValue(mediaLargeFileTimeout);
        ConfigConstants.setMediaXLFileTimeoutValue(mediaXLFileTimeout);
        ConfigConstants.setMediaXXLFileTimeoutValue(mediaXXLFileTimeout);
        ConfigConstants.setMediaXXXLFileTimeoutValue(mediaXXXLFileTimeout);
        // 16. PDF DPI配置
        ConfigConstants.setPdfDpiEnabledValue(pdfDpiEnabled);
        ConfigConstants.setPdfSmallDpiValue(pdfSmallDpi);
        ConfigConstants.setPdfMediumDpiValue(pdfMediumDpi);
        ConfigConstants.setPdfLargeDpiValue(pdfLargeDpi);
        ConfigConstants.setPdfXLargeDpiValue(pdfXLargeDpi);
        ConfigConstants.setPdfXXLargeDpiValue(pdfXXLargeDpi);

        // 17. PDF超时配置（新）
        ConfigConstants.setPdfTimeoutSmallValue(pdfTimeoutSmall);
        ConfigConstants.setPdfTimeoutMediumValue(pdfTimeoutMedium);
        ConfigConstants.setPdfTimeoutLargeValue(pdfTimeoutLarge);
        ConfigConstants.setPdfTimeoutXLargeValue(pdfTimeoutXLarge);

        // 18. PDF线程配置
        ConfigConstants.setPdfMaxThreadsValue(pdfMaxThreads);

        // 19. CAD水印配置
        ConfigConstants.setCadwatermarkValue(cadwatermark);

        // 20. SSL忽略配置
        ConfigConstants.setIgnoreSSLValue(ignoreSSL);

        // 21. 重定向启用配置
        ConfigConstants.setEnableRedirectValue(enableRedirect);

        // 22. 重定向启用配置
        ConfigConstants.setRefreshScheduleValue(refreshSchedule);

        // 23. 其他配置
        ConfigConstants.setIsShowaesKeyValue(isShowaesKey);
        ConfigConstants.setIsJavaScriptValue(isJavaScript);
        ConfigConstants.setXlsxAllowEditValue(xlsxAllowEdit);
        ConfigConstants.setXlsxShowtoolbarValue(xlsxShowtoolbar);
        ConfigConstants.setisShowKeyValue(isShowKey);
        ConfigConstants.setscriptJsValue(scriptJs);
    }

    /**
     * 设置水印配置
     */
    private void setWatermarkConfig(Properties properties) {
        String watermarkTxt = properties.getProperty("watermark.txt", WatermarkConfigConstants.DEFAULT_WATERMARK_TXT);
        String watermarkXSpace = properties.getProperty("watermark.x.space", WatermarkConfigConstants.DEFAULT_WATERMARK_X_SPACE);
        String watermarkYSpace = properties.getProperty("watermark.y.space", WatermarkConfigConstants.DEFAULT_WATERMARK_Y_SPACE);
        String watermarkFont = properties.getProperty("watermark.font", WatermarkConfigConstants.DEFAULT_WATERMARK_FONT);
        String watermarkFontsize = properties.getProperty("watermark.fontsize", WatermarkConfigConstants.DEFAULT_WATERMARK_FONTSIZE);
        String watermarkColor = properties.getProperty("watermark.color", WatermarkConfigConstants.DEFAULT_WATERMARK_COLOR);
        String watermarkAlpha = properties.getProperty("watermark.alpha", WatermarkConfigConstants.DEFAULT_WATERMARK_ALPHA);
        String watermarkWidth = properties.getProperty("watermark.width", WatermarkConfigConstants.DEFAULT_WATERMARK_WIDTH);
        String watermarkHeight = properties.getProperty("watermark.height", WatermarkConfigConstants.DEFAULT_WATERMARK_HEIGHT);
        String watermarkAngle = properties.getProperty("watermark.angle", WatermarkConfigConstants.DEFAULT_WATERMARK_ANGLE);

        WatermarkConfigConstants.setWatermarkTxtValue(watermarkTxt);
        WatermarkConfigConstants.setWatermarkXSpaceValue(watermarkXSpace);
        WatermarkConfigConstants.setWatermarkYSpaceValue(watermarkYSpace);
        WatermarkConfigConstants.setWatermarkFontValue(watermarkFont);
        WatermarkConfigConstants.setWatermarkFontsizeValue(watermarkFontsize);
        WatermarkConfigConstants.setWatermarkColorValue(watermarkColor);
        WatermarkConfigConstants.setWatermarkAlphaValue(watermarkAlpha);
        WatermarkConfigConstants.setWatermarkWidthValue(watermarkWidth);
        WatermarkConfigConstants.setWatermarkHeightValue(watermarkHeight);
        WatermarkConfigConstants.setWatermarkAngleValue(watermarkAngle);
    }
}