package guanyue.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
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
import org.json.JSONArray;
import org.json.JSONObject;

import guanyue.util.ParametersUtil;

public class Comments {
	private static CloseableHttpClient client = null;

	public static void main(String[] args) {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000)
				.setCookieSpec(CookieSpecs.DEFAULT).build();
		client = HttpClients.custom().setDefaultRequestConfig(config).setUserAgent(
				"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
				.build();

		String url = "http://music.163.com/";
		startSomethingHere(url);

		String id = "461525011";
		getMusicCommentsById("起风了", id, 0);
	}

	/**
	 * 准备工作
	 * 
	 * @param url
	 */
	private static void startSomethingHere(String url) {
		// TODO Auto-generated method stub
		try {
			// Request URL:http://music.163.com/
			// Request Method:GET
			HttpGet getDoc = new HttpGet("http://music.163.com/");
			HttpResponse response;
			response = client.execute(getDoc);
			System.out.printf("status:%2$s  url:%1$s %n", url, response.getStatusLine());
			getDoc.abort();

			// Request URL:http://music.163.com/discover
			// Request Method:GET
			HttpGet discover = new HttpGet("http://music.163.com/discover");
			response = client.execute(discover);
			System.out.printf("status:%2$s  url:%1$s %n", "http://music.163.com/discover", response.getStatusLine());
			discover.abort();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				if (client != null) {
					client.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		} finally {

		}

	}

	/**
	 * 获取歌曲评论
	 * 
	 * @param id
	 *            歌曲id
	 * @param offset
	 *            偏移量
	 */
	private static void getMusicCommentsById(String desc, String id, int offset) {
		// TODO Auto-generated method stub
		PrintStream filestream = null;
		PrintStream ps = System.out;
		try {
			File dir = new File("src/res/Comments/Music");
			if (!dir.exists())
				dir.mkdirs();
			File file = new File("src/res/Comments/Music" + "/" + desc + ".txt");
			if (!file.exists())
				file.createNewFile();
			filestream = new PrintStream(new FileOutputStream(file, true));
			System.setOut(filestream);

			HttpPost musicComment = new HttpPost(
					"https://music.163.com/weapi/v1/resource/comments/R_SO_4_" + id + "?csrf_token=");
			musicComment.addHeader("Content-Type", "application/x-www-form-urlencoded");

			String param1 = "{\"rid\":\"R_SO_4_" + id + "\",\"offset\":\"" + offset
					+ "\",\"total\":\"false\",\"limit\":\"20\",\"csrf_token\":\"\"}", param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			musicComment.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			HttpResponse response = client.execute(musicComment);
			String rawStr = EntityUtils.toString(response.getEntity());
			musicComment.abort();

			if (parseCommet(rawStr)) {
				offset += 20;
				getMusicCommentsById(desc, id, offset);
			}
		} catch (IOException e) {
			// TODO: handle exception
			try {
				if (client != null)
					client.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		} finally {
			System.setOut(ps);
			if (filestream != null)
				filestream.close();

		}
	}

	/**
	 * 评论数据解析
	 * 
	 * @param rawStr
	 * @return
	 */
	private static boolean parseCommet(String rawStr) {
		boolean more = false;
		try {
			JSONObject raw = new JSONObject(rawStr);
			JSONArray comments = raw.has("comments") ? raw.getJSONArray("comments") : null;
			int total = raw.has("total") ? raw.getInt("total") : 0;
			more = raw.has("more") ? raw.getBoolean("more") : false;
			if (comments != null && total != 0) {
				comments.forEach((comment) -> {

					String commentStr = !((JSONObject) comment).get("content").equals(null)
							? ((JSONObject) comment).getString("content")
							: "null";
					JSONObject user = ((JSONObject) comment).getJSONObject("user");
					int userId = user.getInt("userId");
					String nickname = user.getString("nickname");
					String avatarUrl = user.getString("avatarUrl");
					// System.out.printf("用户Id:%1$s 昵称：%2$s(头像URL:%3$s) %n\t评论：%4$s %n", userId,
					// nickname, avatarUrl,
					// commentStr);
					System.out.printf("%4$s %n", userId, nickname, avatarUrl, commentStr);
					JSONArray beReplied = ((JSONObject) comment).getJSONArray("beReplied");
					if (beReplied.length() > 0) {
//						System.out.println("\t被评论:");
						System.out.println("\t:");
						beReplied.forEach((people) -> {
							JSONObject peo = ((JSONObject) people).getJSONObject("user");
							int puserId = ((JSONObject) peo).getInt("userId");
							String pnickname = ((JSONObject) peo).getString("nickname");
							String pavatarUrl = ((JSONObject) peo).getString("avatarUrl");
							String str = !((JSONObject) people).get("content").equals(null)
									? ((JSONObject) people).getString("content")
									: "null";
							// System.out.printf("\t\t%4$s 用户Id:%1$s 昵称：%2$s(头像URL:%3$s) %n", puserId,
							// pnickname,
							// pavatarUrl, str);
							System.out.printf("\t\t%4$s%n", puserId, pnickname,
									pavatarUrl, str);
						});
					}
				});
			} else {
				System.out.println("\t\t:尚无评论！(" + rawStr + ")");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(rawStr);
			System.exit(0);
		}
		return more;
	}
}
