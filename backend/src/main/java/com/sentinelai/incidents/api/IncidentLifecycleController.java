package com.sentinelai.incidents.api;
import com.sentinelai.incidents.domain.Incident; import com.sentinelai.incidents.infrastructure.IncidentRepository; import java.util.UUID; import org.springframework.http.HttpStatus; import org.springframework.security.core.Authentication; import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/v1/incidents") public class IncidentLifecycleController {
 private final IncidentRepository incidents; public IncidentLifecycleController(IncidentRepository incidents){this.incidents=incidents;}
 @GetMapping("/{id}") public Incident get(@PathVariable UUID id,Authentication auth){return incidents.findById(id).filter(i->tenant(auth).equals(i.getTenantId())).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
 @PatchMapping("/{id}/status") public Incident status(@PathVariable UUID id,@RequestBody StatusRequest request,Authentication auth){var i=get(id,auth); try{i.changeStatus(request.status());}catch(IllegalArgumentException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());} return incidents.save(i);}
 private String tenant(Authentication a){if(a instanceof JwtAuthenticationToken j){var t=j.getToken().getClaimAsString("tenant_id");if(t!=null&&!t.isBlank())return t;}throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Tenant missing");} public record StatusRequest(String status){}
}
