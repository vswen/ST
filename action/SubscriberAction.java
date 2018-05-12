package action;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.Subscriber;
import service.SubscriberService;
import util.MD5;
import util.Tools;

@Controller
public class SubscriberAction {
	
	@Resource
	private SubscriberService subService;
	
	private int pageSize = 10;
	
	@RequestMapping("/sub_init")
	public String init(ModelMap map,@RequestParam Map<String, String> paramMap,@RequestParam(value="page", defaultValue="0")int page){
		List<Subscriber> list = subService.paramQuery(paramMap,page,pageSize);
		int count = subService.paramQueryCount(paramMap);
		int countPage = count%pageSize==0?count/pageSize:count/pageSize+1;
		map.put("count", count);
		map.put("countPage", countPage);
		map.put("page", page);
		map.put("name", paramMap.get("map_name"));
		map.put("domain", paramMap.get("map_domain"));
		map.put("list", list);
		return "subscriber";
	}
	
	@ResponseBody
	@RequestMapping("/sub_add")
	public JSONObject add(Subscriber subscriber){
		JSONObject json = new JSONObject();
		int t = 0;
		String ha1 = MD5.getMD5Str(subscriber.getUserName()+":"+subscriber.getDomain()+":"+subscriber.getPassword());
		String ha1b = MD5.getMD5Str(subscriber.getUserName()+"@"+subscriber.getDomain()+":"+subscriber.getDomain()+":"+subscriber.getPassword());
		subscriber.setHa1(ha1);
		subscriber.setHa1b(ha1b);
		try {
			t = subService.addExtend(subscriber);
		} catch (Exception e) {
			json.put("msg", e.getMessage());
		}
		json.put("success", t>0);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/sub_del")
	public JSONObject del(int id){
		JSONObject json = new JSONObject();
		int t = subService.del(id);
		json.put("success", t>0);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/sub_up")
	public JSONObject up(Subscriber subscriber){
		JSONObject json = new JSONObject();
		int t = 0;
		Subscriber subscriber2 = subService.getById(subscriber.getId());
		subscriber.setPassword(Tools.getRandomStr(6, 3));
		String ha1 = MD5.getMD5Str(subscriber2.getUserName()+":"+subscriber2.getDomain()+":"+subscriber.getPassword());
		String ha1b = MD5.getMD5Str(subscriber2.getUserName()+"@"+subscriber2.getDomain()+":"+subscriber2.getDomain()+":"+subscriber.getPassword());
		subscriber.setHa1(ha1);
		subscriber.setHa1b(ha1b);
		try {
			t = subService.up(subscriber);
		} catch (Exception e) {
			json.put("msg", e.getMessage());
		}
		json.put("success", t>0);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/sub_dels")
	public JSONObject dels(int[] ids){
		JSONObject json = new JSONObject();
		int t = 0;
		for (int id : ids) {
			t = subService.del(id);
		}
		json.put("success", t>0);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/sub_ups")
	public JSONObject ups(int[] ids){
		JSONObject json = new JSONObject();
		int t = 0;
		for (int id : ids) {
			Subscriber subscriber = new Subscriber();
			subscriber.setId(id);
			Subscriber subscriber2 = subService.getById(subscriber.getId());
			subscriber.setPassword(Tools.getRandomStr(6, 3));
			String ha1 = MD5.getMD5Str(subscriber2.getUserName()+":"+subscriber2.getDomain()+":"+subscriber.getPassword());
			String ha1b = MD5.getMD5Str(subscriber2.getUserName()+"@"+subscriber2.getDomain()+":"+subscriber2.getDomain()+":"+subscriber.getPassword());
			subscriber.setHa1(ha1);
			subscriber.setHa1b(ha1b);
			try {
				t = subService.up(subscriber);
			} catch (Exception e) {
				json.put("msg", e.getMessage());
			}
		}
		
		json.put("success", t>0);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/sub_adds")
	public JSONObject adds(String strs){
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
        Matcher m = p.matcher(strs);  
        strs = m.replaceAll("");  
		String[] array = strs.split(";");
		int errx = 0;
		for (String string : array) {
			String[] array2 = string.split("-");
			if(array2.length<3){
				errx++;
				jsonArray.add(string);
				continue;
			}
			Subscriber subscriber = new Subscriber();
			subscriber.setUserName(array2[0]);
			subscriber.setPassword(array2[1]);
			subscriber.setDomain(array2[2]);
			if(array2.length>3){
				subscriber.setEmailAddress(array2[3]);
			}
			if(array2.length>4){
				subscriber.setRpid(array2[4]);
			}
			String ha1 = MD5.getMD5Str(subscriber.getUserName()+":"+subscriber.getDomain()+":"+subscriber.getPassword());
			String ha1b = MD5.getMD5Str(subscriber.getUserName()+"@"+subscriber.getDomain()+":"+subscriber.getDomain()+":"+subscriber.getPassword());
			subscriber.setHa1(ha1);
			subscriber.setHa1b(ha1b);
			int t = 0;
			try {
				t = subService.addExtend(subscriber);
			} catch (Exception e) {
				//json.put("msg", e.getMessage());
			}
			if(t<1){
				errx++;
				jsonArray.add(string);
			}
		}
		json.put("success", errx==0);
		json.put("msg", jsonArray);
		return json;
	}
}
