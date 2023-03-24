package main.java.com.bdreiss.dataAPI;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataModelTest {

	private final String SAVE_FILE_PATH = "files/";

	DataModel data;

	@BeforeEach
	void prepare() {
		data = new DataModel(SAVE_FILE_PATH);
	}

	// tests whether the constructor creates a new file
	@Test
	void dataModel() {
		// assert file exists
		data.save();
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

	// tests whether ailments are added correctly and can be retrieved
	@Test
	void addAndGetAilment() {
		//ailments to be added
		String[] ailments = { "Migraine" };
		//intensities of ailments above
		Intensity[][] intensities = { { Intensity.medium } };
		
		//loop through ailments
		for (int i = 0; i < ailments.length; i++) {
			
			//Store LocalDateTime for entries to check later
			ArrayList<LocalDateTime> datesAdded = new ArrayList<>();
			
			//add them
			for (int j = 0; j < intensities[i].length; j++)
				datesAdded.add(data.addAilment(ailments[i], intensities[i][j]));

			//assert the size of ailments added is right
			assert (data.getAilmentsSize() == i + 1);

			//get iterator for ailment for today
			Iterator<Datum> it = data.getAilmentData(ailments[i], LocalDate.now());

			//iterate over data for today and assert date and intensity are correct 
			int j = 0;
			while (it.hasNext()) {
				Datum datum = it.next();
				assert (datum.getDate().equals(datesAdded.get(j)));
				assert (datum.getIntensity() == intensities[i][j]);
				j++;
			}
		}

	}

	// tests whether ailments with custom dates are added correctly and can be retrieved
	//TODO add test cases where the dates are not ordered to test whether they are returned in order
	@Test
	void addAilmentWithCustomDate() {
		//get today as LocalDateTime for generating new dates
		LocalDateTime today = LocalDateTime.now();
		//ailments to be added
		String[] ailments = { "Migraine" };
		//intensities of ailments above
		Intensity[][] intensities = { { Intensity.medium } };
		//custom dates to for intensities -> they have to be chronological so the assertions at the end don't get scrambled up
		LocalDateTime[][] dates = { { today.minusDays(1) } };
		//days for which keys are produced in the second Map in DataModel
		ArrayList<LocalDate> daysForWhichKeyIsAddedInMap = new ArrayList<>();
		
		//loop through ailments		
		for (int i = 0; i < ailments.length; i++) {
			//add intensities with custom date for ailment
			for (int j = 0; j < intensities[i].length; j++) {
				data.addAilment(ailments[i], intensities[i][j], dates[i][j]);
				//store new day for keys
				if (!daysForWhichKeyIsAddedInMap.contains(dates[i][j].toLocalDate()))
					daysForWhichKeyIsAddedInMap.add(dates[i][j].toLocalDate());
			}
			
			//assert ailments size is correct
			assert (data.getAilmentsSize() == i + 1);

			//for all days for which data has been added: fetch data, iterate over it and assert date and intensity are correct
			int j = 0;
			for (LocalDate day : daysForWhichKeyIsAddedInMap) {
				//get Iterator
				Iterator<Datum> it = data.getAilmentData(ailments[i], day);

				//iterate over data;
				while (it.hasNext()) {
					Datum datum = it.next();
					assert (datum.getDate().equals(dates[i][j]));
					assert (datum.getIntensity().equals(intensities[i][j]));
					j++;
				}
			}
		}

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
