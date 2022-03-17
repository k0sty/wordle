package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.lang.IllegalStateException;

// Specific to invokeTrieAlgorithm()
import web.utils.Serializer;
import web.wordle_solver.SingletonSteward;
import java.util.HashSet;
import java.util.StringTokenizer;
import trie.PotentialWordsWrapper;
import java.util.stream.Stream;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

// Handler value: example.HandlerStream
public class Handler implements RequestStreamHandler {
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
  {
    // JSONParser parser = new JSONParser();
    JSONObject headerJson = new JSONObject();
    JSONObject responseBody = new JSONObject();
    JSONObject responseJson = new JSONObject();
    LambdaLogger logger = context.getLogger();
    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("US-ASCII"))));
    try
    {
      JSONTokener tokener = new JSONTokener(inputStream);
      JSONObject event = new JSONObject(tokener);

      logger.log("STREAM TYPE: " + inputStream.getClass().toString());
      logger.log("EVENT TYPE: " + event.toString());

      String missingCharsCSV = null;
      if (event.get("queryStringParameters") != null) {
          JSONObject qps = (JSONObject) event.get("queryStringParameters");
          if (qps.get("missingCharsCSV") != null) {
              missingCharsCSV = (String) qps.get("missingCharsCSV");
          }
      }
      String charGuessesMapCSV = null;
      if (event.get("queryStringParameters") != null) {
          JSONObject qps = (JSONObject) event.get("queryStringParameters");
          if (qps.get("charGuessesMapCSV") != null) {
              charGuessesMapCSV = (String) qps.get("charGuessesMapCSV");
          }
      }
      String currentGuess = null;
      if (event.get("queryStringParameters") != null) {
          JSONObject qps = (JSONObject) event.get("queryStringParameters");
          if (qps.get("currentGuess") != null) {
              currentGuess = (String) qps.get("currentGuess");
          }
      }

      headerJson.put("Access-Control-Allow-Origin","*");

      responseBody.put("missingCharsCSV", missingCharsCSV);
      responseBody.put("charGuessesMapCSV", charGuessesMapCSV);
      responseBody.put("currentGuess", currentGuess);
      String trieAlgorightmResult = invokeTrieAlgorithm(missingCharsCSV, charGuessesMapCSV, currentGuess);
      if (trieAlgorightmResult.startsWith("400:")) {
        responseJson.put("statusCode", 400);
      } else {
        responseJson.put("statusCode", 200);
      }
      responseBody.put("potentialWords", trieAlgorightmResult); // will contain the result set, or error message

      responseJson.put("headers", headerJson);
      responseJson.put("body", responseBody.toString());
      if (writer.checkError())
      {
        logger.log("WARNING: Writer encountered an error.");
      }
    }
    catch (IllegalStateException | JsonSyntaxException exception)
    {
      logger.log(exception.toString());
    }
    finally
    {
      // reader.close();
      writer.write(responseJson.toString());
      writer.close();
    }
  }

  private String invokeTrieAlgorithm(String missingCharsCSV, String charGuessesMapCSV, String currentGuess) {
    // missingChars
		HashSet<Character> missingChars = new HashSet(26);
		StringTokenizer st = new StringTokenizer(missingCharsCSV, ",");
		while (st.hasMoreTokens()) {
			String nextToken = st.nextToken();

			// Needs to be a single character
			if (nextToken.length()>1) {
				return "400: missingCharsCSV contains non-char: " + nextToken;
			}

			missingChars.add(nextToken.charAt(0));
		}

		// charGuessesMap, parsed of the form "charGuessesMapCSV=0n,2c"
		Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
		st = new StringTokenizer(charGuessesMapCSV, ",");
		while (st.hasMoreTokens()) {
			String nextToken = st.nextToken();

			if (nextToken.length()>2) {
				return "400: charGuessesMapCSV contains bad map pair: " + nextToken;
			}
			
			int mapIndex = Character.getNumericValue( nextToken.charAt(0) );
			char guessedChar = nextToken.charAt(1);
			charGuessesMap.put(guessedChar, Stream.of(mapIndex).collect(Collectors.toCollection(HashSet::new)));

		}

		// currentGuess
		if (currentGuess.length()!=5) {
			return "400: currentGuess is invalid: " + currentGuess;
		}

		SingletonSteward singletonSteward = SingletonSteward.getInstance();
        final PotentialWordsWrapper potentialWordsWrapper = singletonSteward.trie.generatePotentialWords(currentGuess, charGuessesMap, missingChars);
		String potentialWordsJSON = Serializer.createJSONString(potentialWordsWrapper);

    return potentialWordsJSON;
  }
}