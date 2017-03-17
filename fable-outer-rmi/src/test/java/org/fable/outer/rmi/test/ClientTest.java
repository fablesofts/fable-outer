package org.fable.outer.rmi.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fable.hamal.shuttle.communication.client.Communication;
import com.fable.outer.rmi.event.example.ExampleEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-rmi-client-test.xml" })
public class ClientTest {

	@Resource(name = "client")
	Communication communication;

	@Value(value = "${rmi.url}")
	String port;
	@Test
	public void test() {
		String a = (String) communication.call(
				"rmi://localhost:1099/eventService", new ExampleEvent());
		System.out.println(a);
		
		System.out.println(port);

	}

}
