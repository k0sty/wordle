package web.wordle_solver;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class WordleSolverApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void webServiceIndex() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Index of WorldSolver!")));
	}

	@Test
	public void demoTrieEndpoint() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/demoTrie?missingCharsCSV=a,r,s,m,o,v,t,l,h&charGuessesMap=0n,2c&currentGuess=-i--e")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("{\"potentialWords\":[\"wince\"]}")));
	}

}
