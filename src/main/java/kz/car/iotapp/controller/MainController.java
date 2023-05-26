package kz.car.iotapp.controller;

import kz.car.iotapp.config.NotificationHandler;
import kz.car.iotapp.model.entity.Profile;
import kz.car.iotapp.repository.ProfileRepository;
import kz.car.iotapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Controller
@RequiredArgsConstructor
@RequestMapping("/welcome")
public class MainController {

    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final NotificationHandler notificationHandler;
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public String panelPage(Principal principal) throws IOException {
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
        Profile profile = profileRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST));
        if (profile.getRoles().contains(roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST))))
            return "admin-panel";
        else return "welcome";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-panel")
    public String userPanelPage() {
        return "welcome";
    }
}
