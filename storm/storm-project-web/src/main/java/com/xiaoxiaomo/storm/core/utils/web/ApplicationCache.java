package com.xiaoxiaomo.storm.core.utils.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import com.xiaoxiaomo.storm.core.CoreConst;
import com.xiaoxiaomo.storm.core.utils.MyConfigUtils;

/**
 * 应用缓存。 所有需要放到ServletContext中的缓存信息一律在这里添加，所有web应用加载， 在页面使用el表达式读取这些信息。目的是统一管理，
 * 防止丢三落四和冲突。
 * 
 * @author XXO
 * 
 */
@Component
public final class ApplicationCache
{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private String ctx = null;
	/**
	 * 把全局参数配置注入到ServletContext的属性中
	 * 
	 * @param servletContext
	 */
	public void cacheTo(ServletContext servletContext) {
		this.ctx = servletContext.getContextPath();
		
		Map<String, String> map = init();
		
		System.err.println("ServletContext中的缓存数据");
		for (Entry<String, String> entry : map.entrySet()) {
			String value = entry.getValue();
			String name = entry.getKey();
			servletContext.setAttribute(name, value);
			System.err.println("      【" + name + "】:【" + value+"】");
		}
	}

	private Map<String, String> init() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		String osName = System.getProperty("os.name");
		if(osName.toLowerCase().contains("windows")){
			loadDataFromFile(map);
			System.err.println("从文件windows.config.properties加载配置参数");
		}else{
			loadDataFromDb(map);
			System.err.println("从数据库表config_param加载配置参数");
		}
		map.put("home", ctx+"/home");
		map.put("static_home", ctx);
		
		map.put("cms", ctx+"/cms");
		map.put("static_cms", ctx);
		
		map.put("crm", ctx+"/crm");
		map.put("static_crm", ctx);
		
		map.put("upload_server", ctx+"/upload");
		
		map.put("login_cookie_key", "userId");
		map.put("login_cookie_domain", ".xiaoxiaomo.com");
		map.put("login_cookie_max_age", "3600");
		
		map.put("userlogin_redirect_url", ctx+"/userlogin");
		map.put("crmindex_redirect_url", ctx+"/crmindex");
		
		map.put("home_image_url", ctx+"/image/");
		return map;
	}

	private void loadDataFromDb(HashMap<String, String> map) {
		map.put("server_ip_video", MyConfigUtils.getValueByName(CoreConst.SERVER_IP_VIDEO));
		map.put("home_upload_image_dir", MyConfigUtils.getValueByName(CoreConst.HOME_UPLOAD_IMAGE_DIR));
		map.put("home_upload_file_dir", MyConfigUtils.getValueByName(CoreConst.HOME_UPLOAD_FILE_DIR));
	}
	
	private void loadDataFromFile(HashMap<String, String> map){
		try {
			ResourcePropertySource propertySource = new ResourcePropertySource("resource", "classpath:windows.config.properties");
			if(propertySource==null || propertySource.getSource()==null){
				throw new RuntimeException("找不到配置文件windows.config.properties");
			}
			map.put("server_ip_video", propertySource.getProperty("server_ip_video").toString());
			map.put("home_upload_image_dir", propertySource.getProperty("home_upload_image_dir").toString());
			map.put("home_upload_file_dir", propertySource.getProperty("home_upload_file_dir").toString());
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("读取配置文件windows.config.properties内容出错", e);
		}
	}
}
