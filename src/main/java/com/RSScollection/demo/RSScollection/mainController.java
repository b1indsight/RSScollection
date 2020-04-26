package com.RSScollection.demo.RSScollection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class mainController {
    private static final Logger log = LoggerFactory.getLogger(mainController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private RssRepository rssRepository;

    @GetMapping(path = "/signup")
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
        log.info(user.toString() + " saved to the repo");
        return "Saved";
    }

    @GetMapping(path = "/")
    public String welcomePage(@RequestParam(name = "user", required = false, defaultValue = "World") String namel,
            HttpServletRequest request, HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            User anonymousUser = User.createAnonymous();
            session.setAttribute("currentUser", anonymousUser);
            session.setAttribute("name", "anonymousUser");
        }
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            List<Rss> currtentRss = rssRepository.findByUserId(currentUser.getId());
            ArrayList<Posts> posts = getAllPostsFromRss(currtentRss, currentUser.getId());
            request.setAttribute("posts", posts);
            System.out.println();
            System.out.println("***********************************");
            log.info(session.getAttribute("currentUser") + "currentUser is here");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }

    @GetMapping(path = "/login")
    public String login(@RequestParam String name, @RequestParam String password, HttpServletRequest request,
            HttpSession session) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        User currtentUser = userRepository.findByName(name).get(0);
        System.out.println("currtent User=" + currtentUser + "User=" + user);
        if (user.getPassword().equals(currtentUser.getPassword())) {
            log.info("login success");
            session.setAttribute("currentUser", currtentUser);

        } else {
            log.info("login failed");
        }
        log.info(user.toString() + " login");
        log.info(session.getAttribute("currentUser") + "currentUser is here");
        return "forward:/";
    }

    @PostMapping(path = "/addRss")
    public @ResponseBody String addRss(@RequestParam String url, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        Rss rss = new Rss(url, user.getId());
        rssRepository.save(rss);
        log.info("add Rss is done");
        return "success";
    }

    private ArrayList<Posts> getAllPostsFromRss(List<Rss> currtentRss, int currentUserId)
            throws MalformedURLException, IOException, IllegalArgumentException, FeedException {
        ArrayList<Posts> res = new ArrayList<Posts>();
        for (Rss p : currtentRss) {
            XmlReader reader = new XmlReader(new URL(p.getUrl()));
            SyndFeed feed = new SyndFeedInput().build(reader);
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                Posts post = new Posts(entry.getTitle(), entry.getLink(), 
                                    entry.getAuthor(), entry.getPublishedDate(),
                                    entry.getDescription().getValue(), currentUserId);
                res.add(post);
            }
        } 
        return res;
    }

}                                    