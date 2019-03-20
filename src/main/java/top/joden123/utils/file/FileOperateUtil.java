package top.joden123.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 文件上传下载处理工具类
 * <p>
 * @author JodenHe
 * @create 2017年9月29日 下午4:30:54
 */
public class FileOperateUtil {

    private static Logger log = LoggerFactory.getLogger(FileOperateUtil.class);

    /** 
     * 下载 
     *  
     * @date 2012-5-5 下午12:25:39
     * @param request 
     * @param response 
     * @param storeName 
     * @param contentType 
     * @param realName 
     * @throws Exception 
     */  
    public static void download(HttpServletRequest request, HttpServletResponse response, String storeName, String contentType, String realName) throws Exception {
        String ctxPath = request.getSession().getServletContext().getRealPath("/");
        String downLoadPath = ctxPath + storeName;
        File file = new File(downLoadPath);
        file.renameTo(new File(ctxPath + realName));
        download(request, response, contentType, file);
    }  
    

    /**
     * 下载 兼容文件名含有空格
     * @param request
     * @param response
     * @param contentType
     * @throws Exception
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String contentType, File file) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String realName = file.getName();
        long fileLength = file.length();

        response.setHeader("Content-disposition", "attachment; filename=\""
                + new String(realName.getBytes("utf-8"), "ISO8859-1")+"\"");
        response.setHeader("Content-Length", String.valueOf(fileLength));

        if (contentType != null) {
            response.setContentType(contentType);
        } else {
            // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
        }

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("Download file error, e = {}", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }

    /**
     * url 文件代理中转
     * @param response
     * @param address
     * @param contentType
     * @param fileName
     * @throws IOException
     */
    public static void proxyUrlFile(HttpServletResponse response, String address, String contentType, String fileName) throws IOException {
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(address);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();

            outputStream = response.getOutputStream();

            if (contentType != null) {
                response.setContentType(contentType);
            } else {
                // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
                response.setContentType("multipart/form-data");
            }
            if (fileName != null) {
                response.setHeader("Content-disposition", "attachment; filename=\""
                        + new String(fileName.getBytes("utf-8"), "ISO8859-1")+"\"");
            }

            // 创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            // 每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            // 使用一个输入流从buffer里把数据读取出来
            while((len = inputStream.read(buffer)) != -1 ){
                // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len 代表读取的长度
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("Proxy URL File error, e = {}", e);
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}