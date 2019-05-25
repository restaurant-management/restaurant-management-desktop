package dao.restApi;

import dao.exceptions.RequestFailException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HttpConnection {
    private static String url = "https://restaurant-management-server.herokuapp.com";
    private HttpClient client;

    public HttpConnection() {
        client = HttpClientBuilder.create().build();
    }

    public String post(final String uri, final Header[] headers, final List<NameValuePair> body) throws IOException, RequestFailException {
        HttpPost request = new HttpPost(url + uri);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeaders(headers);
        request.setEntity(new UrlEncodedFormEntity(body));
        System.out.print("POST " + uri);
        return handleResponse(request);
    }

    public String get(final String uri, final Header[] headers) throws IOException, RequestFailException {
        HttpGet request = new HttpGet(url + uri);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeaders(headers);
        System.out.print("GET " + uri);
        return handleResponse(request);
    }

    public String put(final String uri, final Header[] headers, final List<NameValuePair> body) throws IOException, RequestFailException {
        HttpPut request = new HttpPut(url + uri);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeaders(headers);
        request.setEntity(new UrlEncodedFormEntity(body));
        System.out.print("PUT " + uri);
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
        return result.toString();
    }

}
