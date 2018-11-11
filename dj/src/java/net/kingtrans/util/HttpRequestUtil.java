package net.kingtrans.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestUtil {

	public static Logger logger = Logger.getLogger(HttpRequestUtil.class);

	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000)
			.setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();

	public String sendHttpsPost(String url, String params) {
		if (logger.isDebugEnabled()) {
			logger.debug("\n请求地址：【" + url + "】");
			logger.debug("\n提交数据：【" + params + "】");
		}
		HttpClient httpClient = null;
		String result = null;
		try {
			// String[] cp = getCertAndPass();
			httpClient = HttpsClientFactory.getNewHttpClient(null, null);
			HttpPost httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
			httpPost.setConfig(requestConfig);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("\n返回结果：\n" + result);
		}
		return result;
	}

	public String sendHttpsPost(String httpUrl, Map<String, String> maps) {
		if (logger.isDebugEnabled()) {
			logger.debug("\n请求地址：【" + httpUrl + "】");
			logger.debug("\n提交数据：【" + maps + "】");
		}
		HttpClient httpClient = null;
		String result = null;
		try {
			httpClient = HttpsClientFactory.getNewHttpClient();
			HttpPost httpPost = new HttpPost(httpUrl);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (String key : maps.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			httpPost.setConfig(requestConfig);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("\n返回结果\n：" + result);
		}
		return result;
	}

	public String sendHttpGet(String httpUrl) {
		if (logger.isDebugEnabled()) {
			logger.debug("\n请求地址：【" + httpUrl + "】");
		}
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(httpUrl);
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("\n返回结果\n：" + result);
		}
		return result;
	}

	public String sendHttpsGet(String httpUrl) {
		if (logger.isDebugEnabled()) {
			logger.debug("\n请求地址：【" + httpUrl + "】");
		}
		HttpClient httpClient = null;
		HttpResponse response = null;
		String result = null;
		try {
			httpClient = HttpsClientFactory.getNewHttpClient();
			HttpGet httpGet = new HttpGet(httpUrl);
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("\n返回结果\n：" + result);
		}
		return result;
	}

}