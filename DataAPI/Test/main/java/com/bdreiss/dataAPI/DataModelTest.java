package main.java.com.bdreiss.dataAPI;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DataModelTest {

	@Test
	void test() {
		DataModel data = new DataModel(".");
		data.load();
//		data.print();
		fail("Not yet implemented");
	}

}
