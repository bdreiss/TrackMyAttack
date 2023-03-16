package main.java.com.bdreiss.dataAPI;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataModelTest {

	private final String SAVE_FILE_PATH = "./";
	
	DataModel data;
	
	@BeforeEach
	void prepare() {
		data = new DataModel(SAVE_FILE_PATH);
	}

	//tests whether the constructor creates a new file
	@Test
	void dataModel() {
		//assert file exists
		assert(new File(SAVE_FILE_PATH + data.getSaveFileName()).exists());		
	}

	//tests whether method deletes file correctly
	@Test
	void deleteSaveFile() {
		data.deleteSaveFile();
		//assert file does not exist
		assert(!new File(SAVE_FILE_PATH + data.getSaveFileName()).exists());
	}

	@AfterEach
	void cleanUp() {
		data.deleteSaveFile();
	}
	
	@Test
	void addMigraine() {
		
	}

	@Test
	void addHabit() {
		
	}
	
	@Test
	void addSymptom() {
		
	}
	
	@Test
	void addRemedy() {
		
	}
	
	@Test
	void returnMigraines() {
		
	}
	
	@Test
	void returnHabits() {
		
	}
	
	@Test
	void returnSymptoms() {
		
	}
	
	@Test
	void returnRemedies() {
		
	}

	@Test
	void save() {
		
	}
	
	@Test
	void load() {
		
	}

}
