package guanyue.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.palette.ColorPalette;

/**
 * @author Guan Yue
 * @time 2018年4月17日上午11:35:16
 *
 */
public class MusicComments {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String file = "src/res/Comments/Music/起风了.txt";
		List<WordFrequency> wordFrequencies = wordFreq(file);

		final Dimension dimension = new Dimension(990, 618);
		WordCloud cloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		cloud.setKumoFont(new KumoFont(new Font("华文新魏", Font.BOLD, 16)));
		cloud.setAngleGenerator(new AngleGenerator(0));// 不偏转
		cloud.setPadding(2);
		// cloud.setBackground(new RectangleBackground(dimension));
		cloud.setBackground(new PixelBoundryBackground("backgrounds/whale.png"));
		cloud.setColorPalette(new ColorPalette(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW));
		cloud.setFontScalar(new LinearFontScalar(10, 40));
		cloud.build(wordFrequencies);
		cloud.writeToFile("src/res/Comments/Music/起风了-Whale.png");
	}

	private static List<WordFrequency> wordFreq(String file) {
		// TODO Auto-generated method stub
		Map<String, Integer> words = new HashMap<String, Integer>();
		List<Map.Entry<String, Integer>> sortWords = null;
		IKSegmenter ikSegmenter = null;
		try {
			ikSegmenter = new IKSegmenter(new InputStreamReader(Files.newInputStream(Paths.get(file)), "utf-8"), true);
			Lexeme lexeme = null;
			while ((lexeme = ikSegmenter.next()) != null) {
				String con = lexeme.getLexemeText();
				if (con.length() >= 2)//过滤掉仅有一个字的字符串
					if (words.containsKey(con)) {
						words.put(con, words.get(con) + 1);
					} else {
						words.put(con, 1);
					}
			}
			sortWords = new ArrayList<Map.Entry<String, Integer>>(words.entrySet());
			Collections.sort(sortWords, new Comparator<Map.Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					// TODO Auto-generated method stub
					return o2.getValue() - o1.getValue();
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
		}
		return convertList(sortWords);

	}

	private static List<WordFrequency> convertList(List<Entry<String, Integer>> sortWords) {
		// TODO Auto-generated method stub
		List<WordFrequency> res = new LinkedList<WordFrequency>();
		for (Entry<String, Integer> entry : sortWords) {
			res.add(new WordFrequency(entry.getKey(), entry.getValue()));
		}
		return res.subList(0, 800);// .subList(0, 1000);
	}
}
