package main.java.com.bdreiss.dataAPI;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

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

	// tests whether the constructor creates a new file
	@Test
	void dataModel() {
		// assert file exists
		assert (new File(SAVE_FILE_PATH + data.getSaveFileName()).exists());
	}

	// tests whether method deletes file correctly
	@Test
	void deleteSaveFile() {
		data.deleteSaveFile();
		// assert file does not exist
		assert (!new File(SAVE_FILE_PATH + data.getSaveFileName()).exists());
	}

	@AfterEach
	void cleanUp() {
		data.deleteSaveFile();
	}

	@Test
	void addAilment() {

	}

	@Test
	void addAilmentWithCustomDate() {

	}

	@Test
	void addCause() {

	}

	@Test
	void addCauseWithCustomDate() {

	}

	@Test
	void addCauseWithIntensity() {
		
	}
	
	@Test
	void addCauseWithIntensityAndCustomDate() {
		
	}
		
	@Test
	void addSymptom() {
		
	}

	@Test
	void addSymptomWithCustomDate() {
		
	}
	@Test
	void addRemedy() {

	}

	@Test
	void addRemedyWithCustomDate() {
		
	}
	
	@Test
	void addRemedyWithIntensity() {
		
	}
	
	@Test
	void addRemedyWithIntensityAndCustomDate() {
		
	}
	
	@Test
	void getAilmentsList() {

	}

	@Test
	void getCausesList() {

	}

	@Test
	void getSymptomsList() {

	}

	@Test
	void getRemediesList() {

	}

	@Test
	void getAilmentData() {
		
	}
	
	@Test
	void getCausesData() {
		
	}
	
	@Test
	void getSymptomData() {
		
	}
	
	@Test
	void getRemedyData() {
		
	}
	
	@Test
	void getAilmentsListSize() {
		
	}
	
	@Test
	void getCausesListSize() {
		
	}
	@Test
	void getSymptomsListSize() {
		
	}
	@Test
	void getRemediesListSize() {
		
	}
	
	@Test
	void save() {

	}

	@Test
	void load() {

	}

}
