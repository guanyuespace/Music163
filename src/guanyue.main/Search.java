package guanyue.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

/**
 * @author Guan Yue
 * @time 2018年1月14日下午4:10:02
 *
 */
public class Search {
	private static CloseableHttpClient client = null;

	// Content-Type:application/x-www-form-urlencoded
	// Origin:https://music.163.com
	// Referer:https://music.163.com/search/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000)
					.setCookieSpec(CookieSpecs.DEFAULT).build();
			client = HttpClients.custom().setDefaultRequestConfig(config).setUserAgent(
					"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
					.build();

			String url = "http://music.163.com/";
			startSomethingHere(url);

			String name = "hello";
			searchUserByName(name);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

		}
	}

	private static void startSomethingHere(String url) {
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

			// Request URL:https://music.163.com/search/
			// Request Method:GET
			HttpGet search = new HttpGet("https://music.163.com/search/");
			response = client.execute(search);
			System.out.printf("status:%2$s  url:%1$s %n", "https://music.163.com/search/", response.getStatusLine());
			search.abort();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				if (client != null)
					client.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		} finally {

		}

	}

	private static void searchUserByName(String name) {
		// TODO Auto-generated method stub
		try {
			String res = null, param1 = null, param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";
			// Request URL:https://music.163.com/weapi/search/suggest/web?csrf_token=
			// Request Method:POST
			for (int i = 1; i <= name.length(); i++) {
				System.out.println(
						"*********************************" + i + "*******************************************");
				HttpPost suggest = new HttpPost("https://music.163.com/weapi/search/suggest/web?csrf_token=");
				suggest.addHeader("Content-Type", "application/x-www-form-urlencoded");
				suggest.addHeader("Origin", "https://music.163.com");
				suggest.addHeader("Referer", "https://music.163.com/search/");
				param1 = "{\"s\":\"" + name.substring(0, i) + "\",\"limit\":\"8\",\"csrf_token\":\"\"}";

				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
				nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
				suggest.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
				HttpResponse response = client.execute(suggest);
				res = EntityUtils.toString(response.getEntity());
				suggest.abort();
				parseMultiResult(res);
			}

			getUserProfile(name, 0);

			// Request URL:https://music.163.com/weapi/search/suggest/multimatch?csrf_token=
			// Request Method:POST

			HttpPost querry = new HttpPost("https://music.163.com/weapi/search/suggest/multimatch?csrf_token=");
			querry.addHeader("Content-Type", "application/x-www-form-urlencoded");
			querry.addHeader("Origin", "https://music.163.com");
			querry.addHeader("Referer", "https://music.163.com/search/");
			param1 = "{\"s\":\"" + name + "\",\"csrf_token\":\"\"}";

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			querry.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			HttpResponse response = client.execute(querry);
			res = EntityUtils.toString(response.getEntity());
			querry.abort();
			System.out.println(
					"***************************************multimatch********************************************");
			// System.out.println(res);
			parseMultiResult(res);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				if (client != null)
					client.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		} finally {

		}
	}

	private static void getUserProfile(String name, int offset) {
		// TODO Auto-generated method stub

		try {
			String res = null, param1 = null, param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";

			// Request URL:https://music.163.com/weapi/cloudsearch/get/web?csrf_token=
			// Request Method:POST
			HttpPost searchByName = new HttpPost("https://music.163.com/weapi/cloudsearch/get/web?csrf_token=");
			searchByName.addHeader("Content-Type", "application/x-www-form-urlencoded");
			searchByName.addHeader("Origin", "https://music.163.com");
			searchByName.addHeader("Referer", "https://music.163.com/search/");
			param1 = "{\"hlpretag\":\"<span class=\\\"s-fc7\\\">\",\"hlposttag\":\"</span>\",\"s\":\"" + name
					+ "\",\"type\":\"1002\",\"offset\":\"" + offset
					+ "\",\"total\":\"true\",\"limit\":\"30\",\"csrf_token\":\"\"}";

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			searchByName.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			HttpResponse response = client.execute(searchByName);
			res = EntityUtils.toString(response.getEntity());
			searchByName.abort();
			// System.out.println(res);
			parseResult(res);
			if (offset <= 900) {
				offset += 30;
				getUserProfile(name, offset);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}

	}

	private static void parseResult(String res) {
		// TODO Auto-generated method stub
		PrintStream filestream = null;
		PrintStream ps = System.out;
		try {
			File dir = new File("src/res/logs/Search");
			if (!dir.exists())
				dir.mkdirs();
			File file = new File("src/res/logs/Search" + "/" + "UserProfile.txt");
			if (!file.exists())
				file.createNewFile();
			filestream = new PrintStream(new FileOutputStream(file, true));
			System.setOut(filestream);

			JSONObject result = new JSONObject(res).has("result") ? new JSONObject(res).getJSONObject("result") : null;
			if (result != null) {
				JSONArray userprofiles = result.has("userprofiles") ? result.getJSONArray("userprofiles") : null;
				if (!userprofiles.equals(null)) {
					userprofiles.forEach((user) -> {
						System.out.println("UserProfile:");
						String avatarUrl = ((JSONObject) user).getString("avatarUrl");
						String gender = ((JSONObject) user).getInt("gender") == 1 ? "男" : "女";// 男 1 女 2
						long userId = ((JSONObject) user).getLong("userId");
						String nickname = ((JSONObject) user).getString("nickname");
						String signature = ((JSONObject) user).getString("signature");
						String desc = ((JSONObject) user).getString("description");
						String detailDesc = ((JSONObject) user).getString("detailDescription");
						String backgroundUrl = ((JSONObject) user).getString("backgroundUrl");
						System.out.printf(
								"\t用户:%1$s(%2$d)  性别:%4$s  头像Url:%8$s (%3$s)%n个性签名：%5$s%n简介：%6$s%n详细介绍：%7$s%n",
								nickname, userId, avatarUrl, gender, signature, desc, detailDesc, backgroundUrl);
						if (!avatarUrl.equals("") && gender.equals("男")) {
							new Thread(new PicHandler(avatarUrl, true)).start();
						} else if (!avatarUrl.equals("") && gender.equals("女")) {
							new Thread(new PicHandler(avatarUrl, false)).start();
						}
					});
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(res);
			System.exit(0);
		} finally {
			System.setOut(ps);
			if (filestream != null)
				filestream.close();

		}

	}

	private static void parseMultiResult(String res) {
		// TODO Auto-generated method stub
		try {
			JSONObject raw = new JSONObject(res).has("result") ? new JSONObject(res).getJSONObject("result") : null;
			if (!raw.equals(null)) {
				JSONArray orders = raw.has("order") ? !raw.get("order").equals(null) ? raw.getJSONArray("order") : null
						: !raw.get("orders").equals(null) ? raw.getJSONArray("orders") : null;
				if (orders != null) {
					orders.forEach((order) -> {
						if (((String) order).equals("artists") || ((String) order).equals("artist")) {
							JSONArray artists = raw.has("artists") ? raw.getJSONArray("artists")
									: raw.getJSONArray("artist");
							artists.forEach((ar) -> {
								System.out.println("ARTIST:");
								String name = ((JSONObject) ar).getString("name");
								long id = ((JSONObject) ar).getLong("id");
								String picUrl = !((JSONObject) ar).get("picUrl").equals(null)
										? ((JSONObject) ar).getString("picUrl")
										: null;
								String img1v1Url = ((JSONObject) ar).getString("img1v1Url");

								System.out.printf("\t歌手：%1$s(%2$d)  头像：%3$s(%4$s) %n", name, id, picUrl, img1v1Url);
							});
						} else if (((String) order).equals("albums") || ((String) order).equals("album")) {// 专辑
							System.out.println("ALBUM:");
							JSONArray albums = raw.has("albums") ? raw.getJSONArray("albums")
									: raw.getJSONArray("album");
							albums.forEach((alb) -> {
								String name = ((JSONObject) alb).getString("name");
								long id = ((JSONObject) alb).getLong("id");
								JSONObject ar = ((JSONObject) alb).getJSONObject("artist");
								String arname = ((JSONObject) ar).getString("name");
								long arid = ((JSONObject) ar).getLong("id");
								String arpicUrl = !((JSONObject) ar).get("picUrl").equals(null)
										? ((JSONObject) ar).getString("picUrl")
										: null;
								String arimg1v1Url = ((JSONObject) ar).getString("img1v1Url");
								System.out.printf("\t专辑：%1$s(%2$d)   歌手：%3$s(%4$d)  头像：%5$s(%6$s) %n", name, id, arname,
										arid, arpicUrl, arimg1v1Url);
							});
						} else if (((String) order).equals("songs") || ((String) order).equals("song")) {// 歌曲
							System.out.println("SONGS:");
							JSONArray songs = raw.has("songs") ? raw.getJSONArray("songs") : raw.getJSONArray("song");
							songs.forEach((song) -> {
								long id = ((JSONObject) song).getLong("id");
								String name = ((JSONObject) song).getString("name");
								long copyrightId = ((JSONObject) song).getLong("copyrightId");
								// JSONObject album = ((JSONObject) song).getJSONObject("album");
								JSONArray artists = ((JSONObject) song).getJSONArray("artists");
								System.out.printf("\t歌曲：%2$s(%1$d)   版权：%3$d%n", id, name, copyrightId);
								artists.forEach((ar) -> {
									String arname = ((JSONObject) ar).getString("name");
									long arid = ((JSONObject) ar).getLong("id");
									String arpicUrl = !((JSONObject) ar).get("picUrl").equals(null)
											? ((JSONObject) ar).getString("picUrl")
											: null;
									String arimg1v1Url = ((JSONObject) ar).getString("img1v1Url");

									System.out.printf("\t\t歌手：%1$s(%2$d)  头像：%3$s(%4$s) %n", arname, arid, arpicUrl,
											arimg1v1Url);
								});
							});

						} else if (((String) order).equals("playlists") || ((String) order).equals("playlist")) {
							System.out.println("PLAYLISTS:");
							JSONArray playlists = raw.has("playlists") ? raw.getJSONArray("playlists")
									: raw.getJSONArray("playlist");
							playlists.forEach((playlist) -> {
								long id = ((JSONObject) playlist).getLong("id");
								String name = ((JSONObject) playlist).getString("name");
								String coverImgUrl = ((JSONObject) playlist).getString("coverImgUrl");
								int trackCount = ((JSONObject) playlist).getInt("trackCount");
								long userId = ((JSONObject) playlist).getLong("userId");
								long playCount = ((JSONObject) playlist).getLong("playCount");
								int bookCount = ((JSONObject) playlist).getInt("bookCount");
								System.out.printf(
										"\t歌单：%1$s(%2$d)   创建用户id：%5$d   歌曲数目：%4$d   播放次数：%6$d   %n\t订阅量：%7$d  封面：%3$s%n",
										name, id, coverImgUrl, trackCount, userId, playCount, bookCount);
							});
						} else if (((String) order).equals("mvs") || ((String) order).equals("mv")) {
							System.out.println("MVS:");
							JSONArray mvs = raw.has("mvs") ? raw.getJSONArray("mvs") : raw.getJSONArray("mv");
							mvs.forEach((mv) -> {
								int id = ((JSONObject) mv).getInt("id");
								String cover = ((JSONObject) mv).getString("cover");
								String name = ((JSONObject) mv).getString("name");
								int playCount = ((JSONObject) mv).getInt("playCount");
								String briefDesc = !((JSONObject) mv).get("briefDesc").equals(null)
										? ((JSONObject) mv).getString("briefDesc")
										: null;
								String desc = !((JSONObject) mv).get("desc").equals(null)
										? ((JSONObject) mv).getString("desc")
										: null;
								String artistName = ((JSONObject) mv).getString("artistName");
								int artistId = ((JSONObject) mv).getInt("artistId");
								System.out.printf(
										"\tmv:%3$s(%1$d)   Artist:%7$s(%8$d)   播放次数：%4$d  %n\t封面%2$s%n\t简介：%5$s%n\t详细介绍：%6$s%n",
										id, cover, name, playCount, briefDesc, desc, artistName, artistId);
							});

						} else if (((String) order).equals("videos") || ((String) order).equals("video")) {
							System.out.println("VIDEOS:");
							JSONArray videos = raw.has("videos") ? raw.getJSONArray("videos")
									: raw.getJSONArray("video");
							videos.forEach((video) -> {
								String vid = ((JSONObject) video).getString("vid");
								String coverUrl = ((JSONObject) video).getString("coverUrl");
								String title = ((JSONObject) video).getString("title");
								JSONArray creators = ((JSONObject) video).getJSONArray("creator");
								System.out.printf("\tvideo:%2$s(%3$s)    封面：%1$s", coverUrl, title, vid);
								creators.forEach((creator) -> {
									long userId = ((JSONObject) creator).getLong("userId");
									String userName = ((JSONObject) creator).getString("userName");
									System.out.printf("   %2$s(%1$s)", userId, userName);
								});
								System.out.println();
							});

						} else {
							System.out.println(
									"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
											+ order);

						}
					});
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			if (e.getMessage().equals("Duplicate key \"alias\" at 4664 [character 4665 line 1]"))
				;
			else
				System.exit(0);
		} finally {
		}
	}
}

class PicHandler implements Runnable {
	private String url = null;
	private boolean male = false;

	public PicHandler(String url, boolean male) {
		super();
		this.url = url;
		this.male = male;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		HttpURLConnection hcon = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File file = null;
		try {
			String fileName = url.split("\\/")[url.split("\\/").length - 1];
			File dir = new File("src/res/pics/m");
			if (!dir.exists())
				dir.mkdirs();

			dir = new File("src/res/pics/f");
			if (!dir.exists())
				dir.mkdirs();

			if (male)
				file = new File("src/res/pics/m" + "/" + fileName);
			else
				file = new File("src/res/pics/f" + "/" + fileName);
			if (!file.exists()) {
				hcon = (HttpURLConnection) new URL(url).openConnection();
				hcon.setConnectTimeout(5000);
				hcon.setRequestMethod("GET");
				hcon.addRequestProperty("Origin", "https://music.163.com");
				hcon.addRequestProperty("Referer", "https://music.163.com/search/");
				hcon.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
				hcon.connect();

				file.createNewFile();
				bis = new BufferedInputStream(hcon.getInputStream(), 1024000);
				bos = new BufferedOutputStream(new FileOutputStream(file), 1024000);
				byte[] bs = new byte[1024000];
				int size = 0;
				while ((size = bis.read(bs)) != -1) {
					bos.write(bs, 0, size);
					bos.flush();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (file.exists())
				file.delete();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
				if (hcon != null)
					hcon.disconnect();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

}
