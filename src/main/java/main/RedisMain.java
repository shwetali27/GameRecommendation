package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.web.servlet.ModelAndView;

import model.RecModel;
import service.RecServiceImpl;

public class RedisMain {
	
	RecServiceImpl recSerImpl=new RecServiceImpl();
	
	public ModelAndView getByModel( RecModel rm) {
		System.out.println("Inside redis controller");
		List<RecModel>suggestionList=new ArrayList<RecModel>();
		recSerImpl.addVisitor(rm);
		recSerImpl.addToContentMap(rm);
		Set<String> suggestionSet= recSerImpl.getSuggestion(rm);
		Iterator<String> suggestionSetIterator=suggestionSet.iterator();
		while(suggestionSetIterator.hasNext()){
			suggestionList.add(recSerImpl.getbyContentID(suggestionSetIterator.next()));
		}
		suggestionList.forEach(x->System.out.print(" "+x.getmContentID()+" "+x.getmCategoryName()));
		return new ModelAndView("suggestionPage","suggestionList",suggestionList);
	}
	
	
	public ModelAndView getByContent(RecModel rm){
		System.out.println("Inside redis controller: contentid :"+rm.getmContentID());
		List<RecModel>suggestionList=new ArrayList<RecModel>();
		Set<String> suggestionSet= recSerImpl.getSuggestion(rm);
		Iterator<String> suggestionSetIterator=suggestionSet.iterator();
		while(suggestionSetIterator.hasNext()){
			suggestionList.add(recSerImpl.getbyContentID(suggestionSetIterator.next()));
		}
		return new ModelAndView("suggestionPage","suggestionList",suggestionList);
	}
}
