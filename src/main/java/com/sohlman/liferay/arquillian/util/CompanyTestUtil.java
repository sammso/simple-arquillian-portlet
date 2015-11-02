package com.sohlman.liferay.arquillian.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;

public class CompanyTestUtil {

	public static Company getCompany() throws PortalException, SystemException {
		String companyWebId = PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID);
		return CompanyLocalServiceUtil.getCompanyByWebId(companyWebId);
	}
	
	public static long getCompanyId() throws PortalException, SystemException {
		return getCompany().getCompanyId();
	}
	
	public static Company addCompany() throws Exception {
		return addCompany(ServiceTestUtil.randomString());
	}

	public static Company addCompany(String name) throws Exception {
		String virtualHostname = name + "." +  ServiceTestUtil.randomString(3);

		String shardDefaultName = PropsUtil.get(PropsKeys.SHARD_DEFAULT_NAME);
		
		return CompanyLocalServiceUtil.addCompany(
			name, virtualHostname, virtualHostname,
			shardDefaultName, false, 0, true);
	}
}
