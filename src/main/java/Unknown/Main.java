package Unknown;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class Main {
    private static final String API_KEY = "";
    private static final String API_URL = "";

    public static void main(String[] args) {
        Main client = new Main();
        String response = client.getCompletion("Write a Java program for prime number generation");
        System.out.println(response);
    }

    public String getCompletion(String prompt) {
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = createJsonRequestBody(prompt);
        RequestBody body = RequestBody.create(requestBody, mediaType);

        Request request = new Request.Builder()
                .url(API_URL + "/chat/completions")
                .post(body)
                .addHeader("Authorization", "API_KEY " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                ApiResponse apiResponse = new Gson().fromJson(response.body().string(), ApiResponse.class);
                return apiResponse.choices[0].message.content;
            } else {
                return "Failed to get response: " + response.code();
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String createJsonRequestBody(String prompt) {
        return "{\"model\":\"gpt-4\",\"temperature\":0,\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}]}";
    }

    static class ApiResponse {
        Choice[] choices;
    }

    static class Choice {
        Message message;
    }

    static class Message {
        String content;
    }
}