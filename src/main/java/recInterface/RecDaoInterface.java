package recInterface;

import org.springframework.stereotype.Repository;

import model.RecModel;

@Repository
public interface RecDaoInterface {
	//public RecModel getEntry(String contentId);
	public void addRecord(RecModel pRecord);
	
	public RecModel getByContentID(String pContentID);
	
	public RecModel getByContentName(String pContentName);
}
