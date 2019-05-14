(function(){
	var c4g=NEJ.P,em6g=c4g("nej.g"),v4z=c4g("nej.j"),k4o=c4g("nej.u"),Tg9X=c4g("nm.x.ek"),l4p=c4g("nm.x");
	if(v4z.bn5s.redefine)return;
	window.GEnc=true;
	var buv7o=function(coa8S){var o4s=[];k4o.bd4h(coa8S,function(cnX8P){o4s.push(Tg9X.emj[cnX8P])});return o4s.join("")};
	var cnS8K=v4z.bn5s;
	v4z.bn5s=function(Y4c,e4i){
		var j4n={},e4i=NEJ.X({},e4i),lE9v=Y4c.indexOf("?");
		if(window.GEnc&&/(^|\.com)\/api/.test(Y4c)&&!(e4i.headers&&e4i.headers[em6g.yg3x]==em6g.EK5P)&&!e4i.noEnc){
			if(lE9v!=-1){
				j4n=k4o.hv7o(Y4c.substring(lE9v+1));
				Y4c=Y4c.substring(0,lE9v)
				}
			if(e4i.query){
				j4n=NEJ.X(j4n,k4o.fG7z(e4i.query)?k4o.hv7o(e4i.query):e4i.query)
				}
			if(e4i.data){
				j4n=NEJ.X(j4n,k4o.fG7z(e4i.data)?k4o.hv7o(e4i.data):e4i.data)
				}
			j4n["csrf_token"]=v4z.gy7r("__csrf");
			Y4c=Y4c.replace("api","weapi");
			e4i.method="post";
			delete e4i.query;
			
			var bPc2x=window.asrsea(JSON.stringify(j4n),buv7o(["流泪","强"]),buv7o(Tg9X.md),buv7o(["爱心","女孩","惊恐","大笑"]));
			e4i.data=k4o.cE5J({params:bPc2x.encText,encSecKey:bPc2x.encSecKey})
			}
		cnS8K(Y4c,e4i)
		};
		v4z.bn5s.redefine=true
	})();
	
	
!function(){
	function a(a){     						//16位随机字符串
		var d,e,b="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",c="";
		for(d=0;a>d;d+=1)
			e=Math.random()*b.length,		//生成随机数
			e=Math.floor(e),				//取整
			c+=b.charAt(e);					//比较选取字符
		return c
		}
		
	function b(a,b){
		var c=CryptoJS.enc.Utf8.parse(b),
		d=CryptoJS.enc.Utf8.parse("0102030405060708"),
		e=CryptoJS.enc.Utf8.parse(a),
		f=CryptoJS.AES.encrypt(e,c,			//e 待加密内容  c 密钥
			{iv:d,							//初始向量
			 mode:CryptoJS.mode.CBC			//模式
			}
			);
		return f.toString()
		}
		
	function c(a,b,c){					
		var d,e;
		return setMaxDigits(131),   	//131 => n的十六进制位数/2+3  
			d=new RSAKeyPair(b,"",c),	//公钥（e1,n）(pubkey,modulus)
			e=encryptedString(d,a)		//d密钥 + a原文
		}
		
	function d(d,e,f,g){
		var h={},i=a(16);               //16bytes 128位密钥  
		return h.encText=b(d,g),		//utf-8编码的数据d使用AES加密密钥为utf-8编码的数据g  AES/CBC/padding     iv("0102030405060708".getBytes())   
		h.encText=b(h.encText,i),		//再次AES加密
		h.encSecKey=c(i,e,f),			//对16位随机字符RSA加密
		h
		}
		
	function e(a,b,d,e){
		var f={};return f.encText=c(a+e,b,d),f
		}
		
	window.asrsea=d,window.ecnonasr=e
	}();
