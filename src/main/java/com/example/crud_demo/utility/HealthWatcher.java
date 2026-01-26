package com.example.crud_demo.utility;

import com.example.crud_demo.repository.ProductDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * This component act as automated watcher
 * unlike a static endpoint which awaits to be called this class actively execute
 * health checks at regular intervals
 * * N.B: @EnableScheduling is required in the main class to make this system work
 */


@Component
public class HealthWatcher {

    private static final Logger logger = LoggerFactory.getLogger(HealthWatcher.class);
    private final CustomHealthIndicator healthIndicator;

    @Autowired
    public HealthWatcher(CustomHealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }

    /**
     * @Scheduled(fixedRate = 10000) tells spring to execute this every 10s.
     */
    @Scheduled(fixedDelay = 10000)
    public void checkSystemStatus()
    {
        logger.info("Starting health check...");

        //here we will use our customHealthIndicator, alternatively we could execute a lightweight operation like  db.count();  to confirm that the db connection works


        try
        {
            var currentHealth = healthIndicator.health();

            if(!currentHealth.getStatus().equals(Status.UP))
            {
                //something is wrong, extract the message and handle it
                String reason = currentHealth.getDetails().get("message").toString();
                String status = currentHealth.getStatus().toString();
                triggerAlert(status, reason);
            }
            else
            {
                logger.info("System is reachable and has returned with an UP status and the following message: " + currentHealth.getDetails().get("message").toString());
            }



        } catch (Exception e) {
            triggerAlert( "EXCEPTION", e.getMessage());
        }
    }


    /**
     * function to react to the possible downtime, in a real project here we could send an email or try some backup recovery
     */
    private void triggerAlert(String status,String errorMessage) {

        logger.error("===== ALERT: Possible downtime =====");

        logger.error("System status: " + status + " with the following message: " + errorMessage);
    }

}
