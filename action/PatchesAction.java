package action;

import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import service.PatchesService;
import util.SysConstants;
import util.Tools;
import com.alibaba.fastjson.JSONObject;
import entity.Patches;

@Controller
public class PatchesAction {

	@Resource
	private PatchesService ps;
	
	@RequestMapping("/patches_init")
	public String init(ModelMap map){
		List<Patches> list = ps.showAll(0);
		map.put("list", list);
		return "patches";
	}
	
	@ResponseBody
	@RequestMapping("/patches_add")
	public JSONObject add(MultipartFile file,HttpServletRequest request,String version,String verifyMd5){
		JSONObject json = new JSONObject();
		if(file == null||file.getSize() == 0){
			json.put("errcode",1);
			json.put("errmsg","文件不能为空！");
			return json;
		}
		boolean b = ps.verifyV(version,0);
		if(!b){
			json.put("errcode", 3);
			json.put("errmsg", "版本号必须不同!");
			return json;
		}
		JSONObject details = new JSONObject();
		String fileName = file.getOriginalFilename();
		details.put("rawFileName", fileName);
		String fn = fileName.substring(0,fileName.lastIndexOf(".")==-1?fileName.length():fileName.lastIndexOf("."));
		String fnhz = fileName.substring(fn.length(),fileName.length());
		String newFileName = fn+version+fnhz;
		try {
			File newFile = new File(SysConstants.PATCHES_FILE_PATH + newFileName);
			if(!newFile.getParentFile().exists()){
				newFile.getParentFile().mkdirs();
            }
			file.transferTo(newFile);
		} catch (Exception e) {
			json.put("errcode", 400);
			json.put("errmsg", "保存文件出现异常！");
			return json;
		}
		Patches gp = new Patches();
		gp.setVersion(version);
		gp.setFileName(newFileName);
		gp.setVerifyMd5(verifyMd5);
		details.put("fileSize", file.getSize());
		details.put("uploadIp", Tools.getRealRemoteIP(request));
		details.put("filePath", SysConstants.PATCHES_FILE_PATH + newFileName);
		gp.setDetails(details.toJSONString());
		b = ps.add(gp)>0;
		if(b){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 100);
			json.put("errmsg", "新增补丁包失败！");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/patches_up")
	public JSONObject up(String version,int id,@RequestParam(value="deleted", defaultValue="0")int deleted){
		JSONObject json = new JSONObject();
		if(version!=null&&!"".equals(version)){
			boolean b2 = ps.verifyV(version, id);
			if(!b2){
				json.put("errcode", 3);
				json.put("errmsg", "版本号必须不同!");
				return json;
			}
		}
		boolean b = ps.up(version, 0, deleted, null, id);
		if(b){
			json.put("errcode", 0);
			json.put("errmsg", "ok");
		}else{
			json.put("errcode", 120);
			json.put("errmsg", "修改补丁包失败！");
		}
		return json;
	}
}
