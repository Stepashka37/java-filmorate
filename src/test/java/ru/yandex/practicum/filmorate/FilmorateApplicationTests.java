package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.ServletException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc

	class FilmorateApplicationTests {
		@Autowired
		private MockMvc mockMvc;
		@Autowired
		private ObjectMapper objectMapper;
		@Autowired
		private FilmController filmController;
		@Autowired
		private UserController userController;

		private Film.FilmBuilder filmBuilder = new Film.FilmBuilder();
	private User.UserBuilder userBuilder = new User.UserBuilder();



		@Test
		public void test() throws Exception {
			assertNotNull(filmController);
			assertNotNull(userController);

		}

	@SneakyThrows
	@Test
	public void filmGetMethodTest() {


		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();

		Film film = filmBuilder.setName("name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016, 03, 04))
				.setDuration(120)
				.build();

		Film filmCreated = filmBuilder.setName("name")
				.setId(1)
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016, 03, 04))
				.setDuration(120)
				.build();




		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(gson1)
		).andExpect(status().isOk());

		MvcResult result = mockMvc.perform(get("/films")
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();


		String json = result.getResponse().getContentAsString();

		List<Film> films = mapper.readValue(json, new TypeReference<List<Film>>(){});

		assertEquals(filmCreated,  films.get(0));
	}

	@SneakyThrows
	@Test
	public void filmPostMethodTestValidValue(){
		String validFilm = "{\"id\":3,\"name\":\"name\",\"description\":\"Film description\",\"releaseDate\":\"2016-03-04\",\"duration\":120}";

		Film film = filmBuilder.setName("name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		MvcResult result = mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(gson1)
				).andDo(h -> {
					System.out.println(h.getResponse());
				})
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();

		assertEquals(validFilm, result.getResponse().getContentAsString());
	}








			@SneakyThrows
	@Test
	public void filmPostMethodTestInvalidName(){
		Film film = filmBuilder.setName("")
						.setDescription("Film description")
						.setReleaseDate(LocalDate.of(2016,03,04))
						.setDuration(120)
						.build();
				String gson1 = objectMapper.writeValueAsString(film);
				mockMvc.perform(post("/films")
								.contentType("application/json")
								.content(gson1)
						).andExpect(status().isBadRequest())
						.andExpect(h -> h.getResponse().equals("Ошибка валидации name"));

				Film filmNull = filmBuilder.setName(null)
						.setDescription("Film description")
						.setReleaseDate(LocalDate.of(2016,03,04))
						.setDuration(120)
						.build();
				gson1 = objectMapper.writeValueAsString(filmNull);
				mockMvc.perform(post("/films")
								.contentType("application/json")
								.content(gson1)
						).andExpect(status().isBadRequest())
						.andExpect(h -> h.getResponse().equals("Ошибка валидации name"));
	}

	@SneakyThrows
	@Test
	public void filmPostMethodTestInvalidDate(){
		Film filmBefore = filmBuilder.setName("Film")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(1895,03,04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(filmBefore);
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации releaseDate"));

		Film filmAfter = filmBuilder.setName("Film")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.now().plusDays(1))
				.setDuration(120)
				.build();
		gson1 = objectMapper.writeValueAsString(filmAfter);
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации releaseDate"));

	}

	@SneakyThrows
	@Test
	public void filmPostMethodTestInvalidDescription(){
		Film film = filmBuilder.setName("Film")
				.setDescription("The Social Network is a 2010 American biographical drama film directed by David Fincher" +
						" and written by Aaron Sorkin, based on the 2009 book The Accidental Billionaires by Ben Mezrich." +
						" It portrays the founding of social networking website Facebook. It stars Jesse Eisenberg as the" +
						" Facebook founder Mark Zuckerberg, with Andrew Garfield as Eduardo Saverin, Justin Timberlake as" +
						" Sean Parker, Armie Hammer as Cameron and Tyler Winklevoss, and Max Minghella as Divya Narendra." +
						" Neither Zuckerberg nor any other Facebook staff were involved with the project, although Saverin" +
						" was a consultant for Mezrich's book.")
				.setReleaseDate(LocalDate.of(2010,03,04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации description"));

	}

	@SneakyThrows
	@Test
	public void filmPostMethodTestInvalidDuration(){
		Film film = filmBuilder.setName("Film")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2010,03,04))
				.setDuration(-120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации duration"));

	}

	@SneakyThrows
	@Test
	public void filmPutMethodTestInvalidName(){
		Film film = filmBuilder.setName("Name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isOk());

		Film filmNull = filmBuilder.setId(1)
				.setName(null)
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(120)
				.build();
		gson1 = objectMapper.writeValueAsString(filmNull);
		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации name"));
	}

	@SneakyThrows
	@Test
	public void filmPutMethodTestInvalidDate(){
		Film film = filmBuilder.setName("Name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(gson1)
		).andExpect(status().isOk());

		Film filmBefore = filmBuilder.setId(1)
				.setName("name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(1895,03,04))
				.setDuration(120)
				.build();
		gson1 = objectMapper.writeValueAsString(filmBefore);
		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации release date"));

		Film filmAfter = filmBuilder.setId(1)
				.setName("name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2123,03,04))
				.setDuration(120)
				.build();
		gson1 = objectMapper.writeValueAsString(filmAfter);
		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации release date"));
	}

	@SneakyThrows
	@Test
	public void filmPutMethodTestInvalidDescription(){
		Film film = filmBuilder.setName("Name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(gson1)
		).andExpect(status().isOk());

		Film filmNull = filmBuilder.setId(1)
				.setName("name")
				.setDescription("\"The Social Network is a 2010 American biographical drama film directed by David Fincher" +
						"and written by Aaron Sorkin, based on the 2009 book The Accidental Billionaires by Ben Mezrich." +
						" It portrays the founding of social networking website Facebook. It stars Jesse Eisenberg as the" +
						" Facebook founder Mark Zuckerberg, with Andrew Garfield as Eduardo Saverin, Justin Timberlake as" +
						"Sean Parker, Armie Hammer as Cameron and Tyler Winklevoss, and Max Minghella as Divya Narendra." +
						"Neither Zuckerberg nor any other Facebook staff were involved with the project, although Saverin" +
						" was a consultant for Mezrich's book.\"")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(120)
				.build();
		gson1 = objectMapper.writeValueAsString(filmNull);
		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации description"));
	}

	@SneakyThrows
	@Test
	public void filmPutMethodTestInvalidDuration(){
		Film film = filmBuilder.setName("Name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(post("/films")
				.contentType("application/json")
				.content(gson1)
		).andExpect(status().isOk());

		Film filmNull = filmBuilder.setId(1)
				.setName("name")
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016,03,04))
				.setDuration(-120)
				.build();
		gson1 = objectMapper.writeValueAsString(filmNull);
		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации duration"));
	}

	@SneakyThrows
	@Test
	public void filmPutMethodTestInvalidPresence() {
		Film film = filmBuilder.setName("Name")
				.setId(100)
				.setDescription("Film description")
				.setReleaseDate(LocalDate.of(2016, 03, 04))
				.setDuration(120)
				.build();
		String gson1 = objectMapper.writeValueAsString(film);
		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка проверки на наличие"));
	}

	@SneakyThrows
	@Test
	public void userGetMethodTest() {

// or, 2.x before 2.9
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();

		User user = userBuilder.setName("name")
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();

		User userCreated = userBuilder.setName("name")
				.setId(1)
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();




		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(gson1)
		).andExpect(status().isOk());

		MvcResult result = mockMvc.perform(get("/users")
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();


		String json = result.getResponse().getContentAsString();

		List<User> users = mapper.readValue(json, new TypeReference<List<User>>(){});

		assertEquals(userCreated,  users.get(0));
	}

	@SneakyThrows
	@Test
	public void userPostMethodTestValidValue(){
		String validUser = "{\"id\":1,\"email\":\"email@yandex.ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"1997-06-05\"}";

		User user = userBuilder.setName("name")
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		MvcResult result = mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andDo(h -> {
					System.out.println(h.getResponse());
				})
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();

		assertEquals(validUser, result.getResponse().getContentAsString());
	}

	@SneakyThrows
	@Test
	public void userPostMethodTestInvalidEmail(){
		User user = userBuilder.setName("name")
				.setEmail("")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

		 user = userBuilder.setName("name")
				.setEmail("это-неправильный?эмейл@")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

		User userNull = userBuilder.setName("name")
				.setEmail("email.yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(userNull);
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации email"));
	}

	@SneakyThrows
	@Test
	public void userPostMethodTestInvalidLogin(){
		User user = userBuilder.setName("name")
				.setEmail("email@yandex.ru")
				.setLogin("")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации login"));

		User userNull = userBuilder.setName("name")
				.setEmail("email.yandex.ru")
				.setLogin("login with space")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(userNull);
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации login"));
	}

	@SneakyThrows
	@Test
	public void userPostMethodTestInvalidBirthday(){
		User user = userBuilder.setName("name")
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.now().plusDays(1))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации birthday"));


	}

	@SneakyThrows
	@Test
	public void userPostMethodTestNameAsLogin() {

			User user1 = userBuilder.setName("")
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();

		String gson1 = objectMapper.writeValueAsString(user1);
		MvcResult result1 = mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isOk())
				.andReturn();

		User user2 = userBuilder.setName(null)
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();

		String gson2 = objectMapper.writeValueAsString(user2);
		MvcResult result2 = mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(gson2)
		).andExpect(status().isOk())
				.andReturn();

		User userCreated = userBuilder.setName("login")
				.setId(2)
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();

		User fromGson1 = objectMapper.readValue(result1.getResponse().getContentAsString(), User.class);
		User fromGson2 = objectMapper.readValue(result2.getResponse().getContentAsString(), User.class);
		assertEquals(userCreated, fromGson1);
		userCreated.setId(3);
		assertEquals(userCreated, fromGson2);


	}

	@SneakyThrows
	@Test
	public void userPutMethodTestInvalidEmail(){
		User user = userBuilder.setName("name")
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isOk());

		User userToPut = userBuilder.setName("name")
				.setId(1)
				.setEmail("email.yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(userToPut);
		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

		userToPut = userBuilder.setName("name")
				.setId(1)
				.setEmail("")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(userToPut);
		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

		userToPut = userBuilder.setName("name")
				.setId(1)
				.setEmail("это-неправильный?эмейл@")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(userToPut);
		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации email"));
	}

	@SneakyThrows
	@Test
	public void userPutMethodTestInvalidLogin(){
		User user = userBuilder.setName("name")
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(gson1)
		).andExpect(status().isOk());

		User userInvalidLogin = userBuilder.setName("name")
				.setId(1)
				.setEmail("email.yandex.ru")
				.setLogin("")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(userInvalidLogin);
		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации login"));

		userInvalidLogin = userBuilder.setName("name")
				.setId(1)
				.setEmail("email.yandex.ru")
				.setLogin("login with space")
				.setName("name")
				.setBirthday(LocalDate.of(1997,06,05))
				.build();
		gson1 = objectMapper.writeValueAsString(userInvalidLogin);
		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации login"));
	}

	@SneakyThrows
	@Test
	public void userPutMethodTestInvalidBirthday() {
		User user = userBuilder.setName("name")
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997, 06, 05))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(post("/users")
				.contentType("application/json")
				.content(gson1)
		).andExpect(status().isOk());

		User userInvalidBirthday = userBuilder.setName("name")
				.setId(1)
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.now().plusDays(1))
				.build();
		gson1 = objectMapper.writeValueAsString(userInvalidBirthday);
		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка валидации birthday"));
	}

	@SneakyThrows
	@Test
	public void userPutMethodTestInvalidPresence() {
		User user = userBuilder.setName("name")
				.setId(10)
				.setEmail("email@yandex.ru")
				.setLogin("login")
				.setName("name")
				.setBirthday(LocalDate.of(1997, 06, 05))
				.build();
		String gson1 = objectMapper.writeValueAsString(user);
		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(gson1)
				).andExpect(status().isBadRequest())
				.andExpect(h -> h.getResponse().equals("Ошибка проверки на наличие"));
	}











}

