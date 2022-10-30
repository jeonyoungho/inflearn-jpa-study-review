package study.datajpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jyh1004 on 2022-10-31
 */

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
}
