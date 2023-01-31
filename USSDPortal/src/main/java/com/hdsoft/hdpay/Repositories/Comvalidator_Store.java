package com.hdsoft.hdpay.Repositories;

public class Comvalidator_Store 
{
	private String CUSPRD_PROD_CODE;
	private String MPROD_PROD_DESC;
	private String CURR_CODE;
	private String CURR_NAME;

	public String getCURR_CODE() {
		return CURR_CODE;
	}
	public void setCURR_CODE(String cURR_CODE) {
		CURR_CODE = cURR_CODE;
	}
	public String getCURR_NAME() {
		return CURR_NAME;
	}
	public void setCURR_NAME(String cURR_NAME) {
		CURR_NAME = cURR_NAME;
	}
	public String getCUSPRD_PROD_CODE() {
		return CUSPRD_PROD_CODE;
	}
	public void setCUSPRD_PROD_CODE(String cUSPRD_PROD_CODE) {
		CUSPRD_PROD_CODE = cUSPRD_PROD_CODE;
	}
	public String getMPROD_PROD_DESC() {
		return MPROD_PROD_DESC;
	}
	public void setMPROD_PROD_DESC(String mPROD_PROD_DESC) {
		MPROD_PROD_DESC = mPROD_PROD_DESC;
	}
}
