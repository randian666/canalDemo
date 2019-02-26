package com.scy.canal.process;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.scy.canal.entity.CanalRowChange;
import com.scy.canal.entity.Goods;


public class GoodsProcess extends BaseProcess{

	private static final Log logger = LogFactory.getLog(GoodsProcess.class);
	
	@Override
	public boolean processInsert(CanalRowChange rowChange) {
		try {
			List<Goods> data = super.processConvert(rowChange, Goods.class, true);
			logger.info("添加前"+data);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean processUpdate(CanalRowChange rowChange) {
		try {
			List<Goods> data = super.processConvert(rowChange, Goods.class, false);
			List<Goods> data2 = super.processConvert(rowChange, Goods.class, true);
			logger.info("修改前："+data);
			logger.info("修改后："+data2);
			return true;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
		
	}

	@Override
	public boolean processDelete(CanalRowChange rowChange) {
		try {
			List<Goods>	data = super.processConvert(rowChange, Goods.class, false);
			logger.info("删除前："+data);
			return true;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

}
