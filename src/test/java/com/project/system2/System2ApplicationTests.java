package com.project.system2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class System2ApplicationTests {

	@Test
	void contextLoads() {
		int sum=0;
		for (int i = 1; i <= 100; i++) {
			sum+=i;
		}
		System.out.println(sum);
	}

}
