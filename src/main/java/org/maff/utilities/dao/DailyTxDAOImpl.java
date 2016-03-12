package org.maff.utilities.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.maff.utilities.dao.util.DaoUtil;
import org.maff.utilities.model.DailyTransaction;



public class DailyTxDAOImpl implements DailyTxDAO{

	private static final Logger logger = Logger.getLogger(DailyTxDAOImpl.class);
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void add(DailyTransaction entity) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(entity);
		tx.commit();
		session.close();
	}

	@Override
	public void delete(DailyTransaction entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(DailyTransaction entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DailyTransaction find(int key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void batchInsert(List<DailyTransaction> txList) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Date createdDate = new Date();

		int ctr = 0;
		for (DailyTransaction entry: txList){
			entry.setCreatedDate(createdDate);
			entry.setUpdatedDate(createdDate);
			
			session.save(entry);
				if (ctr % 50 == 0){
					session.flush();
					session.clear();
				}
		}
		
		tx.commit();
		session.close();
	}

	@Override
	public void deleteAllDailyTxRecord(){
		logger.info("Deleting all records from Daily Transactions table ");
		
		String sql = "delete from DailyTransaction";
		
		logger.info("SQL QUERY :::: " + sql);
		
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		session.createSQLQuery(sql).executeUpdate();
		
		tx.commit();
		session.close();

	}
	
	/**
	 * Retrieve Daily Transactions based on the reference numbers input
	 * These reference numbers are transactions which haven't been posted
	 * on Beam annd have encountered credit card hash error
	 * 
	 * @author Lee
	 * Created Date 2016-03-12
	 * */
	public List<DailyTransaction> retrieveRefNum(List<String> listOfRefNum){
		
		logger.info("Retrieving Daily Transaction information from Daily Transactions Table ");
		DaoUtil subProcess = new DaoUtil();
		List<DailyTransaction> listOfDlyTx = new ArrayList<DailyTransaction>();
		
		String refNoParams = subProcess.refNumParamBuilder(listOfRefNum);
		
		String sql = "select * from DailyTransaction where referenceNumber in ("+ refNoParams + ")";
		
		logger.info("SQL QUERY :::: " + sql);
		
		Session session = this.sessionFactory.openSession();
//		Transaction tx = session.beginTransaction();
		
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(DailyTransaction.class);
		
		listOfDlyTx = query.list();
		
		logger.info("DailyTransactions Retrieved from DB: ");
		for (DailyTransaction tx: listOfDlyTx){
			
			logger.info(tx.getReferenceNumber());
		}
		
		return listOfDlyTx;
	}
	
}
