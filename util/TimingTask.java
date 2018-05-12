package util;

import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.WeatherService;

@Component
public class TimingTask{

	@Resource
	private WeatherService ws;
	
	 @Scheduled(cron="0 0 3 * * *")//每天03:00执行
     public void task(){
		 ws.helpWeather();
	 }
}
