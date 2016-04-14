package com.sohlman.liferay.arquillian.poc;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.FileUtil;
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
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.sohlman.liferay.arquillian.util.GroupTestUtil;
import com.sohlman.liferay.arquillian.util.ServiceContextTestUtil;
import com.sohlman.liferay.arquillian.util.UserTestUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DocumentAndMediaTest {

	@Before
	public void setup() throws Exception {
		Group group = GroupTestUtil.getDefeaultCompanyGuestGroup();
		User user = UserTestUtil.getAdminUser(group.getCompanyId());
		PermissionChecker permissionChecker = PermissionCheckerFactoryUtil
				.create(user);
		PermissionThreadLocal.setPermissionChecker(permissionChecker);
		PrincipalThreadLocal.setName(user.getUserId());
		List<FileEntry> fileEntries = DLAppServiceUtil.getFileEntries(
				group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		for (FileEntry fileEntry : fileEntries) {
			DLAppServiceUtil.deleteFileEntry(fileEntry.getFileEntryId());
		}
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testAddFileToDocumentAndMedia() throws Exception {
		Group group = GroupTestUtil.getDefeaultCompanyGuestGroup();
		User user = UserTestUtil.getAdminUser(group.getCompanyId());

		PermissionChecker permissionChecker = PermissionCheckerFactoryUtil
				.create(user);
		PermissionThreadLocal.setPermissionChecker(permissionChecker);
		PrincipalThreadLocal.setName(user.getUserId());

		FileEntry fileEntry = addFileEntry(user.getUserId(),
				group.getGroupId(), 0, getFile("/Lorem.pdf"));

		_log.fatal(fileEntry.getTitle() + "  " + fileEntry.getSize());

		DLAppServiceUtil.deleteFileEntry(fileEntry.getFileEntryId());
	}

	@Test
	public void testAddFolderToDocumentAndMedia() throws Exception {
		Group group = GroupTestUtil.getDefeaultCompanyGuestGroup();
		User user = UserTestUtil.getAdminUser(group.getCompanyId());

		PermissionChecker permissionChecker = PermissionCheckerFactoryUtil
				.create(user);
		PermissionThreadLocal.setPermissionChecker(permissionChecker);
		PrincipalThreadLocal.setName(user.getUserId());

		String name = "Folder name";
		String description = "Folder description";
		long repositoryId = group.getGroupId();
		ServiceContext serviceContext = ServiceContextTestUtil
				.getServiceContext(group.getGroupId());

		DLAppServiceUtil.addFolder(repositoryId,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, name, description,
				serviceContext);

		Folder folder1 = DLAppServiceUtil.getFolder(repositoryId,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, name);

		Assert.assertEquals(folder1.getName(), name);

		DLAppServiceUtil.deleteFolder(repositoryId,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, name);

		try {
			DLAppServiceUtil.getFolder(repositoryId,
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, name);
			Assert.fail("folder should not exist");
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), NoSuchFolderException.class);
		}
	}

	@Test
	public void testUpdateFileToDocumentAndMedia() throws Exception {
		Group group = GroupTestUtil.getDefeaultCompanyGuestGroup();
		User user = UserTestUtil.getAdminUser(group.getCompanyId());

		PermissionChecker permissionChecker = PermissionCheckerFactoryUtil
				.create(user);
		PermissionThreadLocal.setPermissionChecker(permissionChecker);
		PrincipalThreadLocal.setName(user.getUserId());
		
		File loremFile = getFile("/Lorem.pdf");
		File loremEsFile = getFile("/LoremEs.pdf");

		FileEntry fileEntry = addFileEntry(user.getUserId(),
				group.getGroupId(), 0, loremFile);

		fileEntry = updateFileEntry(fileEntry.getFileEntryId(), loremEsFile);
		
		fileEntry = DLAppServiceUtil.getFileEntry(fileEntry.getFileEntryId());
		
		
		byte[] loremEsBytes = FileUtil.getBytes(loremEsFile);
		byte[] filentryBytes = FileUtil.getBytes(fileEntry.getContentStream());
		
 		
		Assert.assertArrayEquals(loremEsBytes, filentryBytes);
	}

	public FileEntry addFileEntry(long userId, long groupId, long folderId,
			File file) throws Exception {
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

	protected FileEntry updateFileEntry(long fileEntryId,
			File file) throws Exception {
		FileEntry fileEntry = DLAppServiceUtil.getFileEntry(fileEntryId);
		
		// find out the MimeType
		
		String mimeType = MimeTypesUtil.getContentType(file.getName());
		
		String title = file.getName();
		String sourceFileName = file.getName();
		

		int workflowAction = WorkflowConstants.ACTION_PUBLISH;

		ServiceContext serviceContext = ServiceContextTestUtil
				.getServiceContext(fileEntry.getGroupId());
		
		serviceContext.setWorkflowAction(workflowAction);
		boolean majorVersion = false;
		return DLAppServiceUtil.updateFileEntry(fileEntryId, sourceFileName, mimeType, title, "description", "changeLog", majorVersion, file, serviceContext);
	}
	
	protected File getFile(String fileNameAtClassPath) throws URISyntaxException {
		URL url = this.getClass().getResource(fileNameAtClassPath);
		return new File(url.toURI());
	}

	private Company _company;
	private Log _log = LogFactoryUtil.getLog(DocumentAndMediaTest.class);
}
