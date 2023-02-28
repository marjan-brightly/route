package rs.brightly.route.dto;

import java.util.List;

public class RouteResponse {
    List<String> route;

    public RouteResponse(List<String> route) {
        this.route = route;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }
}
