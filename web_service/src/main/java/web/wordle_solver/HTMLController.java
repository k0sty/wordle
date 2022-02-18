package web.wordle_solver;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HTMLController {

    @RequestMapping("/")
	public String index() {
		return "index.html";
	}

}