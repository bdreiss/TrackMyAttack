package net.berndreiss.trackmyattack.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import net.berndreiss.trackmyattack.GeoData.DataByCountry;
import net.berndreiss.trackmyattack.GeoData.GeoDataType;
import net.berndreiss.trackmyattack.GeoData.GeoDatum;
import net.berndreiss.trackmyattack.data.core.AbstractData;
import net.berndreiss.trackmyattack.data.core.DataModel;
import net.berndreiss.trackmyattack.data.enums.Category;
import net.berndreiss.trackmyattack.data.enums.Intensity;
import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;
import net.berndreiss.trackmyattack.data.exceptions.TypeMismatchException;
import net.berndreiss.trackmyattack.data.util.Coordinate;
import net.berndreiss.trackmyattack.data.util.Datum;
import net.berndreiss.trackmyattack.data.util.DatumWithIntensity;

/**
 * AbstractCategoryDataModel getting data about the weather (currently only supported for Austria).
 */

public class DataWrapper extends AbstractData implements Serializable {

    private static final long serialVersionUID = 1L;
    private static String SAVEPATH;

    private final GeoDataType[] TYPES_TO_USE = {GeoDataType.TEMPERATURE_MAX, GeoDataType.TEMPERATURE_MEDIAN,
            GeoDataType.TEMPERATURE_MIN, GeoDataType.VAPOR};

    private final DataModel originalData;

    /**
     * Testing this class
     *
     * @param args No arguments passed
     * @throws TypeMismatchException no description
     */
    public static void main(String[] args) throws TypeMismatchException {
        DataModel data = new DataModel();

        class TestCase {

            public final LocalDateTime date;
            public final Coordinate coordinates;

            public TestCase(LocalDateTime date, Double x, Double y) {
                this.date = date;
                this.coordinates = new Coordinate(x, y);
            }
        }

        TestCase[] testCases = {new TestCase(LocalDateTime.now(), 48.187028431591635, 16.313893863502347),//Wien Schönbrunn
                new TestCase(LocalDateTime.now().minusDays(1), 48.187028431591635, 16.313893863502347),//Wien Schönbrunn
                new TestCase(LocalDateTime.now().minusDays(2), 47.07678064842626, 15.42400131089504),//Graz
                new TestCase(LocalDateTime.now().minusDays(3), 47.07678064842626, 15.42400131089504),//Graz
                new TestCase(LocalDateTime.now().minusDays(4), 47.26349950302004, 11.386525823183192),//Innsbruck
                new TestCase(LocalDateTime.now().minusDays(5), 47.26349950302004, 11.386525823183192),//Innsbruck
                new TestCase(LocalDateTime.now().minusDays(7), 47.26349950302004, 11.386525823183192),};//Innsbruck

        for (TestCase testCase : testCases) {
            data.addCause("test", testCase.date, testCase.coordinates);
        }

//		GeoData geoData = new GeoData(data);
    }

    /**
     * Instantiates a new instance of DataWrapper.
     *
     * @param originalData An instance of DataModel inheriting data to get geological data for
     */
    public DataWrapper(DataModel originalData) {

        data = new DataModel();

        this.originalData = originalData;

        if (SAVEPATH == null) {
            SAVEPATH = originalData.getSaveFile().getAbsolutePath() + "Geo";
            File saveFile = new File(SAVEPATH);

            if (saveFile.exists())
                load(saveFile);

        }
        category = Category.CAUSE;

        update();

    }

    /**
     *
     */
    public String print() {
        return data.print();
    }

    private void load(File saveFile) {

        try {
            FileInputStream fis = new FileInputStream(saveFile);

            ObjectInputStream ois = new ObjectInputStream(fis);

            data = (DataModel) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        data.removeAilmentKey("Migraine");
        // add Migraine data to the loading data model -> the original data for
        // migraines has to be transferred
        // since otherwise migraines can not be shown in the GeoDataPanel
        data.addAilmentKey("Migraine");

        try {
            Iterator<Datum> it = originalData.getAilmentData("Migraine");

            while (it.hasNext()) {
                DatumWithIntensity migraine = (DatumWithIntensity) it.next();
                data.addAilment("Migraine", migraine.getIntensity(), migraine.getDate(), null);
            }

        } catch (EntryNotFoundException | TypeMismatchException e) {
            e.printStackTrace();
        }
    }

    private void update() {

        // if there is no firstDate in the original data, there are no entries and the
        // method returns
        if (originalData.firstDate == null)
            return;

        LocalDate startDate = originalData.firstDate;

        // if there are no entries for GeoData, start with the first date in the
        // original data
        if (data.firstDate == null) {
            updateRange(startDate, LocalDate.now());
        } else {

            // if the first date in GeoData is bigger than the start date in the original
            // data, get GeoData for this span
            if (data.firstDate.isAfter(startDate)) {
                updateRange(startDate, data.firstDate.minusDays(1));
                startDate = data.firstDate;
            }

            Iterator<Datum> it;

            try {
                it = data.getCauseData(GeoDataType.HUMIDITY.toString());
                while (it.hasNext())
                    startDate = it.next().getDate().toLocalDate();
            } catch (EntryNotFoundException e) {
                data.addCauseKey(GeoDataType.HUMIDITY.toString(), false);
                try {
                    it = data.getCauseData(GeoDataType.HUMIDITY.toString());
                    while (it.hasNext())
                        startDate = it.next().getDate().toLocalDate();
                } catch (EntryNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

            updateRange(startDate, LocalDate.now());

        }

        if (SAVEPATH != null)
            save();
    }

    private void save() {
        try {
            FileOutputStream fos = new FileOutputStream(SAVEPATH);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(data);

            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateRange(LocalDate startDate, LocalDate finalDate) {

        DataByCountry.getData(startDate, finalDate, this, category);

    }

    // TODO implement add methods so the whole category thing isn't necessary ->
    // also adapt queries to take GeoData instead of GeoDataModel?
    @Override
    public void addKey(String key, boolean intensity) {
    }

    @Override
    public DataModel getData() {
        return originalData;
    }

    @Override
    public Iterator<Datum> getData(String key) throws EntryNotFoundException {
        try {
            data.getCauseData(key);
        } catch (EntryNotFoundException e) {
            data.addCauseKey(key, false);
        }
        return data.getCauseData(key);
    }

    @Override
    public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
        return data.getCauseData(key, date);
    }

    @Override
    public void addData(String key, Coordinate coordinates){
    }

    @Override
    public void addData(String key, Intensity intensity, Coordinate coordinates) {
    }

    @Override
    public void removeEntry(String key, LocalDateTime date) {
    }

    @Override
    public void removeKey(String key) {
    }

    @Override
    public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) {
    }

    @Override
    public void editIntensity(String key, LocalDateTime date, Intensity intensity) {
    }

    @Override
    public int getSize() {
        return TYPES_TO_USE.length;
    }

    @Override
    public float count(String key, LocalDate date) throws EntryNotFoundException {
        Iterator<Datum> it = data.getCauseData(key, date);

        return it.hasNext() ? ((GeoDatum) it.next()).getValue() : 0;
    }

    @Override
    public float getMedium(String key) throws EntryNotFoundException {
        int days = 0;

        float total = 0;
        Iterator<Datum> it = data.getCauseData(key);

        while (it.hasNext()) {
            days++;
            total += ((GeoDatum) it.next()).getValue();
        }

        return days == 0 ? 0 : total / days;

    }

    @Override
    public Iterator<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();
        for (GeoDataType g : TYPES_TO_USE)
            keys.add(g.toString());

        return keys.iterator();
    }


}
