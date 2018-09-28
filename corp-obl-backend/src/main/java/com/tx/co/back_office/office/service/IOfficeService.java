package com.tx.co.back_office.office.service;

import java.util.List;
import java.util.Optional;

import com.tx.co.back_office.office.api.model.OfficeTaskTemplates;
import com.tx.co.back_office.office.api.model.TaskTempOfficies;
import com.tx.co.back_office.office.domain.Office;

public interface IOfficeService {

	List<Office> findAllOffice();
	
	Office saveUpdateOffice(Office office);
	
	Optional<Office> findByIdOffice(Long idOffice);
	
	void deleteOffice(Long idOffice);
	
	List<OfficeTaskTemplates> searchOfficeTaskTEmplates(TaskTempOfficies taskTempOfficies);
}
