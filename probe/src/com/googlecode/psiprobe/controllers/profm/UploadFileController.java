package com.googlecode.psiprobe.controllers.profm;

import com.googlecode.psiprobe.controllers.TomcatContainerController;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

public class UploadFileController extends TomcatContainerController {
	@SuppressWarnings("rawtypes")
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (FileUpload.isMultipartContent(new ServletRequestContext(request))) {
			String rootPath = null;
			File tmpFile = null;

			FileItemFactory factory = new DiskFileItemFactory(1048000, new File(System.getProperty("java.io.tmpdir")));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(-1L);
			upload.setHeaderEncoding("UTF8");
			try {
				for (Iterator it = upload.parseRequest(request).iterator(); it.hasNext();) {
					FileItem fi = (FileItem) it.next();
					if (!fi.isFormField()) {
						if ((fi.getName() != null) && (fi.getName().length() > 0)) {
							tmpFile = new File(System.getProperty("java.io.tmpdir"),
									FilenameUtils.getName(fi.getName()));
							fi.write(tmpFile);
						}
					} else if ("rootPath".equals(fi.getFieldName())) {
						rootPath = fi.getString();
					}
				}
			} catch (Exception e) {
				this.logger.fatal("Could not process file upload", e);
				request.setAttribute("errorMessage", getMessageSourceAccessor()
						.getMessage("probe.src.deploy.war.uploadfailure", new Object[] { e.getMessage() }));

				if ((tmpFile != null) && (tmpFile.exists())) {
					tmpFile.delete();
				}
				tmpFile = null;
			}

			String errMsg = null;

			if (tmpFile != null) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					if (tmpFile.getName().endsWith(".zip")) {
						//����zip�ļ�
						String zipName=rootPath+sdf.format(new Date())+"\\"+tmpFile.getName();
						File zipFile=new File(zipName);
						FileUtils.copyFile(tmpFile, zipFile);
						if(zipFile.exists()){
							//��ѹzip�������Ŀ¼��
							String path=zipName.toLowerCase().replace(".zip", "");
							deleteFolder(path);
								
							ZipPath.unzip(zipName, path);
			                //request.setAttribute("success", Boolean.TRUE);
						}
					} else {
						//��zip�ļ���ֱ�ӱ���
						String filename=rootPath+sdf.format(new Date())+"\\"+tmpFile.getName();
						FileUtils.copyFile(tmpFile, new File(filename));
		                //request.setAttribute("success", Boolean.TRUE);
					}
				} catch (Exception e) {
					errMsg = getMessageSourceAccessor().getMessage("probe.src.deploy.war.failure",
							new Object[] { e.getMessage() });
					this.logger.error("Tomcat throw an exception when trying to deploy", e);
				} finally {
					if (errMsg != null) {
						request.setAttribute("errorMessage", errMsg);
					}
				}
			}
		}
		return new ModelAndView(new InternalResourceView(getViewName()));
	}
	public boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// �ж�Ŀ¼���ļ��Ƿ����
		if (!file.exists()) {// �����ڷ��� false
			return flag;
		}else{
			// �ж��Ƿ�Ϊ�ļ�
			if (file.isFile()){// Ϊ�ļ�ʱ����ɾ���ļ�����
				return deleteFile(sPath);
			} else {// ΪĿ¼ʱ����ɾ��Ŀ¼����
				return deleteDirectory(sPath);
			}
		}
	}
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);// ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
		if (file.isFile() && file.exists()){
			file.delete();
			flag = true;
			}
		return flag;
	}
	public boolean deleteDirectory(String sPath){
		boolean flag = false;
		//���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
			//���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
			if (!dirFile.exists() || !dirFile.isDirectory()){
			return false;
		}
		flag = true;
		//ɾ���ļ����µ������ļ�(������Ŀ¼)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++){
			//ɾ�����ļ�
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) break;
			} //ɾ����Ŀ¼
			else {
			flag = deleteDirectory(files[i].getAbsolutePath());
			if (!flag) break;
			}
		}
		if (!flag) return false;
		//ɾ����ǰĿ¼
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
}
