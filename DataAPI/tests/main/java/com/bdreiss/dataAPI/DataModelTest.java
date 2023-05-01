package main.java.com.bdreiss.dataAPI;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
		String[] strings = { "Migraine", "Testing", "", "Apple", "Something", "something2" };
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


	
	//TODO test whether Iterators are returned as right instanceof
	
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
		
		//Test whether Iterators are returned as the correct type
		addInterface.add("Test");
		
		assert(!(addInterface.getAllData("Test") instanceof IteratorWithIntensity));
		assert(!(addInterface.getData("Test", LocalDate.now()) instanceof IteratorWithIntensity));
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

		//test whether Iterators are returned as the right type
		addInterface.add("Test", LocalDateTime.now());
		addInterface.addKey("Test1", false);
		
		assert(!(addInterface.getAllData("Test") instanceof IteratorWithIntensity));
		assert(!(addInterface.getData("Test", LocalDate.now()) instanceof IteratorWithIntensity));
		assert(!(addInterface.getAllData("Test1") instanceof IteratorWithIntensity));
		assert(!(addInterface.getData("Test1", LocalDate.now()) instanceof IteratorWithIntensity));

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
		
		//test whether Iterators are returned as the right type
		addInterface.add("Test", null);
		addInterface.add("Test1", Intensity.HIGH);
		addInterface.addKey("Test2", true);
		addInterface.addKey("Test3", false);
		
		assert(!(addInterface.getAllData("Test") instanceof IteratorWithIntensity));
		assert(!(addInterface.getData("Test", LocalDate.now()) instanceof IteratorWithIntensity));
		assert(addInterface.getAllData("Test1") instanceof IteratorWithIntensity);
		assert(addInterface.getData("Test1", LocalDate.now()) instanceof IteratorWithIntensity);
		assert(addInterface.getAllData("Test2") instanceof IteratorWithIntensity);
		assert(addInterface.getData("Test2", LocalDate.now()) instanceof IteratorWithIntensity);
		assert(!(addInterface.getAllData("Test3") instanceof IteratorWithIntensity));
		assert(!(addInterface.getData("Test3", LocalDate.now()) instanceof IteratorWithIntensity));


	}

	// abstracts away the task of adding with custom date by providing an interface
	// with the relevant methods
	private void addWithIntensityAndCustomDate(AddWithIntensityAndCustomDate addInterface)
			throws EntryNotFoundException {
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
		//test whether Iterators are returned as the right type
		addInterface.add("Test", null, LocalDateTime.now());
		addInterface.add("Test1", Intensity.HIGH,LocalDateTime.now());
		addInterface.addKey("Test2", true);
		addInterface.addKey("Test3", false);
		
		assert(!(addInterface.getAllData("Test") instanceof IteratorWithIntensity));
		assert(!(addInterface.getData("Test", LocalDate.now()) instanceof IteratorWithIntensity));
		assert(addInterface.getAllData("Test1") instanceof IteratorWithIntensity);
		assert(addInterface.getData("Test1", LocalDate.now()) instanceof IteratorWithIntensity);
		assert(addInterface.getAllData("Test2") instanceof IteratorWithIntensity);
		assert(addInterface.getData("Test2", LocalDate.now()) instanceof IteratorWithIntensity);
		assert(!(addInterface.getAllData("Test3") instanceof IteratorWithIntensity));
		assert(!(addInterface.getData("Test3", LocalDate.now()) instanceof IteratorWithIntensity));


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

			@Override
			public void addKey(String s, boolean b) {
				data.addAilmentKey(s, b);
				
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

			@Override
			public void addKey(String s, boolean b) {
				data.addAilmentKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addCauseKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addCauseKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addCauseKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addCauseKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addSymptomKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addSymptomKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addRemedyKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addRemedyKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addRemedyKey(s, b);
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
			@Override
			public void addKey(String s, boolean b) {
				data.addRemedyKey(s, b);
			}
		});
	}

	// tests whether keys are added to ailments correctly
	@Test
	void addAilmentKey() {
		String[] testStrings = getTestStrings();

		for (String s : testStrings)
			data.addAilmentKey(s,false);

		Iterator<String> it = data.getAilments();

		Arrays.sort(testStrings, COMPARATOR);
		;

		int i = 0;

		while (it.hasNext()) {
			assert (it.next() == testStrings[i]);
			i++;
		}

		assert (i == testStrings.length);
	}

	// tests whether keys are added to causes correctly
	@Test
	void addCauseKey() {
		String[] testStrings = getTestStrings();

		for (String s : testStrings)
			data.addCauseKey(s,false);

		Iterator<String> it = data.getCauses();

		Arrays.sort(testStrings, COMPARATOR);
		;

		int i = 0;

		while (it.hasNext()) {
			assert (it.next() == testStrings[i]);
			i++;
		}

		assert (i == testStrings.length);
	}

	// tests whether keys are added to symptoms correctly
	@Test
	void addSymptomKey() {
		String[] testStrings = getTestStrings();

		for (String s : testStrings)
			data.addSymptomKey(s,false);

		Iterator<String> it = data.getSymptoms();

		Arrays.sort(testStrings, COMPARATOR);
		;

		int i = 0;

		while (it.hasNext()) {
			assert (it.next() == testStrings[i]);
			i++;
		}

		assert (i == testStrings.length);
	}

	// tests whether keys are added to remedies correctly
	@Test
	void addRemedyKey() {
		String[] testStrings = getTestStrings();

		for (String s : testStrings)
			data.addRemedyKey(s,false);

		Iterator<String> it = data.getRemedies();

		Arrays.sort(testStrings, COMPARATOR);
		;

		int i = 0;

		while (it.hasNext()) {
			assert (it.next() == testStrings[i]);
			i++;
		}

		assert (i == testStrings.length);
	}

	// abstracts testing whether entries can be edited correctly, including dates and
	// intensities
	private void editEntry(Edit edit) throws EntryNotFoundException {

		/*
		 * The following code tests editing dates
		 */

		String[] testStrings = { "String1", "String2" };

		for (String s : testStrings)
			edit.addKey(s);

		int numberOfTestDates = 5;

		ArrayList<LocalDateTime> datesAdded = new ArrayList<>();

		for (int i = 0; i < numberOfTestDates; i++)
			datesAdded.add(LocalDateTime.now());

		for (String s : testStrings)
			for (LocalDateTime ldt : datesAdded)
				edit.add(s, Intensity.LOW, ldt);

		LocalDateTime now = LocalDateTime.now();

		int minuteOffset = 0;

		//change all dates to now + 1 minute per iteration but only for first testString
		for (LocalDateTime ldt: datesAdded) {
			edit.editEntry(testStrings[0], ldt, now.minusMinutes(minuteOffset));
			minuteOffset++;
		}

		Iterator<Datum> it = edit.getData(testStrings[0]);

		// assert all dates for the first test string have changed
		minuteOffset--;
		while (it.hasNext()) {
			Datum datum = it.next();
			assert (datum.getDate().compareTo(now.minusMinutes(minuteOffset))==0);
			minuteOffset--;
		}

		// assert all dates for the second test string stayed the same
		it = edit.getData(testStrings[1]);
		int counter = 0;
		while (it.hasNext()) {
			Datum datum = it.next();
			assert (datum.getDate().equals(datesAdded.get(counter)));
			counter++;
		}

		/*
		 * The following code tests editing intensities
		 */

		// create new DataModel
		prepare();

		String[] testStringsIntensity = { "String1", "String2" };

		int numberOfIntensities = 5;

		now = LocalDateTime.now();

		LocalDateTime[] dates = { now, now.plusMinutes(1), now.plusMinutes(2), now.plusMinutes(3), now.plusMinutes(4),
				now.plusMinutes(5) };

		// loop through intensities and add entries with them
		for (String s : testStringsIntensity) {
			edit.addKey(s);

			for (int i = 0; i < numberOfIntensities; i++)
				edit.add(s, Intensity.values()[i % Intensity.values().length], dates[i]);
		}

		// change intensities for first testString only
		Iterator<Datum> itIntensity = edit.getData(testStringsIntensity[0]);

		while (itIntensity.hasNext()) {
			edit.editEntry(testStringsIntensity[0], itIntensity.next().getDate(), Intensity.NO_INTENSITY);
		}

		// assert all intensities have been changed for first testString
		itIntensity = edit.getData(testStringsIntensity[0]);
		while (itIntensity.hasNext()) {
			assert (((DatumWithIntensity) itIntensity.next()).getIntensity() == Intensity.NO_INTENSITY);
		}

		// assert intensities haven't changed for second testString
		itIntensity = edit.getData(testStringsIntensity[1]);
		counter = 0;

		while (itIntensity.hasNext()) {
			assert (((DatumWithIntensity) itIntensity.next())
					.getIntensity() == Intensity.values()[counter % Intensity.values().length]);
			counter++;
		}

	}

	//tests whether ailment entries can be edited properly
	@Test
	public void editAilmentEntry() throws EntryNotFoundException {
		editEntry(new Edit() {

			@Override
			public void addKey(String key) {
				data.addAilmentKey(key,false);
			}

			@Override
			public void add(String s, Intensity intensity, LocalDateTime ldt) {
				data.addAilment(s, intensity, ldt);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) {
				data.editAilmentEntry(s, ldt, ldtNew);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, Intensity i) {
				data.editAilmentEntry(s, ldt, i);
			}

			@Override
			public Iterator<Datum> getData(String s) {
				try {
					return data.getAilmentData(s);
				} catch (EntryNotFoundException e) {
					
					System.out.println("Entry not found.");
					e.printStackTrace();
					assert(false);
					return null;
				}
			}
			
		});
	}
	

	//tests whether cause entries can be edited properly
	@Test
	public void editCauseEntry() throws EntryNotFoundException {
		editEntry(new Edit() {

			@Override
			public void addKey(String key) {
				data.addCauseKey(key,false);
			}

			@Override
			public void add(String s, Intensity intensity, LocalDateTime ldt) {
				data.addCause(s, intensity, ldt);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) {
				data.editCauseEntry(s, ldt, ldtNew);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, Intensity i) {
				data.editCauseEntry(s, ldt, i);
			}

			@Override
			public Iterator<Datum> getData(String s) {
				try {
					return data.getCauseData(s);
				} catch (EntryNotFoundException e) {
					
					System.out.println("Entry not found.");
					e.printStackTrace();
					assert(false);
					return null;
				}
			}
			
		});
	}
	//tests whether symptom entries can be edited properly
	@Test
	public void editSymptomEntry() throws EntryNotFoundException {
		editEntry(new Edit() {

			@Override
			public void addKey(String key) {
				data.addSymptomKey(key,false);
			}

			@Override
			public void add(String s, Intensity intensity, LocalDateTime ldt) {
				data.addSymptom(s, intensity, ldt);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) {
				data.editSymptomEntry(s, ldt, ldtNew);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, Intensity i) {
				data.editSymptomEntry(s, ldt, i);
			}

			@Override
			public Iterator<Datum> getData(String s) {
				try {
					return data.getSymptomData(s);
				} catch (EntryNotFoundException e) {
					
					System.out.println("Entry not found.");
					e.printStackTrace();
					assert(false);
					return null;
				}
			}
			
		});
	}

	//tests whether remedy entries can be edited properly
	@Test
	public void editRemedyEntry() throws EntryNotFoundException {
		editEntry(new Edit() {

			@Override
			public void addKey(String key) {
				data.addRemedyKey(key,false);
			}

			@Override
			public void add(String s, Intensity intensity, LocalDateTime ldt) {
				data.addRemedy(s, intensity, ldt);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) {
				data.editRemedyEntry(s, ldt, ldtNew);
			}

			@Override
			public void editEntry(String s, LocalDateTime ldt, Intensity i) {
				data.editRemedyEntry(s, ldt, i);
			}

			@Override
			public Iterator<Datum> getData(String s) {
				try {
					return data.getRemedyData(s);
				} catch (EntryNotFoundException e) {
					
					System.out.println("Entry not found.");
					e.printStackTrace();
					assert(false);
					return null;
				}
			}
			
		});
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
				now.minusDays(2).minusHours(2) };

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
				{ now.minusDays(2).minusHours(2), now.minusDays(1).minusHours(1) },
				{ now.minusDays(1).minusHours(1) }, };

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

			// remove last entry and assert that key has been removed from map
			removeInterface.remove(s, now.minusDays(1).minusHours(1));
			try {
				removeInterface.getData(s);
				assert (false);
			} catch (EntryNotFoundException e) {
				assert (true);
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
