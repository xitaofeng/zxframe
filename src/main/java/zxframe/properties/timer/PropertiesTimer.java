package zxframe.properties.timer;

import java.util.concurrent.TimeUnit;

import zxframe.config.ZxFrameConfig;
import zxframe.properties.PropertiesCache;
import zxframe.properties.service.PropertiesService;
import zxframe.task.TaskRunnable;
import zxframe.task.ThreadResource;
import zxframe.task.Timer;
import zxframe.util.ServiceLocator;
@Timer
public class PropertiesTimer implements TaskRunnable{
	private static PropertiesService propertiesService;
	public void run() {
		String version=PropertiesCache.get("system-version");
		if(propertiesService==null) {
			propertiesService = ServiceLocator.getSpringBean("propertiesService");
		}
		String cversion=propertiesService.getListVersion();
		if(!cversion.equals(version)) {
			PropertiesCache.init();
		}
	}
	public void init() {
		if(ZxFrameConfig.useDBProperties) {
			long initialDelay = 60*5;// 第一次延迟多少秒开始执行
			long delay = 60;// 间隔多少秒执行一次
			ThreadResource.getTaskPool().scheduleWithFixedDelay(this, initialDelay,
							delay, TimeUnit.SECONDS);
		}
	}

}
