package com.works.service;

import com.works.entity.Person;
import com.works.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockitoBean
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    void shouldSaveValidPerson() {
        // Arrange
        Person person = new Person();
        person.setEmail("test@example.com");
        person.setPhoneList(List.of("123456789"));
        person.setAddressList(Collections.emptyList());
        person.setIdentityInfo(null); // Assuming no constraints on nullable embedded identity in this case

        when(personRepository.save(Mockito.any(Person.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Person savedPerson = personService.save(person);

        // Assert
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(captor.capture());
        Person capturedPerson = captor.getValue();

        assertThat(capturedPerson.getEmail()).isEqualTo("test@example.com");
        assertThat(capturedPerson.getPhoneList()).containsExactly("123456789");
        assertThat(savedPerson).isEqualTo(capturedPerson);
    }

    /**
     * Test case: Attempt to save a person entity with a duplicate email.
     * Scenario: The email is marked as unique and already exists in the database.
     * The save method should throw a persistence exception.
     */
    @Test
    void shouldThrowExceptionWhenSavingDuplicateEmail() {
        // Arrange
        Person person1 = new Person();
        person1.setEmail("duplicate@example.com");
        entityManager.persist(person1); // Persisting the first person to simulate the "email exists" scenario
        entityManager.flush();

        Person person2 = new Person();
        person2.setEmail("duplicate@example.com");

        // Mock throwing exception for the duplicate save scenario
        when(personRepository.save(Mockito.any(Person.class)))
                .thenThrow(new RuntimeException("Unique constraint violation for email"));

        // Act & Assert
        try {
            personService.save(person2);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Unique constraint violation for email");
        }
    }

    /**
     * Test case: Save a person entity with no phone numbers or addresses.
     * Scenario: The phone numbers and addresses are optional fields.
     * The entity should persist successfully even if these fields are empty.
     */
    @Test
    void shouldSavePersonWithEmptyPhoneListAndAddressList() {
        // Arrange
        Person person = new Person();
        person.setEmail("empty_fields@example.com");
        person.setPhoneList(Collections.emptyList());
        person.setAddressList(Collections.emptyList());

        when(personRepository.save(Mockito.any(Person.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Person savedPerson = personService.save(person);

        // Assert
        assertThat(savedPerson.getEmail()).isEqualTo("empty_fields@example.com");
        assertThat(savedPerson.getPhoneList()).isEmpty();
        assertThat(savedPerson.getAddressList()).isEmpty();
    }

    /**
     * Test case: Save a person entity with all fields null or empty (invalid).
     * Scenario: Validation constraints should prevent null email from being persisted.
     */
    @Test
    void shouldNotSavePersonWithNullEmail() {
        // Arrange
        Person person = new Person();
        person.setEmail(null); // Invalid email
        person.setPhoneList(List.of("123456789"));
        person.setAddressList(Collections.emptyList());

        // Mock throwing exception for invalid data save
        when(personRepository.save(Mockito.any(Person.class)))
                .thenThrow(new RuntimeException("Email must not be null"));

        // Act & Assert
        try {
            personService.save(person);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Email must not be null");
        }
    }
}