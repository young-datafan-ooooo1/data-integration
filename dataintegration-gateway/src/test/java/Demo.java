import com.datafan.dataintegration.core.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * @author gavin
 * @create 2020/6/30 7:51 下午
 */
public class Demo {

    @Test
    public void all_project_bak() throws Exception {
        final CloseableHttpClient client = HttpClientBuilder.create().build();

        final HttpGet get = new HttpGet("http://gitlab.vincenthsing.top:50805/api/v4/projects?per_page=100");
        String json = request(client, get);

        List<HashMap<String, Object>> data = JsonUtils.parseArray(json, new TypeReference<List<HashMap<String, Object>>>() {
        });

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File("all_project_bak.sh")))) {

            int count = 0;
            for (HashMap<String, Object> datum : data) {
                HttpGet branchesGet = new HttpGet("http://gitlab.vincenthsing.top:50805/api/v4/projects/" + datum.get("id") + "/repository/branches");
                String branchesJson = request(client, branchesGet);

                List<HashMap<String, Object>> branchesData = JsonUtils.parseArray(branchesJson, new TypeReference<List<HashMap<String, Object>>>() {
                });
                for (HashMap<String, Object> branchesDatum : branchesData) {
                    String cmd = "git clone -b " + branchesDatum.get("name") + " "
                            + ((String) datum.get("http_url_to_repo")).replace("http://gitlab.vincenthsing.top", "http://gitlab.vincenthsing.top:50805")
                            + " " + datum.get("name") + "_" + branchesDatum.get("name");

                    outputStreamWriter.write(cmd);
                    outputStreamWriter.write("\n\n");
                    count++;
                }
            }

            System.out.println(count);
        }
    }

    private String request(CloseableHttpClient client, HttpGet get) throws IOException {
        String json;
        get.setHeader("PRIVATE-TOKEN", "e1RtVf7QzeZSqFS8os31");
        try (CloseableHttpResponse execute = client.execute(get)) {

            json = EntityUtils.toString(execute.getEntity());
            EntityUtils.consume(execute.getEntity());
        }
        return json;
    }
}
