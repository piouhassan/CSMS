package com.togo.c_sms.Helper;

public class ApiUrl {



    private static String URL = "http://192.168.1.154:7000/api/";



	public static String URL_GET_CREDIT= URL+"cuser/cgetcredit-";

	public static String URL_PAYMENT_FIRST = URL+"user/pay";


	// Server user login url
	public static String URL_LOGIN = URL+"login/cuser";

	// Server user register url
	public static String URL_REGISTER = URL+"add/cuser";


	public static final String URL_GROUPS = URL+"cgroup/list-";


	public static final String URL_GROUPS_ADD = URL+"cgroup/add";


	public static final String URL_GROUPS_DELETE = URL+"cgroup/delete-group";

	public static final String URL_HISTORIC_DELETE_ALL = URL+"historic_delete/one";

	public static final String URL_HISTORIC_DELETE_ONE= URL+"historic_delete/one";


	public static final String URL_GROUP_DETAIL = URL+"cgroup/contact_single";

	public static final String URL_CONTACT_TO_GROUP_ADD = URL+"cgroup/add_contact_single";

	public static final String URL_CONTACT_DELETE = URL+"cgroup/contact/delete-one";


	public static final String URL_HISTORIC = URL+"historic/list-";

      public  static final String URL_IMAGE = "http://192.168.8.100:8000/";

}
