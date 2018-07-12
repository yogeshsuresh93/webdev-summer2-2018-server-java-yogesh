package com.example.webdevsummer22018serverjavayogesh.services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.webdevsummer22018serverjavayogesh.models.User;
import com.example.webdevsummer22018serverjavayogesh.repositories.UserRepository;

@RestController
public class UserService {

	@Autowired
	UserRepository userRepository;

	// http://localhost:8080/register
	@PostMapping("/register")
	public User register(@RequestBody User user, HttpSession session) {
		User cu = userRepository.save(user);

		session.setAttribute("currentUser", cu);

		return cu;
	}
	
	@GetMapping("/profile")
	public Optional<User> profile(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		
		return userRepository.findById(currentUser.getId());
		
	}
	
	@GetMapping("/logout")
	public void logout(HttpSession session) {
		session.invalidate();
		
	}
	
	@GetMapping("/api/user/{userId}")
	public Optional<User> findUserByUserId(@PathVariable("userId") String userId) {
		int id = Integer.parseInt(userId);
		return  userRepository.findById(id);
	}
	
	@GetMapping("/api/user")
	public List<User> findAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	@PostMapping("/login")
	public User login(@RequestBody User user, HttpSession session) {
		user= userRepository.findUserByCredentials(user.getUsername(), user.getPassword());
		if(user != null)
		{	session.setAttribute("currentUser", user);
			return user;
		}
		return null;
	}

	@PutMapping("/api/user/{userId}")
	public User updateUser(
			@PathVariable("userId") int id,
			@RequestBody User newUser) {
		Optional<User> optional = userRepository.findById(id);
		
		if(optional.isPresent()) {
			User user = optional.get();
			user.setFirstname(newUser.getFirstname());
			user.setLastname(newUser.getLastname());
			user.setPhoneNo(newUser.getPhoneNo());
			user.setEmail(newUser.getEmail());
			user.setRole(newUser.getRole());
			user.setDateOfBirth(newUser.getDateOfBirth());
			return userRepository.save(user);
		}
		return null;
	}
	
	@DeleteMapping("/api/user/{userId}")
	public void deleteUser(@PathVariable("userId") int id) {
		userRepository.deleteById(id);
	}
}