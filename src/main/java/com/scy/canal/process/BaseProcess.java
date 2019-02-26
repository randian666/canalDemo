package com.scy.canal.process;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.support.DefaultConversionService;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.scy.canal.entity.CanalRowChange;
import com.scy.canal.entity.DateConverter;

/**
 * 数据消费基类
 * 
 * @author liuxun
 *
 */
public abstract class BaseProcess {

	private DefaultConversionService convertor = new DefaultConversionService() {
		{
			addConverter(new DateConverter());
		}
	};

	private Map<String, Map<String, Field>> clzFieldsCached;

	/**
	 * 处理数据添加方法
	 * @param rowChange
	 * @return
	 */
	public abstract boolean processInsert(CanalRowChange rowChange);

	/**
	 * 处理修改数据方法
	 * @param rowChange
	 * @return
	 */
	public abstract boolean processUpdate(CanalRowChange rowChange);

	
	/**
	 * 处理删除数据方法
	 * @param rowChange
	 * @return
	 */
	public abstract boolean processDelete(CanalRowChange rowChange);

	/**
	 * 数据转换方法
	 * 
	 * @param rowChange
	 * @param clz
	 * @param isAfter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T> List<T> processConvert(CanalRowChange rowChange, Class<T> clz, boolean isAfter)
			throws InstantiationException, IllegalAccessException {

		if (rowChange == null || clz == null) {
			return null;
		}

		if (clzFieldsCached == null) {
			clzFieldsCached = new HashMap<String, Map<String, Field>>();
		}

		Map<String, Field> fieldscached = clzFieldsCached.get(clz.getName());

		if (fieldscached == null || fieldscached.size() <= 0) {
			fieldscached = new HashMap<String, Field>();
			for (Field field : clz.getDeclaredFields()) {
				field.setAccessible(true);
				fieldscached.put(field.getName().toLowerCase(), field);
			}
			clzFieldsCached.put(clz.getName(), fieldscached);
		}

		List<RowData> rowDatas = rowChange.getRowData();

		if (rowDatas == null || rowDatas.size() <= 0) {
			return null;
		}

		List<T> beans = new ArrayList<T>();

		for (RowData rowData : rowDatas) {

			T bean = clz.newInstance();

			List<Column> cols = isAfter ? rowData.getAfterColumnsList() : rowData.getBeforeColumnsList();

			if (cols == null || cols.size() <= 0) {
				return null;
			}

			int count = 0;
			for (Column col : cols) {

				String name = col.getName();
				String value = col.getValue();

				Field field = fieldscached.get(name.toLowerCase());

				if (field == null) {
					continue;
				}

				if (StringUtils.isNotBlank(value)) {
					Object nvalue = convertor.convert(value, field.getType());
					field.set(bean, nvalue);
				}
				count++;
			}

			if (count != fieldscached.size()) {
				return null;
			}
			beans.add(bean);
		}

		if (beans.size() != rowDatas.size()) {
			return null;
		}

		return beans;

	}

}
