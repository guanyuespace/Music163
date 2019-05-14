package guanyue.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import guanyue.util.ParametersUtil;

/**
 * @author Guan Yue
 * @time 2018年1月11日下午5:00:04
 *
 */
public class Login {
	private static CloseableHttpClient client = null;

	private static final String USER_NAME = "***********@163.com";
	private static final String PASSWORD = "*************";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).setConnectTimeout(500)
					.build();
			client = HttpClients.custom().setDefaultRequestConfig(config).setUserAgent(
					"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
					.build();
			String url = "http://music.163.com/weapi/login?csrf_token=";

			loginMusic163(url);

		} finally {
			try {
				if (client != null)
					client.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	private static void loginMusic163(String url) {// http://music.163.com/weapi/login?csrf_token=
		// TODO Auto-generated method stub
		try {
			//
			HttpGet getDoc = new HttpGet("http://music.163.com/");
			HttpResponse response;
			response = client.execute(getDoc);
			System.out.println(response.getStatusLine());
			getDoc.abort();

			//

			HttpPost loginMusic = new HttpPost("http://music.163.com/weapi/login?csrf_token=");
			loginMusic.addHeader("Content-Type", "application/x-www-form-urlencoded");
			loginMusic.addHeader("Referer", "https://music.163.com/");

			String param1 = "\"username\":\"" + USER_NAME + "\",\"password\":\"" + ParametersUtil.getMD5(PASSWORD)
					+ "\",\"rememberLogin\":\"true\",\"checkToken\":\"9ca17ae2e6ffcda170e2e6ee98b54491908e8eed6995ebbb8ed247acad86bbc87ff596bcabdb6d98ecaa8fb82af0feaec3b92a91b287aac533a7ed8da8d13ba687bdaee94df79ee191c1398e91a6aec25d898dee9e\",\"csrf_token\":\"\"";
			param1 = "{\"username\":\"" + USER_NAME + "\",\"password\":\"" + ParametersUtil.getMD5(PASSWORD)
					+ "\",\"rememberLogin\":\"true\",\"csrf_token\":\"\"}";

			List<NameValuePair> nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			loginMusic.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			response = client.execute(loginMusic);
			System.out.printf("url: %1$s   status: %2$s %n", loginMusic.getRequestLine().getUri(),
					response.getStatusLine());

			for (Header e : response.getHeaders("SET-COOKIE")) {
				String v = e.getValue().split(";")[0];
				BasicClientCookie ck = new BasicClientCookie(v.split("=")[0], v.split("=")[1]);
				ck.setVersion(0);
				ck.setDomain("music.163.com");
				ck.setPath("/");
				// ck.setExpiryDate(new Date());//全部过期无效
				ck.setExpiryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-05-08 19:44:26"));
				store.addCookie(ck);
			}
			System.out.println(EntityUtils.toString(response.getEntity()));
			loginMusic.abort();

			//
			HttpPost discovery = new HttpPost("http://music.163.com/weapi/discovery/recommend/resource?csrf_token=");
			discovery.addHeader("Content-Type", "application/x-www-form-urlencoded");
			discovery.addHeader("Referer", "https://music.163.com/");

			param1 = "{\"csrf_token\":\"\"}";
			nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			discovery.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			response = client.execute(discovery);
			System.out.printf("url: %1$s   status: %2$s %n", discovery.getRequestLine().getUri(),
					response.getStatusLine());
			System.out.println(EntityUtils.toString(response.getEntity()));
			discovery.abort();

			//
			HttpPost playlist = new HttpPost("http://music.163.com/weapi/user/playlist?csrf_token=");
			playlist.addHeader("Content-Type", "application/x-www-form-urlencoded");
			playlist.addHeader("Referer", "https://music.163.com/");

			param1 = "{\"offset\":\"0\",\"limit\":\"1001\",\"uid\":\"275438773\",\"csrf_token\":\"\"}";
			nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			playlist.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			response = client.execute(playlist);
			System.out.printf("url: %1$s   status: %2$s %n", playlist.getRequestLine().getUri(),
					response.getStatusLine());
			System.out.println(EntityUtils.toString(response.getEntity()));
			playlist.abort();

			//
			HttpPost detatils = new HttpPost("http://music.163.com/weapi/v3/playlist/detail?csrf_token=");
			detatils.addHeader("Content-Type", "application/x-www-form-urlencoded");
			detatils.addHeader("Referer", "https://music.163.com/");

			param1 = "{\"id\":\"378179227\",\"offset\":\"0\",\"total\":\"true\",\"limit\":\"1000\",\"n\":\"1000\",\"csrf_token\":\"\"}";
			nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			detatils.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			response = client.execute(detatils);
			System.out.printf("url: %1$s   status: %2$s %n", detatils.getRequestLine().getUri(),
					response.getStatusLine());
			System.out.println(EntityUtils.toString(response.getEntity()));
			detatils.abort();

			//
			HttpPost logout = new HttpPost("http://music.163.com/weapi/logout?csrf_token=");
			logout.addHeader("Content-Type", "application/x-www-form-urlencoded");
			logout.addHeader("Referer", "https://music.163.com/");

			param1 = "{\"csrf_token\":\"\"}";
			nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			logout.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			response = client.execute(logout);
			System.out.printf("url: %1$s   status: %2$s %n", logout.getRequestLine().getUri(),
					response.getStatusLine());
			System.out.println(EntityUtils.toString(response.getEntity()));
			logout.abort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				if (client != null)
					client.close();
			} catch (Exception e2) {
				// TODO: handle exception	·
				e2.printStackTrace();
			}

		} finally {
			// TODO: handle finally clause
		}

	}

}
