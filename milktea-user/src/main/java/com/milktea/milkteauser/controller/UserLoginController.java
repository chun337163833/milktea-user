package com.milktea.milkteauser.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.milktea.milkteauser.domain.TeaLoginWeixin;
import com.milktea.milkteauser.exception.MilkTeaErrorConstant;
import com.milktea.milkteauser.exception.MilkTeaException;
import com.milktea.milkteauser.service.UserLoginService;
import com.milktea.milkteauser.util.HttpUtil;
import com.milktea.milkteauser.vo.ClassGoodsRequestVo;
import com.milktea.milkteauser.vo.ResponseBody;







@RestController
@RequestMapping("/userLogin")
public class UserLoginController {

	public static String weiXinAppid = "wxbac9e1b7d8104470";
	
	public static String weiXinSecret = "08695399b120b9ed523db01ddd51d38d";
	
	public static String weiXinGrantType = "authorization_code";
	
	public String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	public String param;
	
	@Autowired
    private UserLoginService userLoginService;
	
	//微信客户登入
	@RequestMapping(value="/weixin", method = RequestMethod.POST)
	public ResponseBody<TeaLoginWeixin>  userInfoLogin(String code) throws MilkTeaException{
		Logger logger = LoggerFactory.getLogger(UserLoginController.class);
		
		ResponseBody<TeaLoginWeixin> responseBody = new ResponseBody<TeaLoginWeixin>();
		
		//获取code后，请求以下链接获取access_token https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		param = "appid=" + weiXinAppid +"&" + "secret=" + weiXinSecret + "&" + "code=" + code + "&" + "grant_type=authorization_code";
		String result = "";
        BufferedReader in = null;
        String access_token = "";
        String openid = "";
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JSONObject json = JSON.parseObject(result);
            access_token = json.getString("access_token");
            openid = json.getString("openid");
            System.out.println(result);
            TeaLoginWeixin teaLoginWeixin = new TeaLoginWeixin();
            teaLoginWeixin.setWeixinOpenid(openid);
            responseBody.setData(teaLoginWeixin);
        } catch (Exception e) {
        	logger.error(MilkTeaErrorConstant.WEIXIN_ACCESSTOKEN_FAILURE.getCnErrorMsg(), e);
            throw new MilkTeaException(MilkTeaErrorConstant.WEIXIN_ACCESSTOKEN_FAILURE, e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        
       //获取access_token后，拉取用户信息 https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
        url = "https://api.weixin.qq.com/sns/userinfo";
      		param = "access_token=" + access_token +"&" + "openid=" + openid + "&" + "lang=zh_CN";
      		result = "";
            in = null;
              try {
                  String urlNameString = url + "?" + param;
                  URL realUrl = new URL(urlNameString);
                  // 打开和URL之间的连接
                  URLConnection connection = realUrl.openConnection();
                  // 设置通用的请求属性
                  connection.setRequestProperty("accept", "*/*");
                  connection.setRequestProperty("connection", "Keep-Alive");
                  connection.setRequestProperty("user-agent",
                          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                  // 建立实际的连接
                  connection.connect();
                  // 获取所有响应头字段
                  Map<String, List<String>> map = connection.getHeaderFields();
                  // 遍历所有的响应头字段
                  for (String key : map.keySet()) {
                      System.out.println(key + "--->" + map.get(key));
                  }
                  // 定义 BufferedReader输入流来读取URL的响应
                  in = new BufferedReader(new InputStreamReader(
                          connection.getInputStream()));
                  String line;
                  while ((line = in.readLine()) != null) {
                      result += line;
                  }
                  TeaLoginWeixin teaLoginWeixin = new TeaLoginWeixin();
                  JSONObject json = JSON.parseObject(result);
                  teaLoginWeixin.setWeixinOpenid(json.getString("openid"));
                  teaLoginWeixin.setWeixinNickname(json.getString("nickname"));
                  teaLoginWeixin.setWeixinSex(json.getString("sex"));
                  teaLoginWeixin.setPrivilege(json.getString("privilege"));
                  teaLoginWeixin.setCity(json.getString("city"));
                  teaLoginWeixin.setCountry(json.getString("country"));
                  teaLoginWeixin.setHeadimgurl(json.getString("headimgurl"));
                  teaLoginWeixin.setWeixinProvince(json.getString("province"));
                  this.userLoginService.insert(teaLoginWeixin);
                  
                  responseBody.setData(teaLoginWeixin);
                  
                  
                  System.out.println(result);
              } catch (Exception e) {
            	  logger.error(MilkTeaErrorConstant.WEIXIN_GETUSERINFO_FAILURE.getCnErrorMsg(), e);
                  throw new MilkTeaException(MilkTeaErrorConstant.WEIXIN_GETUSERINFO_FAILURE, e);
              }
              // 使用finally块来关闭输入流
              finally {
                  try {
                      if (in != null) {
                          in.close();
                      }
                  } catch (Exception e2) {
                      e2.printStackTrace();
                  }
              }
        
        

		return responseBody;
	}
	
	//得到所有店铺LIST
	@RequestMapping(value="/storelist", method = RequestMethod.GET)
	public ResponseBody<JSONObject>  getStoreList(@RequestParam("lang") String lang) throws MilkTeaException{
		BufferedReader in = null;
		String result = "";
		Logger logger = LoggerFactory.getLogger(UserLoginController.class);
		ResponseBody<JSONObject> responseBody = new ResponseBody<JSONObject>();
		JSONObject jsonObject = new JSONObject();
		//调用商品后台，取得默认登入商铺内的商品及所有商铺
        
        //所有商铺 
//		param = "appid=" + weiXinAppid +"&" + "secret=" + weiXinSecret + "&" + "code=" + code + "&" + "grant_type=authorization_code";
        String url = "http://localhost:8081/queryStores";
        String urlNameString = "" ;
        if("".equals(lang)){
        	 urlNameString = url ;
        } else {
        	urlNameString = url + "/" +lang;
        }
        
        
        try {
	        URL realUrl = new URL(urlNameString);
	        // 打开和URL之间的连接
	        URLConnection connection = realUrl.openConnection();
	        // 设置通用的请求属性
	        connection.setRequestProperty("accept", "*/*");
	        connection.setRequestProperty("connection", "Keep-Alive");
	        connection.setRequestProperty("user-agent",
	                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        // 建立实际的连接
	        connection.connect();
	        // 获取所有响应头字段
	        Map<String, List<String>> map = connection.getHeaderFields();
	        // 遍历所有的响应头字段
	        for (String key : map.keySet()) {
	            System.out.println(key + "--->" + map.get(key));
	        }
	        // 定义 BufferedReader输入流来读取URL的响应
	        in = new BufferedReader(new InputStreamReader(
	                connection.getInputStream()));
	        String line = "";
	        while ((line = in.readLine()) != null) {
	            result += line;
	        }
	        System.out.println(result);
	        jsonObject = JSON.parseObject(result);
	        responseBody.setData(jsonObject);
        }  catch (Exception e) {
        	logger.error(MilkTeaErrorConstant.MILETEA_SHOP_FAILURE.getCnErrorMsg(), e);
            throw new MilkTeaException(MilkTeaErrorConstant.MILETEA_SHOP_FAILURE, e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
		return responseBody;
	}
	
	//取得店铺内的商品
	@RequestMapping(value="/getClassGoods")
	public ResponseBody<JSONObject>  getClassGoods(@RequestParam("storeNo") String storeNo,@RequestParam("classType") String classType,@RequestParam("lang") String lang) throws MilkTeaException{
		BufferedReader in = null;
		String result = "";
		Logger logger = LoggerFactory.getLogger(UserLoginController.class);
		ResponseBody<JSONObject> responseBody = new ResponseBody<JSONObject>();
		JSONObject jsonObject = new JSONObject();
		JsonObject message = new JsonObject();
		PrintWriter out = null;
		String path = "http://localhost:8081/queryClassGoodsNational"; 
	        
        

		try {

			HttpUtil HttpUtil = new HttpUtil();
			Map<String,String> mapParam = new HashMap<String,String>();
			mapParam.put("storeNo", storeNo);
			mapParam.put("classType", classType);
			mapParam.put("lang", lang);
			String retStr = HttpUtil.post(path, mapParam);
			System.out.println(retStr);
			jsonObject = JSON.parseObject(retStr);
	        responseBody.setData(jsonObject);
			
		
          
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
		return responseBody;
	}
	
	//取得轮播图
		@RequestMapping(value="/getCarouselFigure")
		public ResponseBody<JSONObject>  getCarouselFigure(@RequestParam("storeNo") String storeNo,@RequestParam("lang") String lang) throws MilkTeaException{
			BufferedReader in = null;
			String result = "";
			Logger logger = LoggerFactory.getLogger(UserLoginController.class);
			ResponseBody<JSONObject> responseBody = new ResponseBody<JSONObject>();
			JSONObject jsonObject = new JSONObject();
			JsonObject message = new JsonObject();
			PrintWriter out = null;
			String path = "http://localhost:8081/queryCarouselFigureNation"; 
		        
			try {

				HttpUtil HttpUtil = new HttpUtil();
				Map<String,String> mapParam = new HashMap<String,String>();
				mapParam.put("storeNo", storeNo);
				mapParam.put("lang", lang);
				String retStr = HttpUtil.post(path, mapParam);
				
				System.out.println(retStr);
				jsonObject = JSON.parseObject(retStr);
		        responseBody.setData(jsonObject);
			
	          
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        
			return responseBody;
		}
	
	
	
	
	
}
