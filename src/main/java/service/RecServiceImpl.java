package service;

import java.util.List;
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
	
	public void addVisitor(RecModel pRecModelObject) {
		//System.out.println("visitor_id: "+pRecModelObject.getmVisitorID()+" visitor mview:"+pRecModelObject.getmView());
		redisImpl.addVisitorView(pRecModelObject);
		redisImpl.addVisitorDownload(pRecModelObject);
		redisImpl.addContentID(pRecModelObject);
	}
	
	public void createContentIDMap(){
		redisImpl.createContentIDMap();
	}
	
	public Set<String> getSuggestion(RecModel pRecModelObject){
		return redisImpl.getSuggestion(pRecModelObject.getmContentID(), pRecModelObject.getmVisitorID());
		
	}
	public void addToContentMap(RecModel pRecModelObject){
		redisImpl.addToContentMap(pRecModelObject);
	}
	
	public void addToDao(RecModel pRecModelObject){
		recDaoImpl.addRecord(pRecModelObject);
	}
	
	public RecModel getbyContentID(String pContentID){
		return recDaoImpl.getByContentID(pContentID);
	}
	
	public RecModel getbyContentName(String pContentName) {
		return recDaoImpl.getByContentName(pContentName);
		
	}

	@Override
	public List<String> getSuggestion(String pContentName) {
		
		return recDaoImpl.getSuggestion(pContentName);
	}
}
