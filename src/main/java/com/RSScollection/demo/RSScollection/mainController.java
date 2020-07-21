package com.RSScollection.demo.RSScollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.WireFeedOutput;
import com.rometools.rome.io.XmlReader;
import com.RSScollection.demo.RSScollection.models.Posts;
import com.RSScollection.demo.RSScollection.models.Rss;
import com.RSScollection.demo.RSScollection.models.User;
import com.rometools.opml.*;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.opml.io.impl.OPML20Generator;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class mainController {
  private static final Logger log = 
      LoggerFactory.getLogger(mainController.class);

  @GetMapping(path = "/signup")
  public @ResponseBody String addNewUser(
      @RequestParam String name,
      @RequestParam String password) {
    User user = new User();
    user.setName(name);
    user.setPassword(password);
    try(SqlSession sqlSession = MybatisUtil.getSqlSession()){
      sqlSession.insert("saveUser", user);
      sqlSession.commit();
    }
    log.info(user.toString() + " saved to the repo");
    return "Saved";
  }

  @GetMapping(path = "/")
  public String welcomePage(
      @RequestParam(name = "user", 
          required = false, 
          defaultValue = "World") String namel,
      HttpServletRequest request, HttpSession session) {
    if (session.getAttribute("islogin") == null 
        || (Boolean) session.getAttribute("islogin") != true) {
      log.info("user is not login");
      session.setAttribute("islogin", false);
    } else {
      User currentUser = (User) session.getAttribute("currentUser");
      log.info("user id =" + currentUser.getId());
      ArrayList<Rss> currentRss = new ArrayList<Rss>();

      for (String url : currentUser.getRssUrl()) {
        log.info("Rss url is" + url);
        currentRss.add(new Rss(url, currentUser.getId()));
      }
      log.info("Rss list size is" + currentRss.size());

      ArrayList<Posts> posts;
      try {
        posts = getAllPostsFromRss(currentRss, currentUser.getId());
      } catch (IllegalArgumentException | IOException | FeedException e) {
        e.printStackTrace();
        throw new RuntimeException("can't get posts from Rss list.");
      } finally{
        MybatisUtil.closeSqlSession();
      }
      session.setAttribute("posts", posts);
    }
      log.info("");
      log.info("***********************************");
      log.info(session.getAttribute("currentUser") + "currentUser is here");
    
      return "index";
  }

  @GetMapping(path = "/login")
  public String login(@RequestParam String name, 
      @RequestParam String password, 
      HttpServletRequest request,
      HttpSession session) {
    User user = new User();
    user.setName(name);
    user.setPassword(password);
    
    try(SqlSession sqlSession = MybatisUtil.getSqlSession()){
      User currentUser = sqlSession.selectOne("findByName", name);
    
      System.out.println("current User=" + currentUser + "User=" + user);
      if (user.getPassword().equals(currentUser.getPassword())) {
        log.info("login success");
        session.setAttribute("currentUser", currentUser);
        session.setAttribute("islogin", true);
      } else {
        log.info("login failed");
      }

      log.info(user.toString() + " login");
      log.info(session.getAttribute("currentUser") + "currentUser is here");
    }finally{
      MybatisUtil.closeSqlSession();
    }
    
    return "forward:/";
  }

  @PostMapping(path = "/addRss")
  public @ResponseBody String addRss(@RequestParam String url, 
      HttpServletRequest request, 
      HttpSession session) {
    User user = (User) session.getAttribute("currentUser");
    Rss rss = new Rss(url, user.getId());

    try (SqlSession sqlSession = MybatisUtil.getSqlSession()){
      User currentUser = sqlSession.selectOne("findById", user.getId());
      currentUser.addUrl(rss.getUrl());
      sqlSession.update("update", currentUser);
      sqlSession.commit();
    }finally{
      MybatisUtil.closeSqlSession();
    }
    log.info("add Rss is done");

    return "success";
  }

  private ArrayList<Posts> getAllPostsFromRss(List<Rss> currentRss, 
      int currentUserId)
      throws MalformedURLException, IOException, IllegalArgumentException, 
      FeedException {
    ArrayList<Posts> res = new ArrayList<Posts>();

    for (Rss p : currentRss) {
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

  @PostMapping(path = "/uploadOpml")
  public String uploadOpml(@RequestParam("file") MultipartFile file, 
      RedirectAttributes redirectAttributes, HttpSession session) {
    List<Rss> uploadRss = 
        parsedOpmlFile(file, (User) session.getAttribute("currentUser"));

    for (Rss rss : uploadRss) {
      log.info(rss.getUrl() + " saved");
    }

    redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded " + file.getOriginalFilename() + "!");

    return "redirect:/";
  }

  private List<Rss> parsedOpmlFile(MultipartFile file, User user) {
    ArrayList<Rss> res = new ArrayList<Rss>();
    WireFeedInput input = new WireFeedInput();
    String path = "E:\\";
    File tmpFile = new File(path, "demo.xml");

    try (SqlSession sqlSession = MybatisUtil.getSqlSession()) {
      FileUtils.copyInputStreamToFile(file.getInputStream(), tmpFile);
      Opml feed = (Opml) input.build(tmpFile);
      log.info(feed.getTitle());
      List<Outline> outlines = feed.getOutlines();

      for (Outline outline : outlines) {
        for (Outline child : outline.getChildren()) {
          Rss rss = new Rss(child.getXmlUrl(), user.getId());
          rss.setTitle(child.getTitle());
          res.add(rss);
          sqlSession.insert("saveRss", rss);
        }
      }
      sqlSession.commit();

      if (true) {
        log.info("file delete success");
      }
    } catch (IllegalArgumentException | IOException | FeedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally{
      MybatisUtil.closeSqlSession();
    }

    return (List<Rss>) res;
  }

  @GetMapping(path = "/exportOpml")
  public ResponseEntity<Resource> exportOpml(HttpSession session) 
      throws IOException {
    String path = "\\";
    File tmpFile = new File(path, "tmp.opml");
    OPML20Generator generator = new OPML20Generator();
    User currentUser = (User) session.getAttribute("currentUser");
    List<Rss> rss;
    
    try (SqlSession sqlSession = MybatisUtil.getSqlSession()){
      rss = sqlSession.selectList("findByUserId", currentUser.getId());
    }finally {
      MybatisUtil.closeSqlSession();
    }

    ArrayList<Module> tmpModules = new ArrayList<Module>();
    for (int i = 0; i < rss.size(); i++) {
      SyndFeed feed = new SyndFeedImpl();
      Rss tmp = rss.get(i);
      feed.setUri(tmp.getUrl());
      feed.setTitle(tmp.getTitle());
      List<Module> modules = feed.getModules();
      tmpModules.addAll(modules);
    }

    Document res;
    Opml wirefeed = new Opml();
    wirefeed.setModules(tmpModules);
    wirefeed.setFeedType("rss_2.0");
    wirefeed.setTitle("export");
    try {
      res = generator.generate(wirefeed);
      XMLOutputter XMLoutput = new XMLOutputter();
      FileOutputStream fileOutput = new FileOutputStream(tmpFile);
      XMLoutput.output(res, fileOutput);
    } catch (IllegalArgumentException | FeedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    HttpHeaders header = new HttpHeaders();
    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=1.opml");
    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
    header.add("Pragma", "no-cache");
    header.add("Expires", "0");

    Path resPath = Paths.get(tmpFile.getAbsolutePath());
    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(resPath));

    return ResponseEntity.ok()
        .headers(header)
        .contentLength(tmpFile.length())
        .contentType(MediaType.APPLICATION_XML)
        .body(resource);
  }

  /**
   * this view function will return the full post in a page  
   * @param id post id 
   * @param session
   * @return
   */
  @GetMapping(path = "/post/{id}")
  public String getPost(@PathVariable int id, HttpServletRequest request,
      HttpSession session) {
    PostService ps = PostService.getInstance();
    Posts p = ps.getPost(id);
    request.setAttribute("post", p);
    return "post";
  }
}

