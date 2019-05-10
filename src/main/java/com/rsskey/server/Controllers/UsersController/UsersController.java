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

import javax.validation.constraints.Null;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping(path="/users")
public class UsersController {

    @RequestMapping(value="/create", method = POST)
    public ResponseEntity add(@RequestBody User user) {
        System.out.println(user);
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
            email.setMsg("Hello "+ user.getLogin() + ",\nWelcome to RSSKey, Sah, What a pleasure to see you here !!! \nMuch love from RSSKey Team!");
            email.addTo(user.getEmail());
            email.send();
        } catch (Exception e) {
            return new ResponseEntity(new APIError(HttpStatus.INTERNAL_SERVER_ERROR,"Failure on sending email" , e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        repo.add(user);
        User added = repo.findbyLogin(user.getLogin());

        return new ResponseEntity(new User(added.getLogin(), added.getEmail(), null, added.getID()), HttpStatus.CREATED);
    }

    @GetMapping(value="/authentification")
    public ResponseEntity authentification(@RequestParam(value="login") String name ,@RequestParam(value="password") String password)
    {
        UserRepository repo =  DAOFactory.getInstance().getUserRepository();
        User user = null;
        String token = null;

        if ((user = repo.findByLoginPass(name,password)) != null) {
            token = TokenAuth.createJWT(user.getID().toString(),user.getLogin(),user.getEmail(),80000000 );
            user.setToken(token);

            repo.update(user);
            return new ResponseEntity(new AuthorizationToken(token),
                    HttpStatus.OK);
        }
        else {
            return new ResponseEntity(new APIError(HttpStatus.UNAUTHORIZED, "Wrong email - password"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value="/updatepassword", method = PUT)
    public ResponseEntity modify(@RequestBody UpdatePasswordRequest request, @RequestHeader("token") String token)
    {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        if (user.getPassword() != request.getOldpassword()) {
            return new ResponseEntity(new APIError(HttpStatus.UNAUTHORIZED, "Wrong old password"), HttpStatus.UNAUTHORIZED);
        }
        if (request.getNewpassword() == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "New password empty"), HttpStatus.BAD_REQUEST);
        }
        user.setPassword(request.getNewpassword());
        UserRepository repo = DAOFactory.getInstance().getUserRepository();
        repo.update(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/update", method = PUT)
    public ResponseEntity infoUpdate(@RequestBody User user, @RequestHeader("token") String token)
    {
        User userData = TokenAuth.getUserByToken(token);

        if (userData == null) {
            return APIError.unauthorizedResponse();
        }
        if (user.getLogin() == null && user.getEmail() == null) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Missing Login or Mail to update"), HttpStatus.BAD_REQUEST);
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            return new ResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Email is not valid"), HttpStatus.BAD_REQUEST);
        }
        UserRepository repo = DAOFactory.getInstance().getUserRepository();

        if (user.getEmail() != null && !userData.getEmail().equals(user.getEmail())) {
            userData.setEmail(user.getEmail());
            try {
                Email email = new SimpleEmail();
                email.setHostName("smtp.googlemail.com");
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator("rsskeyserver", "Rsskey12345"));
                email.setSSLOnConnect(true);
                email.setFrom("user@gmail.com");
                email.setSubject("Welcome to RSSKey !");
                email.setMsg("Hello "+ user.getLogin() + ",\nYour address mail has been updated :) !!! \nMuch love from RSSKey Team!");
                email.addTo(userData.getEmail());
                email.send();
            } catch (Exception e) {
                return new ResponseEntity(new APIError(HttpStatus.INTERNAL_SERVER_ERROR,"Failure on sending email" , e), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (user.getLogin() != null && !userData.getLogin().equals(user.getLogin())) {
            userData.setLogin(user.getLogin());
            try {
                Email email = new SimpleEmail();
                email.setHostName("smtp.googlemail.com");
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator("rsskeyserver", "Rsskey12345"));
                email.setSSLOnConnect(true);
                email.setFrom("user@gmail.com");
                email.setSubject("Welcome to RSSKey !");
                email.setMsg("Hello "+ userData.getLogin() + ",\nYour login has been updated :) !!! \nMuch love from RSSKey Team!");
                email.addTo(userData.getEmail());
                email.send();
            } catch (Exception e) {
                return new ResponseEntity(new APIError(HttpStatus.INTERNAL_SERVER_ERROR,"Failure on sending email" , e), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        repo.update(userData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/delete", method = DELETE)
    public ResponseEntity delete(@RequestHeader("token") String token) {
        User user = TokenAuth.getUserByToken(token);
        if (user == null) {
            return APIError.unauthorizedResponse();
        }
        UserRepository repo =  DAOFactory.getInstance().getUserRepository();
        repo.delete(user.getID());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
