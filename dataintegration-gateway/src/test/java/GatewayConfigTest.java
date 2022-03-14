import com.datafan.dataintegration.core.util.JsonUtils;
import com.youngdatafan.portal.system.management.gateway.vo.DpGatewayRouteVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

/**
 * @author gavin
 * @create 2020/6/13 4:59 下午
 */
public class GatewayConfigTest {

    public static void main(String[] args) {
        final DpGatewayRouteVO route = new DpGatewayRouteVO();
        route.setId("dataintegration-common-sso-provider");
        route.setUri("lb://dataintegration-common-sso-provider");

        List<PredicateDefinition> predicates = new ArrayList<>();
        predicates.add(new PredicateDefinition("Path=/api/dataintegration-common-sso-provider/**"));
        route.setPredicates(JsonUtils.toString(predicates));

        List<FilterDefinition> filters = new ArrayList<>();
        filters.add(new FilterDefinition("StripPrefix=2"));
        route.setFilters(JsonUtils.toString(filters));

        Map<String, Object> metadata = new HashMap<>();
        route.setMetadata(JsonUtils.toString(metadata));

        System.out.println(JsonUtils.toString(route));
    }
}
