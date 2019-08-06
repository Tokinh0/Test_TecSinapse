import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conection {

    String get_items() throws JSONException, IOException {
        String url_str = "https://eventsync.portaltecsinapse.com.br/public/recrutamento/input?email=jean-recados@outlook.com";
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONArray array_of_items = new JSONArray(response.toString());
        return array_of_items.toString();
    }

    public void send_post(String answer) throws IOException {
        String url_str = "https://eventsync.portaltecsinapse.com.br/public/recrutamento/finalizar?email=jean-recados@outlook.com";
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("POST");
        request.setRequestProperty("Content-Type", "text/plain");

        request.setDoOutput(true);
        OutputStream os = request.getOutputStream();
        os.write(answer.getBytes());
        os.flush();
        os.close();

        int responseCode = request.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    request.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(" return of TecSinapse_API: " + response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }
}
