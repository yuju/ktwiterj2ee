/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ktwtr.rest;

import com.avaje.ebean.Ebean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ktwtr.models.*;

/**
 *
 * @author ram
 */
@Path("/ctrlcomments")
public class CtrlComments {

    @Context
    HttpServletRequest request;

    @Path("/submitcomment")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response submitComment(@FormParam("post-id") long postid, @FormParam("comment") String comment) {

        HttpSession session = request.getSession(true);
        Member member = Member.getMember(session.getAttribute("login").toString());
        Post post = Post.getPost(postid);

        Comment newcomment = new Comment();
        newcomment.setContent(comment);
        newcomment.setPost(post);
        newcomment.setAutor(member);
        Comment.setComment(newcomment);

        List<Post> posts = Post.all();
        List<Comment> comments;
        for (int i = 0; i < posts.size(); i++) {
            posts.get(i).setComments(Comment.getCmntsByPost(posts.get(i)));
            posts.get(i).setPostLikes(Likes.nbrLikesPerPost(posts.get(i)));
        }
        return Response.ok().entity(posts).build();
    }
}
