package net.kingtrans.admin.service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class IndexService {


	public ModelAndView index(HttpSession session) {
		ModelAndView model = new ModelAndView();

		model.setViewName("/admin/index");
		return model;
	}

}
