package action;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import entity.ImgAds;
import service.ImgAdsService;
import util.SysConstants;
import util.Tools;

@Controller
public class ImgAdsAction {
	
	@Resource
	private ImgAdsService imgAdsService;
	
	private int pageSize = 10;
	
	@RequestMapping("/imgAds_init")
	public String init(ModelMap map,@RequestParam Map<String, String> paramMap,@RequestParam(value="page", defaultValue="0")int page){
		List<ImgAds> list = imgAdsService.paramQuery(paramMap,page,pageSize);
		int count = imgAdsService.paramQueryCount(paramMap);
		int countPage = count%pageSize==0?count/pageSize:count/pageSize+1;
		map.put("count", count);
		map.put("countPage", countPage);
		map.put("page", page);
		map.put("deleted", paramMap.get("map_deleted"));
		map.put("beginTime", paramMap.get("map_begin_time"));
		map.put("endTime", paramMap.get("map_end_time"));
		map.put("iaImgPath", SysConstants.ADS_FILE_URL);
		map.put("list", list);
		return "img_ads";
	}
	
	@ResponseBody
	@RequestMapping("/imgAds_add")
	public JSONObject add(MultipartFile file,HttpServletRequest request,Integer playLength,Integer orderValue,String playTime,String endTime){
		JSONObject json = new JSONObject();
		int t = 0;
		if(file == null||file.getSize() == 0){
			json.put("errcode",1);
			json.put("errmsg","文件不能为空！");
			return json;
		}
		JSONObject details = new JSONObject();
		String fileName = file.getOriginalFilename();
		details.put("rawFileName", fileName);
		String fnhz = fileName.substring(fileName.lastIndexOf(".")==-1?fileName.length():fileName.lastIndexOf("."),fileName.length());
		String newFileName = new Date().getTime()+fnhz;
		try {
			File newFile = new File(SysConstants.ADS_FILE_PATH + newFileName);
			if(!newFile.getParentFile().exists()){
				newFile.getParentFile().mkdirs();
            }
			file.transferTo(newFile);
		} catch (Exception e) {
			json.put("errcode", 400);
			json.put("errmsg", "保存文件出现异常！");
			return json;
		}
		ImgAds ia = new ImgAds();
		ia.setFileName(newFileName);
		ia.setPlayLength(playLength==null?0:playLength);
		ia.setOrderValue(orderValue==null?0:orderValue);
		ia.setPlayTime(Tools.str2DateTime(playTime));
		ia.setEndTime(Tools.str2DateTime(endTime));
		details.put("fileSize", file.getSize());
		details.put("uploadIp", Tools.getRealRemoteIP(request));
		details.put("filePath", SysConstants.ADS_FILE_PATH + newFileName);
		ia.setDetails(details.toJSONString());
		t = imgAdsService.add(ia);
		if(t>0){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 100);
			json.put("errmsg", "新增物业广告失败！");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/imgAds_del")
	public JSONObject del(int id){
		JSONObject json = new JSONObject();
		ImgAds ia = imgAdsService.getById(id);
		ia.setDeleted(1);
		int t = imgAdsService.up(ia);
		if(t>0){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 120);
			json.put("errmsg", "删除物业广告失败！");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/imgAds_up")
	public JSONObject up(int id,Integer playLength,Integer orderValue,String playTime,String endTime){
		JSONObject json = new JSONObject();
		ImgAds ia = imgAdsService.getById(id);
		if(playLength != null){
			ia.setPlayLength(playLength);
		}
		if(orderValue != null){
			ia.setOrderValue(orderValue);
		}
		ia.setPlayTime(Tools.str2DateTime(playTime));
		ia.setEndTime(Tools.str2DateTime(endTime));
		int t = imgAdsService.up(ia);
		if(t>0){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 120);
			json.put("errmsg", "修改物业广告失败！");
		}
		return json;
	}
}
