package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import model.RecModel;
import service.RecService;

@RestController
public class RedisController {

	@Autowired
	RecService recSerImpl;

	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ModelAndView getByModel(@ModelAttribute("recmodel") RecModel rm) {
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
	
	@RequestMapping(value="/getContent" ,method=RequestMethod.GET)
	public ModelAndView getByContent(@ModelAttribute("recmodel") RecModel rm){
	
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
