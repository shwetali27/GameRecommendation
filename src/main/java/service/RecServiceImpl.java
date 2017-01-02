package service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.RecModel;
import recInterface.RecDaoInterface;
import recInterface.RedisInterface;

@Service
public class RecServiceImpl implements RecService{

	@Autowired
	RedisInterface redisImpl;
	
	@Autowired
	RecDaoInterface recDaoImpl;
	
	public void addVisitor(RecModel rm) {
		System.out.println("visitor_id: "+rm.getmVisitorID()+" visitor mview:"+rm.getmView());
		redisImpl.addVisitorView(rm);
		redisImpl.addVisitorDownload(rm);
		redisImpl.addContentID(rm);
	}
	
	public void createContentIDMap(){
		redisImpl.createContentIDMap();
	}
	
	public Set<String> getSuggestion(RecModel rm){
		return redisImpl.getSuggestion(rm.getmContentID(), rm.getmVisitorID());
		
	}
	public void addToContentMap(RecModel rm){
		redisImpl.addToContentMap(rm);
	}
	
	public void addToDao(RecModel rm){
		recDaoImpl.addRecord(rm);
	}
	
	public RecModel getbyContentID(String pContentID){
		return recDaoImpl.getByContentID(pContentID);
	}
	
	public RecModel getbyContentName(String pContentName) {
		return recDaoImpl.getByContentName(pContentName);
		
	}
}
