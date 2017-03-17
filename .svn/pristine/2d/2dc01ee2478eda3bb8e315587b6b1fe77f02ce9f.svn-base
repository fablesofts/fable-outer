package com.fable.outer.biz;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fable.outer.dmo.system.resource.UserInfo;
import com.fable.outer.service.system.resource.intf.UserInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
public class ExampleTest {

	@Resource
	UserInfoService userInfoService;
	@Test
	public void test() {
		// UserInfo user = new UserInfo();
		// user.setUsername("wang");
		// user.setPassword("4444");
		// user.setDelFlag("0");
		// user.setRealname("汪");
		//
		UserInfo u = userInfoService.getById(1l);

		System.out.println(u.getRealname());
	}

}
