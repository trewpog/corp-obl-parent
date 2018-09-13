package com.tx.co.cache.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.service.CompanyConsultantService;
import com.tx.co.back_office.company.service.CompanyService;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.service.OfficeService;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.back_office.topic.service.TopicService;
import com.tx.co.common.translation.domain.Translation;
import com.tx.co.common.translation.service.ITranslationService;
import com.tx.co.common.utils.UtilStatic;
import com.tx.co.user.domain.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.cache.Cache;
import javax.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tx.co.common.constants.AppConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

public abstract class UpdateCacheData {

	private static final Logger logger = LogManager.getLogger(UpdateCacheData.class);
	
	private javax.cache.CacheManager cacheManager;
	private CompanyService companyService;
	private OfficeService officeService;
	private TopicService topicService;
	private CompanyConsultantService companyConsultantService;
	private ITranslationService translationService;

	@Autowired
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Autowired
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	@Autowired
	public void setOfficeService(OfficeService officeService) {
		this.officeService = officeService;
	}

	@Autowired
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	
	@Autowired
	public void setTranslationService(ITranslationService translationService) {
		this.translationService = translationService;
	}	

	@Autowired
	public void setCompanyConsultantService(CompanyConsultantService companyConsultantService) {
		this.companyConsultantService = companyConsultantService;
	}

	/**
	 * @return get the Languages from the cache in order to not execute the query to the database
	 */
	public List<String> getLanguagesFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);
		List<String> languageListCache = (List<String>) storageDataCacheManager.get(LANGUAGE_LIST_CACHE);
		Collections.sort(languageListCache, (a, b) -> a.compareToIgnoreCase(b));
		return languageListCache;
	}
	
	/**
	 * @return get the Languages from the cache in order to not execute the query to the database
	 */
	public List<String> getLanguagesNotAvailableFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);
		List<String> languageListCache = (List<String>) storageDataCacheManager.get(LANGUAGE_NOT_AVAILABLE_LIST_CACHE);
		Collections.sort(languageListCache, (a, b) -> a.compareToIgnoreCase(b));
		return languageListCache;
	}
	
	

	/**
	 * @return get the Companies from the cache in order to not execute the query to the database
	 */
	public List<Company> getCompaniesFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<Company> companyListCache = (List<Company>) storageDataCacheManager.get(COMPANY_LIST_CACHE);

		if(isEmpty(companyListCache)) {
			return new ArrayList<>();
		}
		
		try {
			Collections.sort(companyListCache, (a, b) -> a.getDescription().compareToIgnoreCase(b.getDescription()));	
		} catch (Exception e) {
			e.printStackTrace();
			return companyListCache;
		}
		
