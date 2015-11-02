package com.sohlman.liferay.arquillian.poc;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.sohlman.liferay.arquillian.util.CompanyTestUtil;
import com.sohlman.liferay.arquillian.util.UserTestUtil;

import java.util.List;

import javax.management.Query;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserLocalServiceTest {
	@Test
	public void testUserLocalService() throws Exception {
		List<User> list = UserLocalServiceUtil.getCompanyUsers(CompanyTestUtil.getCompanyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		
		for (User user : list) {
			_log.fatal(String.format("screenname: " + user.getScreenName()));
		}
		Assert.assertTrue(list.size() > 0);
		
		List<Role> roles = RoleLocalServiceUtil.getRoles(CompanyTestUtil.getCompanyId());
		
		for (Role role : roles) {
			_log.fatal(String.format("role: " + role.getName()));
		}
		
		User user = UserTestUtil.getAdminUser(CompanyTestUtil.getCompanyId());
	}
	
	private Log _log = LogFactoryUtil.getLog("Arquillian");
}
