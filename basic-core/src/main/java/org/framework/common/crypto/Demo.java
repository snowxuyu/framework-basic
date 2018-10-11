package org.framework.common.crypto;


import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Demo {

    public static void main(String[] args) throws Exception {
    	/**
    	 * ===========================RSA加解密demo=========================
    	 */
    	
        //明文
        String str = "受教育是一件令人羡慕的事。但必须时刻牢记，任何值得理解的东西都不是能由别人传授的。\n" +
                "　　\n" +
                "公共舆论只是在没有思想的地方才能存在。\n" +
                "　　\n" +
                "英国人总是把真理贬低为事实。而当真理变成事实时，它便失去了其思想价值。\n" +
                "　　\n" +
                "如今无用的信息如此难寻，真是一件非常令人悲哀的事。\n" +
                "　　\n" +
                "在当今的英国，文学与戏剧之间给我们剩下的惟一联系就是演出海报。\n" +
                "　　\n" +
                "过去的书是由文人写作，并由大众阅读的。而今的书则由大众写作，但却无人阅读。\n" +
                "　　\n" +
                "友谊远比爱情更富有悲剧性。它持续的时间更久。\n" +
                "　　\n" +
                "生活中不正常的东西对于艺术来说就是正常的。它是生活中唯一跟艺术保持正常关系的东西。\n" +
                "　　\n" +
                "本身优美的题材不能给艺术家以灵感。它缺乏不完美性。\n" +
                "　　\n" +
                "唯一使艺术家看不见的是明显的东西。唯一使公众看得见的也是明显的东西，结果就产生了记者的批评。\n" +
                "　　\n" +
                "艺术是世间唯一严肃的东西。而艺术家却是唯一从来不能严肃的人。\n" +
                "　　\n" +
                "要真正做到中世纪化，人就不能有肉体。要真正做到现代化，人就不能有灵魂。要真正做到希腊化，人就不能穿衣裳。\n" +
                "　　\n" +
                "时髦就是标榜美的绝对现代化。\n" +
                "　　\n" +
                "唯一能够安慰穷人的就是挥金如土，唯一能够安慰富人的就是一毛不拔。\n" +
                "　　\n" +
                "人们决不应该去聆听别人。聆听就是漠视听众的一种迹象。\n" +
                "　　\n" +
                "就连门徒也是有其用处的。他站在你的王位后面，在你得意洋洋之时往你耳朵里灌一些奉承话，使你感觉自己是不朽的。\n" +
                "　　\n" +
                "犯罪分子离我们是如此之近，就连警察也能看到他们。他们离我们又是如此之远，只有诗人才能理解他们。\n" +
                "　　\n" +
                "上帝钟爱的人越活越年轻。";

        //生成RSA的公钥私钥
        Map<String, Object> initKey = RSAUtils.genKeyPair();

        //获取私钥
        String privateKey = RSAUtils.getPrivateKey(initKey);

        //获取公钥
        String publicKey = RSAUtils.getPublicKey(initKey);

        //rsa加密
        String serc = RSAUtils.encryptByPublicKey(str, publicKey);
        //输出加密后内容
        System.out.println(serc);


        //解密
        String encode = RSAUtils.decryptByPrivateKey(serc, privateKey);

        //输出解密后内容
        System.out.println(encode);
        
        /**
         * ============================================MD5验签demo========================================
         * */
        
        String json = "{\"merchantsNo\":\"10010000002\",\"page\":\"pay\",\"outOrderNo\":\"YHFQ201609211830\",\"backUrl\":\"www.snowxuyu.com\",\"phone\":\"13004393842\",\"amount\":3,\"backGoodsUrl\":\"www.snowxuyu.com\",\"receiverInfo\":{\"receiverName\":\"高国祥\",\"addressCode\":\"110102\",\"province\":\"北京\",\"city\":\"北京市\",\"region\":\"宣武区\",\"address\":\"东大名路\",\"receiverPhone\":\"13004393842\"},\"goodsList\":[{\"goodsName\":\"手机\",\"goodsPrice\":3,\"goodsNum\":1,\"goodsImg\":\"\",\"goodsDesc\":\"\"}]}";
        String md5sign = "n8qPvGnJHnJwg+oOzR4VOMPf35CGZ3rXsVSkd06b5L4=";
        
        
        Map<String, String> paramMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String,Object> entry : entries) {
            paramMap.put(entry.getKey(), entry.getValue().toString());
        }
        String linkString = Tools.createLinkString(paramMap, false);
        System.out.println(linkString);

        //签名
        String sign = SignUtil.sign(json, "MD5", md5sign);

        System.out.println(sign);
        
        //验签
        System.out.println(SignUtil.verify(json, sign, md5sign));
        
        
        /**
         * ================================================DES demo===================================
         */
        
        String desStr = "我年青时以为金钱至上，而今年事已迈，发现果真如此。";
        
        String deskey = DESUtils.initKey();
        System.out.println(deskey.length());
        System.out.println(Base64Utils.decode(deskey).length);

        System.out.println("DesedeKey: " + deskey);
        
        //3DES 加密
        //String encode2 = DESUtils.encrypt(desStr.getBytes("UTF-8"), deskey);
        String encode2 = DESUtils.encrypt(desStr, deskey);
        System.out.println("3des加密：" + encode2);
        
        //3DES 解密
        //String decry = DESUtils.decrypt(Base64Utils.decode(encode2), deskey);
        String decry = DESUtils.decrypt(encode2, deskey);
        System.out.println("3des解密：" + decry);
    }
}
