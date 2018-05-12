package action;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import entity.CollStat;
import service.CollStatService;

@Controller
public class CollStatAction {
	
	@Resource
	private CollStatService css;
	
	private int pageSize = 10;
	
	@RequestMapping("/cs_init")
	public String init(ModelMap map,@RequestParam Map<String, String> paramMap,@RequestParam(value="page", defaultValue="0")int page){
		List<CollStat> list = css.paramQuery(paramMap,page,pageSize);
		int count = css.paramQueryCount(paramMap);
		int countPage = count%pageSize==0?count/pageSize:count/pageSize+1;
		map.put("count", count);
		map.put("countPage", countPage);
		map.put("page", page);
		map.put("sipName", paramMap.get("map_sipName"));
		map.put("beginTime", paramMap.get("map_begin_time"));
		map.put("endTime", paramMap.get("map_end_time"));
		map.put("isRead", paramMap.get("map_is_read"));
		map.put("address", paramMap.get("map_address"));
		map.put("roomNumb", paramMap.get("map_roomNumb"));
		map.put("list", list);
		return "coll_stat";
	}
	
	@ResponseBody
	@RequestMapping("/cs_upRead")
	public JSONObject upRead(int[] ids){
		JSONObject json = new JSONObject();
		for (int id : ids) {
			css.upRead(id);
		}
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		return json;
	}
	
	@RequestMapping("/im/cso")
	public String init2(ModelMap map,@RequestParam Map<String, String> paramMap,@RequestParam(value="page", defaultValue="0")int page){
		List<CollStat> list = css.paramQuery(paramMap,page,pageSize);
		int count = css.paramQueryCount(paramMap);
		int countPage = count%pageSize==0?count/pageSize:count/pageSize+1;
		map.put("count", count);
		map.put("countPage", countPage);
		map.put("page", page);
		map.put("sipName", paramMap.get("map_sipName"));
		map.put("beginTime", paramMap.get("map_begin_time"));
		map.put("endTime", paramMap.get("map_end_time"));
		map.put("isRead", paramMap.get("map_is_read"));
		map.put("list", list);
		return "coll_stat_outer";
	}
	
	@ResponseBody
	@RequestMapping("/im/cs_upRead_outer")
	public JSONObject upRead2(int[] ids){
		JSONObject json = new JSONObject();
		for (int id : ids) {
			css.upRead(id);
		}
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/cs_gcr")
	public JSONObject getCountRead(int[] ids){
		JSONObject json = new JSONObject();
		int count = css.countRead();
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		json.put("data", count);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/im/cs_gcr_outer")
	public JSONObject getCountRead2(int[] ids){
		JSONObject json = new JSONObject();
		int count = css.countRead();
		json.put("errcode", 0);
		json.put("errmsg", "ok");
		json.put("data", count);
		return json;
	}
}
