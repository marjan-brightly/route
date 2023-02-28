package rs.brightly.route.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.brightly.route.dto.RouteResponse;
import rs.brightly.route.service.RouteService;

import java.util.List;

@RestController
public class RouteController {

    private RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping(value = "/routing/{origin}/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteResponse> getPassing(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {
        try {
            List<String> route = routeService.getRoute(origin, destination);
            if (route == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(new RouteResponse(route));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
