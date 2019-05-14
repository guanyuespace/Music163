package guanyue.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guanyue.base.Author;
import guanyue.util.ParametersUtil;

/**
 * 不登陆OR运用Cookie爬取
 * 
 * @author Guan Yue
 * @time 2018年1月13日下午3:50:11
 *
 */
public class Crawler {
	private static String Cookie = "";

	private static CloseableHttpClient client = null;

	public static void main(String[] args) {
		try {
			if (args.length > 1) {
				Cookie = args[0];
				System.out.println(Cookie);
			}
			RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000)
					.setCookieSpec(CookieSpecs.DEFAULT).build();
			client = HttpClients.custom().setDefaultRequestConfig(config).setUserAgent(
					"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
					.build();

			String url = "http://music.163.com/";
			startSomethingHere(url);

			String id = "970088217";
			getPlayListDetatilsByID(id);
			getPlaylistComments("诡异如此，更与何人说！", id, 0);

			id = "409650851";
			getMusicByID(id);
			getMusicCommentsById("霜雪千年", id, 0);
		} finally {
			try {
				if (client != null) {
					client.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

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
			String res = EntityUtils.toString(response.getEntity());
			discover.abort();

			// 歌单电台
			Elements playlists = Jsoup.parse(res).body().getElementById("discover-module").select("div.g-mn1").first()
					.select("div.g-mn1c").first().select("div.g-wrap3").first().select("div.n-rcmd").first()
					.select("ul.m-cvrlst.f-cb").first().getElementsByTag("li");
			playlists.stream().forEach((playlist) -> {
				Element play = playlist.select("p.dec").first().select("a").first();
				String id = play.hasAttr("data-res-id") ? play.attr("data-res-id") : null;
				String des = play.hasAttr("title") ? play.attr("title") : null;
				String type = play.hasAttr("data-res-type") ? play.attr("data-res-type") : null;
				if (id != null && des != null && type != null) {
					if (type.equals("13")) {
						System.out.printf("歌单id:%2$s  \t歌单：%1$s %n", des, id);
						// ? * : " < > \ / |
						des = des.replaceAll("\\?|\\*|\\:|\"|\\<|\\>|\\\\|\\/|\\|| ", "");
						getPlayListDetatilsByID(id);
						getPlaylistComments(des, id, 0);
					} else {
						System.out.printf("电台id:%2$s  \t电台：%1$s %n", des, id);
						des = des.replaceAll("\\?|\\*|\\:|\"|\\<|\\>|\\\\|\\/|\\|| ", "");
						getDJCommentsById(des, id, 0);
					}
				}
			});

			// 专辑
			Elements albums = Jsoup.parse(res).body().getElementById("discover-module").getElementById("album-roller")
					.select("div.roll.f-pr").first().select("ul.f-cb.roller-flag").first().getElementsByTag("li");
			albums.stream().forEach((album) -> {
				String albumid = album.select("div.u-cover.u-cover-alb1").first().select("a.icon-play.f-alpha.f-fr")
						.first().hasAttr("data-res-id")
								? album.select("div.u-cover.u-cover-alb1").first().select("a.icon-play.f-alpha.f-fr")
										.first().attr("data-res-id")
								: null;
				String name = album.select("div.u-cover.u-cover-alb1").first().select("a.msk").first().hasAttr("title")
						? album.select("div.u-cover.u-cover-alb1").first().select("a.msk").first().attr("title")
						: null;

				Element author = album.select("p.tit.f-thide").first().select("a").first();
				String authorId = author.hasAttr("href") ? author.attr("href").split("=")[1] : null;
				String authorName = author.hasText() ? author.text() : null;
				if (albumid != null && name != null && authorName != null && authorId != null) {
					System.out.printf("专辑id:%2$s  \t专辑：%1$s ", name, albumid);
					System.out.printf("\t作者Id:%2$s  \t作者: %1$s %n", authorName, authorId);
					getAlbumById(albumid.trim());
				}

				if (authorName != null && authorId != null) {
					System.out.printf("\t作者Id:%2$s \t作者: %1$s %n", authorName, authorId);
					getAuthorById(authorId);
				}
			});

			// 榜单
			Element n_bilst = Jsoup.parse(res, "http://music.163.com/").body().getElementById("discover-module")
					.getElementById("top-flag");
			Elements biaoshenglist = n_bilst.children().first().select("dd").first().select("ol").first().select("li");
			Elements xingelist = n_bilst.children().get(1).select("dd").first().select("ol").first().select("li");
			Elements yuanchuanglist = n_bilst.children().last().select("dd").first().select("ol").first().select("li");

			Element desc = n_bilst.children().first().select("a.msk").first();
			String href = desc.hasAttr("href") ? desc.attr("abs:href") : null;
			String name = desc.hasAttr("title") ? desc.attr("title") : null;
			if (href != null) {
				System.out.println(href + "  " + name);
				biaoshenglist.stream().forEach((ele) -> {
					Element pp = ele.select("a.nm.s-fc0.f-thide").first();
					String musicName = pp.hasAttr("title") ? pp.attr("title") : null;
					String musicId = pp.hasAttr("href") ? pp.attr("href").split("=")[1] : null;
					if (musicName != null) {
						System.out.printf("\t歌曲id:%1$s  \t歌曲：%2$s %n", musicId, musicName);
					}
				});
			}

			desc = n_bilst.children().get(1).select("a.msk").first();
			href = desc.hasAttr("href") ? desc.attr("abs:href") : null;
			name = desc.hasAttr("title") ? desc.attr("title") : null;
			if (href != null) {
				System.out.println(href + "  " + name);
				xingelist.stream().forEach((ele) -> {
					Element pp = ele.select("a.nm.s-fc0.f-thide").first();
					String musicName = pp.hasAttr("title") ? pp.attr("title") : null;
					String musicId = pp.hasAttr("href") ? pp.attr("href").split("=")[1] : null;
					if (musicName != null) {
						System.out.printf("\t歌曲id:%1$s  \t歌曲：%2$s %n", musicId, musicName);
					}
				});
			}

			desc = n_bilst.children().last().select("a.msk").first();
			href = desc.hasAttr("href") ? desc.attr("abs:href") : null;
			name = desc.hasAttr("title") ? desc.attr("title") : null;
			if (href != null) {
				System.out.println(href + "  " + name);
				yuanchuanglist.stream().forEach((ele) -> {
					Element pp = ele.select("a.nm.s-fc0.f-thide").first();
					String musicName = pp.hasAttr("title") ? pp.attr("title") : null;
					String musicId = pp.hasAttr("href") ? pp.attr("href").split("=")[1] : null;
					if (musicName != null) {
						System.out.printf("\t歌曲id:%1$s  \t歌曲：%2$s %n", musicId, musicName);
					}
				});
			}
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
	 * 获取作者信息
	 * 
	 * @param authorId
	 */
	private static void getAuthorById(String authorId) {
		// TODO Auto-generated method stub
		try {
			// Request URL:https://music.163.com/artist?id=12378070
			// Request Method:GET

			HttpGet artist = new HttpGet("https://music.163.com/artist?id=" + authorId.trim());
			HttpResponse response = client.execute(artist);
			String raw = EntityUtils.toString(response.getEntity());
			artist.abort();

			String name = Jsoup.parse(raw).body().getElementById("artist-name").hasAttr("title")
					? Jsoup.parse(raw).body().getElementById("artist-name").attr("title")
					: null;
			Elements musics = Jsoup.parse(raw, "https://music.163.com/").body().getElementById("song-list-pre-cache")
					.select("ul.f-hide").first().select("li");

			// Request URL:http://music.163.com/artist/desc?id=12378070
			// Request Method:GET
			HttpGet ar = new HttpGet("http://music.163.com/artist/desc?id=" + authorId.trim());
			response = client.execute(ar);
			raw = EntityUtils.toString(response.getEntity());
			ar.abort();

			Element text = Jsoup.parse(raw).body().select("div.g-bd4.f-cb").first().select("div.g-mn4").first()
					.select("div.g-mn4c").first().select("div.g-wrap6").first().select("div.n-artdesc").first()
					.select("p").first();
			String simple = text.hasText() ? text.text() : "null";
			System.out.printf("歌手：%1$s  %n\t简介：%2$s %n", name, simple);

			System.out.println(name + "的歌曲推荐:");
			musics.stream().forEach((music) -> {
				String musicName = music.child(0).hasText() ? music.child(0).text() : null;
				String musicHref = music.child(0).hasAttr("href") ? music.child(0).attr("abs:href") : null;
				System.out.printf("\t链接：%2$s   \t歌曲：%1$s %n", musicName, musicHref);
			});

		} catch (Exception e) {
			// TODO: handle exception
			try {
				if (client != null) {
					client.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		} finally {

		}
	}

	/**
	 * 获取专辑信息
	 * 
	 * @param albumid
	 *            专辑id
	 */
	private static void getAlbumById(String albumid) {
		// TODO Auto-generated method stub
		try {
			// Request URL:http://music.163.com/weapi/v1/album/37179075?csrf_token=
			// Request Method:POST
			HttpPost album = new HttpPost("https://music.163.com/weapi/v1/album/" + albumid + "?csrf_token=");
			album.addHeader("Content-Type", "application/x-www-form-urlencoded");
			album.addHeader("Origin", "http://music.163.com");
			album.addHeader("Referer", "http://music.163.com/discover");
			String param1 = "{\"id\":\"" + albumid + "\",\"csrf_token\":\"\"}", param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";

			List<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
			nvPairs.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvPairs.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			album.setEntity(new UrlEncodedFormEntity(nvPairs, "utf-8"));

			HttpResponse res = client.execute(album);
			String resr = EntityUtils.toString(res.getEntity());
			JSONObject raw = new JSONObject(resr);
			album.abort();

			JSONObject alb = raw.has("album") ? raw.getJSONObject("album") : null;
			if (alb != null) {
				int copyrightId = alb.getInt("copyrightId");
				String picUrl = alb.getString("picUrl");
				String company = alb.getString("company");
				String description = alb.getString("description");
				String name = alb.getString("name");
				System.out.printf("版权Id:%1$s  专辑名：%4$s  公司：%2$s  封面url：%5$s  %n简介：%3$s   %n", copyrightId, company,
						description, name, picUrl);
				JSONArray artists = alb.getJSONArray("artists");
				artists.forEach((ar) -> {
					String arpicUrl = ((JSONObject) ar).getString("picUrl");
					String img1v1Url = ((JSONObject) ar).getString("img1v1Url");
					String arname = ((JSONObject) ar).getString("name");
					int arid = ((JSONObject) ar).getInt("id");
					System.out.printf("\t\t 作者id: %2$s  作者： %1$s  头像：%3$s(%4$s) %n", arname, arid, arpicUrl, img1v1Url);
				});
			}
			JSONArray songs = raw.has("songs") ? raw.getJSONArray("songs") : null;
			if (songs != null)
				songs.forEach((song) -> {
					String songname = ((JSONObject) song).getString("name");
					long songid = ((JSONObject) song).getLong("id");
					System.out.printf("\t\t  歌曲id:%2$s   歌曲：%1$s  %n", songname, songid);
				});
			System.out.println();
			System.out.println();
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
	 * 获取歌单信息
	 * 
	 * @param listid
	 *            歌单id
	 */
	private static void getPlayListDetatilsByID(String listid) {
		// TODO Auto-generated method stub
		try {
			HttpPost playlistDetails = new HttpPost("https://music.163.com/weapi/v3/playlist/detail?csrf_token=");
			playlistDetails.addHeader("Content-Type", "application/x-www-form-urlencoded");

			String param1 = "{\"id\":\"" + listid
					+ "\",\"offset\":\"0\",\"total\":\"true\",\"limit\":\"1000\",\"n\":\"1000\",\"csrf_token\":\"\"}",
					param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";

			List<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
			nvPairs.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvPairs.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			playlistDetails.setEntity(new UrlEncodedFormEntity(nvPairs, "utf-8"));
			HttpResponse res = client.execute(playlistDetails);
			String rawStr = EntityUtils.toString(res.getEntity());
			playlistDetails.abort();

			parsePlayList(rawStr);
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
	 * 获取音频url，歌词
	 * 
	 * @param musicid
	 *            歌曲id
	 */
	private static void getMusicByID(String musicid) {
		// TODO Auto-generated method stub
		try {
			HttpPost musicGet = new HttpPost("https://music.163.com/weapi/song/enhance/player/url?csrf_token=");
			musicGet.addHeader("Content-Type", "application/x-www-form-urlencoded");

			String param1 = "{\"ids\":\"[" + musicid + "]\",\"br\":128000,\"csrf_token\":\"\"}", // bab30657eb19cbe86127fc5394dffe79
					param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			musicGet.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			HttpResponse res = client.execute(musicGet);
			JSONObject musicDetails = new JSONObject(EntityUtils.toString(res.getEntity())).getJSONArray("data")
					.getJSONObject(0);
			musicGet.abort();

			int id = musicDetails.getInt("id");
			String musicurl = musicDetails.getString("url");
			String type = musicDetails.getString("type");
			System.out.printf("id:%3$d  type:%2$s  url:%1$s %n", musicurl, type, id);

			HttpPost lyric = new HttpPost("http://music.163.com/weapi/song/lyric?csrf_token=");
			lyric.addHeader("Content-Type", "application/x-www-form-urlencoded");

			param1 = "{\"id\":" + musicid + ",\"lv\":-1,\"tv\":-1,\"csrf_token\":\"\"}";
			nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			lyric.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			res = client.execute(lyric);
			JSONObject raw = new JSONObject(EntityUtils.toString(res.getEntity()));
			lyric.abort();
			JSONObject lyuser = raw.has("lyricUser") ? raw.getJSONObject("lyricUser") : null;
			JSONObject lrc = raw.has("lrc") ? raw.getJSONObject("lrc") : null;

			if (lyuser != null && lrc != null) {
				String lyname = lyuser.has("nickname") ? lyuser.getString("nickname") : null;
				long lyid = lyuser.has("id") ? lyuser.getLong("id") : 0;
				long lyuserid = lyuser.has("userid") ? lyuser.getLong("userid") : 0;

				String lymusic = lrc.has("lyric") ? lrc.getString("lyric") : null;
				if (lyname != null && lymusic != null) {
					System.out.printf("歌词提供者：%1$s(%3$s)  歌曲id:%2$s%n歌词：%4$s%n", lyname, lyid, lyuserid, lymusic);
				}

			} else {
				System.out.println("尚无歌词！");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				if (client != null)
					client.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		} finally {
			// TODO: handle finally clause
		}
	}

	/**
	 * 获取电台评论
	 * 
	 * @param id
	 *            电台id
	 * @param offset
	 *            偏移
	 */
	private static void getDJCommentsById(String desc, String id, int offset) {
		// TODO Auto-generated method stub
		PrintStream filestream = null;
		PrintStream ps = System.out;
		try {
			File dir = new File("src/res/Comments/DJ");
			if (!dir.exists())
				dir.mkdirs();
			File file = new File("src/res/Comments/DJ" + "/" + desc + ".txt");
			if (!file.exists())
				file.createNewFile();
			filestream = new PrintStream(new FileOutputStream(file, true));
			System.setOut(filestream);

			HttpPost dj = new HttpPost("http://music.163.com/weapi/v1/resource/comments/A_DJ_1_" + id + "?csrf_token=");
			dj.addHeader("Content-Type", "application/x-www-form-urlencoded");

			String param1 = "{\"rid\":\"A_DJ_1_" + id + "\",\"offset\":\"" + offset
					+ "\",\"total\":\"false\",\"limit\":\"20\",\"csrf_token\":\"\"}", param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";
			List<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
			nvPairs.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvPairs.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			dj.setEntity(new UrlEncodedFormEntity(nvPairs, "utf-8"));

			HttpResponse res = client.execute(dj);
			String rawStr = EntityUtils.toString(res.getEntity());
			dj.abort();
			if (parseCommet(rawStr)) {
				offset += 20;
				getDJCommentsById(desc, id, offset);
			}
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
			System.setOut(ps);
			if (filestream != null)
				filestream.close();
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
	 * 获取歌单评论
	 * 
	 * @param id
	 *            歌单id
	 * @param offset
	 *            偏移量
	 */
	private static void getPlaylistComments(String desc, String id, int offset) {
		PrintStream filestream = null;
		PrintStream ps = System.out;
		try {
			File dir = new File("src/res/Comments/PlayList");
			if (!dir.exists())
				dir.mkdirs();
			File file = new File("src/res/Comments/PlayList" + "/" + desc + ".txt");
			if (!file.exists())
				file.createNewFile();
			filestream = new PrintStream(new FileOutputStream(file, true));
			System.setOut(filestream);

			HttpPost playlist = new HttpPost(
					"https://music.163.com/weapi/v1/resource/comments/A_PL_0_" + id + "?csrf_token=");
			playlist.addHeader("Content-Type", "application/x-www-form-urlencoded");

			String param1 = "{\"rid\":\"A_PL_0_" + id + "\",\"offset\":\"" + offset
					+ "\",\"total\":\"false\",\"limit\":\"20\",\"csrf_token\":\"\"}", param2 = "010001",
					param3 = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7",
					param4 = "0CoJUm6Qyw8W8jud";

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("params", ParametersUtil.getParams(param1, param4)));
			nvps.add(new BasicNameValuePair("encSecKey", ParametersUtil.getEncSecKey(param2, param3)));
			playlist.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			HttpResponse response = client.execute(playlist);
			String rawStr = EntityUtils.toString(response.getEntity());
			playlist.abort();

			if (parseCommet(rawStr)) {
				offset += 20;
				getPlaylistComments(desc, id, offset);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				if (client != null) {
					client.close();
				}
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
					System.out.printf("用户Id:%1$s  昵称：%2$s(头像URL:%3$s) %n\t评论：%4$s %n", userId, nickname, avatarUrl,
							commentStr);
					JSONArray beReplied = ((JSONObject) comment).getJSONArray("beReplied");
					if (beReplied.length() > 0) {
						System.out.println("\t被评论:");
						beReplied.forEach((people) -> {
							JSONObject peo = ((JSONObject) people).getJSONObject("user");
							int puserId = ((JSONObject) peo).getInt("userId");
							String pnickname = ((JSONObject) peo).getString("nickname");
							String pavatarUrl = ((JSONObject) peo).getString("avatarUrl");
							String str = !((JSONObject) people).get("content").equals(null)
									? ((JSONObject) people).getString("content")
									: "null";
							System.out.printf("\t\t%4$s   用户Id:%1$s  昵称：%2$s(头像URL:%3$s) %n", puserId, pnickname,
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

	/**
	 * 
	 */
	private static void parsePlayList(String rawStr) {
		try {
			JSONObject raw = new JSONObject(rawStr);
			if (raw.has("playlist")) {
				JSONObject playlist = raw.getJSONObject("playlist");
				String name = playlist.getString("name");
				String description = !playlist.get("description").equals(null) ? playlist.getString("description")
						: "null";
				String coverImgUrl = playlist.getString("coverImgUrl");
				int id = playlist.getInt("id");

				JSONObject creator = playlist.getJSONObject("creator");
				String avatarUrl = creator.getString("avatarUrl");
				int userId = creator.getInt("userId");
				String nickname = creator.getString("nickname");
				String signature = creator.getString("signature");
				String backgroundUrl = creator.getString("backgroundUrl");

				System.out.printf(
						"歌单id:%2$s  歌单：%1$s  描述: %3$s  图片：%4$s  %n\t(所属用户 Id: %5$s  用户名：  %6$s  个性签名: %7$s  pic: %8$s  pic: %9$s)%n",
						name, id, description, coverImgUrl, userId, nickname, signature, avatarUrl, backgroundUrl);
				JSONArray tracks = playlist.getJSONArray("tracks");
				for (int i = 0; i < tracks.length(); i++) {
					JSONObject track = tracks.getJSONObject(i);
					name = track.getString("name");
					id = track.getInt("id");
					System.out.printf("\t歌曲id: %2$s     \t歌曲：%1$s  ", name, id);
					// getMusicByID("https://music.163.com/weapi/song/enhance/player/url?csrf_token=",
					// Integer.toString(id));
					JSONArray ar = track.getJSONArray("ar");
					Author[] authors = new Author[ar.length()];
					for (int j = 0; j < ar.length(); j++) {
						name = ar.getJSONObject(j).getString("name");
						id = ar.getJSONObject(j).getInt("id");
						authors[j] = new Author(name, id);
					}
					Arrays.asList(authors).stream().forEach((a) -> {
						System.out.printf("\t歌手id: %2$s  \t歌手：%1$s  ", a.getName(), a.getId());
					});
					System.out.println();
				}
			} else {
				System.out.println("歌单:" + raw);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(rawStr);
			System.exit(0);
		} finally {

		}
	}
}
