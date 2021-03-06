package com.jpinfo.mudengine.item;

import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication(scanBasePackages= {
		"com.jpinfo.mudengine.item",
		"com.jpinfo.mudengine.common"
})
@EnableAspectJAutoProxy
@EnableJms
public class MudItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MudItemApplication.class, args);
	}	
}
