package dao.restApi;

import dao.exceptions.RequestFailException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

class HttpConnection {
    private static String url = "https://restaurant-management-server.herokuapp.com";
    private HttpClient client;

    HttpConnection() {
        client = HttpClientBuilder.create().build();
    }

    String post(final String uri, final Header[] headers, final List<NameValuePair> body) throws IOException, RequestFailException {
        System.out.print("POST " + uri);
        HttpPost request = new HttpPost(url + uri);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        for (Header header : headers) {
            request.addHeader(header);
        }
        request.setEntity(new UrlEncodedFormEntity(body, "UTF-8"));
        return handleResponse(request);
    }

    String get(final String uri, final Header[] headers) throws IOException, RequestFailException {
        System.out.print("GET " + uri);
        HttpGet request = new HttpGet(url + uri);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        for (Header header : headers) {
            request.addHeader(header);
        }
        return handleResponse(request);
    }

    String put(final String uri, final Header[] headers, final List<NameValuePair> body) throws IOException, RequestFailException {
        System.out.print("PUT " + uri);
        HttpPut request = new HttpPut(url + uri);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        for (Header header : headers) {
            request.addHeader(header);
        }
        request.setEntity(new UrlEncodedFormEntity(body, "UTF-8"));
        return handleResponse(request);
    }

    String delete(final String uri, final Header[] headers) throws IOException, RequestFailException {
        System.out.print("DELETE " + uri);
        HttpDelete request = new HttpDelete(url + uri);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        for (Header header : headers) {
            request.addHeader(header);
        }
        return handleResponse(request);
    }

    private String handleResponse(HttpRequestBase request) throws IOException, RequestFailException {
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        int responseCode = response.getStatusLine().getStatusCode();
        System.out.println(" " + responseCode);
        if (responseCode != HttpsURLConnection.HTTP_OK)
            throw new RequestFailException(responseCode, result.toString());
        return new String(result.toString().getBytes(), StandardCharsets.UTF_8);
    }

}
