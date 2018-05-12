package action;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import entity.Acc;
import service.AccService;

@Controller
public class AccAction {

	@Resource
	private AccService as;
	
	private int pageSize = 10;
	
	@RequestMapping("/acc_init")
	public String init(ModelMap map,@RequestParam Map<String, String> paramMap,@RequestParam(value="page", defaultValue="0")int page){
		List<Acc> list = as.paramQuery(paramMap,page,pageSize);
		int count = as.paramQueryCount(paramMap);
		int countPage = count%pageSize==0?count/pageSize:count/pageSize+1;
		map.put("count", count);
		map.put("countPage", countPage);
		map.put("page", page);
		map.put("fromUri", paramMap.get("map_fromUri"));
		map.put("toUri", paramMap.get("map_toUri"));
		map.put("list", list);
		return "acc";
	}
}
