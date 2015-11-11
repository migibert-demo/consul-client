package com.migibert.demo.consul.client;

import com.orbitz.consul.*;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.health.ServiceHealth;

import java.util.List;

public class FirstSteps {
    public static void main(String[] args) throws NotRegisteredException {
        String deadServiceName = "MyDeadService";
        String deadServiceId = "1";
        String livingServiceName = "MyLivingService";
        String livingServiceId = "2";

        // Register service
        Consul consul = Consul.builder().build();
        AgentClient agentClient = consul.agentClient();
        agentClient.register(8080, 3L, deadServiceName, deadServiceId); // registers with a TTL of 3 seconds
        agentClient.pass(deadServiceId);

        agentClient.register(8081, 120L, livingServiceName, livingServiceId); // registers with no TTL
        agentClient.pass(livingServiceId);

        // Find healthy services
        HealthClient healthClient = consul.healthClient();
        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances("DataService").getResponse();
        System.out.println(nodes);
        nodes = healthClient.getHealthyServiceInstances("MyService").getResponse(); // discover only "passing" nodes
        System.out.println(nodes);

        // Key value storage
        KeyValueClient kvClient = consul.keyValueClient();
        kvClient.putValue("myapp.feature.enabled", "false");

        System.out.println("is value foo present ?                         => " + kvClient.getValueAsString("foo").isPresent());
        System.out.println("is value myapp.feature.enabled present ?       => " + kvClient.getValueAsString("myapp.feature.enabled").isPresent());
        System.out.println("what is the value of myapp.feature.enabled ?   => " + kvClient.getValueAsString("myapp.feature.enabled").get());

        // Who is the leader ?
        StatusClient statusClient = consul.statusClient();
        System.out.println(statusClient.getLeader());
    }
}
