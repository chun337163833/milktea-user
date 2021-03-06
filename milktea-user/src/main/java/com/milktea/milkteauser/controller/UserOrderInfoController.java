package com.milktea.milkteauser.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.milktea.milkteauser.domain.TeaOrderInfo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.milktea.milkteauser.exception.MilkTeaException;
import com.milktea.milkteauser.service.UserOrderInfoService;
import com.milktea.milkteauser.util.HttpUtil;
import com.milktea.milkteauser.vo.CustOrderInfoVo;
import com.milktea.milkteauser.vo.PageRequestVo;
import com.milktea.milkteauser.vo.QueryOrdersRequestVo;
import com.milktea.milkteauser.vo.ResponseBody;
import com.milktea.milkteauser.vo.ResponseHeader;



@RestController
@RequestMapping("/userOrderInfo")
public class UserOrderInfoController {

	@Autowired
	UserOrderInfoService userOrderInfoService;
	
	/**
	 * @param custOrderInfoVo
	 * @return ResponseBody<TeaOrderInfo>
	 * @throws MilkTeaException
	 * 只有直接下单才进入此方法，
	 */
	@RequestMapping(value="/userOrderOper", method = RequestMethod.POST)
	public ResponseBody<CustOrderInfoVo>  userOrderOper(@RequestBody CustOrderInfoVo custOrderInfoVo) throws MilkTeaException{
		ResponseBody responseBody = new ResponseBody();
		CustOrderInfoVo CustOrderInfoVo = this.userOrderInfoService.userOrderOper(custOrderInfoVo);
		responseBody.setData(CustOrderInfoVo);
		return responseBody;
	}
	
	@RequestMapping(value="/findOrderByTelephone", method = RequestMethod.GET)
	public ResponseBody<List<CustOrderInfoVo>>  findOrderByTelephone(@RequestParam("telephone") String telephone,@RequestParam("flag") String flag) throws MilkTeaException{
		ResponseBody responseBody = new ResponseBody();
		List<CustOrderInfoVo> listCustOrderInfoVo = this.userOrderInfoService.findOrderByTelephone(telephone,flag);
		responseBody.setData(listCustOrderInfoVo);
		return responseBody;
	}
	
	
	
	/**
	 * 改变订单状态
	 * @param orderNo
	 * @param orderStatus
	 * @return
	 * @throws MilkTeaException
	 */
	@RequestMapping(value="/modfiyOrderStatus", method = RequestMethod.GET)
	public ResponseHeader  modfiyOrderStatus(@RequestParam("orderNo") String orderNo,@RequestParam("orderStatus") String orderStatus) throws MilkTeaException{
		ResponseHeader responseHeader = new ResponseHeader();
		this.userOrderInfoService.modifyOrderStatus(orderNo, orderStatus);
		return responseHeader;
	}
	
	
	/**
	 * 订单结算单生成后，再次改变 备注及订单类型属性
	 * @param orderNo
	 * @param orderStatus
	 * @return
	 * @throws MilkTeaException
	 */
	@RequestMapping(value="/finishPayModfiyOrder", method = RequestMethod.GET)
	public ResponseHeader  finishPayModfiyOrder(@RequestParam("orderNo") String orderNo,@RequestParam("remark") String remark,@RequestParam("orderTime") String orderTime) throws MilkTeaException{
		ResponseHeader responseHeader = new ResponseHeader();
		this.userOrderInfoService.finishPayModfiyOrder(orderNo, remark,orderTime);
		return responseHeader;
	}
	
