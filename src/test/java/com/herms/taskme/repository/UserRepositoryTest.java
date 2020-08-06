package com.herms.taskme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import com.herms.taskme.model.User;

import net.bytebuddy.asm.Advice.Thrown;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void whenCreateShouldPersistData() {
		User user = new User(null, "Emerson", "Ribeiro Junior", "994457676", "Rua Joana da Silva, 139, Uberlândia", "herms", "123456");
		this.userRepository.save(user);
		assertThat(user.getId()).isNotNull();
		assertThat(user.getGivenName()).isEqualTo("Emerson");
		assertThat(user.getFamilyName()).isEqualTo("Ribeiro Junior");
		assertThat(user.getContact()).isEqualTo("994457676");
		assertThat(user.getAddress()).isEqualTo("Rua Joana da Silva, 139, Uberlândia");
		assertThat(user.getUsername()).isEqualTo("herms");
		assertThat(user.getPassword()).isEqualTo("123456");
	}

	@Test
	public void whenDeleteShouldRemoveData() {
		User user = new User(null, "Emerson", "Ribeiro Junior", "994457676", "Rua Joana da Silva, 139, Uberlândia", "herms", "123456");
		this.userRepository.save(user);
		this.userRepository.delete(user);
		assertThat(userRepository.findById(user.getId())).isEmpty();
	}

	@Test
	public void whenUpdateShouldChangeAndPersistData() {
		User user = new User(null, "emerson", "Ribeiro Junior", "994457676", "Rua Joana da Silva, 139, Uberlândia", "herms", "123456");
		this.userRepository.save(user);
		user.setContact("998877873");
		user.setGivenName("Emerson");
		this.userRepository.save(user);

		user = this.userRepository.findById(user.getId()).orElse(null);
		assertThat(user.getContact()).isEqualTo("998877873");
		assertThat(user.getGivenName()).isEqualTo("Emerson");
	}
	
	@Test
	public void whenfindByUsernameShouldIgnoreCase() {
		User user = new User(null, "Emerson", "Ribeiro Junior", "994457676", "Rua Joana da Silva, 139, Uberlândia", "herms", "123456");
		this.userRepository.save(user);
		User userFromLowerCase = this.userRepository.findByUsername("herms");
		User userFromUpperCase = this.userRepository.findByUsername("Herms");

		assertThat(userFromLowerCase).isEqualTo(userFromUpperCase);
	}
	
	@Test
	public void whenCreateWhenUsernameIsNullShouldThrownConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("size must be between 1 and 50");
		User user = new User(null, null, "Ribeiro Junior", "994457676", "Rua Joana da Silva, 139, Uberlândia", "teste", "123456");
		
		this.userRepository.save(user);		
		
	}

}
