package web.wordle_solver;

import trie.Blah;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/")
	public String index() {
		Blah blah = new Blah();
		System.out.println(blah.offerString());
		return "Greetings from Spring Boot!";
	}

}