package via.sep.restful_server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.AgentDTO;
import via.sep.restful_server.model.Agent;
import via.sep.restful_server.repository.AgentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {
    private final AgentRepository agentRepository;

    @Autowired
    public AgentController(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody AgentDTO agentDTO) {
        Agent agent = new Agent();
        agent.setName(agentDTO.getName());
        agent.setContactInfo(agentDTO.getContactInfo());

        Agent savedAgent = agentRepository.save(agent);
        return ResponseEntity.ok(savedAgent);
    }

    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        return ResponseEntity.ok(agentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgent(@PathVariable Long id) {
        return agentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable Long id, @RequestBody AgentDTO agentDTO) {
        return agentRepository.findById(id)
                .map(agent -> {
                    agent.setName(agentDTO.getName());
                    agent.setContactInfo(agentDTO.getContactInfo());
                    return ResponseEntity.ok(agentRepository.save(agent));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgent(@PathVariable Long id) {
        return agentRepository.findById(id)
                .map(agent -> {
                    agentRepository.delete(agent);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}