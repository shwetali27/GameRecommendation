package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import model.RecModel;
import recInterface.RecDaoInterface;

public class RecDaoImpl implements RecDaoInterface {

	@Autowired
	SessionFactory sessionFactory;

	public void addRecord(RecModel record) {
		if (getByContentID(record.getmContentID())==null) {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			//System.out.println("Inside dao add " + record.getmVisitorID());
			try {
				System.out.println("Transaction started..");
				session.save(record);
				System.out.println("Transaction commit");
				tr.commit();
			} catch (Exception e) {
				tr.rollback();
				session.close();
			}
		}else{
			//System.out.println("Already exists");
		}
	}

	/*@SuppressWarnings("unchecked")
	public boolean getByContentID(String pContentID) {
		Session session = sessionFactory.openSession();

		Query query = session.createQuery("from RecModel u where u.mContentID= :ContentID");
		query.setParameter("ContentID", pContentID);
		//query.
		List<String> result = query.getResultList(); // getting hql result
		List<RecModel> res = query.getResultList();
		System.out.println("Inside getcontentbyid : "+res.size());
		// checking result
		if (result != null && result.size() > 0){
			System.out.println("Inside getcontentbyid : contentid "+res.get(0).getmContentID());
			return true;}
		else
			return false;

	}*/
	
	@SuppressWarnings("unchecked")
	public RecModel getByContentID(String pContentID) {
		Session session = sessionFactory.openSession();

		Query<RecModel> query = session.createQuery("from RecModel u where u.mContentID= :ContentID");
		query.setParameter("ContentID", pContentID);
		//query.
		//List<String> result = query.getResultList(); // getting hql result
		
		List<RecModel> res = query.getResultList();
		//System.out.println("Inside getcontentbyid : "+res.size());
		// checking result
		if (res != null && res.size() > 0){
			//System.out.println("Inside getcontentbyid : contentid "+res.get(0).getmContentID());
			return res.get(0);}
		else
			return null;

	}
	
	@SuppressWarnings("unchecked")
	public RecModel getByContentName(String pContentName) {
		Session session = sessionFactory.openSession();

		Query<RecModel> query = session.createQuery("from RecModel u where u.mContentName like :ContentName");
		query.setParameter("ContentName", pContentName);
		//query.
		//List<String> result = query.getResultList(); // getting hql result
		List<RecModel> res = query.getResultList();
		System.out.println("Inside getcontentbyname : "+res.size());
		// checking result
		if (res != null && res.size() > 0){
			System.out.println("Inside getcontentbyname : contentid "+res.get(0).getmContentID()+" content name:"+res.get(0).getmContentName());
			return res.get(0);}
		else
			return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<String> getSuggestion(String pContentName) {
		Session session = sessionFactory.openSession();

		System.out.println("Inside Suggestion"+pContentName);
		Query query = session.createQuery("from RecModel u where u.mContentName like :ContentName");
		query.setString("ContentName", pContentName+"%");
		
		List<RecModel> res = query.list();
		System.out.println(res.size());
		List<String> list = new ArrayList();
		for(int i=0;i<res.size();i++){
			list.add(res.get(i).getmContentName());
		}
		System.out.println("Result :-"+list);
		System.out.println("Inside getcontentbyname : "+list.size());
		// checking result
		return list;

	}
}
