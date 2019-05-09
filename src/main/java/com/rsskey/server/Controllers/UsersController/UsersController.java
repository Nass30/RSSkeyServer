package com.rsskey.server.Controllers.UsersController;

import com.rsskey.server.DAO.Exception.DAOFactory;
import com.rsskey.server.Controllers.APIError;
import com.rsskey.server.Models.User;
import com.rsskey.server.Repository.UserRepository;
import com.rsskey.server.Utils.TokenAuth;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping(path="/users")
public class UsersController {

    @GetMapping(value="/authentification")
    public ResponseEntity authentification(@RequestParam(value="login") String name ,@RequestParam(value="password") String password)
    {
        UserRepository repo =  DAOFactory.getInstance().getUserRepository();
        User user = null;

        if((user = repo.findByLoginPass(name,password)) != null)
            return new ResponseEntity(new AuthorizationToken(TokenAuth.createJWT(user.getID().toString(),user.getLogin(),user.getEmail(),80000000 )),
                    HttpStatus.OK);
        else
            return new ResponseEntity(new APIError(HttpStatus.UNAUTHORIZED, "Wrong email - password"), HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value="/updatepassword/{id}", method = PUT)
    public ResponseEntity modify(@PathVariable Long id, @RequestParam(value="oldpassword") String oldPassword, @RequestParam(value="password") String password)
    {
        System.out.print("ID" + id.toString());
        UserRepository repo =  DAOFactory.getInstance().getUserRepository();
        User user = repo.findbyID(id);
        if (user == null || user.getPassword() != oldPassword) {
            return new ResponseEntity(new APIError(HttpStatus.UNAUTHORIZED, "Wrong id or old password"), HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(password);
        repo.update(user);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/update/{id}", method = PUT)
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

    @RequestMapping(value="/create", method = POST)
    public ResponseEntity add(@RequestBody User user) {
        if (user.getLogin() == null || user.getEmail() == null || user.getPassword() == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST,"Some informations are missing"), HttpStatus.BAD_REQUEST);
        }
        UserRepository repo = DAOFactory.getInstance().getUserRepository();
        if (repo.findbyLogin(user.getLogin()) != null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST,"The login is already taken"), HttpStatus.BAD_REQUEST);
        }
        if (repo.findbyMail(user.getEmail()) != null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST,"The mail is already taken"), HttpStatus.BAD_REQUEST);
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Email is not valid"), HttpStatus.BAD_REQUEST);
        }
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("rsskeyserver", "Rsskey12345"));
            email.setSSLOnConnect(true);
            email.setFrom("user@gmail.com");
            email.setSubject("Welcome to RSSKey !");
            email.setMsg("Hello "+ user.getLogin() + ",\nWelcome to RSSKey, Sah, What a pleasure to see you here !!! \nMuch love from RSSKey Team!\n\n\n\n\nImad le Bronze");
            email.addTo(user.getEmail());
            email.send();
        } catch (Exception e) {
            return new ResponseEntity(new APIError(HttpStatus.INTERNAL_SERVER_ERROR,"Failure on sending email" , e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User added = repo.add(user);
        return new ResponseEntity(added, HttpStatus.OK);
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
