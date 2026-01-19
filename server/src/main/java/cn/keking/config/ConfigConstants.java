package cn.keking.config;

import cn.keking.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: chenjh
 * @since: 2019/4/10 17:22
 */
@Component(value = ConfigConstants.BEAN_NAME)
public class ConfigConstants {
    public static final String BEAN_NAME = "configConstants";


    static {
        // PDFBox兼容低版本JDK
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    // ==================================================
// 常量定义区（按功能模块分组）
// ==================================================

    // 1. 缓存配置常量
    public static final String DEFAULT_CACHE_ENABLED = "true";

    // 2. 文件类型配置常量
    public static final String DEFAULT_TXT_TYPE = "txt,html,htm,asp,jsp,xml,json,properties,md,gitignore,log,java,py,c,cpp,sql,sh,bat,m,bas,prg,cmd,xbrl";
    public static final String DEFAULT_MEDIA_TYPE = "mp3,wav,mp4,flv";
    public static final String DEFAULT_PROHIBIT = "exe,dll";
    public static final String DEFAULT_TIF_PREVIEW_TYPE = "tif";
    public static final String DEFAULT_CAD_PREVIEW_TYPE = "pdf";

    // 3. Office配置常量
    public static final String DEFAULT_OFFICE_PREVIEW_TYPE = "image";
    public static final String DEFAULT_OFFICE_PREVIEW_SWITCH_DISABLED = "false";
    public static final String DEFAULT_OFFICE_TYPE_WEB = "web";
    public static final String DEFAULT_OFFICE_PAQERANQE = "false";  // 注意：拼写错误，应为PAGERANGE
    public static final String DEFAULT_OFFICE_WATERMARK = "false";
    public static final String DEFAULT_OFFICE_QUALITY = "80";
    public static final String DEFAULT_OFFICE_MAXIMAQERESOLUTION = "150";  // 注意：拼写错误，应为MAXIMAGERESOLUTION
    public static final String DEFAULT_OFFICE_EXPORTBOOKMARKS = "true";
    public static final String DEFAULT_OFFICE_EXPORTNOTES = "true";
    public static final String DEFAULT_OFFICE_EOCUMENTOPENPASSWORDS = "true";  // 注意：拼写错误，应为DOCUMENTOPENPASSWORDS

    // 4. FTP配置常量
    public static final String DEFAULT_FTP_USERNAME = null;

    // 5. 路径配置常量
    public static final String DEFAULT_VALUE = "default";

    // 6. PDF配置常量
    public static final String DEFAULT_PDF_PRESENTATION_MODE_DISABLE = "true";
    public static final String DEFAULT_PDF_OPEN_FILE_DISABLE = "true";
    public static final String DEFAULT_PDF_PRINT_DISABLE = "true";
    public static final String DEFAULT_PDF_DOWNLOAD_DISABLE = "true";
    public static final String DEFAULT_PDF_BOOKMARK_DISABLE = "true";
    public static final String DEFAULT_PDF_DISABLE_EDITING = "true";
    public static final String DEFAULT_PDF2_JPG_DPI = "105";

    // 7. CAD配置常量
    public static final String DEFAULT_CAD_TIMEOUT = "90";
    public static final String DEFAULT_CAD_THREAD = "5";

    // 8. TIF配置常量
    public static final String DEFAULT_TIF_TIMEOUT = "90";
    public static final String DEFAULT_TIF_THREAD = "5";

    // 9. 文件操作配置常量
    public static final String DEFAULT_FILE_UPLOAD_DISABLE = "false";
    public static final String DEFAULT_DELETE_SOURCE_FILE = "true";
    public static final String DEFAULT_DELETE_CAPTCHA = "false";
    public static final String DEFAULT_SIZE = "500MB";
    public static final String DEFAULT_PASSWORD = "123456";

    // 10. 首页配置常量
    public static final String DEFAULT_BEIAN = "无";
    public static final String DEFAULT_HOME_PAGENUMBER = "1";
    public static final String DEFAULT_HOME_PAGINATION = "true";
    public static final String DEFAULT_HOME_PAGSIZE = "15";
    public static final String DEFAULT_HOME_SEARCH = "true";

    // 11. 权限配置常量
    public static final String DEFAULT_KEY = "false";
    public static final String DEFAULT_PICTURES_PREVIEW = "true";
    public static final String DEFAULT_GET_CORS_FILE = "true";
    public static final String DEFAULT_ADD_TASK = "true";
    public static final String DEFAULT_AES_KEY = "false";

    // 12. UserAgent配置常量
    public static final String DEFAULT_USER_AGENT = "false";

    // 13. Basic认证配置常量
    public static final String DEFAULT_BASIC_NAME = "";

    // 14. 视频转换配置常量
    public static final String DEFAULT_MEDIA_CONVERT_MAX_SIZE = "300";
    public static final String DEFAULT_MEDIA_TIMEOUT_ENABLED = "true";
    public static final String DEFAULT_MEDIA_SMALL_FILE_TIMEOUT = "30";
    public static final String DEFAULT_MEDIA_MEDIUM_FILE_TIMEOUT = "60";
    public static final String DEFAULT_MEDIA_LARGE_FILE_TIMEOUT = "180";
    public static final String DEFAULT_MEDIA_XL_FILE_TIMEOUT = "300";
    public static final String DEFAULT_MEDIA_XXL_FILE_TIMEOUT = "600";
    public static final String DEFAULT_MEDIA_XXXL_FILE_TIMEOUT = "1200";

    // 15. PDF DPI配置常量
    public static final String DEFAULT_PDF_SMALL_DTI = "150";     // 注意：拼写错误，应为DPI
    public static final String DEFAULT_PDF_MEDIUM_DPI = "120";
    public static final String DEFAULT_PDF_LARGE_DPI = "96";
    public static final String DEFAULT_PDF_XLARGE_DPI = "72";
    public static final String DEFAULT_PDF_XXLARGE_DPI = "72";
    public static final String DEFAULT_PDF_DPI_ENABLED = "true";

    // 16. PDF超时配置常量（新标准化配置）
    public static final String DEFAULT_PDF_TIMEOUT_SMALL = "90";
    public static final String DEFAULT_PDF_TIMEOUT_MEDIUM = "180";
    public static final String DEFAULT_PDF_TIMEOUT_LARGE = "300";
    public static final String DEFAULT_PDF_TIMEOUT_XLARGE = "600";

    // 17. PDF线程配置常量
    public static final String DEFAULT_PDF_MAX_THREADS = "10";

    // 18. CAD水印配置常量
    public static final String DEFAULT_CAD_WATERMARK = "false";

    // 19. SSL忽略配置常量
    public static final String DEFAULT_IGNORE_SSL = "true";

    // 20. 重定向启用配置常量
    public static final String DEFAULT_ENABLE_REDIRECT = "true";

    // 22. 异步定时
    public static final String DEFAULT_ENABLE_REFRECSHSCHEDULE = "5";

    // 23. 其他配置常量
    public static final String DEFAULT_SHOW_AES_KEY = "1234567890123456";
    public static final String DEFAULT_IS_JAVASCRIPT = "false";
    public static final String DEFAULT_XLSX_ALLOW_EDIT = "false";
    public static final String DEFAULT_XLSX_SHOW_TOOLBAR = "false";
    public static final String DEFAULT_IS_SHOW_KEY= "false";
    public static final String DEFAULT_SCRIPT_JS ="false" ;

    // ==================================================
// 配置变量定义区（按功能分类，均为静态变量）
// ==================================================

    // 1. 缓存配置
    private static Boolean cacheEnabled;

    // 2. 文件类型配置
    private static String[] simTexts = {};
    private static String[] medias = {};
    private static String[] convertMedias = {};
    private static String[] prohibit = {};
    private static String mediaConvertDisable;
    private static String tifPreviewType;
    private static String cadPreviewType;

    // 3. Office配置
    private static String officePreviewType;
    private static String officePreviewSwitchDisabled;
    private static String officeTypeWeb;
    private static String officePageRange;
    private static String officeWatermark;
    private static String officeQuality;
    private static String officeMaxImageResolution;
    private static Boolean officeExportBookmarks;
    private static Boolean officeExportNotes;
    private static Boolean officeDocumentOpenPasswords;

    // 4. FTP配置
    private static String ftpUsername;

    // 5. 路径配置
    private static String fileDir = ConfigUtils.getHomePath() + File.separator + "file" + File.separator;
    private static String localPreviewDir;
    private static String baseUrl;

    // 6. 安全配置
    private static CopyOnWriteArraySet<String> trustHostSet;
    private static CopyOnWriteArraySet<String> notTrustHostSet;

    // 7. PDF配置
    private static String pdfPresentationModeDisable;
    private static String pdfDisableEditing;
    private static String pdfOpenFileDisable;
    private static String pdfPrintDisable;
    private static String pdfDownloadDisable;
    private static String pdfBookmarkDisable;
    private static int pdf2JpgDpi;

    // 8. CAD配置
    private static String cadTimeout;
    private static int cadThread;

    // 9. TIF配置
    private static String tifTimeout;
    private static int tifThread;

    // 10. 文件操作配置
    private static Boolean fileUploadDisable;
    private static String size;
    private static String password;
    private static Boolean deleteSourceFile;
    private static Boolean deleteCaptcha;

    // 11. 首页配置
    private static String beian;
    private static String homePageNumber;
    private static String homePagination;
    private static String homePageSize;
    private static String homeSearch;

    // 12. 权限配置
    private static String key;
    private static boolean picturesPreview;
    private static boolean getCorsFile;
    private static boolean addTask;
    private static String aesKey;

    // 13. UserAgent配置
    private static String userAgent;

    // 14. Basic认证配置
    private static String basicName;

    // 15. 视频转换配置
    private static int mediaConvertMaxSize;
    private static boolean mediaTimeoutEnabled;
    private static int mediaSmallFileTimeout;
    private static int mediaMediumFileTimeout;
    private static int mediaLargeFileTimeout;
    private static int mediaXLFileTimeout;
    private static int mediaXXLFileTimeout;
    private static int mediaXXXLFileTimeout;

    // 16. PDF DPI配置
    private static boolean pdfDpiEnabled;
    private static int pdfSmallDpi;
    private static int pdfMediumDpi;
    private static int pdfLargeDpi;
    private static int pdfXLargeDpi;
    private static int pdfXXLargeDpi;

    // 17. PDF超时配置（新）
    private static int pdfTimeoutSmall;
    private static int pdfTimeoutMedium;
    private static int pdfTimeoutLarge;
    private static int pdfTimeoutXLarge;

    // 18. PDF线程配置
    private static int pdfMaxThreads;

    // 19. CAD水印配置
    private static Boolean cadwatermark;

    // 20. SSL忽略配置
    private static Boolean ignoreSSL;

    // 21. 重定向启用配置
    private static Boolean enableRedirect;

    // 22. 异步定时
    private static int refreshSchedule;

    // 23. 其他配置变量
    private static boolean isShowaesKey;
    private static boolean isJavaScript;
    private static boolean xlsxAllowEdit;
    private static boolean xlsxShowtoolbar;
    private static boolean isShowKey;
    private static boolean scriptJs;



    // ==================================================
// 获取方法（按功能分类）
// ==================================================

    /**
     * PDF超时配置获取方法（新）
     */
    public static int getPdfTimeoutSmall() {
        return pdfTimeoutSmall;
    }

    public static int getPdfTimeoutMedium() {
        return pdfTimeoutMedium;
    }

    public static int getPdfTimeoutLarge() {
        return pdfTimeoutLarge;
    }

    public static int getPdfTimeoutXLarge() {
        return pdfTimeoutXLarge;
    }

    public static int getPdfMaxThreads() {
        return pdfMaxThreads;
    }

    /**
     * 根据页数获取优化的DPI值
     * 智能DPI调整策略：
     * - 0-50页: 150 DPI
     * - 51-100页: 120 DPI
     * - 101-200页: 96 DPI
     * - 201-500页: 72 DPI
     * - >500页: 72 DPI
     */
    public static int getOptimizedDpi(int pageCount) {
        if (!pdfDpiEnabled) {
            return ConfigConstants.getPdf2JpgDpi();
        }

        if (pageCount > 500) {
            return pdfXXLargeDpi;
        } else if (pageCount > 200) {
            return pdfXLargeDpi;
        } else if (pageCount > 100) {
            return pdfLargeDpi;
        } else if (pageCount > 50) {
            return pdfMediumDpi;
        } else {
            return pdfSmallDpi;
        }
    }

    // 1. 缓存配置获取方法
    public static Boolean isCacheEnabled() {
        return cacheEnabled;
    }

    // 2. 文件类型配置获取方法
    public static String[] getSimText() {
        return simTexts;
    }

    public static String[] getMedia() {
        return medias;
    }

    public static String[] getConvertMedias() {
        return convertMedias;
    }

    public static String getMediaConvertDisable() {
        return mediaConvertDisable;
    }

    public static String getTifPreviewType() {
        return tifPreviewType;
    }

    public static String[] getProhibit() {
        return prohibit;
    }

    // 3. Office配置获取方法
    public static String getOfficePreviewType() {
        return officePreviewType;
    }

    public static String getOfficePreviewSwitchDisabled() {
        return officePreviewSwitchDisabled;
    }

    public static String getOfficeTypeWeb() {
        return officeTypeWeb;
    }

    public static String getOfficePageRange() {
        return officePageRange;
    }

    public static String getOfficeWatermark() {
        return officeWatermark;
    }

    public static String getOfficeQuality() {
        return officeQuality;
    }

    public static String getOfficeMaxImageResolution() {
        return officeMaxImageResolution;
    }

    public static Boolean getOfficeExportBookmarks() {
        return officeExportBookmarks;
    }

    public static Boolean getOfficeExportNotes() {
        return officeExportNotes;
    }

    public static Boolean getOfficeDocumentOpenPasswords() {
        return officeDocumentOpenPasswords;
    }

    // 4. FTP配置获取方法
    public static String getFtpUsername() {
        return ftpUsername;
    }

    // 5. 路径配置获取方法
    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getFileDir() {
        return fileDir;
    }

    public static String getLocalPreviewDir() {
        return localPreviewDir;
    }

    // 6. 安全配置获取方法
    public static Set<String> getTrustHostSet() {
        return trustHostSet;
    }

    public static Set<String> getNotTrustHostSet() {
        return notTrustHostSet;
    }

    // 7. PDF配置获取方法
    public static String getPdfPresentationModeDisable() {
        return pdfPresentationModeDisable;
    }

    public static String getPdfOpenFileDisable() {
        return pdfOpenFileDisable;
    }

    public static String getPdfPrintDisable() {
        return pdfPrintDisable;
    }

    public static String getPdfDownloadDisable() {
        return pdfDownloadDisable;
    }

    public static String getPdfBookmarkDisable() {
        return pdfBookmarkDisable;
    }

    public static String getPdfDisableEditing() {
        return pdfDisableEditing;
    }

    public static int getPdf2JpgDpi() {
        return pdf2JpgDpi;
    }

    // 8. CAD配置获取方法
    public static String getCadPreviewType() {
        return cadPreviewType;
    }

    public static String getCadTimeout() {
        return cadTimeout;
    }

    public static int getCadThread() {
        return cadThread;
    }

    // 9. TIF配置获取方法
    public static String getTifTimeout() {
        return tifTimeout;
    }

    public static int getTifThread() {
        return tifThread;
    }

    // 10. 文件操作配置获取方法
    public static Boolean getFileUploadDisable() {
        return fileUploadDisable;
    }

    public static String maxSize() {
        return size;
    }

    public static String getPassword() {
        return password;
    }

    public static Boolean getDeleteSourceFile() {
        return deleteSourceFile;
    }

    public static Boolean getDeleteCaptcha() {
        return deleteCaptcha;
    }

    // 11. 首页配置获取方法
    public static String getBeian() {
        return beian;
    }

    public static String getHomePageNumber() {
        return homePageNumber;
    }

    public static String getHomePagination() {
        return homePagination;
    }

    public static String getHomePageSize() {
        return homePageSize;
    }

    public static String getHomeSearch() {
        return homeSearch;
    }

    // 12. 权限配置获取方法
    public static String getKey() {
        return key;
    }

    public static boolean getPicturesPreview() {
        return picturesPreview;
    }

    public static boolean getGetCorsFile() {
        return getCorsFile;
    }

    public static boolean getAddTask() {
        return addTask;
    }

    public static String getaesKey() {
        return aesKey;
    }

    // 13. UserAgent配置获取方法
    public static String getUserAgent() {
        return userAgent;
    }

    // 14. Basic认证配置获取方法
    public static String getBasicName() {
        return basicName;
    }

    // 15. 视频转换配置获取方法
    public static int getMediaConvertMaxSize() {
        return mediaConvertMaxSize;
    }

    public static boolean isMediaTimeoutEnabled() {
        return mediaTimeoutEnabled;
    }

    public static int getMediaSmallFileTimeout() {
        return mediaSmallFileTimeout;
    }

    public static int getMediaMediumFileTimeout() {
        return mediaMediumFileTimeout;
    }

    public static int getMediaLargeFileTimeout() {
        return mediaLargeFileTimeout;
    }

    public static int getMediaXLFileTimeout() {
        return mediaXLFileTimeout;
    }

    public static int getMediaXXLFileTimeout() {
        return mediaXXLFileTimeout;
    }

    public static int getMediaXXXLFileTimeout() {
        return mediaXXXLFileTimeout;
    }

    // 19. CAD水印配置获取方法
    public static boolean getCadwatermark() {
        return cadwatermark;
    }

    // 20. SSL忽略配置获取方法
    public static boolean isIgnoreSSL() {
        return ignoreSSL;
    }

    // 21. 重定向启用配置获取方法
    public static boolean isEnableRedirect() {
        return enableRedirect;
    }

    // 22. 异步定时刷新时间
    public static int getTime() {
        return 0;
    }

    // 23. 其他配置获取方法
    public static boolean getisShowaesKey() {
        return isShowaesKey;
    }

    public static boolean getisJavaScript() {
        return isJavaScript;
    }

    public static boolean getxlsxAllowEdit() {
        return xlsxAllowEdit;
    }

    public static boolean getxlsxShowtoolbar() {
        return xlsxShowtoolbar;
    }

    public static boolean getisShowKey() {
        return isShowKey;
    }

    public static boolean getscriptJs() {
        return scriptJs;
    }

// ==================================================
// Setter方法（按功能分类）
// ==================================================

    // 1. 缓存配置Setter方法
    @Value("${cache.enabled:true}")
    public void setCacheEnabled(String cacheEnabled) {
        setCacheEnabledValueValue(Boolean.parseBoolean(cacheEnabled));
    }

    public static void setCacheEnabledValueValue(Boolean cacheEnabled) {
        ConfigConstants.cacheEnabled = cacheEnabled;
    }

    // 2. 文件类型配置Setter方法
    @Value("${simText:txt,html,htm,asp,jsp,xml,json,properties,md,gitignore,log,java,py,c,cpp,sql,sh,bat,m,bas,prg,cmd,xbrl}")
    public void setSimText(String simText) {
        String[] simTextArr = simText.split(",");
        setSimTextValue(simTextArr);
    }

    public static void setSimTextValue(String[] simText) {
        ConfigConstants.simTexts = simText;
    }

    @Value("${media:mp3,wav,mp4,flv}")
    public void setMedia(String media) {
        String[] mediaArr = media.split(",");
        setMediaValue(mediaArr);
    }

    public static void setMediaValue(String[] Media) {
        ConfigConstants.medias = Media;
    }

    @Value("${convertMedias:avi,mov,wmv,mkv,3gp,rm}")
    public void setConvertMedias(String convertMedia) {
        String[] mediaArr = convertMedia.split(",");
        setConvertMediaValue(mediaArr);
    }

    public static void setConvertMediaValue(String[] ConvertMedia) {
        ConfigConstants.convertMedias = ConvertMedia;
    }

    @Value("${media.convert.disable:true}")
    public void setMediaConvertDisable(String mediaConvertDisable) {
        setMediaConvertDisableValue(mediaConvertDisable);
    }

    public static void setMediaConvertDisableValue(String mediaConvertDisable) {
        ConfigConstants.mediaConvertDisable = mediaConvertDisable;
    }

    @Value("${tif.preview.type:tif}")
    public void setTifPreviewType(String tifPreviewType) {
        setTifPreviewTypeValue(tifPreviewType);
    }

    public static void setTifPreviewTypeValue(String tifPreviewType) {
        ConfigConstants.tifPreviewType = tifPreviewType;
    }

    @Value("${cad.preview.type:svg}")
    public void setCadPreviewType(String cadPreviewType) {
        setCadPreviewTypeValue(cadPreviewType);
    }

    public static void setCadPreviewTypeValue(String cadPreviewType) {
        ConfigConstants.cadPreviewType = cadPreviewType;
    }

    @Value("${prohibit:exe,dll}")
    public void setProhibit(String prohibit) {
        String[] prohibitArr = prohibit.split(",");
        setProhibitValue(prohibitArr);
    }

    public static void setProhibitValue(String[] prohibit) {
        ConfigConstants.prohibit = prohibit;
    }

    // 3. Office配置Setter方法
    @Value("${office.preview.type:image}")
    public void setOfficePreviewType(String officePreviewType) {
        setOfficePreviewTypeValue(officePreviewType);
    }

    public static void setOfficePreviewTypeValue(String officePreviewType) {
        ConfigConstants.officePreviewType = officePreviewType;
    }

    @Value("${office.preview.switch.disabled:true}")
    public void setOfficePreviewSwitchDisabled(String officePreviewSwitchDisabled) {
        ConfigConstants.officePreviewSwitchDisabled = officePreviewSwitchDisabled;
    }

    public static void setOfficePreviewSwitchDisabledValue(String officePreviewSwitchDisabled) {
        ConfigConstants.officePreviewSwitchDisabled = officePreviewSwitchDisabled;
    }

    @Value("${office.type.web:web}")
    public void setOfficeTypeWeb(String officeTypeWeb) {
        setOfficeTypeWebValue(officeTypeWeb);
    }

    public static void setOfficeTypeWebValue(String officeTypeWeb) {
        ConfigConstants.officeTypeWeb = officeTypeWeb;
    }

    @Value("${office.pagerange:false}")
    public void setOfficePageRange(String officePageRange) {
        setOfficePageRangeValue(officePageRange);
    }

    public static void setOfficePageRangeValue(String officePageRange) {
        ConfigConstants.officePageRange = officePageRange;
    }

    @Value("${office.watermark:false}")
    public void setOfficeWatermark(String officeWatermark) {
        setOfficeWatermarkValue(officeWatermark);
    }

    public static void setOfficeWatermarkValue(String officeWatermark) {
        ConfigConstants.officeWatermark = officeWatermark;
    }

    @Value("${office.quality:80}")
    public void setOfficeQuality(String officeQuality) {
        setOfficeQualityValue(officeQuality);
    }

    public static void setOfficeQualityValue(String officeQuality) {
        ConfigConstants.officeQuality = officeQuality;
    }

    @Value("${office.maximageresolution:150}")
    public void setOfficeMaxImageResolution(String officeMaxImageResolution) {
        setOfficeMaxImageResolutionValue(officeMaxImageResolution);
    }

    public static void setOfficeMaxImageResolutionValue(String officeMaxImageResolution) {
        ConfigConstants.officeMaxImageResolution = officeMaxImageResolution;
    }

    @Value("${office.exportbookmarks:true}")
    public void setOfficeExportBookmarks(Boolean officeExportBookmarks) {
        setOfficeExportBookmarksValue(officeExportBookmarks);
    }

    public static void setOfficeExportBookmarksValue(Boolean officeExportBookmarks) {
        ConfigConstants.officeExportBookmarks = officeExportBookmarks;
    }

    @Value("${office.exportnotes:true}")
    public void setExportNotes(Boolean officeExportNotes) {
        setOfficeExportNotesValue(officeExportNotes);
    }

    public static void setOfficeExportNotesValue(Boolean officeExportNotes) {
        ConfigConstants.officeExportNotes = officeExportNotes;
    }

    @Value("${office.documentopenpasswords:true}")
    public void setDocumentOpenPasswords(Boolean officeDocumentOpenPasswords) {
        setOfficeDocumentOpenPasswordsValue(officeDocumentOpenPasswords);
    }

    public static void setOfficeDocumentOpenPasswordsValue(Boolean officeDocumentOpenPasswords) {
        ConfigConstants.officeDocumentOpenPasswords = officeDocumentOpenPasswords;
    }

    // 4. FTP配置Setter方法
    @Value("${ftp.username:}")
    public void setFtpUsername(String ftpUsername) {
        setFtpUsernameValue(ftpUsername);
    }

    public static void setFtpUsernameValue(String ftpUsername) {
        ConfigConstants.ftpUsername = ftpUsername;
    }

    // 5. 路径配置Setter方法
    @Value("${base.url:default}")
    public void setBaseUrl(String baseUrl) {
        setBaseUrlValue(baseUrl);
    }

    public static void setBaseUrlValue(String baseUrl) {
        ConfigConstants.baseUrl = baseUrl;
    }

    @Value("${file.dir:default}")
    public void setFileDir(String fileDir) {
        setFileDirValue(fileDir);
    }

    public static void setFileDirValue(String fileDir) {
        if (!DEFAULT_VALUE.equalsIgnoreCase(fileDir)) {
            if (!fileDir.endsWith(File.separator)) {
                fileDir = fileDir + File.separator;
            }
            ConfigConstants.fileDir = fileDir;
        }
    }

    @Value("${local.preview.dir:default}")
    public void setLocalPreviewDir(String localPreviewDir) {
        setLocalPreviewDirValue(localPreviewDir);
    }

    public static void setLocalPreviewDirValue(String localPreviewDir) {
        if (!DEFAULT_VALUE.equals(localPreviewDir)) {
            if (!localPreviewDir.endsWith(File.separator)) {
                localPreviewDir = localPreviewDir + File.separator;
            }
        }
        ConfigConstants.localPreviewDir = localPreviewDir;
    }

    // 6. 安全配置Setter方法
    @Value("${trust.host:default}")
    public void setTrustHost(String trustHost) {
        setTrustHostSet(getHostValue(trustHost));
    }

    public static void setTrustHostValue(String trustHost) {
        setTrustHostSet(getHostValue(trustHost));
    }

    @Value("${not.trust.host:default}")
    public void setNotTrustHost(String notTrustHost) {
        setNotTrustHostSet(getHostValue(notTrustHost));
    }

    public static void setNotTrustHostValue(String notTrustHost) {
        setNotTrustHostSet(getHostValue(notTrustHost));
    }

    /**
     * 解析主机配置值
     * 支持格式：host1,host2,host3
     * 自动转换为小写并移除空格
     */
    private static CopyOnWriteArraySet<String> getHostValue(String trustHost) {
        if (DEFAULT_VALUE.equalsIgnoreCase(trustHost)) {
            return new CopyOnWriteArraySet<>();
        } else {
            String[] trustHostArray = trustHost.toLowerCase().replaceAll("\\s+", "").split(",");
            return new CopyOnWriteArraySet<>(Arrays.asList(trustHostArray));
        }
    }

    private static void setTrustHostSet(CopyOnWriteArraySet<String> trustHostSet) {
        ConfigConstants.trustHostSet = trustHostSet;
    }

    public static void setNotTrustHostSet(CopyOnWriteArraySet<String> notTrustHostSet) {
        ConfigConstants.notTrustHostSet = notTrustHostSet;
    }

    // 7. PDF配置Setter方法
    @Value("${pdf.presentationMode.disable:true}")
    public void setPdfPresentationModeDisable(String pdfPresentationModeDisable) {
        setPdfPresentationModeDisableValue(pdfPresentationModeDisable);
    }

    public static void setPdfPresentationModeDisableValue(String pdfPresentationModeDisable) {
        ConfigConstants.pdfPresentationModeDisable = pdfPresentationModeDisable;
    }

    @Value("${pdf.openFile.disable:true}")
    public void setPdfOpenFileDisable(String pdfOpenFileDisable) {
        setPdfOpenFileDisableValue(pdfOpenFileDisable);
    }

    public static void setPdfOpenFileDisableValue(String pdfOpenFileDisable) {
        ConfigConstants.pdfOpenFileDisable = pdfOpenFileDisable;
    }

    @Value("${pdf.print.disable:true}")
    public void setPdfPrintDisable(String pdfPrintDisable) {
        setPdfPrintDisableValue(pdfPrintDisable);
    }

    public static void setPdfPrintDisableValue(String pdfPrintDisable) {
        ConfigConstants.pdfPrintDisable = pdfPrintDisable;
    }

    @Value("${pdf.download.disable:true}")
    public void setPdfDownloadDisable(String pdfDownloadDisable) {
        setPdfDownloadDisableValue(pdfDownloadDisable);
    }

    public static void setPdfDownloadDisableValue(String pdfDownloadDisable) {
        ConfigConstants.pdfDownloadDisable = pdfDownloadDisable;
    }

    @Value("${pdf.bookmark.disable:true}")
    public void setPdfBookmarkDisable(String pdfBookmarkDisable) {
        setPdfBookmarkDisableValue(pdfBookmarkDisable);
    }

    public static void setPdfBookmarkDisableValue(String pdfBookmarkDisable) {
        ConfigConstants.pdfBookmarkDisable = pdfBookmarkDisable;
    }

    @Value("${pdf.disable.editing:true}")
    public void setpdfDisableEditing(String pdfDisableEditing) {
        setPdfDisableEditingValue(pdfDisableEditing);
    }

    public static void setPdfDisableEditingValue(String pdfDisableEditing) {
        ConfigConstants.pdfDisableEditing = pdfDisableEditing;
    }

    @Value("${pdf2jpg.dpi:105}")
    public void pdf2JpgDpi(int pdf2JpgDpi) {
        setPdf2JpgDpiValue(pdf2JpgDpi);
    }

    public static void setPdf2JpgDpiValue(int pdf2JpgDpi) {
        ConfigConstants.pdf2JpgDpi = pdf2JpgDpi;
    }

    // 8. CAD配置Setter方法
    @Value("${cad.timeout:90}")
    public void setCadTimeout(String cadTimeout) {
        setCadTimeoutValue(cadTimeout);
    }

    public static void setCadTimeoutValue(String cadTimeout) {
        ConfigConstants.cadTimeout = cadTimeout;
    }

    @Value("${cad.thread:5}")
    public void setCadThread(int cadThread) {
        setCadThreadValue(cadThread);
    }

    public static void setCadThreadValue(int cadThread) {
        ConfigConstants.cadThread = cadThread;
    }

    // 9. TIF配置Setter方法
    @Value("${tif.timeout:90}")
    public void setTifTimeout(String tifTimeout) {
        setTifTimeoutValue(tifTimeout);
    }

    public static void setTifTimeoutValue(String tifTimeout) {
        ConfigConstants.tifTimeout = tifTimeout;
    }

    @Value("${tif.thread:5}")
    public void setTifThread(int tifThread) {
        setTifThreadValue(tifThread);
    }

    public static void setTifThreadValue(int tifThread) {
        ConfigConstants.tifThread = tifThread;
    }

    // 10. 文件操作配置Setter方法
    @Value("${file.upload.disable:true}")
    public void setFileUploadDisable(Boolean fileUploadDisable) {
        setFileUploadDisableValue(fileUploadDisable);
    }

    public static void setFileUploadDisableValue(Boolean fileUploadDisable) {
        ConfigConstants.fileUploadDisable = fileUploadDisable;
    }

    @Value("${spring.servlet.multipart.max-file-size:500MB}")
    public void setSize(String size) {
        setSizeValue(size);
    }

    public static void setSizeValue(String size) {
        ConfigConstants.size = size;
    }

    @Value("${delete.password:123456}")
    public void setPassword(String password) {
        setPasswordValue(password);
    }

    public static void setPasswordValue(String password) {
        ConfigConstants.password = password;
    }

    @Value("${delete.source.file:true}")
    public void setDeleteSourceFile(Boolean deleteSourceFile) {
        setDeleteSourceFileValue(deleteSourceFile);
    }

    public static void setDeleteSourceFileValue(Boolean deleteSourceFile) {
        ConfigConstants.deleteSourceFile = deleteSourceFile;
    }

    @Value("${delete.captcha:false}")
    public void setDeleteCaptcha(Boolean deleteCaptcha) {
        setDeleteCaptchaValue(deleteCaptcha);
    }

    public static void setDeleteCaptchaValue(Boolean deleteCaptcha) {
        ConfigConstants.deleteCaptcha = deleteCaptcha;
    }

    // 11. 首页配置Setter方法
    @Value("${beian:default}")
    public void setBeian(String beian) {
        setBeianValue(beian);
    }

    public static void setBeianValue(String beian) {
        ConfigConstants.beian = beian;
    }

    @Value("${home.pagenumber:1}")
    public void setHomePageNumber(String homePageNumber) {
        setHomePageNumberValue(homePageNumber);
    }

    public static void setHomePageNumberValue(String homePageNumber) {
        ConfigConstants.homePageNumber = homePageNumber;
    }

    @Value("${home.pagination:true}")
    public void setHomePagination(String homePagination) {
        setHomePaginationValue(homePagination);
    }

    public static void setHomePaginationValue(String homePagination) {
        ConfigConstants.homePagination = homePagination;
    }

    @Value("${home.pagesize:15}")
    public void setHomePageSize(String homePageSize) {
        setHomePageSizeValue(homePageSize);
    }

    public static void setHomePageSizeValue(String homePageSize) {
        ConfigConstants.homePageSize = homePageSize;
    }

    @Value("${home.search:1}")
    public void setHomeSearch(String homeSearch) {
        setHomeSearchValue(homeSearch);
    }

    public static void setHomeSearchValue(String homeSearch) {
        ConfigConstants.homeSearch = homeSearch;
    }

    // 12. 权限配置Setter方法
    @Value("${kk.key:false}")
    public void setKey(String key) {
        setKeyValue(key);
    }

    public static void setKeyValue(String key) {
        ConfigConstants.key = key;
    }

    @Value("${kk.Picturespreview:true}")
    public void setPicturesPreview(String picturesPreview) {
        setPicturesPreviewValue(Boolean.parseBoolean(picturesPreview));
    }

    public static void setPicturesPreviewValue(boolean picturesPreview) {
        ConfigConstants.picturesPreview = picturesPreview;
    }

    @Value("${kk.Getcorsfile:true}")
    public void setGetCorsFile(String getCorsFile) {
        setGetCorsFileValue(Boolean.parseBoolean(getCorsFile));
    }

    public static void setGetCorsFileValue(boolean getCorsFile) {
        ConfigConstants.getCorsFile = getCorsFile;
    }

    @Value("${kk.addTask:true}")
    public void setAddTask(String addTask) {
        setAddTaskValue(Boolean.parseBoolean(addTask));
    }

    public static void setAddTaskValue(boolean addTask) {
        ConfigConstants.addTask = addTask;
    }

    @Value("${aes.key:1234567890123456}")
    public void setaesKey(String aesKey) {
        setaesKeyValue(aesKey);
    }

    public static void setaesKeyValue(String aesKey) {
        ConfigConstants.aesKey = aesKey;
    }

    // 13. UserAgent配置Setter方法
    @Value("${useragent:false}")
    public void setUserAgent(String userAgent) {
        setUserAgentValue(userAgent);
    }

    public static void setUserAgentValue(String userAgent) {
        ConfigConstants.userAgent = userAgent;
    }

    // 14. Basic认证配置Setter方法
    @Value("${basic.name:}")
    public void setBasicName(String basicName) {
        setBasicNameValue(basicName);
    }

    public static void setBasicNameValue(String basicName) {
        ConfigConstants.basicName = basicName;
    }

    // 15. 视频转换配置Setter方法
    @Value("${media.convert.max.size:300}")
    public void setMediaConvertMaxSize(int mediaConvertMaxSize) {
        setMediaConvertMaxSizeValue(mediaConvertMaxSize);
    }

    public static void setMediaConvertMaxSizeValue(int mediaConvertMaxSize) {
        ConfigConstants.mediaConvertMaxSize = mediaConvertMaxSize;
    }

    @Value("${media.timeout.enabled:true}")
    public void setMediaTimeoutEnabled(String mediaTimeoutEnabled) {
        setMediaTimeoutEnabledValue(Boolean.parseBoolean(mediaTimeoutEnabled));
    }

    public static void setMediaTimeoutEnabledValue(boolean mediaTimeoutEnabled) {
        ConfigConstants.mediaTimeoutEnabled = mediaTimeoutEnabled;
    }

    @Value("${media.small.file.timeout:30}")
    public void setMediaSmallFileTimeout(int mediaSmallFileTimeout) {
        setMediaSmallFileTimeoutValue(mediaSmallFileTimeout);
    }

    public static void setMediaSmallFileTimeoutValue(int mediaSmallFileTimeout) {
        ConfigConstants.mediaSmallFileTimeout = mediaSmallFileTimeout;
    }

    @Value("${media.medium.file.timeout:60}")
    public void setMediaMediumFileTimeout(int mediaMediumFileTimeout) {
        setMediaMediumFileTimeoutValue(mediaMediumFileTimeout);
    }

    public static void setMediaMediumFileTimeoutValue(int mediaMediumFileTimeout) {
        ConfigConstants.mediaMediumFileTimeout = mediaMediumFileTimeout;
    }

    @Value("${media.large.file.timeout:180}")
    public void setMediaLargeFileTimeout(int mediaLargeFileTimeout) {
        setMediaLargeFileTimeoutValue(mediaLargeFileTimeout);
    }

    public static void setMediaLargeFileTimeoutValue(int mediaLargeFileTimeout) {
        ConfigConstants.mediaLargeFileTimeout = mediaLargeFileTimeout;
    }

    @Value("${media.xl.file.timeout:300}")
    public void setMediaXLFileTimeout(int mediaXLFileTimeout) {
        setMediaXLFileTimeoutValue(mediaXLFileTimeout);
    }

    public static void setMediaXLFileTimeoutValue(int mediaXLFileTimeout) {
        ConfigConstants.mediaXLFileTimeout = mediaXLFileTimeout;
    }

    @Value("${media.xxl.file.timeout:600}")
    public void setMediaXXLFileTimeout(int mediaXXLFileTimeout) {
        setMediaXXLFileTimeoutValue(mediaXXLFileTimeout);
    }

    public static void setMediaXXLFileTimeoutValue(int mediaXXLFileTimeout) {
        ConfigConstants.mediaXXLFileTimeout = mediaXXLFileTimeout;
    }

    @Value("${media.xxxl.file.timeout:1200}")
    public void setMediaXXXLFileTimeout(int mediaXXXLFileTimeout) {
        setMediaXXXLFileTimeoutValue(mediaXXXLFileTimeout);
    }

    public static void setMediaXXXLFileTimeoutValue(int mediaXXXLFileTimeout) {
        ConfigConstants.mediaXXXLFileTimeout = mediaXXXLFileTimeout;
    }

    // 16. PDF DPI配置Setter方法
    @Value("${pdf.dpi.enabled:true}")
    public void setPdfDpiEnabled(String pdfDpiEnabled) {
        setPdfDpiEnabledValue(Boolean.parseBoolean(pdfDpiEnabled));
    }

    public static void setPdfDpiEnabledValue(boolean pdfDpiEnabled) {
        ConfigConstants.pdfDpiEnabled = pdfDpiEnabled;
    }

    @Value("${pdf.dpi.small:150}")
    public void setPdfSmallDpi(int pdfSmallDpi) {
        setPdfSmallDpiValue(pdfSmallDpi);
    }

    public static void setPdfSmallDpiValue(int pdfSmallDpi) {
        ConfigConstants.pdfSmallDpi = pdfSmallDpi;
    }

    @Value("${pdf.dpi.medium:120}")
    public void setPdfMediumDpi(int pdfMediumDpi) {
        setPdfMediumDpiValue(pdfMediumDpi);
    }

    public static void setPdfMediumDpiValue(int pdfMediumDpi) {
        ConfigConstants.pdfMediumDpi = pdfMediumDpi;
    }

    @Value("${pdf.dpi.large:96}")
    public void setPdfLargeDpi(int pdfLargeDpi) {
        setPdfLargeDpiValue(pdfLargeDpi);
    }

    public static void setPdfLargeDpiValue(int pdfLargeDpi) {
        ConfigConstants.pdfLargeDpi = pdfLargeDpi;
    }

    @Value("${pdf.dpi.xlarge:72}")
    public void setPdfXLargeDpi(int pdfXLargeDpi) {
        setPdfXLargeDpiValue(pdfXLargeDpi);
    }

    public static void setPdfXLargeDpiValue(int pdfXLargeDpi) {
        ConfigConstants.pdfXLargeDpi = pdfXLargeDpi;
    }

    @Value("${pdf.dpi.xxlarge:72}")
    public void setPdfXXLargeDpi(int pdfXXLargeDpi) {
        setPdfXXLargeDpiValue(pdfXXLargeDpi);
    }

    public static void setPdfXXLargeDpiValue(int pdfXXLargeDpi) {
        ConfigConstants.pdfXXLargeDpi = pdfXXLargeDpi;
    }

    // 17. PDF超时配置Setter方法（新）
    @Value("${pdf.timeout.small:90}")
    public void setPdfTimeoutSmall(int pdfTimeoutSmall) {
        setPdfTimeoutSmallValue(pdfTimeoutSmall);
    }

    public static void setPdfTimeoutSmallValue(int pdfTimeoutSmall) {
        ConfigConstants.pdfTimeoutSmall = pdfTimeoutSmall;
    }

    @Value("${pdf.timeout.medium:180}")
    public void setPdfTimeoutMedium(int pdfTimeoutMedium) {
        setPdfTimeoutMediumValue(pdfTimeoutMedium);
    }

    public static void setPdfTimeoutMediumValue(int pdfTimeoutMedium) {
        ConfigConstants.pdfTimeoutMedium = pdfTimeoutMedium;
    }

    @Value("${pdf.timeout.large:300}")
    public void setPdfTimeoutLarge(int pdfTimeoutLarge) {
        setPdfTimeoutLargeValue(pdfTimeoutLarge);
    }

    public static void setPdfTimeoutLargeValue(int pdfTimeoutLarge) {
        ConfigConstants.pdfTimeoutLarge = pdfTimeoutLarge;
    }

    @Value("${pdf.timeout.xlarge:600}")
    public void setPdfTimeoutXLarge(int pdfTimeoutXLarge) {
        setPdfTimeoutXLargeValue(pdfTimeoutXLarge);
    }

    public static void setPdfTimeoutXLargeValue(int pdfTimeoutXLarge) {
        ConfigConstants.pdfTimeoutXLarge = pdfTimeoutXLarge;
    }

    // 18. PDF线程配置Setter方法
    @Value("${pdf.max.threads:10}")
    public void setPdfMaxThreads(int pdfMaxThreads) {
        setPdfMaxThreadsValue(pdfMaxThreads);
    }

    public static void setPdfMaxThreadsValue(int pdfMaxThreads) {
        ConfigConstants.pdfMaxThreads = pdfMaxThreads;
    }

    // 19. CAD水印配置Setter方法
    @Value("${cad.watermark:false}")
    public void setCadwatermark(String cadwatermark) {
        setCadwatermarkValue(Boolean.parseBoolean(cadwatermark));
    }

    public static void setCadwatermarkValue(Boolean cadwatermark) {
        ConfigConstants.cadwatermark = cadwatermark;
    }

    // 20. SSL忽略配置Setter方法
    @Value("${kk.ignore.ssl:true}")
    public void setIgnoreSSL(String ignoreSSL) {
        setIgnoreSSLValue(Boolean.parseBoolean(ignoreSSL));
    }

    public static void setIgnoreSSLValue(Boolean ignoreSSL) {
        ConfigConstants.ignoreSSL = ignoreSSL;
    }

    // 21. 重定向启用配置Setter方法
    @Value("${kk.enable.redirect:true}")
    public void setEnableRedirect(String enableRedirect) {
        setEnableRedirectValue(Boolean.parseBoolean(enableRedirect));
    }

    public static void setEnableRedirectValue(Boolean enableRedirect) {
        ConfigConstants.enableRedirect = enableRedirect;
    }

    // 22 异步定时刷新时间
    @Value("${kk.refreshSchedule:5}")
    public void setRefreshSchedule(int refreshSchedule) {
        setRefreshScheduleValue(refreshSchedule);
    }

    public static void setRefreshScheduleValue(int refreshSchedule) {
        ConfigConstants.refreshSchedule = refreshSchedule;
    }

    // 23. 其他配置Setter方法
    @Value("${kk.isshowaeskey:false}")
    public void setIsShowaesKey(String isShowaesKey) {
        setIsShowaesKeyValue(Boolean.parseBoolean(isShowaesKey));
    }

    public static void setIsShowaesKeyValue(boolean isShowaesKey) {
        ConfigConstants.isShowaesKey = isShowaesKey;
    }

    @Value("${kk.isjavascript:false}")
    public void setIsJavaScript(String isJavaScript) {
        setIsJavaScriptValue(Boolean.parseBoolean(isJavaScript));
    }

    public static void setIsJavaScriptValue(boolean isJavaScript) {
        ConfigConstants.isJavaScript = isJavaScript;
    }

    @Value("${kk.xlsxallowedit:false}")
    public void setXlsxAllowEdit(String xlsxAllowEdit) {
        setXlsxAllowEditValue(Boolean.parseBoolean(xlsxAllowEdit));
    }

    public static void setXlsxAllowEditValue(boolean xlsxAllowEdit) {
        ConfigConstants.xlsxAllowEdit = xlsxAllowEdit;
    }

    @Value("${kk.xlsxshowtoolbar:false}")
    public void setXlsxShowtoolbar(String xlsxShowtoolbar) {
        setXlsxShowtoolbarValue(Boolean.parseBoolean(xlsxShowtoolbar));
    }

    public static void setXlsxShowtoolbarValue(boolean xlsxShowtoolbar) {
        ConfigConstants.xlsxShowtoolbar = xlsxShowtoolbar;
    }

    @Value("${kk.isshowkey:false}")
    public void setisShowKey(String isShowKey) {
        setisShowKeyValue(Boolean.parseBoolean(isShowKey));
    }

    public static void setisShowKeyValue(boolean isShowKey) {
        ConfigConstants.isShowKey = isShowKey;
    }

    @Value("${kk.scriptjs:false}")
    public void setscriptJs(String scriptJs) {
        setscriptJsValue(Boolean.parseBoolean(scriptJs));
    }

    public static void setscriptJsValue(boolean scriptJs) {
        ConfigConstants.scriptJs = scriptJs;
    }
}