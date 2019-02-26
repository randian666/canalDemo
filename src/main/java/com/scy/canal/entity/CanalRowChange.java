package com.scy.canal.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

public class CanalRowChange implements Serializable{

	private static final long serialVersionUID = -90027012566550680L;

	private String schemaName;

	private String tableName;

	private List<RowData> rowData;

	private EventType eventType;

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<RowData> getRowData() {
		return rowData;
	}

	public void setRowData(List<RowData> rowData) {
		this.rowData = rowData;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}