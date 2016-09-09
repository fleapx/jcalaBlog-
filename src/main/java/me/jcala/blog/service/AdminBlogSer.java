package me.jcala.blog.service;

import me.jcala.blog.domain.BlogView;
import me.jcala.blog.mapping.AdminBlogMapper;
import me.jcala.blog.service.inter.AdminBlogSerInter;
import me.jcala.blog.service.inter.BlogSerInter;
import me.jcala.blog.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
@Service
public class AdminBlogSer implements AdminBlogSerInter{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminBlogSer.class);
    @Autowired
    private AdminBlogMapper adminBlogMapper;
    @Override
    public int addBlog(BlogView blogView)  throws Exception {//String title, String tags, String article,String md
        blogView.setDate(Tools.getTimestamp());
        adminBlogMapper.addBlog(blogView);
        addViewTag(blogView.getTags(),blogView.getVid());
        return blogView.getVid();
    }
    @Override
    public BlogView getBlogByVid(int vid){
        BlogView blogView=null;
        try {
            blogView=adminBlogMapper.getBlogById(vid);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return blogView;
    }
    @Override
    public List<BlogView> getBlogPage(int id){
        List<BlogView> blogList=new ArrayList<BlogView>();
        try {
            int start=(id-1)*8;
            blogList=adminBlogMapper.getEightBlogs(start);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return blogList;
    }
    @Override
    public int getPageNum(){
        int num=0;
        try {
            num=adminBlogMapper.getBlogNum();
        } catch (Exception e) {
           LOGGER.error(e.getMessage());
    }
    int pageNum=num%8==0?num/8:num/8+1;
    return pageNum;
    }
    @Override
    public void updateBlog(int id,BlogView blogView) throws Exception{
            blogView.setVid(id);
            adminBlogMapper.updateBlogById(blogView);
    }
    private void addViewTag(String tagStr,int vid) throws Exception{
        List<String> tagList=Tools.getTagList(tagStr);
        for (String tag:tagList){
            adminBlogMapper.addViewTag(tag,vid);
            adminBlogMapper.addTag(tag);
        }

    }
}
