/**
 * 
 */
package com.ahmetalbayrak.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ahmetalbayrak.model.Books;
import com.ahmetalbayrak.model.User;
import com.ahmetalbayrak.service.BooksService;
import com.ahmetalbayrak.service.UserService;

/**
 * @author Ahmet
 *
 */

@ComponentScan({"com.ahmetalbayrak.service"})
@EntityScan("com.ahmetalbayrak.model")
@EnableJpaRepositories("com.ahmetalbayrak.repository")
@Controller
@EnableAutoConfiguration
public class HomeController {
	

	@Autowired
	private UserService<User> userService;
	@Autowired 
	private BooksService bookService;
	
	ModelAndView modelAndView = new ModelAndView();
	
	@RequestMapping(value={"/", "/index"}, method = RequestMethod.GET)
	public ModelAndView homePage(){
		
		if(modelAndView.getModel().containsKey("user") == true ) {
			modelAndView.setViewName("book");
		}else {
			modelAndView.setViewName("index");
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public ModelAndView login(){
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView loginUser(@Valid User user, BindingResult bindingResult) {
		User email = new User();
		User pass = new User();
		email=userService.findUserByEmail(user.getEmail());
		pass=userService.findUserByPassword(user.getPassword());
		loginControl(email, pass);		
		return modelAndView;
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView registerUser(@Valid User formUser, BindingResult bindingResult) {
		User user = new User();
		user.setEmail(formUser.getEmail());
		user.setFirstName(formUser.getFirstName());
		user.setLastName(formUser.getLastName());
		user.setPassword(formUser.getPassword());
		if(user!=null) {
			userService.save(user);
			modelAndView.setViewName("login");
			return modelAndView;
		}
		modelAndView.setViewName("register");
		return modelAndView;
	}
	
	@RequestMapping(value="/book", method = RequestMethod.GET)
	public ModelAndView book(){		
		List<Books> books = bookService.getBooks();		
		modelAndView.addObject("books", books);
		modelAndView.setViewName("book");
		return modelAndView;
	}
	
	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public ModelAndView book(@Valid Books formBooks, BindingResult bindingResult) {
		Books book = new Books();
		book.setBookName(formBooks.getBookName());
		if(book!=null) {
			bookService.save(book);
			modelAndView.setViewName("book");
			return modelAndView;
		}
		modelAndView.setViewName("book");
		return modelAndView;
	}	
	
	void loginControl(User email, User pass) {
		if(email == pass && email!=null) {
			modelAndView.addObject("successMessage", "Giriş Başarılı");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("book");
		}else {		
			modelAndView.addObject("errorMessage", "Kullanıcı adı veya şifre Başarısız");
			modelAndView.setViewName("registration");
		}
	}	
}
