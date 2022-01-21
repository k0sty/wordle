package web.wordle_solver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

}