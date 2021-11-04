package ru.job4j.auth.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = AuthApplication.class)
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCanFindById() throws Exception {
        personService.save(new Person("one", "pass1"));
        mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("login", is("one")))
                .andExpect(jsonPath("password", is("pass1")));
    }

    @Test
    public void testCannotFindById() throws Exception {
        personService.save(new Person("one", "pass1"));
        mockMvc.perform(get("/person/2"))
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(0)))
                .andExpect(jsonPath("login", is(nullValue())))
                .andExpect(jsonPath("password", is(nullValue())));
    }

    @Test
    public void testFindAll() throws Exception {
        personService.save(new Person("one", "1"));
        personService.save(new Person("two", "2"));
        mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].login", is("one")))
                .andExpect(jsonPath("$[0].password", is("1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].login", is("two")))
                .andExpect(jsonPath("$[1].password", is("2")));

    }

    @Test
    public void testCreate() throws Exception {
        mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"one\",\"password\":\"1\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("login", is("one")))
                .andExpect(jsonPath("password", is("1")));

    }

    @Test
    public void testUpdate() throws Exception {
        personService.save(new Person("one", "1"));
        mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"login\":\"one11\",\"password\":\"1\"}"))
                .andExpect(status().is2xxSuccessful());

        Person person = personService.findById(1).get();

        assertThat(person.getId(), is(1));
        assertThat(person.getLogin(), is("one11"));
        assertThat(person.getPassword(), is("1"));
    }

    @Test
    public void testDelete() throws Exception {
        personService.save(new Person("one", "1"));
        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());

        assertThat(personService.findById(1).isEmpty(), is(true));
    }
}