package com.example;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.dao.DepartmentDao;
import com.example.dao.PostDao;
import com.example.dao.UserDao;
import com.example.dao.YardDao;
import com.example.entities.Department;
import com.example.entities.Post;
import com.example.entities.User;
import com.example.entities.Yard;

@SpringBootApplication
public class ProyectoFinalPoliamourApiApplication implements CommandLineRunner {

	// @Autowired
	// private UserService userService;

	// @Autowired
	// private DepartmentService departmentService;

	// @Autowired
	// private YardService yardService;

	// @Autowired
	// private PostService postService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private YardDao yardDao;

	@Autowired
	private PostDao postDao;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalPoliamourApiApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		departmentDao.save(Department.builder()
				.name("Informatica")
				.id(1)
				.build());

		departmentDao.save(Department.builder()
				.name("RRHH")
				.id(2)
				.build());

		departmentDao.save(Department.builder()
				.name("Finanzas")
				.id(3)
				.build());

		departmentDao.save(Department.builder()
				.name("Support Manager")
				.id(4)
				.build());


		Yard yard1 = yardDao.save(Yard.builder()
				.id(1)
				.name("Java")
				.department(departmentDao.findById(1L).get())
				.build());

		Yard yard2 = yardDao.save(Yard.builder()
				.id(2)
				.name(".Net")
				.department(departmentDao.findById(1L).get())
				.build());

		// Yard yard3 = yardDao.save(Yard.builder()
		// 		.id(3)
		// 		.name("Node")
		// 		.department(departmentDao.findById(1L).get())
		// 		.build());
		
		// Yard yard4 = yardDao.save(Yard.builder()
		// 		.id(4)
		// 		.name("PHP")
		// 		.department(departmentDao.findById(1L).get())
		// 		.build());

		// Yard yard5 = yardDao.save(Yard.builder()
		// 		.id(5)
		// 		.name("Yard3")
		// 		.department(departmentDao.findById(1L).get())
		// 		.build());

		List<Yard> listaYard1 = new ArrayList<>();
		listaYard1.add(yard1);
		listaYard1.add(yard2);

		// List<Yard> listaYard2 = new ArrayList<>();
		// listaYard2.add(yard3);

		List<String> hobbiesUser1 = new ArrayList<>();
		hobbiesUser1.add("baloncesto");
		hobbiesUser1.add("lectura");

		userDao.save(User.builder()
				.id(1)
				.name("Marina")
				.surnames("Giner")
				.email("marinag@poliamor.com")
				.password("password1")
				.city("Murcia")
				.department(departmentDao.findById(1L).get())
				.yards(listaYard1)
				.hobbie(hobbiesUser1)
				.phone("677888999")
				.build());

		// List<String> hobbiesUser2 = new ArrayList<>();
		// hobbiesUser2.add("futbol");
		// hobbiesUser2.add("lectura");

		// userDao.save(User.builder()
		// 		.id(2)
		// 		.name("Paloma")
		// 		.surnames("Galan")
		// 		.email("palomagalan@gmail.com")
		// 		.password("password2")
		// 		.city("Valencia")
		// 		.department(departmentDao.findById(2L).get())
		// 		.hobbie(hobbiesUser2)
		// 		.phone("654632981")
		// 		.build());

		List<String> hobbiesUser3 = new ArrayList<>();
		hobbiesUser3.add("senderismo");
		hobbiesUser3.add("equitacion");

		// userDao.save(User.builder()
		// 		.id(3)
		// 		.name("Maria")
		// 		.surnames("Romero")
		// 		.email("mariaromero@gmail.com")
		// 		.password("password3")
		// 		.city("Murcia")
		// 		.department(departmentDao.findById(1L).get())
		// 		.hobbie(hobbiesUser3)
		// 		.yards(listaYard2)
		// 		.build());

		List<String> hobbiesUser4 = new ArrayList<>();
		hobbiesUser4.add("equitacion");

		// userDao.save(User.builder()
		// 		.id(4)
		// 		.name("Alex")
		// 		.surnames("Sanchez")
		// 		.email("alexsanchez@gmail.com")
		// 		.password("password3")
		// 		.city("Murcia")
		// 		.department(departmentDao.findById(1L).get())
		// 		.hobbie(hobbiesUser4)
		// 		.yards(listaYard2)
		// 		.build());

		// userDao.save(User.builder()
		// 		.id(5)
		// 		.name("Sheila")
		// 		.surnames("Nuñez")
		// 		.email("sheilanuñez@gmail.com")
		// 		.password("password1")
		// 		.city("Valencia")
		// 		.department(departmentDao.findById(1L).get())
		// 		.phone("677888999")
		// 		.build());

		postDao.save(Post.builder()
				.id(1)
				.text("Hola, esto es un post.")
				.user(userDao.findById(1L))
				.build());

	}
}
