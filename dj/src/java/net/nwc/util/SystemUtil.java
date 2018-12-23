package net.nwc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Cheng
 */
public class SystemUtil {
	private static Logger log = Logger.getLogger(SystemUtil.class);

	// 获取系统绝对路径
	public static String getWebRoot() {
		File f = new File(SystemUtil.class.getClassLoader().getResource("").getFile());
		String root = f.getParentFile().getParent();// class
													// 根目录的上级目录（web-inf文件夹）的上级目录（项目根目录）
		if (log.isDebugEnabled()) {
			log.debug("项目路径： 【" + root + "】");
		}
		return root.replaceAll("%20", " ");
	}

	// 获取发送请求的IP地址
	public static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

	// 保存上传文件，名称随机数
	public static String saveFileFromMultipartFile(MultipartFile multiFile, String filePath) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("上传文件保存: " + multiFile.getOriginalFilename());
		}
		if (multiFile == null || multiFile.getOriginalFilename().trim().length() == 0)
			return null;
		String filename = multiFile.getOriginalFilename();// 上传文件名
		String format = filename.substring(filename.lastIndexOf("."));// 文件格式
		String localFileName = getRandomStr(10) + "_" + new Date().getTime() + format;// 保存文件名
		String localPath = getWebRoot() + filePath;// 保存路径
		File filedir = new File(localPath);
		if (!filedir.exists()) {
			filedir.mkdirs();// 路径不存在则创建
		}
		File file = new File(filedir.getAbsolutePath() + File.separator + localFileName);
		if (log.isDebugEnabled()) {
			log.debug("上传文件保存路径： 【" + file.getAbsolutePath() + "】");
		}
		try {
			multiFile.transferTo(file);
			return localFileName;
		} catch (Exception e) {
			e.printStackTrace();
			file.deleteOnExit();
			throw new Exception(e);
		}
	}

	/**
	 * @EditDate 2018-3-29
	 * @Author Cheng
	 * @param remoteFilePath
	 *            远程文件地址
	 * @param savePath
	 *            保存路径
	 * @param filename
	 *            保存文件名
	 */
	public static void uploadFile(String remoteFilePath, String savePath, String filename) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		savePath = getWebRoot() + savePath;
		File f = new File(savePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		f = new File(savePath + File.separator + filename);
		try {
			URL urlfile = new URL(remoteFilePath);
			HttpURLConnection httpUrl = (HttpURLConnection) urlfile.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(f));
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			bos.flush();
			httpUrl.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Description 根据ftl模板文件创建静态html文件
	 * @EditDate 2017-11-1
	 * @Author Cheng
	 */
	public static boolean createHtmlByFTL(ServletContext sc, Map<String, Object> map, String path, String template,
			String outPath, String name) {
		boolean flag = false;
		Writer out = null;
		if (map == null) {
			map = new HashMap<String, Object>();
			return flag;
		}
		try {
			Configuration cfg = new Configuration();
			cfg.setDefaultEncoding("utf-8");
			path = "/WEB-INF/" + path;
			cfg.setServletContextForTemplateLoading(sc, path);
			Template tmp = cfg.getTemplate(template);
			outPath = sc.getRealPath("/") + File.separator + outPath;
			File filePath = new File(outPath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			File file = new File(filePath, name);
			if (!file.exists())
				file.createNewFile();
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			tmp.process(map, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 生成随机字符串 Add by Cheng 2016-12-30
	 */
	public static String getRandomStr(int length) {
		String array = "0123456789ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwsyz";
		StringBuffer buf = new StringBuffer();
		int arraylength = array.length();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(arraylength);
			char chars = array.charAt(index);
			buf.append(chars);
		}
		return buf.toString();
	}

	public static String getRandomNum(int length) {
		String array = "0123456789";
		StringBuffer buf = new StringBuffer();
		int arraylength = array.length();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(arraylength);
			char chars = array.charAt(index);
			buf.append(chars);
		}
		return buf.toString();
	}


}
