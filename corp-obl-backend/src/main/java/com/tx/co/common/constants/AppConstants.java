package com.tx.co.common.constants;

public final class AppConstants  {

	private AppConstants() {}

	/**
	 * General Exception message
	 */
	public static final String FULLFIT_FORM = "Fulfill the form";
	public static final String EMPTY_FORM = "The form is empty";

	public static final String OFFICE_TASK_END_DATE = "31/12/2999 23:59:59";
	public static final String FORMAT_DATETIME = "dd/MM/yyyy HH:mm:ss";
	public static final String PATTERN_INT_DATE = "yyyyMMdd";

	public static final String DESCRIPTION_QUERY_PARAM = "description";

	/**
	 * Username list for testing purpose
	 * */
	public static final String ADMIN = "ADMIN";
	public static final String FOREIGN = "FOREIGN";
	public static final String INLAND = "INLAND";
	public static final String USER = "USER";

	/**
	 * Recurrence & Expiration constants
	 * */
	public static final String REC_WEEKLY = "weekly";
	public static final String REC_MONTHLY = "monthly";
	public static final String REC_YEARLY = "yearly";

	public static final String EXP_FIX_DAY = "fix_day";
	public static final String EXP_MONTH_START = "month_start";
	public static final String EXP_MONTH_END = "month_end";

	/* Task Office Relations Type */
	public static final Integer CONTROLLER = 1;
	public static final Integer CONTROLLED = 2;

	public static final int EXP_CLOSABLEBY_ALL = 1;
	public static final int EXP_CLOSABLEBY_ONE = 2;

	public static final String TASK_FILE_ICOMING = "TASK_FILE_ICOMING";
	public static final String EXP_FILE_ICOMING = "EXP_FILE_ICOMING";
	
	/* Warning messages */
	public static final String EXPIRATION_NOT_FOUND = "Expiration not found";
	
	public static final String ALERT_PRIMARY = "alert alert-primary";
	public static final String ALERT_WARNING = "alert alert-warning";
	public static final String ALERT_SUCCESS = "alert alert-success";
	
	/* Email components */
	public static final String EXPIRATION_SUBJECT_REJECT = "expiration#subject#reject";
	public static final String EXPIRATION_TEXT_REJECT = "expiration#text#reject";
	public static final String EXPIRATION_SUBJECT_EXPIRED = "expiration#subject#expired";
	public static final String EXPIRATION_TEXT_EXPIRED = "expiration#text#expired";
	
	public static final String MAIL_SEND_FROM = "test@test.com";
	public static final String MAIL_TEMPLATE_REJECT = "mailTemplateReject";
	public static final String MAIL_TEMPLATE_EXPIRED = "mailTemplateExpired";
}
