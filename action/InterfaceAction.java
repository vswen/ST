package action;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.CollStatService;
import service.ImgAdsService;
import service.InformService;
import service.PatchesService;
import service.WeatherService;
import util.SysConstants;
import util.Tools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.CollStat;
import entity.ImgAds;
import entity.Inform;
import entity.Patches;
import entity.Weather;

@Controller
public class InterfaceAction {
	
	@Resource
	private PatchesService ps;
	
	@Resource
	private InformService is;
	
	@Resource
	private WeatherService ws;
	
	@Resource
	private ImgAdsService ias;
	
	@Resource
	private CollStatService css;
	
	/**
	 * 门禁验证转发
	 * @param key
	 * @param doorNumber
	 * @param account
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/vr")
	public JSONObject verifyRelay(String key,String doorNumber,String account){
		JSONObject json = new JSONObject();
		String postJson = "key="+key+"&doorNumber="+doorNumber+"&account="+account+"";
		String js = Tools.resolve3(SysConstants.ACS_SERVER_ADDRESS+"im/verifyToken",postJson);
		if(js==null||"".equals(js)){
			json.put("errcode", 500);
			json.put("errmsg", "服务器[TTG_acs]未响应！");
		}else{
			json = JSON.parseObject(js);
		}
		return json;
	}
	
	/**
	 * 获取补丁包列表
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/spl")
	public JSONObject showPList(int type){
		JSONObject json = new JSONObject();
		List<Patches> list = ps.showAll(type);
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		json.put("data", JSON.toJSON(list));
		return json;
	}
	
	/**
	 * 下载补丁包
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/downP")
	public JSONObject downP(HttpServletResponse response,HttpServletRequest request,int id){
		JSONObject json = new JSONObject();
		Patches gp = ps.getById(id);
		if(gp == null){
			json.put("errcode", 1);
			json.put("errmsg", "未找到匹配的资源！");
			return json;
		}
		String fileName = gp.getFileName();
		String filePath = SysConstants.PATCHES_FILE_PATH;
		try {
			Tools.down(response, request, fileName, filePath, gp.getDetailsJson().getString("fileSize"));
		} catch (Exception e) {
			response.setHeader("Content-Disposition",null);
			json.put("errcode", 401);
			json.put("errmsg", "下载文件出现异常！");
			return json;
		}
		ps.up(null,1,0,null,id);
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		return json;
	}
	
	/**
	 * 天气转发
	 * @param type
	 * @param cityname
	 * @param ip
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/wr")
	public JSONObject weatherRelay(int type,String cityname,String ip){
		JSONObject json = new JSONObject();
		String key = Tools.resolve3(SysConstants.AGENCY_SERVER_ADDRESS+"api/url_readKey","");
		key = key.substring(1,key.length()-1);
		String postJson = "key="+key+"&type="+type+"&cityname="+cityname+"&ip="+ip+"";
		String js = Tools.resolve3(SysConstants.AGENCY_SERVER_ADDRESS+"api/url_weather",postJson);
		if(js==null||"".equals(js)){
			json.put("errcode", 500);
			json.put("errmsg", "服务器[ttgAgency]未响应！");
		}else{
			json = JSON.parseObject((String)JSON.parse(js));
		}
		return json;
	}
	
	/**
	 * 获取物业通知列表
	 * @param type
	 * @param x
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/sil")
	public JSONObject showIList(int type,@RequestParam(value="x", defaultValue="0")int x){
		JSONObject json = new JSONObject();
		List<Inform> list = is.conditionShow(type, x);
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		json.put("data", JSON.toJSON(list));
		return json;
	}
	
	/**
	 * 获取最后一条气象指数数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/sw")
	public JSONObject showWeather(){
		JSONObject json = new JSONObject();
		Weather w = ws.getMaxData();
		JSONArray jsonArray = new JSONArray();
		JSONObject wj = (JSONObject) JSON.toJSON(w);
		
		List<String> keylist = new ArrayList<String>();
		keylist.add("temp");
		keylist.add("humi");
		keylist.add("rait");
		keylist.add("wins");
		keylist.add("winr");
		keylist.add("radi");
		keylist.add("pres");
		keylist.add("raif");
		keylist.add("dens");
		keylist.add("enth");
		
		
		if(wj != null){
			for(int i=0;i<keylist.size();i++)
			{
				String key = keylist.get(i);
				if(wj.getString(key+"Name")==null||wj.getString(key+"Unit")==null){
					continue;
				}
				
				JSONObject tempj = new JSONObject();
				tempj.put("key", key);
				tempj.put("name", wj.getString(key+"Name"));
				tempj.put("unit", wj.getString(key+"Unit"));
				tempj.put("value", wj.getString(key));
				jsonArray.add(tempj);
				
			}
		}
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		json.put("data", jsonArray);
		return json;
	}
	
	/**
	 * 获取物业广告
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/sia")
	public JSONObject showImgAds(){
		JSONObject json = new JSONObject();
		List<ImgAds> list = ias.show();
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		json.put("data", JSON.toJSON(list));
		return json;
	}
	
	/**
	 * 下载物业广告
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/downAds")
	public JSONObject downImgAds(HttpServletResponse response,HttpServletRequest request,int id){
		JSONObject json = new JSONObject();
		ImgAds ia = ias.getById(id);
		if(ia == null){
			json.put("errcode", 1);
			json.put("errmsg", "未找到匹配的资源！");
			return json;
		}
		String fileName = ia.getFileName();
		String filePath = SysConstants.ADS_FILE_PATH;
		try {
			Tools.down(response, request, fileName, filePath, JSON.parseObject(ia.getDetails()).getString("fileSize"));
		} catch (Exception e) {
			response.setHeader("Content-Disposition",null);
			json.put("errcode", 401);
			json.put("errmsg", "下载文件出现异常！");
			return json;
		}
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		return json;
	}
	
	/**
	 * 添加报警信息
	 * @param sip
	 * @param collType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/im/addCS")
	public JSONObject addCS(String sip,String collContent,String collType,String address,String roomNumb){
		JSONObject json = new JSONObject();
		CollStat cs = new CollStat();
		cs.setSipName(sip);
		cs.setCollContent(collContent);
		cs.setCollType(collType);
		cs.setAddress(address);
		cs.setRoomNumb(roomNumb);
		boolean b = css.add(cs)>0;
		if(b){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 100);
			json.put("errmsg", "新增失败！");
		}
		return json;
	}
}
