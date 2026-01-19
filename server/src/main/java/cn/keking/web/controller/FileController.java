package cn.keking.web.controller;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileType;
import cn.keking.model.ReturnResponse;
import cn.keking.utils.CaptchaUtil;
import cn.keking.utils.DateUtils;
import cn.keking.utils.KkFileUtils;
import cn.keking.utils.RarUtils;
import cn.keking.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static cn.keking.utils.CaptchaUtil.CAPTCHA_CODE;
import static cn.keking.utils.CaptchaUtil.CAPTCHA_GENERATE_TIME;

/**
 * @author yudian-it
 * 2017/12/1
 */
@RestController
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final String fileDir = ConfigConstants.getFileDir();
    private final String demoDir = "demo";

    private final String demoPath = demoDir + File.separator;
    public static final String BASE64_DECODE_ERROR_MSG = "Base64解码失败，请检查你的 %s 是否采用 Base64 + urlEncode 双重编码了！";

    @PostMapping("/fileUpload")
    public ReturnResponse<Object> fileUpload(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "path", defaultValue = "") String path) {
        ReturnResponse<Object> checkResult = this.fileUploadCheck(file, path);
        if (checkResult.isFailure()) {
            return checkResult;
        }

        String uploadPath = fileDir + demoPath;
        if (!ObjectUtils.isEmpty(path)) {
            uploadPath += path + File.separator;
        }

        File outFile = new File(uploadPath);
        if (!outFile.exists() && !outFile.mkdirs()) {
            logger.error("创建文件夹【{}】失败，请检查目录权限！", uploadPath);
            return ReturnResponse.failure("创建文件夹失败，请检查目录权限！");
        }

        String fileName = checkResult.getContent().toString();
        logger.info("上传文件：{}{}", uploadPath, fileName);

        try (InputStream in = file.getInputStream();
             OutputStream out = Files.newOutputStream(Paths.get(uploadPath + fileName))) {
            StreamUtils.copy(in, out);
            return ReturnResponse.success(null);
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return ReturnResponse.failure("文件上传失败");
        }
    }

    @PostMapping("/createFolder")
    public ReturnResponse<Object> createFolder(@RequestParam(value = "path", defaultValue = "") String path,
                                               @RequestParam("folderName") String folderName) {
        if (ConfigConstants.getFileUploadDisable()) {
            return ReturnResponse.failure("文件上传接口已禁用");
        }
        try {
            // 验证文件夹名称
            if (ObjectUtils.isEmpty(folderName)) {
                return ReturnResponse.failure("文件夹名称不能为空");
            }

            if (KkFileUtils.isIllegalFileName(folderName)) {
                return ReturnResponse.failure("非法文件夹名称");
            }
            String basePath = fileDir + demoPath;
            if (!ObjectUtils.isEmpty(path)) {
                basePath += path + File.separator;
            }

            File newFolder = new File(basePath + folderName);
            if (newFolder.exists()) {
                return ReturnResponse.failure("文件夹已存在");
            }

            if (newFolder.mkdirs()) {
                logger.info("创建文件夹：{}", newFolder.getAbsolutePath());
                return ReturnResponse.success();
            } else {
                logger.error("创建文件夹失败：{}", newFolder.getAbsolutePath());
                return ReturnResponse.failure("创建文件夹失败，请检查目录权限");
            }
        } catch (Exception e) {
            logger.error("创建文件夹异常", e);
            return ReturnResponse.failure("创建文件夹失败：" + e.getMessage());
        }
    }

    @GetMapping("/deleteFile")
    public ReturnResponse<Object> deleteFile(HttpServletRequest request, String fileName, String password) {
        ReturnResponse<Object> checkResult = this.deleteFileCheck(request, fileName, password);
        if (checkResult.isFailure()) {
            return checkResult;
        }
        fileName = checkResult.getContent().toString();

        // 构建完整路径
        String fullPath = fileDir + demoPath + fileName;
        File file = new File(fullPath);

        logger.info("删除文件/文件夹：{}", file.getAbsolutePath());
        if (file.exists()) {
            if (file.isDirectory()) {
                // 删除文件夹及其内容
                if (deleteDirectory(file)) {
                    WebUtils.removeSessionAttr(request, CAPTCHA_CODE);
                    return ReturnResponse.success();
                } else {
                    String msg = String.format("删除文件夹【%s】失败，请检查目录权限！", file.getPath());
                    logger.error(msg);
                    return ReturnResponse.failure(msg);
                }
            } else {
                // 删除文件
                if (file.delete()) {
                    WebUtils.removeSessionAttr(request, CAPTCHA_CODE);
                    return ReturnResponse.success();
                } else {
                    String msg = String.format("删除文件【%s】失败，请检查目录权限！", file.getPath());
                    logger.error(msg);
                    return ReturnResponse.failure(msg);
                }
            }
        } else {
            return ReturnResponse.failure("文件或文件夹不存在");
        }
    }

    /**
     * 递归删除目录
     */
    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    boolean success = deleteDirectory(child);
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    /**
     * 验证码方法
     */
    @RequestMapping("/deleteFile/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!ConfigConstants.getDeleteCaptcha()) {
            return;
        }

        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -1);
        String captchaCode = WebUtils.getSessionAttr(request, CAPTCHA_CODE);
        long captchaGenerateTime = WebUtils.getLongSessionAttr(request, CAPTCHA_GENERATE_TIME);
        long timeDifference = DateUtils.calculateCurrentTimeDifference(captchaGenerateTime);

        // 验证码为空，且生成验证码超过50秒，重新生成验证码
        if (timeDifference > 50 && ObjectUtils.isEmpty(captchaCode)) {
            captchaCode = CaptchaUtil.generateCaptchaCode();
            // 更新验证码
            WebUtils.setSessionAttr(request, CAPTCHA_CODE, captchaCode);
            WebUtils.setSessionAttr(request, CAPTCHA_GENERATE_TIME, DateUtils.getCurrentSecond());
        } else {
            captchaCode = ObjectUtils.isEmpty(captchaCode) ? "wait" : captchaCode;
        }

        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(CaptchaUtil.generateCaptchaPic(captchaCode), "jpeg", outputStream);
        outputStream.close();
    }

    @PostMapping("/listFiles")
    public Map<String, Object> getFiles(@RequestParam(value = "path", defaultValue = "") String path,
                                        @RequestParam(value = "searchText", defaultValue = "") String searchText,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(required = false) String order) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> fileList = new ArrayList<>();

        try {
            // 构建完整路径
            String basePath = fileDir + demoPath;
            if (!ObjectUtils.isEmpty(path)) {
                basePath += path + File.separator;
            }

            File currentDir = new File(basePath);
            if (currentDir.exists() && currentDir.isDirectory()) {
                File[] files = currentDir.listFiles();
                if (files != null) {
                    // 转换为List
                    List<File> fileObjects = new ArrayList<>(Arrays.asList(files));

                    // 如果搜索文本不为空，进行过滤
                    if (!ObjectUtils.isEmpty(searchText)) {
                        String searchLower = searchText.toLowerCase();
                        fileObjects.removeIf(f -> !f.getName().toLowerCase().contains(searchLower));
                    }

                    // 排序
                    Comparator<File> comparator = getFileComparator(sort, order);
                    if (comparator != null) {
                        fileObjects.sort(comparator);
                    }

                    int total = fileObjects.size();
                    int start = page * size;
                    int end = Math.min(start + size, total);

                    if (start < total) {
                        for (int i = start; i < end; i++) {
                            File f = fileObjects.get(i);
                            Map<String, Object> fileInfo = new HashMap<>();

                            fileInfo.put("name", f.getName());
                            fileInfo.put("isDirectory", f.isDirectory());
                            fileInfo.put("lastModified", f.lastModified());
                            fileInfo.put("size", f.length());

                            // 构建路径信息
                            String relativePath = demoDir + "/" + (ObjectUtils.isEmpty(path) ? "" : path + "/") + f.getName();
                            fileInfo.put("relativePath", relativePath);

                            // 如果是目录，保存完整的相对路径用于导航
                            if (f.isDirectory()) {
                                String fullPath = ObjectUtils.isEmpty(path) ? f.getName() : path + "/" + f.getName();
                                fileInfo.put("fullPath", fullPath);
                            }

                            // 获取文件属性
                            try {
                                Path filePath = f.toPath();
                                BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
                                fileInfo.put("creationTime", attrs.creationTime().toMillis());
                            } catch (IOException e) {
                                logger.warn("获取文件属性失败: {}", f.getName(), e);
                            }

                            fileList.add(fileInfo);
                        }
                    }

                    result.put("total", total);
                    result.put("data", fileList);
                }
            } else {
                result.put("total", 0);
                result.put("data", Collections.emptyList());
            }
        } catch (Exception e) {
            logger.error("获取文件列表失败", e);
            result.put("total", 0);
            result.put("data", Collections.emptyList());
        }

        return result;
    }

    /**
     * 获取文件比较器
     */
    private Comparator<File> getFileComparator(String sort, String order) {
        if (ObjectUtils.isEmpty(sort)) {
            // 默认按文件夹优先，然后按名称排序
            return (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            };
        }

        boolean isDesc = "desc".equalsIgnoreCase(order);

        return switch (sort) {
            case "name" -> (f1, f2) -> {
                int compare = f1.getName().compareToIgnoreCase(f2.getName());
                return isDesc ? -compare : compare;
            };
            case "lastModified" -> (f1, f2) -> {
                long compare = Long.compare(f1.lastModified(), f2.lastModified());
                return isDesc ? (int) -compare : (int) compare;
            };
            case "size" -> (f1, f2) -> {
                if (f1.isDirectory() && f2.isDirectory()) {
                    return 0;
                } else if (f1.isDirectory()) {
                    return isDesc ? 1 : -1;
                } else if (f2.isDirectory()) {
                    return isDesc ? -1 : 1;
                } else {
                    long compare = Long.compare(f1.length(), f2.length());
                    return isDesc ? (int) -compare : (int) compare;
                }
            };
            case "isDirectory" -> (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return isDesc ? 1 : -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return isDesc ? -1 : 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            };
            default -> null;
        };
    }

    /**
     * 上传文件前校验
     */
    private ReturnResponse<Object> fileUploadCheck(MultipartFile file, String path) {
        if (ConfigConstants.getFileUploadDisable()) {
            return ReturnResponse.failure("文件上传接口已禁用");
        }

        String fileName = WebUtils.getFileNameFromMultipartFile(file);
        if (fileName.lastIndexOf(".") == -1) {
            return ReturnResponse.failure("不允许上传的类型");
        }
        if (!KkFileUtils.isAllowedUpload(fileName)) {
            return ReturnResponse.failure("不允许上传的文件类型: " + fileName);
        }
        if (KkFileUtils.isIllegalFileName(fileName)) {
            return ReturnResponse.failure("不允许上传的文件名: " + fileName);
        }
        FileType type = FileType.typeFromFileName(fileName);
        if (Objects.equals(type, FileType.OTHER)) {
            return ReturnResponse.failure("该文件格式还不支持预览，请联系管理员，添加该格式: " + fileName);
        }

        // 判断是否存在同名文件
        if (existsFile(fileName, path)) {
            return ReturnResponse.failure("存在同名文件，请先删除原有文件再次上传");
        }

        return ReturnResponse.success(fileName);
    }

    /**
     * 删除文件前校验
     */
    private ReturnResponse<Object> deleteFileCheck(HttpServletRequest request, String fileName, String password) {
        if (ObjectUtils.isEmpty(fileName)) {
            return ReturnResponse.failure("文件名为空，删除失败！");
        }
        try {
            fileName = WebUtils.decodeUrl(fileName, "base64");
        } catch (Exception ex) {
            String errorMsg = String.format(BASE64_DECODE_ERROR_MSG, fileName);
            return ReturnResponse.failure(errorMsg + "删除失败！");
        }

        if (ObjectUtils.isEmpty(fileName)) {
            return ReturnResponse.failure("文件名为空，删除失败！");
        }
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        if (KkFileUtils.isIllegalFileName(fileName)) {
            return ReturnResponse.failure("非法文件名，删除失败！");
        }

        if (ObjectUtils.isEmpty(password)) {
            return ReturnResponse.failure("密码 or 验证码为空，删除失败！");
        }

        String expectedPassword = ConfigConstants.getDeleteCaptcha() ?
                WebUtils.getSessionAttr(request, CAPTCHA_CODE) :
                ConfigConstants.getPassword();

        if (!password.equalsIgnoreCase(expectedPassword)) {
            logger.error("删除文件【{}】失败，密码错误！", fileName);
            return ReturnResponse.failure("删除文件失败，密码错误！");
        }

        return ReturnResponse.success(fileName);
    }

    @GetMapping("/directory")
    public Object directory(String urls) {
        String fileUrl;
        try {
            fileUrl = WebUtils.decodeUrl(urls,"base64");
        } catch (Exception ex) {
            String errorMsg = String.format(BASE64_DECODE_ERROR_MSG, "url");
            return ReturnResponse.failure(errorMsg);
        }
        fileUrl = fileUrl.replaceAll("http://", "");
        if (KkFileUtils.isIllegalFileName(fileUrl)) {
            return ReturnResponse.failure("不允许访问的路径:");
        }
        return RarUtils.getTree(fileUrl);
    }

    private boolean existsFile(String fileName, String path) {
        String fullPath = fileDir + demoPath;
        if (!ObjectUtils.isEmpty(path)) {
            fullPath += path + File.separator;
        }
        File file = new File(fullPath + fileName);
        return file.exists();
    }
}