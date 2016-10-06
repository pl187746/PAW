package pl.iis.paw.trello.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

	@RequestMapping("/world")
	public String sayHello() {
		return "Hello world";
	}
	
	@RequestMapping("/login/{login}")
	public String saySomething(@PathVariable String login) {
		return "Hello " + login + "!";
	}
}
