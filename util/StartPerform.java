package util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import entity.Weather;
import service.WeatherService;

/**
 * 项目启动执行
 * 线程模式
 * 接收udp发送给本机的气象指数数据
 * @author wilson
 *
 */
@Component
public class StartPerform extends Thread{

	@Resource
	private WeatherService ws;
	
	public void run() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(SysConstants.UDP_HOST);
			byte[] receiveData = new byte[8];
			while (true) {
				// 构造数据包接收数据
				DatagramPacket receivePacket = new DatagramPacket(
						receiveData, receiveData.length);
				// 接收数据
				serverSocket.receive(receivePacket);
				// 解析数据
				byte[] b = receivePacket.getData();
				
				String s = Tools.byte2HexStr(b);
				
				String[] strings = s.split(" ");
				int b3 = Integer.valueOf(strings[3],16);
				int b4 = Integer.valueOf(strings[4],16);
				int S = (b3>>7) & 0x1;
			    int E = (b3>>3) & 0x0f;
			    int M = ((b3&0x7) << 8) | b4;
			    // (0.01*M)*2^E
			    float value = (float) ((0.01*M) * (1 << E));
			    if (S == 1) {
			        value = 0 - value;
			    }
			  
			    Weather w = ws.getMaxData();
			    if(w == null){
			    	w = new Weather();
			    }else{
			    	if(new Date().getTime()-w.getCreateTime().getTime()<10000){
			    		continue;
			    	}
			    }
			    if("01".equals(strings[2])){
			    	w.setTemp(value);
			    }else if ("02".equals(strings[2])) {
			    	w.setHumi(value);
				}else if ("03".equals(strings[2])) {
			    	w.setRadi(value);
				}else if ("04".equals(strings[2])) {
			    	w.setRait(value);
				}else if ("05".equals(strings[2])) {
			    	w.setRaif(value);
				}else if ("06".equals(strings[2])) {
			    	w.setWinr(value);
				}else if ("07".equals(strings[2])) {
			    	w.setWins(value);
				}else if ("08".equals(strings[2])) {
			    	w.setDens(value);
				}else if ("09".equals(strings[2])) {
			    	w.setEnth(value);
				}else if ("0A".equals(strings[2])) {
			    	w.setPres(value);
				}
			    ws.add(w);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@PostConstruct
	public void receiveWeatherData(){
		this.start();
	}
}
