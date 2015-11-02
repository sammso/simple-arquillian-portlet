package com.sohlman.liferay.arquillian.util;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.util.List;

/**
 * @author Alberto Chaparro
 * @author Manuel de la Peña
 */
public class UserTestUtil {
	public static User getAdminUser() throws PortalException, SystemException {
		return getAdminUser(CompanyTestUtil.getCompanyId());
	}
	
	public static User getAdminUser(long companyId) throws PortalException, SystemException {
		Role role = RoleLocalServiceUtil.getRole(companyId,
				RoleConstants.ADMINISTRATOR);

		List<User> users = UserLocalServiceUtil.getRoleUsers(role.getRoleId(), 0, 2);

		if (!users.isEmpty()) {
			return users.get(0);
		}
		throw new NoSuchUserException();
	}	
}