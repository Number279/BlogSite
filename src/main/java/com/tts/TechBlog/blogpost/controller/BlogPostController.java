package com.tts.TechBlog.blogpost.controller;

import com.tts.TechBlog.blogpost.model.BlogPost;
import com.tts.TechBlog.blogpost.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogPostController {

    @Autowired
    private BlogPostService service;
    private static List<BlogPost> posts = new ArrayList<>();

    @GetMapping("/")
    public String index(BlogPost blogPost, Model model) {
        posts = service.listAll();
        model.addAttribute("posts", posts);
        return "blogpost/index";
    }

    private BlogPost blogPost;

    @PostMapping(value = "/blogposts")
    public String addNewBlogPost(BlogPost blogPost, Model model) {
        service.addBlogPost(new BlogPost(blogPost.getTitle(), blogPost.getAuthor(), blogPost.getBlogEntry()));
        posts = service.listAll();
        model.addAttribute("title", blogPost.getTitle());
        model.addAttribute("author", blogPost.getAuthor());
        model.addAttribute("blogEntry", blogPost.getBlogEntry());
        return "blogpost/result";
    }

    @GetMapping(value = "/blogposts/createPost")
    public String newBlog(BlogPost blogPost) {
        return "blogpost/createPost";
    }

    @RequestMapping(value = "/blogposts/{id}", method = RequestMethod.DELETE)
    public String deletePostWithId(@PathVariable Long id, BlogPost blogPost, Model model) {
        service.deleteBlogPost(id);
        posts = service.listAll();
        model.addAttribute("posts", posts);
        return "blogpost/delete";
    }

    @GetMapping(value = "/blogposts/{id}")
    public String editPostWithId(@PathVariable Long id, BlogPost blogPost, Model model) {
        Optional<BlogPost> post = service.findById(id);
        if (post.isPresent()) {
            BlogPost actualPost = post.get();
            model.addAttribute("blogPost", actualPost);
        }
        posts = service.listAll();
        return "blogpost/edit";
    }

    @RequestMapping(value = "/blogposts/update/{id}", method = RequestMethod.POST)
    public String updateExistingPost(@PathVariable Long id, BlogPost blogPost, Model model) {
        Optional<BlogPost> post = service.findById(id);
        if (post.isPresent()) {
            BlogPost actualPost = post.get();
            actualPost.setTitle(blogPost.getTitle());
            actualPost.setAuthor(blogPost.getAuthor());
            actualPost.setBlogEntry(blogPost.getBlogEntry());
            service.addBlogPost(actualPost);
            model.addAttribute("blogPost", actualPost);
            model.addAttribute("title", blogPost.getTitle());
            model.addAttribute("author", blogPost.getAuthor());
            model.addAttribute("blogEntry", blogPost.getBlogEntry());

        }
        return "blogpost/result";
    }
}
