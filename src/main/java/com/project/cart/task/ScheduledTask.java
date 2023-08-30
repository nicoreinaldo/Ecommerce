package com.project.cart.task;

import com.project.cart.model.Cart;
import com.project.cart.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private CartService cartService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    //Run every 10 minute
    @Scheduled(fixedRate = 600000)
    public void cleanupExpiredCarts() {
        try {
            List<Cart> carts = cartService.getAllCarts();

            LocalDateTime currentTime = LocalDateTime.now();
            if(carts.size() > 0){
                for (Cart cart : carts) {
                    LocalDateTime lastActivity = cart.getLastActivity();
                    long minutesDiff = ChronoUnit.MINUTES.between(lastActivity, currentTime);

                    if (minutesDiff >= 10) {
                        cartService.deleteCart(cart.getId());
                        log.info("Deleted cart with ID: {} At time {}" , cart.getId() , currentTime);
                    }
                }
            }
            log.info("Deleted carts complete {} ", currentTime);
        } catch (Exception e) {
            log.info("Error to try delete carts {} ", e);
        }
    }
}
