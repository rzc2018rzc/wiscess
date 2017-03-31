package com.wiscess.wechat.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wiscess.common.utils.StringUtil;
import com.wiscess.wechat.service.ICoreService;
import com.wiscess.wechat.util.SignUtil;

/**
 * ������ĺ�����
 * @author wanghai
 * @date 2014-06-10
 */
public class CoreServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7990991839833228487L;

	public String token;
	public ICoreService weixinCoreService;
	public static ServletContext context;

	public void init(ServletConfig config) throws ServletException {
		// �뿪��ģʽ�ӿ�������Ϣ�е�Token����һ��
		token=config.getInitParameter("tokenName");
		String beanName=config.getInitParameter("beanName");
		if(StringUtil.isEmpty(token))
			token="token";
		if(StringUtil.isEmpty(beanName))
			beanName="weixinCoreService";
		context = config.getServletContext();
		weixinCoreService = (ICoreService) WebApplicationContextUtils
			.getWebApplicationContext(context).getBean(beanName);
	}
	
	/**
	 * ǩ����֤��ʹ��get��ʽ��ȷ����������΢�ŷ�������
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		//΢�ż���ǩ��
		String signature = request.getParameter("signature");
		//ʱ���
		String timestamp = request.getParameter("timestamp");
		//�����
		String nonce = request.getParameter("nonce");
		//����ַ���
		String echostr = request.getParameter("echostr");

		if(StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(nonce) || StringUtil.isEmpty(signature) || StringUtil.isEmpty(echostr)){
			System.out.println("���ò�������ȷ");
			return;
		}
		
		PrintWriter out = response.getWriter();
		
		//����У�飬������ɹ���ԭ������echostr����ʾ����ɹ����������ʧ��
		if(SignUtil.checkSignature(signature,token, timestamp,nonce)){
			out.print(echostr);
		}
		out.close();
		out = null;
	}
	/**
	 * ����У���봦��
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ��������Ӧ�ı��������ΪUTF-8����ֹ�������룩
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// ���ղ�����΢�ż���ǩ���� ʱ����������
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		PrintWriter out = response.getWriter();
		// ����У��
		if (SignUtil.checkSignature(signature, token,timestamp, nonce)) {
			//System.out.println("���յ���Ϣ����֤ͨ��");
			// ���ú��ķ�������մ�������
			String respXml = processRequest(request);
			out.print(respXml);
		}
		out.close();
		out = null;
	}
	
	/**
	 * ����΢�ŷ�������Ϣ�������ط��ͷ�
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request){
		return weixinCoreService.processRequest(request);
	}
}
