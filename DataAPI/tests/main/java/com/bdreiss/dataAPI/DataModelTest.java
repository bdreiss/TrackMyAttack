package main.java.com.bdreiss.dataAPI;


import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataModelTest {

	private final String SAVE_FILE_PATH = "files/";

	DataModel data;

	//strings to be tested when adding data
	private String[] getTestStrings() {
		String[] strings = { "Migraine", "Test", "", "Apple", "Something","something2" };
		return strings;
	}

	//dates to be tested when adding data with custom dates
	private LocalDateTime[] getTestDates() {
		// get today as LocalDateTime for generating new dates

		LocalDateTime[] dates = { LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2),
				LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1),
				LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(0), LocalDateTime.now().minusDays(0),
				LocalDateTime.now().minusDays(0) };
		return dates;
	}

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

	private void add(Add addInterface) {

		// Strings to be added
		String[] strings = getTestStrings();

		// loop through strings
		for (int i = 0; i < strings.length; i++) {

			// Store LocalDateTime for entries to check later
			ArrayList<LocalDateTime> datesAdded = new ArrayList<>();

			// for every Intensity add one test
			for (int j = 0; j < strings.length; j++) {
				datesAdded.add(addInterface.add(strings[i]));
			}
			// assert the size of strings added is right
			assert (addInterface.getSize() == i + 1);

			// get iterator for today
			Iterator<Datum> it = addInterface.getData(strings[i], LocalDate.now());

			// iterate over data for today and assert date and intensity are correct
			int j = 0;
			while (it.hasNext()) {
				Datum datum = it.next();
				assert (datum.getDate().equals(datesAdded.get(j)));
				j++;
			}

			// assert the dataset is complete
			assert (j == datesAdded.size());
		}
	}

	private void addWithCustomDate(AddWithCustomDate addInterface) {
		// strings to be added
		String[] testStrings = getTestStrings();
		//
		LocalDateTime[] testCases = getTestDates();

		// custom dates to for intensities
		LocalDateTime[][] testDates = new LocalDateTime[testStrings.length][testCases.length];

		// set test dates
		for (int i = 0; i < testStrings.length; i++)
			testDates[i] = testCases;

		// expected results after adding dates to data model (should be ordered
		// chronologically)
		LocalDateTime[][] expectedResults = new LocalDateTime[testStrings.length][testCases.length];

		// add expected dates in chronological order
		Arrays.sort(testCases);
		for (int i = 0; i < testStrings.length; i++) {
			expectedResults[i] = testCases;
		}
		// loop through strings
		for (int i = 0; i < testStrings.length; i++) {
			// add string with custom date and loop through intensities
			for (int j = 0; j < testDates[i].length; j++)
				addInterface.add(testStrings[i], testDates[i][j]);

			// assert size is correct
			assert (addInterface.getSize() == i + 1);

			// current date for asserts
			LocalDate currentDate = null;

			// for all days for which data has been added: fetch data, iterate over it and
			// assert date and intensity are correct
			int j = 0;

			while (j < expectedResults[i].length) {

				// set currentDate to new day
				currentDate = expectedResults[i][j].toLocalDate();

				// get iterator for day
				Iterator<Datum> it = addInterface.getData(testStrings[i], currentDate);

				// iterate over data;
				while (it.hasNext()) {
					Datum datum = it.next();
					assert (datum.getDate().equals(expectedResults[i][j]));
					j++;
				}

			}

			// assert processed data length equals test cases
			assert (j == testDates[i].length);
		}
	}

	private void addWithIntensity(AddWithIntensity addInterface) {
		// Strings to be added
		String[] strings = getTestStrings();

		// loop through strings
		for (int i = 0; i < strings.length; i++) {

			// Store LocalDateTime for entries to check later
			ArrayList<LocalDateTime> datesAdded = new ArrayList<>();

			// for every Intensity add one test
			for (int j = 0; j < Intensity.values().length; j++) {
				datesAdded.add(addInterface.add(strings[i], Intensity.values()[j]));
			}
			// assert the size of strings added is right
			assert (addInterface.getSize() == i + 1);

			// get iterator for today
			Iterator<Datum> it = addInterface.getData(strings[i], LocalDate.now());

			// iterate over data for today and assert date and intensity are correct
			int j = 0;
			while (it.hasNext()) {
				DatumWithIntensity datum = (DatumWithIntensity) it.next();
				assert (datum.getDate().equals(datesAdded.get(j)));
				assert (datum.getIntensity() == Intensity.values()[j]);
				j++;
			}

			// assert the dataset is complete
			assert (j == datesAdded.size());
		}

	}

	// abstracts away the task of adding with custom date by providing an interface
	// with the relevant methods
	private void addWithIntensityAndCustomDate(AddWithIntensityAndCustomDate addInterface) {
		// strings to be added
		String[] testStrings = getTestStrings();
		//
		LocalDateTime[] testCases = getTestDates();

		// custom dates to for intensities
		LocalDateTime[][] testDates = new LocalDateTime[testStrings.length][testCases.length];

		// set test dates
		for (int i = 0; i < testStrings.length; i++)
			testDates[i] = testCases;

		// expected results after adding dates to data model (should be ordered
		// chronologically)
		LocalDateTime[][] expectedResults = new LocalDateTime[testStrings.length][testCases.length];

		// add expected dates in chronological order
		Arrays.sort(testCases);
		for (int i = 0; i < testStrings.length; i++) {
			expectedResults[i] = testCases;
		}
		// loop through strings
		for (int i = 0; i < testStrings.length; i++) {
			// add string with custom date and loop through intensities
			for (int j = 0; j < testDates[i].length; j++)
				addInterface.add(testStrings[i], Intensity.values()[j % Intensity.values().length], testDates[i][j]);

			// assert size is correct
			assert (addInterface.getSize() == i + 1);

			// current date for asserts
			LocalDate currentDate = null;

			// for all days for which data has been added: fetch data, iterate over it and
			// assert date and intensity are correct
			int j = 0;

			while (j < expectedResults[i].length) {

				// set currentDate to new day
				currentDate = expectedResults[i][j].toLocalDate();

				// get iterator for day
				Iterator<Datum> it = addInterface.getData(testStrings[i], currentDate);

				// iterate over data;
				while (it.hasNext()) {
					DatumWithIntensity datum = (DatumWithIntensity) it.next();
					assert (datum.getDate().equals(expectedResults[i][j]));
					assert (datum.getIntensity().equals(Intensity.values()[j % Intensity.values().length]));
					j++;
				}

			}

			// assert processed data length equals test cases
			assert (j == testDates[i].length);
		}
	}

	// tests whether ailments are added correctly and can be retrieved
	@Test
	void addAilment() {
		addWithIntensity(new AddWithIntensity() {

			@Override
			public LocalDateTime add(String s, Intensity i) {
				return data.addAilment(s, i);
			}

			@Override
			public int getSize() {
				return data.getAilmentsSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getAilmentData(s, d);
			}

		});
	}

	// tests whether ailments with custom dates are added correctly and can be
	// retrieved
	@Test
	void addAilmentWithCustomDate() {
		addWithIntensityAndCustomDate(new AddWithIntensityAndCustomDate() {

			@Override
			public void add(String s, Intensity i, LocalDateTime d) {
				data.addAilment(s, i, d);
			}

			@Override
			public int getSize() {
				return data.getAilmentsSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getAilmentData(s, d);
			}
		});

	}

	// tests whether causes are added correctly and can be retrieved
	@Test
	void addCause() {
		add(new Add() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getCauseData(s, d);
			}

			@Override
			public LocalDateTime add(String s) {
				return data.addCause(s);
			}
		});
	}

	// tests whether causes with custom dates are added correctly and can be
	// retrieved
	@Test
	void addCauseWithCustomDate() {
		addWithCustomDate(new AddWithCustomDate() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getCauseData(s, d);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addCause(s, d);
			}
		});
	}

	// tests whether causes with intensity are added correctly and can be retrieved
	@Test
	void addCauseWithIntensity() {
		addWithIntensity(new AddWithIntensity() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getCauseData(s, d);
			}

			@Override
			public LocalDateTime add(String s, Intensity i) {
				return data.addCause(s, i);
			}
		});
	}

	// tests whether causes with custom dates and intensity are added correctly and
	// can be retrieved
	@Test
	void addCauseWithIntensityAndCustomDate() {
		addWithIntensityAndCustomDate(new AddWithIntensityAndCustomDate() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getCauseData(s, d);
			}

			@Override
			public void add(String s, Intensity i, LocalDateTime d) {
				data.addCause(s, i, d);
			}
		});
	}

	// tests whether symptoms are added correctly and can be retrieved
	@Test
	void addSymptom() {
		addWithIntensity(new AddWithIntensity() {

			@Override
			public int getSize() {
				return data.getSymptomsSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getSymptomData(s, d);
			}

			@Override
			public LocalDateTime add(String s, Intensity i) {
				return data.addSymptom(s, i);
			}
		});
	}

	// tests whether symptoms with custom dates are added correctly and can be
	// retrieved
	@Test
	void addSymptomWithCustomDate() {
		addWithIntensityAndCustomDate(new AddWithIntensityAndCustomDate() {

			@Override
			public int getSize() {
				return data.getSymptomsSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getSymptomData(s, d);
			}

			@Override
			public void add(String s, Intensity i, LocalDateTime d) {
				data.addSymptom(s, i, d);
			}
		});
	}

	// tests whether remedies are added correctly and can be retrieved
	@Test
	void addRemedy() {
		add(new Add() {

			@Override
			public LocalDateTime add(String s) {
				return data.addRemedy(s);
			}

			@Override
			public int getSize() {
				return data.getRemediesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getRemedyData(s, d);
			}

		});
	}

	// tests whether remedies with custom date are added correctly and can be
	// retrieved
	@Test
	void addRemedyWithCustomDate() {
		addWithCustomDate(new AddWithCustomDate() {

			@Override
			public int getSize() {
				return data.getRemediesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getRemedyData(s, d);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addRemedy(s, d);
			}
		});
	}

	// tests whether remedies with intensity are added correctly and can be
	// retrieved
	@Test
	void addRemedyWithIntensity() {
		addWithIntensity(new AddWithIntensity() {

			@Override
			public int getSize() {
				return data.getRemediesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getRemedyData(s, d);
			}

			@Override
			public LocalDateTime add(String s, Intensity i) {
				return data.addRemedy(s, i);
			}
		});
	}

	// tests whether remedies with intensity and custom date are added correctly and
	// can be retrieved
	@Test
	void addRemedyWithIntensityAndCustomDate() {
		addWithIntensityAndCustomDate(new AddWithIntensityAndCustomDate() {

			@Override
			public int getSize() {
				return data.getRemediesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) {
				return data.getRemedyData(s, d);
			}

			@Override
			public void add(String s, Intensity i, LocalDateTime d) {
				data.addRemedy(s, i, d);
			}
		});
	}

	//tests whether added ailments are returned in correct order
	@Test
	void getAilmentsList() {

		String[] testStrings = getTestStrings();
		
		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults,String.CASE_INSENSITIVE_ORDER);
		
		//add test data
		for (String s: testStrings)
			data.addAilment(s, null);
		
		//keep count of items
		int i = 0;
		
		//iterate over ailments
		for (Iterator<String> it = data.getAilments(); it.hasNext();i++) {
			assert(it.next().equals(expectedResults[i]));
		}
		
		//assert iterations equal test sample size
		assert(i==testStrings.length);
		
	}

	// tests whether added causes are returned in correct order
	@Test
	void getCausesList() {
		String[] testStrings = getTestStrings();
		
		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults,String.CASE_INSENSITIVE_ORDER);
		
		//add test data
		for (String s: testStrings)
			data.addCause(s);
		
		//keep count of items
		int i = 0;
		
		//iterate over causes
		for (Iterator<String> it = data.getCauses(); it.hasNext();i++) {
			assert(it.next().equals(expectedResults[i]));
		}
		
		//assert iterations equal test sample size
		assert(i==testStrings.length);
	}

	//tests whether added symptoms are returned in correct order
	@Test
	void getSymptomsList() {
		String[] testStrings = getTestStrings();
		
		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults,String.CASE_INSENSITIVE_ORDER);
		
		//add test data
		for (String s: testStrings)
			data.addSymptom(s, null);
		
		//keep count of items
		int i = 0;
		
		//iterate over symptoms
		for (Iterator<String> it = data.getSymptoms(); it.hasNext();i++) {
			assert(it.next().equals(expectedResults[i]));
		}
		
		//assert iterations equal test sample size
		assert(i==testStrings.length);
	}

	//tests whether added remedies are returned in correct order
	@Test
	void getRemediesList() {
		String[] testStrings = getTestStrings();
		
		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults,String.CASE_INSENSITIVE_ORDER);
		
		//add test data
		for (String s: testStrings)
			data.addRemedy(s);
		
		//keep count of items
		int i = 0;
		
		//iterate over remedies
		for (Iterator<String> it = data.getRemedies(); it.hasNext();i++) {
			assert(it.next().equals(expectedResults[i]));
		}
		
		//assert iterations equal test sample size
		assert(i==testStrings.length);

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
