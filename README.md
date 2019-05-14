# Music163
## core.js的注入 参照[Chrome Extension注入](https://github.com/guanyuespace/ReplaceScripts)
## 网易云音乐API分析

### 主要文件：

#### 1 src/guanyue.util/ParametersUtil.java

完成数据的加密，构造参数 params 与 encSecKey

#### 2 src/guanyue.main/Crawler.java

对网易云音乐首页，歌单电台，专辑，单曲  分析

歌单：所包含歌曲 对歌单的品论

电台：评论

专辑：歌手，专辑音乐

亦可获取歌词，单曲的评论

#### 3 src/guanyue.main/Search.java

数据搜索

#### 4 src/guanyue.main/Login.java

模拟登录网易云音乐

获取个人歌单、、、
数据构造参照USER-API.md

#### 5 src/guanyue.main/Comments.java&src/guanyue.main/MusicComments.java

获取当前歌曲《起风了》评论，并利用分词工具包IK-Analyzer.jar和java词云包Kumo.jar，对获取的评论进行分析，画词云

```xml
<dependency>
    	<groupId>com.kennycason</groupId>
   	 <artifactId>kumo-core</artifactId>
    	<version>1.13</version>
</dependency>
<!-- 直接利用Kumo.jar产生中文分词需使用 1.12版本 -->
<dependency>
	<groupId>com.kennycason</groupId>
	<artifactId>kumo-tokenizers</artifactId>
	<version>1.12</version>
</dependency>

```

### 示例

#### 词云图展示
<table>
  <tr>
    <td><img src="src/res/Comments/Music/起风了-Whale.png?raw=true" width="500"/></td>
    <td><img src="src/res/Comments/Music/起风了-helloWhale.png?raw=true" width="500"/></td>
  </tr>
  <tr>
    <td><img src="src/res/Comments/Music/起风了-Rects.png?raw=true" width="500"/></td>
    <td><img src="src/res/Comments/Music/起风了-helloRect.png?raw=true" width="500"/></td>
  </tr>
</table>

#### 歌单电台评论
<table>
    <tr>
	<td><img src="src/res/Comments/PlayList/di.jpg?raw=true" width="500"/></td>
	<td><img/src="src/res/Comments/PlayList/disp.jpg?raw=true" width="500"></td>
    </tr>
    <tr>
	<td><img src="src/res/Comments/DJ/dis.jpg?raw=true" width="500"/></td>
	<td><img/src="src/res/Comments/DJ/disp.jpg?raw=true" width="500"></td>
    </tr>
</table>

#### 用户头像
<table>
  <tr>
    <td><img src="src/res/res/pics/display.jpg?raw=true" width="300"/></td>
    <td><img src="src/res/res/pics/f/528865135211884.jpg?raw=true" width="300"/></td>
    <td><img src="src/res/res/pics/f/528865135556168.jpg?raw=true" width="300"/></td>
  </tr>
  <tr>
    <td><img src="src/res/res/pics/f/528865157435538.jpg?raw=true" width="300"/></td>
    <td><img src="src/res/res/pics/f/1364493958734867.jpg?raw=true" width="300"/></td>
    <td><img src="src/res/res/pics/f/1371091001984865.jpg?raw=true" width="300"/></td>
  </tr>
</table>