//		Collections.sort(companyListCache, new Comparator<Company>() {
//		    public int compare(Company a, Company b) {
//		    	if(isEmpty(a)) return 1;
//		    	if(isEmpty(b)) return -1;
//		        return a.getDescription().compareToIgnoreCase(b.getDescription());
//		    }
//		});

		return companyListCache;
	}

	/**
	 * @return get the Offices from the cache in order to not execute the query to the database
	 */
	public List<Office> getOfficesFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<Office> officeListCache = isEmpty(storageDataCacheManager.get(OFFICE_LIST_CACHE)) ? new ArrayList<>() : (List<Office>) storageDataCacheManager.get(OFFICE_LIST_CACHE);

		Collections.sort(officeListCache, (a, b) -> a.getDescription().compareToIgnoreCase(b.getDescription()));

		return officeListCache;
	}

	/**
	 * @return get the Topics from the cache in order to not execute the query to the database
	 */
	public List<Topic> getTopicsFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<Topic> topicListCache = isEmpty(storageDataCacheManager.get(TOPIC_LIST_CACHE)) ? new ArrayList<>() : (List<Topic>) storageDataCacheManager.get(TOPIC_LIST_CACHE);

		Collections.sort(topicListCache, (a, b) -> a.getDescription().compareToIgnoreCase(b.getDescription()));

		return topicListCache;
	}
	
	/**
	 * @return get the Topic Consultants from the cache in order to not execute the query to the database
	 */
	public List<TopicConsultant> getTopicConsultantsFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<TopicConsultant> topicConsultantListCache = isEmpty(storageDataCacheManager.get(TOPIC_CONSULTANT_LIST_CACHE)) ? new ArrayList<>() : (List<TopicConsultant>) storageDataCacheManager.get(TOPIC_CONSULTANT_LIST_CACHE);

		Collections.sort(topicConsultantListCache, (a, b) -> a.getCompanyConsultant().getName().compareToIgnoreCase(b.getCompanyConsultant().getName()));

		return topicConsultantListCache;
	}
	
	/**
	 * @return get the CompanyConsultant from the cache in order to not execute the query to the database
	 */
	public Map<Long, List<CompanyConsultant>> getCompanyConsultantsFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		Map<Long, List<CompanyConsultant>> companyConsultantMap = (Map<Long, List<CompanyConsultant>>) storageDataCacheManager.get(COMPANY_CONSULTANT_LIST_CACHE);

		return companyConsultantMap == null ? new HashMap<>() : companyConsultantMap;
	}

	/**
	 * @param company
	 * @param updateFromDB
	 */
	public void updateCompaniesCache(Company company, Boolean updateFromDB) {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<Company> companyList = getCompaniesFromCache();

		// Object to update or to save as new one
		int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyCompanyList(company.getIdCompany(), companyList); 

		if(updateFromDB) {
			Optional<Company> companyFromDB = companyService.findByIdCompany(company.getIdCompany());
			if(companyFromDB.isPresent()) {
				company = companyFromDB.get();
			}
		}

		if(indexToUpdateOrInsert == -1) {
			companyList.add(company);
		} else if(!company.getEnabled()) {
			companyList.remove(indexToUpdateOrInsert);
		} else {
			companyList.set(indexToUpdateOrInsert, company);
		}

		storageDataCacheManager.put(COMPANY_LIST_CACHE, companyList);
	}

	/**
	 * @param office
	 * @param updateFromDB
	 */
	public void updateOfficesCache(Office office, Boolean updateFromDB) {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<Office> officeList = getOfficesFromCache();

		// Object to update or to save as new one
		int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyOfficeList(office.getIdOffice(), officeList); 

		if(updateFromDB) {
			Optional<Office> officeFromDB = officeService.findByIdOffice(office.getIdOffice());
			if(officeFromDB.isPresent()) {
				office = officeFromDB.get();
			}
		}

		if(indexToUpdateOrInsert == -1) {
			officeList.add(office);
		} else if(!office.getEnabled()) {
			officeList.remove(indexToUpdateOrInsert);
		} else {
			officeList.set(indexToUpdateOrInsert, office);
		}

		storageDataCacheManager.put(OFFICE_LIST_CACHE, officeList);
	}

	/**
	 * @param topic
	 * @param updateFromDB
	 */
	public void updateTopicsCache(Topic topic, Boolean updateFromDB) {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<Topic> topicList = getTopicsFromCache();

		// Object to update or to save as new one
		int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyTopicList(topic.getIdTopic(), topicList); 

		if(updateFromDB) {
			Optional<Topic> topicFromDB = topicService.findByIdTopic(topic.getIdTopic());
			if(topicFromDB.isPresent()) {
				topic = topicFromDB.get();
				List<Translation> translationList = translationService.getTranslationByEntityIdAndTablename(topic.getIdTopic(), "co_topic");
				if(!isEmpty(translationList)) {
					topic.setTranslationList(translationList);
				}
			}
		}

		if(indexToUpdateOrInsert == -1) {
			topicList.add(topic);
		} else if(!topic.getEnabled()) {
			topicList.remove(indexToUpdateOrInsert);
		} else {
			topicList.set(indexToUpdateOrInsert, topic);
		}

		storageDataCacheManager.put(TOPIC_LIST_CACHE, topicList);
	}
	
	/**
	 * @param companyConsultant
	 * @param updateFromDB
	 */
	public void updateCompanyConsultantCache(CompanyConsultant companyConsultant, Boolean updateFromDB) {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		Map<Long, List<CompanyConsultant>> companyConsultantMap = getCompanyConsultantsFromCache();
		
		Long idCompany = companyConsultant.getCompany().getIdCompany();
		
		List<CompanyConsultant> companyConsultantList = (List<CompanyConsultant>) companyConsultantMap.get(idCompany);

		// Object to update or to save as new one
		int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyCompanyConsultantList(companyConsultant.getIdCompanyConsultant(), companyConsultantList); 

		if(updateFromDB) {
			Optional<CompanyConsultant> companyConsultantFromDB = companyConsultantService.findByIdCompanyConsultant(companyConsultant.getIdCompanyConsultant());
			if(companyConsultantFromDB.isPresent()) {
				companyConsultant = companyConsultantFromDB.get();
			}
		}

		if(indexToUpdateOrInsert == -1) {
			companyConsultantList = new ArrayList<>();
			companyConsultantList.add(companyConsultant);
		} else if(!companyConsultant.getEnabled()) {
			companyConsultantList.remove(indexToUpdateOrInsert);
		} else {
			companyConsultantList.set(indexToUpdateOrInsert, companyConsultant);
		}
		
		companyConsultantMap.put(idCompany, companyConsultantList);

		storageDataCacheManager.put(COMPANY_CONSULTANT_LIST_CACHE, companyConsultantMap);
	}
	
	/**
	 * @param topicConsultant
	 * @param updateFromDB
	 */
	public void updateTopicConsultantsCache(TopicConsultant topicConsultant, Boolean updateFromDB) {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		List<TopicConsultant> topicConsultantList = getTopicConsultantsFromCache();

		// Object to update or to save as new one
		int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyTopicLConsultantist(topicConsultant.getIdTopicConsultant(), topicConsultantList); 

		if(updateFromDB) {
			Optional<TopicConsultant> topicConsultantFromDB = topicService.findByIdTopicConsultant(topicConsultant);
			if(topicConsultantFromDB.isPresent()) {
				topicConsultant = topicConsultantFromDB.get();
			}
		}

		if(indexToUpdateOrInsert == -1) {
			topicConsultantList.add(topicConsultant);
		} else if(!topicConsultant.getEnabled()) {
			topicConsultantList.remove(indexToUpdateOrInsert);
		} else {
			topicConsultantList.set(indexToUpdateOrInsert, topicConsultant);
		}

		storageDataCacheManager.put(TOPIC_CONSULTANT_LIST_CACHE, topicConsultantList);
		
		updateTopicsCache(topicConsultant.getTopic(), true);
	}

	/**
	 * @param idCompany
	 * @return the existing Company on the cache
	 */
	public Company getCompanyById(Long idCompany) {
		Company company = null;
		List<Company> companyListCache = getCompaniesFromCache();
		if(!isEmpty(companyListCache)) {
			for (Company companyLoop : companyListCache) {
				if(idCompany.compareTo(companyLoop.getIdCompany()) == 0) {
					company = companyLoop;
					break;
				}
			}
		}
		return company;
	}

	/**
	 * @return get the Users from the cache in order to not execute the query to the database
	 */
	public List<User> getUsersFromCache() {

		final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

		return isEmpty(storageDataCacheManager.get(USER_LIST_CACHE)) ? new ArrayList<>() : (List<User>) storageDataCacheManager.get(USER_LIST_CACHE);
	}

	public User getUserFromUsername(String username) {

		List<User> userList = getUsersFromCache();
		User userRetrived = null;
		for (User user : userList) {
			if(user.getUsername().equals(username)) {
				userRetrived = user;
				break;
			}
		}
		return userRetrived;
	}
	
	/**
	 * @param idCompanyConsultant
	 * @return the existing CompanyConsultant on the cache
	 */
	public CompanyConsultant getCompanyConsultantById(Long idCompanyConsultant) {
		CompanyConsultant companyConsultant = null;
		Map<Long, List<CompanyConsultant>> companyConsultantMap = getCompanyConsultantsFromCache();
		if(!isEmpty(companyConsultantMap)) {
			for (Long idCompany : companyConsultantMap.keySet()) {
				for (CompanyConsultant companyConsultantLoop : companyConsultantMap.get(idCompany)) {
					if(idCompanyConsultant.compareTo(companyConsultantLoop.getIdCompanyConsultant()) == 0) {
						companyConsultant = companyConsultantLoop;
						break;
					}
				}
				
			}
		}
		return companyConsultant;
	}
	
	public Topic getTopicById(Long idTopic, String idCompany) {
		Topic topic = null;
		List<Topic> topicListCache = getTopicsFromCache();
		if (!isEmpty(topicListCache)) {
			for (Topic topicLoop : topicListCache) {
				if (idTopic.compareTo(topicLoop.getIdTopic()) == 0) {
					topic = topicLoop;
					topic.setCompanyConsultantsList(getConsultantByIdTopicAndIdCompany(idTopic, idCompany));
				}
			}
		}
		return topic;
	}
	
	public List<CompanyConsultant> getConsultantByIdTopicAndIdCompany(Long idTopic, String idCompany) {
		
		if(isEmpty(idTopic) || isEmpty(idCompany)) {
			return new ArrayList<>();
		}
		
		List<CompanyConsultant> companyConsultants = new ArrayList<>();
		List<TopicConsultant> topicConsultantListCache = getTopicConsultantsFromCache();
		if (!isEmpty(topicConsultantListCache)) {
			for (TopicConsultant topicConsultantLoop : topicConsultantListCache) {
				if (idCompany.compareTo(topicConsultantLoop.getCompanyConsultant().getCompany().getIdCompany().toString()) == 0 &&
						idTopic.compareTo(topicConsultantLoop.getTopic().getIdTopic()) == 0) {
					companyConsultants.add(topicConsultantLoop.getCompanyConsultant());
				}
			}
		}
		return companyConsultants;
	}
	
	public TopicConsultant getTopicConsultantByIds(TopicConsultant topicConsultantToRetrieve) {
		TopicConsultant topicConsultant = null;
		List<TopicConsultant> topicConsultantListCache = getTopicConsultantsFromCache();
		if (!isEmpty(topicConsultantListCache)) {
			for (TopicConsultant topicConsultantLoop : topicConsultantListCache) {
				if (!isEmpty(topicConsultantToRetrieve.getCompanyConsultant()) && 
						topicConsultantToRetrieve.getCompanyConsultant().getIdCompanyConsultant().compareTo(topicConsultantLoop.getCompanyConsultant().getIdCompanyConsultant()) == 0 &&
						!isEmpty(topicConsultantToRetrieve.getTopic()) &&
						topicConsultantToRetrieve.getTopic().getIdTopic().compareTo(topicConsultantLoop.getTopic().getIdTopic()) == 0) {
					topicConsultant = topicConsultantLoop;
				}
			}
		}
		return topicConsultant;
	}

}
