package controllers;

import org.json.JSONObject;
import views.MainView;
import views.ResultsView;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class FindReplaceController {
    private static final String SEARCH_URL = "https://cs.csub.edu/~srivera/3390/lab08/findReplace.php";
    private final HttpClient http = HttpClient.newHttpClient();

    public FindReplaceController(MainView view) {

        view.attachFindReplaceListener(e -> {
            String findTerm = view.getFindTerm().trim();
            String replaceTerm = view.getReplaceTerm().trim();
            String findReplaceText = view.getFindReplaceText().trim();

            if (findTerm.isEmpty() || replaceTerm.isEmpty() || findReplaceText.isEmpty()) {
                view.displayError("Please enter both a search term and search text.");
                return;
            }

            try {
                String html = postFindReplace(findTerm, replaceTerm, findReplaceText);

                ResultsView resultsView = new ResultsView("Find/Replace", html);
                new ResultsController(resultsView);
                resultsView.setVisible(true);
            } catch (RuntimeException ex) {
                view.displayError("Unable to perform search!");
            }
        });
    }

    private String postFindReplace(String find, String replace, String text) {
        try {
            JSONObject postBody = new JSONObject();
            postBody.put("find", find);
            postBody.put("replace", replace);
            postBody.put("text", text);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SEARCH_URL))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Accept", "text/html")
                    .POST(HttpRequest.BodyPublishers.ofString(postBody.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Invalid server request");
            }

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}