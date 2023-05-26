package kz.car.iotapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Controller
public class NotificationController {

    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    @GetMapping("/send-notification")
    public void sendNotification() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    for (SseEmitter emitter : emitters) {
                        emitter.send(SseEmitter.event()
                                .data("New Notification")
                                .name("notification"));
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
