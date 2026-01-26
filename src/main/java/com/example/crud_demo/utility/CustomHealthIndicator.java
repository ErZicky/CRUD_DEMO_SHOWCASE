package com.example.crud_demo.utility;

import com.example.crud_demo.repository.ProductDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;


/**
 * This class implements HealthIndicator interface from Spring Boot Actuator.
 * it lets define custom health check that will be exposed automatically by the endpoint /actuator/health
 */

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final ProductDatabase db;

    @Autowired
    public CustomHealthIndicator(ProductDatabase productDatabase) {
        this.db = productDatabase;
    }


    @Override
    public Health health()
    {

        try
        {
            // in this project we consider the db down if it's empty or the latency becomese too high otherwise "healthy"

            long count = db.count();

            if(count <= 0)
            {
                //in addition to the status we can also return custom message with it
                return Health.down()
                        .withDetail("message", "the db is empty, maybe it's down?")
                        .build();

            }

            //latency check
            long start = System.currentTimeMillis();

            //we execute a query like counting elements
            db.count();

            long latency = System.currentTimeMillis() - start;

            if(latency > 500)
            {
                /* we can also return custom status in addition to up and down, for example if the latency is high
                * technically the db is up but not optimal, so degraded
                */

                return Health.status("DEGRADED")
                        .withDetail("latency", latency + "ms")
                        .withDetail("message", "The db is reachable but the latency is high, maybe the db is overloaded?")
                        .build();
            }

            //if we pass all checks then the system is healthy
            return Health.up()
                    .withDetail("count", count)
                    .withDetail("message", "the system is up and operational and it's processing transactions")
                    .build();

        } catch (Exception e) {
        // in case of any problem (like the db unreachable) we consider it down
        return Health.down(e)
                .withDetail("error", "Health checking failed for some reason")
                .build();
    }

    }

}
