package space.artway.artwayuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ArtwayUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtwayUserApplication.class, args);
    }

}
