package com.RSScollection.demo.RSScollection;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mainRestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RssRepository rssRepository;

    private class state {
        private boolean state;
        
        state(boolean s) {
            this.state = s;
        }

        public boolean getState() {
            return this.state;
        }
    }

    private class connectSession {
        String session_id;

        connectSession(String s) {
            this.session_id = s;
        }

        public String getSessionId() {
            return this.session_id;
        }
    }

    @GetMapping(path = "/user")
    public User user (HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    @GetMapping(path = "/isLoggedIn")
    public state isLoggedIn(HttpSession session) {
        if (session.getAttribute("currentUser").equals(User.createAnonymous())) {
            return new state(false);
        } else {
            return new state(true);
        }
    }

    @GetMapping(path = "/getFeeds")
    public List<Rss> getFeeds(HttpSession session) {
        User currentUser = (User)session.getAttribute("currentUser");
        List<Rss> res = rssRepository.findByUserId(currentUser.getId());
        return res;
    }

    @GetMapping(path = "/getLogin")
    public connectSession getLogin (HttpSession session , @RequestParam String name, 
            @RequestParam String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        User currtentUser = userRepository.findByName(name).get(0);
        session.setAttribute("currentUser", currtentUser);
        return new connectSession(session.getId());
    }
}