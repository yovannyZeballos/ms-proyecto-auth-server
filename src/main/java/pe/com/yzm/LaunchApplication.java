package pe.com.yzm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class LaunchApplication {

  @Autowired
  private BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(LaunchApplication.class, args);
	}

  /*@Bean
  public CommandLineRunner startup() {
    return args -> {
      System.out.println("pass = " + encoder.encode("pass"));

    };
  }*/

}
