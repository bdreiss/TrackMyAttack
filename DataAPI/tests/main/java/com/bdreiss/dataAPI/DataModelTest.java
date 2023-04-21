package main.java.com.bdreiss.dataAPI;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataModelTest {

	private final String SAVE_FILE_PATH = "files/";

	private static final Comparator<String> COMPARATOR = String.CASE_INSENSITIVE_ORDER;
	
	DataModel data;

	// strings to be tested when adding data
	private String[] getTestStrings() {
		String[] strings = { "Migraine", "Test", "", "Apple", "Something", "something2" };
		return strings;
	}

	// dates to be tested when adding data with custom dates
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

	private void add(Add addInterface) throws EntryNotFoundException {

		// Strings to be added
		String[] testStrings = getTestStrings();

		// loop through strings
		for (int i = 0; i < testStrings.length; i++) {

			// Store LocalDateTime for entries to check later
			ArrayList<LocalDateTime> datesAdded = new ArrayList<>();

			// for every Intensity add one test
			for (int j = 0; j < testStrings.length; j++) {
				datesAdded.add(addInterface.add(testStrings[i]));
			}
			// assert the size of strings added is right
			assert (addInterface.getSize() == i + 1);

			// get iterator for today
			Iterator<Datum> it = addInterface.getData(testStrings[i], LocalDate.now());

			// iterate over data for today and assert date and intensity are correct
			int j = 0;
			while (it.hasNext()) {
				Datum datum = it.next();
				assert (datum.getDate().equals(datesAdded.get(j)));
				j++;
			}

			// assert the dataset is complete
			assert (j == datesAdded.size());

			// get data for all dates and iterate over it
			it = addInterface.getAllData(testStrings[i]);
			int k = 0;

			while (it.hasNext()) {
				assert (it.next().getDate().equals(datesAdded.get(k)));
				k++;
			}

			// assert the dataset is complete
			assert (k == datesAdded.size());

		}
	}

	private void addWithCustomDate(AddWithCustomDate addInterface) throws EntryNotFoundException {
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

			// get data for all dates and iterate over it
			Iterator<Datum> it = addInterface.getAllData(testStrings[i]);
			int k = 0;

			while (it.hasNext()) {
				assert (it.next().getDate().equals(expectedResults[i][k]));
				k++;
			}

			// assert the dataset is complete
			assert (k == expectedResults[i].length);
		}
	}

	private void addWithIntensity(AddWithIntensity addInterface) throws EntryNotFoundException {
		// Strings to be added
		String[] testStrings = getTestStrings();

		// loop through strings
		for (int i = 0; i < testStrings.length; i++) {

			// Store LocalDateTime for entries to check later
			ArrayList<LocalDateTime> datesAdded = new ArrayList<>();

			// for every Intensity add one test
			for (int j = 0; j < Intensity.values().length; j++) {
				datesAdded.add(addInterface.add(testStrings[i], Intensity.values()[j]));
			}
			// assert the size of strings added is right
			assert (addInterface.getSize() == i + 1);

			// get iterator for today
			Iterator<Datum> it = addInterface.getData(testStrings[i], LocalDate.now());

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

			// get data for all dates and iterate over it
			it = addInterface.getAllData(testStrings[i]);
			int k = 0;

			while (it.hasNext()) {
				assert (it.next().getDate().equals(datesAdded.get(k)));
				k++;
			}

			// assert the dataset is complete
			assert (k == datesAdded.size());
		}

	}

	// abstracts away the task of adding with custom date by providing an interface
	// with the relevant methods
	private void addWithIntensityAndCustomDate(AddWithIntensityAndCustomDate addInterface) throws EntryNotFoundException {
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

			// get data for all dates and iterate over it
			Iterator<Datum> it = addInterface.getAllData(testStrings[i]);
			int k = 0;

			while (it.hasNext()) {
				assert (it.next().getDate().equals(expectedResults[i][k]));
				k++;
			}

			// assert the dataset is complete
			assert (k == expectedResults[i].length);

		}
	}

	// tests whether ailments are added correctly and can be retrieved
	@Test
	void addAilment() throws EntryNotFoundException {
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
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getAilmentData(s, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getAilmentData(s);
			}

		});
	}

	// tests whether ailments with custom dates are added correctly and can be
	// retrieved
	@Test
	void addAilmentWithCustomDate() throws EntryNotFoundException {
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
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getAilmentData(s, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getAilmentData(s);
			}
		});

	}

	// tests whether causes are added correctly and can be retrieved
	@Test
	void addCause() throws EntryNotFoundException {
		add(new Add() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getCauseData(s, d);
			}

			@Override
			public LocalDateTime add(String s) {
				return data.addCause(s);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getCauseData(s);
			}
		});
	}

	// tests whether causes with custom dates are added correctly and can be
	// retrieved
	@Test
	void addCauseWithCustomDate() throws EntryNotFoundException {
		addWithCustomDate(new AddWithCustomDate() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getCauseData(s, d);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addCause(s, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getCauseData(s);
			}
		});
	}

	// tests whether causes with intensity are added correctly and can be retrieved
	@Test
	void addCauseWithIntensity() throws EntryNotFoundException {
		addWithIntensity(new AddWithIntensity() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getCauseData(s, d);
			}

			@Override
			public LocalDateTime add(String s, Intensity i) {
				return data.addCause(s, i);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getCauseData(s);
			}
		});
	}

	// tests whether causes with custom dates and intensity are added correctly and
	// can be retrieved
	@Test
	void addCauseWithIntensityAndCustomDate() throws EntryNotFoundException {
		addWithIntensityAndCustomDate(new AddWithIntensityAndCustomDate() {

			@Override
			public int getSize() {
				return data.getCausesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getCauseData(s, d);
			}

			@Override
			public void add(String s, Intensity i, LocalDateTime d) {
				data.addCause(s, i, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getCauseData(s);
			}
		});
	}

	// tests whether symptoms are added correctly and can be retrieved
	@Test
	void addSymptom() throws EntryNotFoundException {
		addWithIntensity(new AddWithIntensity() {

			@Override
			public int getSize() {
				return data.getSymptomsSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getSymptomData(s, d);
			}

			@Override
			public LocalDateTime add(String s, Intensity i) {
				return data.addSymptom(s, i);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getSymptomData(s);
			}
		});
	}

	// tests whether symptoms with custom dates are added correctly and can be
	// retrieved
	@Test
	void addSymptomWithCustomDate() throws EntryNotFoundException {
		addWithIntensityAndCustomDate(new AddWithIntensityAndCustomDate() {

			@Override
			public int getSize() {
				return data.getSymptomsSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getSymptomData(s, d);
			}

			@Override
			public void add(String s, Intensity i, LocalDateTime d) {
				data.addSymptom(s, i, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getSymptomData(s);
			}
		});
	}

	// tests whether remedies are added correctly and can be retrieved
	@Test
	void addRemedy() throws EntryNotFoundException {
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
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getRemedyData(s, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getRemedyData(s);
			}

		});
	}

	// tests whether remedies with custom date are added correctly and can be
	// retrieved
	@Test
	void addRemedyWithCustomDate() throws EntryNotFoundException {
		addWithCustomDate(new AddWithCustomDate() {

			@Override
			public int getSize() {
				return data.getRemediesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getRemedyData(s, d);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addRemedy(s, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getRemedyData(s);
			}
		});
	}

	// tests whether remedies with intensity are added correctly and can be
	// retrieved
	@Test
	void addRemedyWithIntensity() throws EntryNotFoundException {
		addWithIntensity(new AddWithIntensity() {

			@Override
			public int getSize() {
				return data.getRemediesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getRemedyData(s, d);
			}

			@Override
			public LocalDateTime add(String s, Intensity i) {
				return data.addRemedy(s, i);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getRemedyData(s);
			}
		});
	}

	// tests whether remedies with intensity and custom date are added correctly and
	// can be retrieved
	@Test
	void addRemedyWithIntensityAndCustomDate() throws EntryNotFoundException {
		addWithIntensityAndCustomDate(new AddWithIntensityAndCustomDate() {

			@Override
			public int getSize() {
				return data.getRemediesSize();
			}

			@Override
			public Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException {
				return data.getRemedyData(s, d);
			}

			@Override
			public void add(String s, Intensity i, LocalDateTime d) {
				data.addRemedy(s, i, d);
			}

			@Override
			public Iterator<Datum> getAllData(String s) throws EntryNotFoundException {
				return data.getRemedyData(s);
			}
		});
	}
	
	//tests whether keys are added to ailments correctly
	@Test
	void addAilmentKey() {
		String[] testStrings = getTestStrings();

		for (String s: testStrings)
			data.addAilmentKey(s);
		
		Iterator<String> it = data.getAilments();
		
		Arrays.sort(testStrings, COMPARATOR);;
		
		int i = 0;
		
		while (it.hasNext()) {
			assert(it.next() == testStrings[i]);
			i++;
		}
		
		assert(i==testStrings.length);
	}
	
	//tests whether keys are added to causes correctly
	@Test
	void addCauseKey() {
		String[] testStrings = getTestStrings();

		for (String s: testStrings)
			data.addCauseKey(s);
		
		Iterator<String> it = data.getCauses();
		
		Arrays.sort(testStrings, COMPARATOR);;
		
		int i = 0;
		
		while (it.hasNext()) {
			assert(it.next() == testStrings[i]);
			i++;
		}
		
		assert(i==testStrings.length);
	}

	//tests whether keys are added to symptoms correctly
	@Test
	void addSymptomKey() {
		String[] testStrings = getTestStrings();

		for (String s: testStrings)
			data.addSymptomKey(s);
		
		Iterator<String> it = data.getSymptoms();
		
		Arrays.sort(testStrings, COMPARATOR);;
		
		int i = 0;
		
		while (it.hasNext()) {
			assert(it.next() == testStrings[i]);
			i++;
		}
		
		assert(i==testStrings.length);
	}

	
	//tests whether keys are added to remedies correctly
	@Test
	void addRemedyKey() {
		String[] testStrings = getTestStrings();

		for (String s: testStrings)
			data.addRemedyKey(s);
		
		Iterator<String> it = data.getRemedies();
		
		Arrays.sort(testStrings, COMPARATOR);;
		
		int i = 0;
		
		while (it.hasNext()) {
			assert(it.next() == testStrings[i]);
			i++;
		}
		
		assert(i==testStrings.length);
	}
	

	
	// tests whether added ailments are returned in correct order
	@Test
	void getAilmentsList() {

		String[] testStrings = getTestStrings();

		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults, COMPARATOR);

		// add test data
		for (String s : testStrings)
			data.addAilment(s, null);

		// keep count of items
		int i = 0;

		// iterate over ailments
		for (Iterator<String> it = data.getAilments(); it.hasNext(); i++) {
			assert (it.next().equals(expectedResults[i]));
		}

		// assert iterations equal test sample size
		assert (i == testStrings.length);

	}

	// tests whether added causes are returned in correct order
	@Test
	void getCausesList() {
		String[] testStrings = getTestStrings();

		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults, COMPARATOR);

		// add test data
		for (String s : testStrings)
			data.addCause(s);

		// keep count of items
		int i = 0;

		// iterate over causes
		for (Iterator<String> it = data.getCauses(); it.hasNext(); i++) {
			assert (it.next().equals(expectedResults[i]));
		}

		// assert iterations equal test sample size
		assert (i == testStrings.length);
	}

	// tests whether added symptoms are returned in correct order
	@Test
	void getSymptomsList() {
		String[] testStrings = getTestStrings();

		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults, COMPARATOR);

		// add test data
		for (String s : testStrings)
			data.addSymptom(s, null);

		// keep count of items
		int i = 0;

		// iterate over symptoms
		for (Iterator<String> it = data.getSymptoms(); it.hasNext(); i++) {
			assert (it.next().equals(expectedResults[i]));
		}

		// assert iterations equal test sample size
		assert (i == testStrings.length);
	}

	// tests whether added remedies are returned in correct order
	@Test
	void getRemediesList() {
		String[] testStrings = getTestStrings();

		String[] expectedResults = testStrings.clone();
		Arrays.sort(expectedResults, COMPARATOR);

		// add test data
		for (String s : testStrings)
			data.addRemedy(s);

		// keep count of items
		int i = 0;

		// iterate over remedies
		for (Iterator<String> it = data.getRemedies(); it.hasNext(); i++) {
			assert (it.next().equals(expectedResults[i]));
		}

		// assert iterations equal test sample size
		assert (i == testStrings.length);

	}

	// test if size of ailments is returned correctly
	@Test
	void getAilmentsListSize() {
		String[] testStrings = getTestStrings();

		// add test data
		for (String s : testStrings)
			data.addAilment(s, null);

		// assert returned value equals size of testStrings
		assert (data.getAilmentsSize() == testStrings.length);

	}

	// test if size of causes is returned correctly
	@Test
	void getCausesListSize() {
		String[] testStrings = getTestStrings();

		// add test data
		for (String s : testStrings)
			data.addCause(s);

		// assert returned value equals size of testStrings
		assert (data.getCausesSize() == testStrings.length);

	}

	// test if size of symptoms is returned correctly
	@Test
	void getSymptomsListSize() {
		String[] testStrings = getTestStrings();

		// add test data
		for (String s : testStrings)
			data.addSymptom(s, null);

		// assert returned value equals size of testStrings
		assert (data.getSymptomsSize() == testStrings.length);

	}

	// test if size of remedies is returned correctly
	@Test
	void getRemediesListSize() {
		String[] testStrings = getTestStrings();

		// add test data
		for (String s : testStrings)
			data.addRemedy(s);

		// assert returned value equals size of testStrings
		assert (data.getRemediesSize() == testStrings.length);

	}

	void removeEntry(String[] tests, Remove removeInterface) throws EntryNotFoundException {

		LocalDateTime now = LocalDateTime.now();

		LocalDateTime[] dates = { now.minusDays(2), now.minusDays(2).minusHours(1), now.minusDays(2).minusHours(2),
				now.minusDays(1), now.minusDays(1).minusHours(1), now.minusDays(1).minusHours(2), now,
				now.minusHours(1), now.minusHours(2) };

		LocalDateTime[] datesToBeRemoved = { null, now.minusHours(3), now, now.minusDays(1), now.minusHours(2),
				now.minusDays(2), now.minusDays(1).minusHours(2), now.minusHours(1), now.minusDays(2).minusHours(1),
				now.minusDays(2).minusHours(2)};

		LocalDateTime[][] expectedResults = {
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(2),
						now.minusDays(1).minusHours(2), now.minusDays(1).minusHours(1), now.minusDays(1),
						now.minusHours(2), now.minusHours(1), now },
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(2),
						now.minusDays(1).minusHours(2), now.minusDays(1).minusHours(1), now.minusDays(1),
						now.minusHours(2), now.minusHours(1), now },
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(2),
						now.minusDays(1).minusHours(2), now.minusDays(1).minusHours(1), now.minusDays(1),
						now.minusHours(2), now.minusHours(1) },
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(2),
						now.minusDays(1).minusHours(2), now.minusDays(1).minusHours(1), now.minusHours(2),
						now.minusHours(1) },
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(2),
						now.minusDays(1).minusHours(2), now.minusDays(1).minusHours(1), now.minusHours(1) },
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(1).minusHours(2),
						now.minusDays(1).minusHours(1), now.minusHours(1) },
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(1).minusHours(1),
						now.minusHours(1) },
				{ now.minusDays(2).minusHours(2), now.minusDays(2).minusHours(1), now.minusDays(1).minusHours(1) },
				{ now.minusDays(2).minusHours(2), now.minusDays(1).minusHours(1) }, { now.minusDays(1).minusHours(1) },
				 };

		for (String s : tests) {
			for (LocalDateTime d : dates) {
				removeInterface.add(s, d);
			}
		}

		for (String s : tests) {
			for (int i = 0; i < datesToBeRemoved.length; i++) {

				removeInterface.remove(s, datesToBeRemoved[i]);
				Iterator<Datum> it = removeInterface.getData(s);

				int j = 0;

				while (it.hasNext()) {
					assert (it.next().getDate().equals(expectedResults[i][j]));
					j++;
				}

				assert (j == expectedResults[i].length);
			}
			
			//remove last entry and assert that key has been removed from map
			removeInterface.remove(s, now.minusDays(1).minusHours(1));
			try {
				removeInterface.getData(s);
				assert(false);
			} catch (EntryNotFoundException e) {
				assert(true);
			}
		}
	}

	@Test
	public void removeAilment() throws EntryNotFoundException {
		String[] testStrings = getTestStrings();
		removeEntry(testStrings, new Remove() {

			@Override
			public void remove(String s, LocalDateTime d) {
				data.removeAilment(s, d);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addAilment(s, Intensity.NO_INTENSITY, d);
			}

			@Override
			public Iterator<Datum> getData(String s) throws EntryNotFoundException {
				return data.getAilmentData(s);
			}

		});
	}

	@Test
	public void removeCause() throws EntryNotFoundException {
		String[] testStrings = getTestStrings();
		removeEntry(testStrings, new Remove() {

			@Override
			public void remove(String s, LocalDateTime d) {
				data.removeCause(s, d);
			}

			@Override
			public Iterator<Datum> getData(String s) throws EntryNotFoundException {
				return data.getCauseData(s);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addCause(s, d);
			}
		});
	}

	@Test
	public void removeSymptom() throws EntryNotFoundException {
		String[] testStrings = getTestStrings();
		removeEntry(testStrings, new Remove() {

			@Override
			public void remove(String s, LocalDateTime d) {
				data.removeSymptom(s, d);
			}

			@Override
			public Iterator<Datum> getData(String s) throws EntryNotFoundException {
				return data.getSymptomData(s);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addSymptom(s, Intensity.NO_INTENSITY, d);
			}
		});
	}

	@Test
	public void removeRemedy() throws EntryNotFoundException {
		String[] testStrings = getTestStrings();
		removeEntry(testStrings, new Remove() {

			@Override
			public void remove(String s, LocalDateTime d) {
				data.removeRemedy(s, d);
			}

			@Override
			public Iterator<Datum> getData(String s) throws EntryNotFoundException {
				return data.getRemedyData(s);
			}

			@Override
			public void add(String s, LocalDateTime d) {
				data.addRemedy(s, d);
			}
		});
	}

}
