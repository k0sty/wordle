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
		mvc.perform(MockMvcRequestBuilders.get("/demoTrie?missingCharsCSV=a,r,s,m,o,v,t,l,h&charGuessesMapCSV=0n,2c&currentGuess=-i--e")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo("{\"potentialWords\":[\"wince\"]}")));
	}

	@Test
	public void requesetWithBadMissingCharsCSV() throws Exception {
		String notChar = "br";
		mvc.perform(MockMvcRequestBuilders.get("/demoTrie?missingCharsCSV=a,"+notChar+",s,m,o,v,t,l,h&charGuessesMapCSV=0n,2c&currentGuess=-i--e")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().string(equalTo("missingCharsCSV contains non-char: " + notChar)));
	}

	@Test
	public void requesetWithBadCharGuessMap() throws Exception {
		String badMapPair = "02b";
		mvc.perform(MockMvcRequestBuilders.get("/demoTrie?missingCharsCSV=a,s,m,o,v,t,l,h&charGuessesMapCSV=0n,"+badMapPair+"&currentGuess=-i--e")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().string(equalTo("charGuessesMapCSV contains bad map pair: " + badMapPair)));
	}

	@Test
	public void requesetWithBadCurrentGuess() throws Exception {
		String badCurrentGuess = "-i--e---";
		mvc.perform(MockMvcRequestBuilders.get("/demoTrie?missingCharsCSV=a,s,m,o,v,t,l,h&charGuessesMapCSV=0n&currentGuess="+badCurrentGuess)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().string(equalTo("currentGuess is invalid: " + badCurrentGuess)));
	}

}
