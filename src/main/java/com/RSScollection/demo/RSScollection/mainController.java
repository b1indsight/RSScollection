package com.RSScollection.demo.RSScollection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.RSScollection.demo.RSScollection.User;

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
    public String welcomePage(@RequestParam(name="name", required=false, defaultValue="World")
        String namel){
            return "index";
    }
}