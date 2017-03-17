package com.fable.outer.rmi.event.handler.example;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventHandler;
import com.fable.hamal.shuttle.communication.event.EventRegisterCenter;
import com.fable.outer.rmi.type.CommonEventTypes;

@SuppressWarnings("restriction")
@Service
public class ExampleEventHandler implements EventHandler {

	@PostConstruct
	public void init() {
		EventRegisterCenter.regist(CommonEventTypes.EXAMPLE, this);
	}

	public String handle(Event event) {

		System.out.println("***********RMI服务测试************");

		return "example-test!";
	}

}
