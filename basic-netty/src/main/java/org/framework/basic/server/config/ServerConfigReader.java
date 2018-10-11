package org.framework.basic.server.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * 功能描述：<code>ConfigurationReader</code>类是配置项读取器。
 * <p>
 * 功能详细描述：系统加载时，本读取器将加载<b>server.properties</b>配置文件并读取所有配置项缓存到系统内存中。
 * 本类为单例实例，获取配置项示例代码如下：
 * 
 * <pre>
 * ServerConfigReader configReader = ServerConfigReader.getInstance();
 * String appServerUrl = configReader.getString(&quot;app.server.port&quot;);
 * </pre>
 *
 */
public final class ServerConfigReader {

    /** 系统配置文件读取器 */
    private static ServerConfigReader configReader = new ServerConfigReader();


	private Properties properties = new Properties();
	
    /**
     * 私有构造器
     */
    private ServerConfigReader() {

    }

    public ServerConfigReader init(String configPath){
        try {
            Resource res = new ClassPathResource(configPath);
            properties.load(res.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + configPath);
        }
        return this;
    }

    /**
     * 获取配置文件读取器对象
     * 
     * @return 配置文件读取器对象
     */
    public static ServerConfigReader getInstance() {
        return configReader;
    }

    /**
     * 根据key获取值
     * 
     * 值类型为字符串
     * 
     * @param key
     *            配置键
     * @return 字符类型的值
     */
    public String getString(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        try {
            return properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * 根据key获取值
     * 
     * 值类型为Int数值类型
     * 
     * @param key
     *            配置键
     * @return Int数值类型的值
     */
    public Integer getInt(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        String value = this.getString(key);
        if(null == value){
            value = "0";
        }
        return Integer.valueOf(value);
    }

    /**
     * 根据key获取值
     * 
     * 值类型为Boolean类型
     * 
     * @param key
     *            配置键
     * @return Boolean类型的值
     */
    public Boolean getBoolean(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        return Boolean.valueOf(this.getString(key));
    }
}
