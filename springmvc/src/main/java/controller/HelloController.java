package controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/*
 * 
 *�������ʵ��Controller�ӿ�
 *
 */
public class HelloController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("handlerRequest()");
		
		/*
		 * ModelAndView������������
		 * (1)ModelAndView(String viewName),viewName����ͼ��
		 * (2)modelAndView(String viewName,Map data),Map���ڷ�װ����������
		 */
		return new ModelAndView("hello");
	}


	
}
