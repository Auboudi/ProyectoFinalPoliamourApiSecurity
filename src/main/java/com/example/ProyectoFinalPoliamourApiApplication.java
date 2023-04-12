package com.example;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.entities.Department;
import com.example.entities.Post;
import com.example.entities.User;
import com.example.entities.Yard;
import com.example.services.DepartmentService;
import com.example.services.PostService;
import com.example.services.UserService;
import com.example.services.YardService;

@SpringBootApplication
public class ProyectoFinalPoliamourApiApplication  implements CommandLineRunner{

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private YardService yardService;

	@Autowired
	private PostService postService;


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalPoliamourApiApplication.class, args);


		
	

}

	@Override
	public void run(String... args) throws Exception {
		
		departmentService.save(Department.builder()
			.name("Informatica")
			.build());
		
		departmentService.save(Department.builder()
			.name("RRHH")
			.build());

		Yard yard1 =  yardService.save(Yard.builder()
			.id(1)
			.name("Yard1")
			.department(departmentService.findbyId(1))
			.build());
		
		Yard yard2 = yardService.save(Yard.builder()
			.id(2)
			.name("Yard2")
			.department(departmentService.findbyId(1))
			.build());

		Yard yard3 = yardService.save(Yard.builder()
			.id(3)
			.name("Yard3")
			.department(departmentService.findbyId(1))
			.build());

		List<Yard> listaYard1 = new ArrayList<>();
		listaYard1.add(yard1);
		listaYard1.add(yard2);

		List<Yard> listaYard2 = new ArrayList<>();
		listaYard2.add(yard3);

		
		
		userService.save(User.builder()
			.id(1)
			.name("Marina")
			.surnames("Giner")
			.email("marinaginer@gmail.com")
			.password("password1")
			.city("Murcia")
			.department(departmentService.findbyId(1))
			.yards(listaYard1)
			.hobbie("padel")
			.phone("677888999")
			.build());

	

		userService.save(User.builder()
			.id(2)
			.name("Paloma")
			.surnames("Galan")
			.email("palomagalan@gmail.com")
			.password("password2")
			.city("Valencia")
			.department(departmentService.findbyId(2))
			.hobbie("tenis")
			.phone("654632981")
			.build());

	
		

		userService.save(User.builder()
			.id(3)
			.name("Maria")
			.surnames("Romero")
			.email("mariaromero@gmail.com")
			.password("password3")
			.city("Murcia")
			.department(departmentService.findbyId(1))
			.hobbie2("padel")
			.yards(listaYard2)
			.build());

	
			
	
		userService.save(User.builder()
			.id(4)
			.name("Alex")
			.surnames("Sanchez")
			.email("alexsanchez@gmail.com")
			.password("password3")				
			.city("Murcia")
			.department(departmentService.findbyId(1))
			.hobbie("Tenis")				
			.yards(listaYard2)
			.build());	

			

		
		userService.save(User.builder()
			.id(5)
			.name("Sheila")
			.surnames("Nuñez")
			.email("sheilanuñez@gmail.com")
			.password("password1")
			.city("Valencia")
			.department(departmentService.findbyId(2))
			.phone("677888999")
			.build());

		postService.save(Post.builder()
			.id(1)
			.text("Hola, esto es un post.")
			.user(userService.findbyId(1))
			.build());
		






		
			
	}
}
