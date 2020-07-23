package com.RSScollection.demo.RSScollection;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.RSScollection.demo.RSScollection.models.Rss;
import com.RSScollection.demo.RSScollection.models.User;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mainRestController {

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
    return new state( (boolean) session.getAttribute("islogin"));
  }

  @GetMapping(path = "/getFeeds")
  public List<Rss> getFeeds(HttpSession session) {
    List<Rss> res = new ArrayList<Rss>();
    User currentUser = (User)session.getAttribute("currentUser");
    try (SqlSession sqlSession = MybatisUtil.getSqlSession()){
      List<Object> tmp = 
          sqlSession.selectList("findByUserId", currentUser.getId());
      if (tmp.get(0) instanceof Rss) {
        res = (List<Rss>)(List<?>) tmp;
      }
    }
    
    return res;
  }

  @GetMapping(path = "/getLogin")
  public connectSession getLogin (HttpSession session, 
      @RequestParam String name, @RequestParam String password) {
    User user = new User();
    user.setName(name);
    user.setPassword(password);
    try (SqlSession sqlSession = MybatisUtil.getSqlSession()){
      User currentUser = sqlSession.selectOne("findByName", user.getName());
      if (currentUser.getPassword().equals(user.getPassword())){
        session.setAttribute("currentUser", currentUser);
        session.setAttribute("islogin", true);
      }
    }
    return new connectSession(session.getId());
  }

  

}