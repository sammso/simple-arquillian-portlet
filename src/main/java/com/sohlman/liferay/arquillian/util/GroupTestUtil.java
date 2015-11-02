package com.sohlman.liferay.arquillian.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;

public class GroupTestUtil {
	public static Group getDefeaultCompanyGuestGroup() throws PortalException, SystemException {
		return getGuestGroup(CompanyTestUtil.getCompanyId());
	}
	
	public static long getDefeaultCompanyGuestGroupId() throws PortalException, SystemException {
		return getDefeaultCompanyGuestGroup().getGroupId();
	}

	public static Group getGuestGroup(long companyId) throws PortalException, SystemException {
		return GroupLocalServiceUtil.getGroup(companyId, GroupConstants.GUEST);
	}	
	
	public static long getGuestGroupId(long companyId) throws PortalException, SystemException {
		return getGuestGroup(companyId).getGroupId();
	}

}