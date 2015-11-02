package com.sohlman.liferay.arquillian.poc;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.sohlman.liferay.arquillian.util.GroupTestUtil;
import com.sohlman.liferay.arquillian.util.ServiceContextTestUtil;
import com.sohlman.liferay.arquillian.util.UserTestUtil;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DocumentAndMediaTest {
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	
	@Test
	public void testCopyDocumentAndMedia() throws Exception {
		Group group = GroupTestUtil.getDefeaultCompanyGuestGroup();
		User user = UserTestUtil.getAdminUser(group.getCompanyId());
		
		PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user);
		PermissionThreadLocal.setPermissionChecker(permissionChecker);
		PrincipalThreadLocal.setName(user.getUserId());
			
		FileEntry fileEntry = addFileEntry(user.getUserId(),
				group.getGroupId(), 0, "/Lorem.pdf");

		_log.fatal(fileEntry.getTitle() + "  " + fileEntry.getSize());

		DLAppServiceUtil.deleteFileEntry(fileEntry.getFileEntryId());
	}

	public FileEntry addFileEntry(long userId, long groupId, long folderId,
			String fileNameAtClassPath) throws Exception {

		// Read the file from the classpath
		
		URL url = this.getClass().getResource(fileNameAtClassPath);
		File file = new File(url.toURI());

		// find out the MimeType
		
		String mimeType = MimeTypesUtil.getContentType(file.getName());
		
		String title = file.getName();
		String sourceFileName = file.getName();

		int workflowAction = WorkflowConstants.ACTION_PUBLISH;

		ServiceContext serviceContext = ServiceContextTestUtil
				.getServiceContext(groupId);
		
		serviceContext.setWorkflowAction(workflowAction);
		
		return DLAppServiceUtil.addFileEntry(groupId,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, sourceFileName,
				mimeType, title, "description", "changeLog", file,
				serviceContext);
	}

	
	private Company _company;
	private Log _log = LogFactoryUtil.getLog(DocumentAndMediaTest.class);
}
