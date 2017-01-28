package implimentation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import model.RecModel;
import recInterface.RedisInterface;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisImpl implements RedisInterface {

	/* Connection to local redis server */
	final static Jedis redisConnect = new Jedis("localhost");
	static Utility u = new Utility();
	Properties prop = u.getProperties();
	Logger log = Logger.getLogger(RedisImpl.class);

	/*
	 * To check, fetch visitor_view_hashset from redis. If set exist add new
	 * visitor to set otherwise create new set.
	 */
	public void addVisitorView(RecModel pRecModelObject) {
		redisConnect.select(3);

		/* create visitor_id_set*/
		redisConnect.sadd(prop.getProperty("VISITOR_SET"), pRecModelObject.getmVisitorID()
				.substring(Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))));

		/* Fetch visitor_id_hashset containing the value list of Content id and field value is visitor id from redis */
		String record = redisConnect.hget(
				prop.getProperty("VISITOR_ID_VIEW_SET") + ":" + pRecModelObject.getmVisitorID().substring(
						Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
				pRecModelObject.getmVisitorID());

		/* If set exist add new visitor to set otherwise create new set. */
		Set<String> recordSet = new HashSet<String>();
		if (record != null) {
			//System.out.println(pRecModelObject.getmVisitorID());
			//System.out.println("Record"+record);
			if (Integer.parseInt(pRecModelObject.getmView()) > 0) {
				recordSet = u.toSet(record);
				//System.out.println("RecordSet"+recordSet);
				addToViewSet(recordSet, pRecModelObject.getmVisitorID(), pRecModelObject.getmContentID());
			}

		} else {
			addToViewSet(recordSet, pRecModelObject.getmVisitorID(), pRecModelObject.getmContentID());
		}
	}

	/* Add record to visitor_view_hashset */
	private void addToViewSet(Set<String> recordSet, String visitorID, String contentID) {
		String contentIDString = null;
		try {
			recordSet.add(contentID);
		} catch (NullPointerException e) {
			log.debug(e);
			log.info(e);
		}
		contentIDString = u.toJson(recordSet);
		//System.out.println("contentIDString"+contentIDString);
		redisConnect.hset(
				prop.getProperty("VISITOR_ID_VIEW_SET") + ":" + visitorID.substring(
						Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
				visitorID, contentIDString);
	}

	/*
	 * To check, fetch visitor_download_set from redis. If set exist add new
	 * visitor to set otherwise create new set.
	 */
	public void addVisitorDownload(RecModel rm) {
		redisConnect.select(3);
		/* create visitor_id_set */
		redisConnect.sadd(prop.getProperty("VISITOR_SET"), rm.getmVisitorID()
				.substring(Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))));

		/* Fetch visitor_id_hashset from redis */
		String record = redisConnect.hget(
				prop.getProperty("VISITOR_ID_DOWNLOAD_SET") + ":" + rm.getmVisitorID().substring(
						Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
				rm.getmVisitorID());

		/* If set exist add new visitor to set otherwise create new set. */
		Set<String> recordSet = new HashSet<String>();
		if (record != null) {
			if (Integer.parseInt(rm.getmDownload()) > 0) {
				recordSet = u.toSet(record);
				addToDownloadSet(recordSet, rm.getmVisitorID(), rm.getmContentID());
			}
		} else {
			addToDownloadSet(recordSet, rm.getmVisitorID(), rm.getmContentID());
		}
	}

	/* Add record to visitor_download_set */
	private void addToDownloadSet(Set<String> recordSet, String visitorID, String contentID) {
		String contentIDString = null;
		try {
			recordSet.add(contentID);
		} catch (NullPointerException e) {
			log.debug(e);
			log.info(e);
		}
		contentIDString = u.toJson(recordSet);
		redisConnect.hset(
				prop.getProperty("VISITOR_ID_DOWNLOAD_SET") + ":" + visitorID.substring(
						Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
				visitorID, contentIDString);
	}

	/* To create content_id_set for */
	public void addContentID(RecModel rm) {
		redisConnect.select(3);
		/*System.out.println("Inside add content id");
		System.out.println("Content id add : " + rm.getmContentID());*/

		String contentString = null;

		/* Create json string of content_id information */
		contentString = u.createContent(rm.getmContentName(), rm.getmCategoryName());

		/* Set contentId json string in content_id_hash */
		if (Integer.parseInt(rm.getmContentID()) > 100) {

			//System.out.println("ContentString"+contentString);
			redisConnect.hset(
					prop.getProperty("CONTENT_ID_SET") + ":" + rm.getmContentID().substring(
							Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
					rm.getmContentID(), contentString);
		} else {
			
			/*System.out.println(prop.getProperty("CONTENT_ID_SET") + ":" + rm.getmContentID()+" "+rm.getmContentID()+" "+
					contentString);*/
			redisConnect.hset(prop.getProperty("CONTENT_ID_SET") + ":" + rm.getmContentID(), rm.getmContentID(),
					contentString);
		}
	}

	/* Create content map for recommendation using visitor_views_set */
	public void createContentIDMap() {
		redisConnect.select(3);
		String nextViewVisitorID, nextDownloadVisitorID;
		String[] contentIDViewSetArr = null, contentIDDownloadSetArr = null;
		Set<String> contentIDDownloadSet, contentIDViewSet;
		Iterator<String> mapViewKeySetIterator, mapDownloadKeySetIterator;

		try {
			/* To import the visitor_id hash keys */
			Set<String> visitorSet = redisConnect.smembers(prop.getProperty("VISITOR_SET"));
			String[] visitorSetArray = (String[]) visitorSet.toArray(new String[visitorSet.size()]);
			Arrays.sort(visitorSetArray);
			for (int i = 0; i < visitorSetArray.length; i++) {
				//System.out.print(" " + visitorSetArray[i]);
			}

			/**/
			for (int i = 0; i < visitorSetArray.length; i++) {
				//System.out.println("\nvisitorSetArray" + visitorSetArray[i]);
				//set for visitor_id containing view set starting from particular visitor set i.e. 100 200 etc 
				Set<String> mapViewKeySet = getVisitorIDSet(prop.getProperty("VISITOR_ID_VIEW_SET"),
						visitorSetArray[i]);
				//set for visitor_id containing download set
				Set<String> mapDownloadKeySet = getVisitorIDSet(prop.getProperty("VISITOR_ID_DOWNLOAD_SET"),
						visitorSetArray[i]);
				
				mapViewKeySetIterator = mapViewKeySet.iterator();
				mapDownloadKeySetIterator = mapDownloadKeySet.iterator();
				
				while (mapDownloadKeySetIterator.hasNext() || mapViewKeySetIterator.hasNext()) {

					nextViewVisitorID = mapViewKeySetIterator.next();//visitor ID
					nextDownloadVisitorID = mapDownloadKeySetIterator.next();
					contentIDViewSet = getcontentIDArray("VISITOR_ID_VIEW_SET", visitorSetArray[i], nextViewVisitorID);
					contentIDDownloadSet = getcontentIDArray("VISITOR_ID_DOWNLOAD_SET", visitorSetArray[i],
							nextDownloadVisitorID);

					contentIDViewSet.removeAll(contentIDDownloadSet);

					contentIDViewSetArr = (String[]) contentIDViewSet.toArray(new String[contentIDViewSet.size()]);
					contentIDDownloadSetArr = (String[]) contentIDDownloadSet
							.toArray(new String[contentIDDownloadSet.size()]);

					/*System.out.println("View length : " + contentIDViewSetArr.length + " Download length: "
							+ contentIDDownloadSetArr.length);*/

					if ((contentIDDownloadSetArr.length) > 1) {

						setContentMap(contentIDDownloadSetArr, prop.getProperty("two"));

					}
					if ((contentIDViewSetArr.length) > 1) {

						setContentMap(contentIDViewSetArr, prop.getProperty("one"));

					}
				}
			}

		} catch (NullPointerException e) {
			log.debug(e);
			log.info(e);
		} catch (JedisConnectionException e) {
			log.debug(e);
			log.info(e);
		}
	}

	private void setContentMap(String[] contentIDSetArr, String value) {
		String contentString = null;
		//System.out.println("Array length"+contentIDSetArr.length);
		
		for (int k = 0; k < contentIDSetArr.length; k++) {
			for (int j = 0; j < contentIDSetArr.length; j++) {
				if (contentIDSetArr[k] != contentIDSetArr[j]) {
					/*getting content string from redis data for
					 * for mapping between two content ids
					*/
					contentString = getContentMap(contentIDSetArr[k], contentIDSetArr[j]);
					if (contentString != null) {
						//System.out.println("contentString before"+contentString);
						contentString = String.valueOf((Integer.parseInt(contentString) + Integer.parseInt(value)));
						//System.out.println("contentString"+contentString);
						setContentMapValue(contentIDSetArr[k], contentIDSetArr[j], contentString);
					} else {
						//System.out.println("contentString"+contentString);
						setContentMapValue(contentIDSetArr[k], contentIDSetArr[j], value);
					}
				}
			}
		}
	}

	private Set<String> getVisitorIDSet(String setName, String setKey) {
		/* To import the map of each visitor_id key */
		Map<String, String> visitorMapView = redisConnect.hgetAll(setName + ":" + setKey);
		Set<String> mapKeySet = visitorMapView.keySet();
		//returns the key values for perticular hashmap
		return mapKeySet;
	}

	private Set<String> getcontentIDArray(String setName, String setKey, String nextVisitorID) {
		/*
		 * To import json of content_id of view_set for particular visitor_id
		 */
		String contentIDList = redisConnect.hget(prop.getProperty(setName) + ":" + setKey, nextVisitorID);

		/*
		 * To convert json containing content_id of view_set for particular
		 * visitor_id to set and return it.
		 */
		return u.toSet(contentIDList);
	}

	/* Add to content_map */
	private void setContentMapValue(String visitorID1, String visitorID2, String contentString) {
		if (Integer.parseInt(visitorID1) > 100) {
			redisConnect.hset(
					prop.getProperty("CONTENT_MAP") + ":"
							+ visitorID1.substring(Integer.parseInt(prop.getProperty("low")),
									Integer.parseInt(prop.getProperty("high"))),
					visitorID1 + ":" + visitorID2, contentString);
		} else {
			redisConnect.hset(prop.getProperty("CONTENT_MAP") + ":" + visitorID1, visitorID1 + ":" + visitorID2,
					contentString);
		}

	}

	/* Fetch from content_map */
	private String getContentMap(String contentID1, String contentID2) {
		//System.out.println(contentID1+":::::::::"+contentID2);
		String contentString;
		if (Integer.parseInt(contentID1) > 100) {
			contentString = redisConnect.hget(
					prop.getProperty("CONTENT_MAP") + ":" + contentID1.substring(
							Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
					contentID1 + ":" + contentID2);
		} else {
			contentString = redisConnect.hget(prop.getProperty("CONTENT_MAP") + ":" + contentID1,
					contentID1 + ":" + contentID2);
		}
		return contentString;
	}

	public void addToContentMap(RecModel rm) {
		String contentIDViewString = redisConnect.hget("VISITOR_ID_VIEW_SET:" + rm.getmVisitorID()
				.substring(Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
				rm.getmVisitorID());
		Set<String> contentIDViewSet = u.toSet(contentIDViewString);
		String contentIDDownloadString = redisConnect.hget("VISITOR_ID_DOWNLOAD_SET:" + rm.getmVisitorID()
				.substring(Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
				rm.getmVisitorID());
		Set<String> contentIDDownloadSet = u.toSet(contentIDDownloadString);

		contentIDViewSet.removeAll(contentIDDownloadSet);

		String[] contentIDViewSetArr = (String[]) contentIDViewSet.toArray(new String[contentIDViewSet.size()]);
		String[] contentIDDownloadSetArr = (String[]) contentIDDownloadSet
				.toArray(new String[contentIDDownloadSet.size()]);

		/*System.out.println(
				"View length : " + contentIDViewSetArr.length + " Download length: " + contentIDDownloadSetArr.length);*/
		if ((contentIDDownloadSetArr.length) > 1) {
			//System.out.println("inside download");
			setContentMap(contentIDDownloadSetArr, prop.getProperty("two"));

		}
		if ((contentIDViewSetArr.length) > 1) {
			//System.out.println("inside view");
			setContentMap(contentIDViewSetArr, prop.getProperty("one"));

		}
	}

	public Set<String> getSuggestion(String contentID, String visitorID) {
		redisConnect.select(3);
		/* Fetching content_map from redis */
		Map<String, String> contentMap = redisConnect.hgetAll(prop.getProperty("CONTENT_MAP") + ":" + contentID
				.substring(Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))));

		/* Getting field values set from content_map eg: 1916233:1920562*/
		Set<String> contentIDKeySet = contentMap.keySet();

		String[] contentIDKeyArray = (String[]) contentIDKeySet.toArray(new String[contentIDKeySet.size()]);

		System.out.println(contentIDKeyArray.length);
		/*
		 * To create ordered set of content_id for suggestion with content_map
		 * values as score
		 */
		for (int i = 0; i < contentIDKeyArray.length; i++) {
			String[] contentIDValues = contentIDKeyArray[i].split(":");
			
			if (contentIDValues[0].equals(contentID)) {
				//System.out.println(contentIDValues[0]+" "+contentIDValues[1]);
				redisConnect.zadd(
						prop.getProperty("SUGGESTION_LIST") + ":"
								+ contentIDValues[Integer.parseInt(prop.getProperty("low"))],
						Double.parseDouble(contentMap.get(contentIDKeyArray[i])),
						contentIDValues[Integer.parseInt(prop.getProperty("one"))]);
				/*System.out.println(prop.getProperty("SUGGESTION_LIST") + ":"
								+ contentIDValues[Integer.parseInt(prop.getProperty("low"))]+"-----"+
						Double.parseDouble(contentMap.get(contentIDKeyArray[i]))+"-----:"+
						contentIDValues[Integer.parseInt(prop.getProperty("one"))]);*/
			}
		}

		/* Getting suggestion set from redis for given content_id */
		Set<String> redisSuggestionList = redisConnect.zrevrange(prop.getProperty("SUGGESTION_LIST") + ":" + contentID,
				0, -1);
		System.out.println(redisSuggestionList.size());

		//visitorID="479a7d0a6a36534b";
		if (visitorID != null) {
			/* Fetching download_set for given visitor_id */
			String visitorContentIDDownload = redisConnect.hget(
					prop.getProperty("VISITOR_ID_DOWNLOAD_SET") + ":" + visitorID.substring(
							Integer.parseInt(prop.getProperty("low")), Integer.parseInt(prop.getProperty("high"))),
					visitorID);

			Set<String> visitorContentIDDownloadSet = u.toSet(visitorContentIDDownload);
			System.out.println("SUGGESTION_LIST:" + contentID);
			System.out.println("Size = " + visitorContentIDDownloadSet.size());

			/* Removing already downloaded content_id from suggestion_set */
			redisSuggestionList.removeAll(visitorContentIDDownloadSet);
		}

		// long result = redisConnect.del(prop.getProperty("SUGGESTION_LIST") +
		// ":" + contentID);

		
		//System.out.println("Result : " + res + " Size = " + redisSuggestionList.size());
		return redisSuggestionList;
	}

}