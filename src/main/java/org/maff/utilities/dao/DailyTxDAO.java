package org.maff.utilities.dao;

import java.util.List;

import org.maff.utilities.model.DailyTransaction;

public interface DailyTxDAO {

	public void add(DailyTransaction entity);
	public void delete(DailyTransaction entity);
	public void update(DailyTransaction entity);
	public DailyTransaction find(int key);
	public void batchInsert(List<DailyTransaction> tx );
	public void deleteAllDailyTxRecord();
	
}
