# 用户登录   
### `/guanyue.main/Login.java`  数据构造


```
# Login


POST http://music.163.com/weapi/login?csrf_token=

加密数据: d={"username":"**************@163.com","password":"**********************","rememberLogin":"true","checkToken":"9ca17ae2e6ffcda170e2e6ee98b54491908e8eed6995ebbb8ed247acad86bbc87ff596bcabdb6d98ecaa8fb82af0feaec3b92a91b287aac533a7ed8da8d13ba687bdaee94df79ee191c1398e91a6aec25d898dee9e","csrf_token":""}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud


# UserInfo


POST http://music.163.com/weapi/topic/user/info?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud

返回：{"status":2,"isQualified":2,"code":200}


POST http://music.163.com/weapi/reward/user/showicon?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud

返回：{"code":200,"showIcon":false}

POST http://music.163.com/weapi/event/user/permission?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud

返回：{"topEventPermission":false,"pubLongMsgEvent":false,"LongMsgNum":400,"pubEventWithPics":true,"pubEventWithoutResource":false,"pubEventWithPictureForbiddenNotice":"等级达到Lv.0，即可添加图片","eventVideoUploadNosType":1,"code":200}


# 首页


POST http://music.163.com/weapi/discovery/recommend/resource?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud


# 歌单


用户歌单列表
POST http://music.163.com/weapi/user/playlist?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"offset":"0","limit":"1001","uid":"275438773","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud


歌单详情

POST http://music.163.com/weapi/v3/playlist/detail?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"id":"378179227","offset":"0","total":"true","limit":"1000","n":"1000","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud


# 关注歌手

POST http://music.163.com/weapi/artist/sublist?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"offset ":" 0","limit":"1000","order":"true","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud

http://music.163.com/weapi/v3/playlist/detail?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"id":"378179227","offset":"0","total":"true","limit":"1000","n":"1000","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud





http://music.163.com/weapi/user/getfollows/275438773?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"offset ":" 0","limit":"1000","order":"true","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud




http://music.163.com/weapi/v1/resource/comments/A_PL_0_378179227?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"rid":"A_PL_0_378179227","offset":"0","total":"true","limit":"20","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud




http://music.163.com/weapi/artist/sublist?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"offset":"0","total":"true","limit":"25","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud





http://music.163.com/weapi/cloudvideo/allvideo/sublist?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"realTotal":"20","offset":"0","total":"true","limit":"20","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud


http://music.163.com/weapi/nmusician/userinfo/get?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud
{"code":200,"desc":"成功","result":{"userStatus":0}}




http://music.163.com/weapi/user/playlist?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"uid":"275438773","wordwrap":"7","offset":"0","total":"true","limit":"36","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud


http://music.163.com/weapi/v1/play/record?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"uid":"275438773","type":"-1","limit":"1000","offset":"0","total":"true","csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud



http://music.163.com/weapi/logout?csrf_token=1ecad5a58ef5b2faf61971f6b2185537

加密数据: d={"csrf_token":"1ecad5a58ef5b2faf61971f6b2185537"}	 e=010001	 f=00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7	 g=0CoJUm6Qyw8W8jud
{"code":200}


```

