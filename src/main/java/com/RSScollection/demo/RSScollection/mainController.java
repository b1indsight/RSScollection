package com.RSScollection.demo.RSScollection;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.RSScollection.demo.RSScollection.User;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Controller
public class mainController {
    private static final Logger log = LoggerFactory.getLogger(mainController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/signup") 
    public @ResponseBody String addNewUser (@RequestParam String name
                    , @RequestParam String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
        log.info(user.toString()+" saved to the repo");
        return "Saved";
    }

    @GetMapping(path="/")
    public String welcomePage(@RequestParam(name="user", required=false, defaultValue="World")
        String namel,HttpServletRequest request,HttpSession session){
        String url = "https://pythonhunter.org/episodes/feed.xml";
        User anonymousUser = User.createAnonymous();
        session.setAttribute("currentUser", anonymousUser);
        session.setAttribute("name", "anonymousUser");
        try{
            XmlReader reader = new XmlReader(new URL(url));
            SyndFeed feed = new SyndFeedInput().build(reader);
            System.out.println(feed.getTitle());
            System.out.println("***********************************");
            String title = feed.getTitle();
            System.out.println(title);
            request.setAttribute("title", title);
            List<SyndEntry> entries =  feed.getEntries();
            request.setAttribute("entries", entries);
        }catch(Exception e){
            e.printStackTrace();
        }

        return "index";}

    @GetMapping(path="/login") 
    public @ResponseBody String login (@RequestParam String name
                    , @RequestParam String password, HttpServletRequest request
                    , HttpSession session) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        User currtentUser =  userRepository.findByName(name).get(0);
        System.out.println("currtent User=" + currtentUser + "User=" + user);
        if (user.getPassword().equals(currtentUser.getPassword())) {
            log.info("login success");
            session.setAttribute("currentUser", currtentUser);
        }else {
            log.info("login failed");
        }        
        log.info(user.toString()+" login");
        return "index";
    }
}