package com.groupx.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    private final RabbitTemplate rabbit;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random rnd = new Random();

    public UserService(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void startConsuming() throws Exception {
        while (true) {
            double factor = usageFactor();
            double kwh = 0.001 + 0.004 * factor * rnd.nextDouble();
            Map<String,Object> msg = new HashMap<>();
            msg.put("type", "USER");
            msg.put("association", "COMMUNITY");
            msg.put("kwh", Math.round(kwh*10000)/10000.0);
            msg.put("datetime", Instant.now().toString());
            rabbit.convertAndSend("energy", mapper.writeValueAsString(msg));
            System.out.println("Sent USER message: " + msg);
            Thread.sleep(1000 + rnd.nextInt(4000));
        }
    }

    /**
     * Returns a usage factor based on UTC hour:
     * peak hours 6–10 & 17–21 → random 0.8–1.0, else 0.2–0.6
     */
    private double usageFactor() {
        int hour = Instant.now().atZone(ZoneOffset.UTC).getHour();
        if ((hour >= 6 && hour < 10) || (hour >= 17 && hour < 21)) {
            return 0.8 + 0.2 * rnd.nextDouble();
        } else {
            return 0.2 + 0.4 * rnd.nextDouble();
        }
    }
}