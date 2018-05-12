package action;

import java.io.File;
import java.util.HashMap;
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
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import entity.Inform;
import service.InformService;
import util.SysConstants;
import util.Tools;

@Controller
public class InformAction {
	
	@Resource
	private InformService informService;
	
	private int pageSize = 10;
	
	@RequestMapping("/inform_init")
	public String init(ModelMap map,@RequestParam Map<String, String> paramMap,@RequestParam(value="page", defaultValue="0")int page){
		paramMap.put("map_publisher", Tools.getUTF8(paramMap.get("map_publisher")));
		List<Inform> list = informService.paramQuery(paramMap,page,pageSize);
		int count = informService.paramQueryCount(paramMap);
		int countPage = count%pageSize==0?count/pageSize:count/pageSize+1;
		map.put("count", count);
		map.put("countPage", countPage);
		map.put("page", page);
		map.put("beginTime", paramMap.get("map_begin_time"));
		map.put("endTime", paramMap.get("map_end_time"));
		map.put("publisher", paramMap.get("map_publisher"));
		map.put("list", list);
		map.put("informImgPath", SysConstants.INFORM_FILE_URL);
		return "inform";
	}
	
	@ResponseBody
	@RequestMapping("/inform_add")
	public JSONObject add(MultipartFile[] files,Inform inform){
		JSONObject json = new JSONObject();
		Map<String, String> map = new HashMap<String, String>();
		int type = 1;
		int countLength = 0;
		Pattern r = Pattern.compile("<img.*?/>");
		Matcher m = r.matcher(inform.getContent());
		while (m.find()) {
			Matcher m2 = Pattern.compile("x=\"(.*?)\" src=\"(.*?)\"").matcher(m.group());
			if (m2.find()) {
				map.put(m2.group(1), m2.group(2));
				type = 2;
				countLength += m2.group().length()+7;
			}
		}
		if(countLength!=0&&countLength!=inform.getContent().length()){
			type = 3;
		}
		if(files != null){
			for (int i=0;i<files.length;i++) {
				if(!files[i].isEmpty()&&map.get((i+1)+"")!=null){
					String val = map.get((i+1)+"");
					String fileName = val.substring(val.lastIndexOf("/")+1);
					try {
						File newFile = new File(SysConstants.INFORM_FILE_PATH + fileName);
						if(!newFile.getParentFile().exists()){
							newFile.getParentFile().mkdirs();
			            }
						files[i].transferTo(newFile);
					} catch (Exception e) {
						json.put("errcode", 400);
						json.put("errmsg", "保存文件出现异常！");
						return json;
					}
				}
			}
		}
		inform.setType(type);
		int t = informService.add(inform);
		if(t>0){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 100);
			json.put("errmsg", "新增物业通知失败！");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/inform_up")
	public JSONObject up(MultipartFile[] files,Inform inform){
		JSONObject json = new JSONObject();
		Map<String, String> map = new HashMap<String, String>();
		int type = 1;
		int countLength = 0;
		Pattern r = Pattern.compile("<img.*?/>");
		Matcher m = r.matcher(inform.getContent());
		while (m.find()) {
			Matcher m2 = Pattern.compile("x=\"(.*?)\" src=\"(.*?)\"").matcher(m.group());
			if (m2.find()) {
				map.put(m2.group(1), m2.group(2));
				type = 2;
				countLength += m2.group().length()+7;
			}
		}
		if(countLength!=0&&countLength!=inform.getContent().length()){
			type = 3;
		}
		if(files != null){
			for (int i=0;i<files.length;i++) {
				if(!files[i].isEmpty()&&map.get((i+1)+"")!=null){
					String val = map.get((i+1)+"");
					String fileName = val.substring(val.lastIndexOf("/")+1);
					try {
						File newFile = new File(SysConstants.INFORM_FILE_PATH + fileName);
						if(!newFile.getParentFile().exists()){
							newFile.getParentFile().mkdirs();
			            }
						files[i].transferTo(newFile);
					} catch (Exception e) {
						json.put("errcode", 400);
						json.put("errmsg", "保存文件出现异常！");
						return json;
					}
				}
			}
		}
		inform.setType(type);
		
		int t = informService.up(inform);
		if(t>0){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 120);
			json.put("errmsg", "修改物业通知失败！");
		}
		return json;
	}
}
