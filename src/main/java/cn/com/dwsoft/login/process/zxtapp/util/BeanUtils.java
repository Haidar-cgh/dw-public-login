package cn.com.dwsoft.login.process.zxtapp.util;

/**
 *功能描述 bean转换工具
 * @author sqw
 * @param  
 * @return 
 * @throws 
 * @date 2020/8/11
 */
public class BeanUtils<E, T> {

	/**
	 * dot 转换为Do 工具类
	 * 
	 * @param entity
	 * @param tClass
	 * @return
	 */
	public static <T> T eToT(Object entity, Class<T> tClass) {
		// 判断dto是否为空!
		if (entity == null) {
			return null;
		}
		// 判断DoClass 是否为空
		if (tClass == null) {
			return null;
		}
		try {
			T newInstance = tClass.newInstance();
			org.springframework.beans.BeanUtils.copyProperties(entity, newInstance);
			// Dto转换Do
			return newInstance;
		} catch (Exception e) {
			return null;
		}
	}
	// 后面集合类型带封装
}