	/**
	 * 根据客户号分页条件查询订单
	 * @param requestVo
	 * @return
	 * @throws MilkTeaException
	 */
	@RequestMapping(value="/queryOrdersByUserNoPage", method = RequestMethod.GET)
	public ResponseBody<JSONObject>  queryOrdersByUserNoPage(@RequestParam("jsonStr") String jsonStr) throws MilkTeaException{
		BufferedReader in = null;
		String result = "";
		Logger logger = LoggerFactory.getLogger(UserLoginController.class);
		ResponseBody<JSONObject> responseBody = new ResponseBody<JSONObject>();
		JSONObject jsonObject = new JSONObject();
		JsonObject message = new JsonObject();
		PrintWriter out = null;
		String path = "http://localhost:8081/queryOrdersByUserNo";
	        
		try {

			HttpUtil HttpUtil = new HttpUtil();
			Map<String,String> mapParam = new HashMap<String,String>();

//			mapParam.put("userNo", requestVo.getUserNo());
//			mapParam.put("telephone", requestVo.getTelephone());
//			mapParam.put("orderNo", requestVo.getOrderNo());
//			mapParam.put("lang", requestVo.getLang());
//			mapParam.put("storeNo", requestVo.getStoreNo());
//			mapParam.put("promotionId", requestVo.getPromotionId());
//			mapParam.put("orderType", requestVo.getOrderType());
//			mapParam.put("orderStatus", requestVo.getOrderStatus());
//			mapParam.put("payStatus", requestVo.getPayStatus());
//			if(null != requestVo.getBeginDate())
//			{
//				mapParam.put("beginDate", requestVo.getBeginDate().toString());
//			}
//			if(null != requestVo.getEndDate())
//			{
//				mapParam.put("endDate", requestVo.getEndDate().toString());
//			}
			CloseableHttpClient client = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(path);
	        //将Map转为Json
	        Gson gson = new Gson();
//	        String json = gson.toJson(requestVo);
	        String json =jsonStr;
	        StringEntity entity = new StringEntity(json);
	        httpPost.setEntity(entity);
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-type", "application/json");

	        CloseableHttpResponse response = client.execute(httpPost);
	        int statusCode=response.getStatusLine().getStatusCode();
	        if (statusCode!=200){
	            throw new Exception("无法连接该地址");
	         }
	        HttpEntity resEntity=response.getEntity(); // 获取返回实体
	        String content=EntityUtils.toString(resEntity,"utf-8");
	      //  assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
	        response.close();
	        client.close();
			
			
			System.out.println(content);
			jsonObject = JSON.parseObject(content);
	        responseBody.setData(jsonObject);
		
          
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 
		 
		 
		return responseBody;
	}

	/**
	 * 根据客户号条件查询订单
	 * @param requestVo
	 * @return
	 * @throws MilkTeaException
	 */
	@RequestMapping(value="/queryOrdersByUserNo", method = RequestMethod.POST)
	public ResponseBody<JSONObject>  queryOrdersByUserNo(@RequestBody QueryOrdersRequestVo requestVo) throws MilkTeaException{
		BufferedReader in = null;
		String result = "";
		Logger logger = LoggerFactory.getLogger(UserLoginController.class);
		ResponseBody<JSONObject> responseBody = new ResponseBody<JSONObject>();
		JSONObject jsonObject = new JSONObject();
		JsonObject message = new JsonObject();
		PrintWriter out = null;
		String path = "http://localhost:8081/queryOrdersByUserNo";
	        
		try {

			HttpUtil HttpUtil = new HttpUtil();
			Map<String,String> mapParam = new HashMap<String,String>();
			mapParam.put("userNo", requestVo.getUserNo());
			mapParam.put("telephone", requestVo.getTelephone());
			mapParam.put("orderNo", requestVo.getOrderNo());
			mapParam.put("lang", requestVo.getLang());
			mapParam.put("storeNo", requestVo.getStoreNo());
			mapParam.put("promotionId", requestVo.getPromotionId());
			mapParam.put("orderType", requestVo.getOrderType());
			mapParam.put("orderStatus", requestVo.getOrderStatus());
			mapParam.put("payStatus", requestVo.getPayStatus());
			if(null != requestVo.getBeginDate())
			{
				mapParam.put("beginDate", requestVo.getBeginDate().toString());
			}
			if(null != requestVo.getEndDate())
			{
				mapParam.put("endDate", requestVo.getEndDate().toString());
			}
			
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

	/**
	 * 根据订单编号查询订单
	 * @param orderNo
	 * @return
	 * @throws MilkTeaException
	 */
	@RequestMapping("/selectbyorderno")
	public ResponseBody<TeaOrderInfo> selectByOrderno(String orderNo) throws MilkTeaException{

		TeaOrderInfo teaOrderInfo=userOrderInfoService.findOrderByOrderNo(orderNo);
		ResponseBody<TeaOrderInfo> responseBody=new ResponseBody<>();
		responseBody.setData(teaOrderInfo);

		return responseBody;

	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
