package com.rsskey.server.Controllers;

import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Models.User;
import com.rsskey.server.Repository.UserRepository;
import com.rsskey.server.Utils.TokenAuth;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping(path="/users")
public class UsersController {

    @RequestMapping(value="/authentification", method = GET)
    public String authentification(@RequestParam(value="username") String name ,@RequestParam(value="password") String password)
    {
        UserRepository repo =  DAOFactory.getInstance().getUserRepository();
        String token = null;
        String jwtId = "SOMEID1234";
        String jwtIssuer = "JWT Demo";
        String jwtSubject = "Andrew";
        int jwtTimeToLive = 800000;
        User u = repo.findByLoginPass(name,password);

        if(u != null){
            token = TokenAuth.createJWT(u.getID().toString(),u.getLogin(),"test", jwtTimeToLive);
            try
            {
                TokenAuth.decodeJWT(token);
                System.out.println("TOKEN VALIDER!");
            } catch (Exception e) {
                System.out.println("TOKEN NON VALIDER!");
            }

            //request.getSession().setAttribute("token", token);
        } else
            token = "FUCK YOU DON'T TRY TO HACK US !!";
        return token;
    }

    @RequestMapping(value="/passwordupdate/{id}", method = POST)
    public User modify(@PathVariable Long id, @RequestParam(value="password") String password)
    {
        System.out.print( "ID" + id.toString());
        UserRepository repo =  DAOFactory.getInstance().getUserRepository();
        User user = repo.findbyID(id);

        if(user != null){
            user.setPassword(password);
            repo.update(user);
        }
        return user;
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public User infoUpdate(@PathVariable Long id, @RequestParam(value="email") String email)
    {
        UserRepository repo = DAOFactory.getInstance().getUserRepository();
        User user = repo.findbyID(id);

        if(user != null) {
            user.setEmail(email);
            repo.update(user);
        }
        return user;
    }

    @RequestMapping(value = "/delete/{id}", method = GET)
    public Boolean delete(@PathVariable Long id) {
        UserRepository repo =  DAOFactory.getInstance().getUserRepository();
        User user = repo.findbyID(id);
        Boolean erased = false;

        if (user != null) {
            repo.delete(id);
            erased = true;
        }
        return erased;
    }
}
