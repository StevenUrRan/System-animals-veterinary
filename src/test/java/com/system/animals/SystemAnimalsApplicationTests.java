package com.system.animals;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class SystemAnimalsApplicationTests {

	@Test
	void contextLoads() {
		assertDoesNotThrow(SystemAnimalsApplication::new);
	}

}
