package com.bdreiss.trackmyattack;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;

import static org.hamcrest.CoreMatchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.DataModel;

public class UITests {

    String FILES_PATH;
    String testString = "ßd1a2d3o4f5a6j7d8f9";

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){
        FILES_PATH = InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir().getAbsolutePath();

    }


    //tests whether new keys are added correctly via the UI
    private void addKey(AddKey addInterface){

        //open view
        onView(withId(addInterface.getViewId())).perform(click());

        //access the add key button via its tag because button was created dynamically
        onView(withTagValue(is("add_key_button"))).perform(click());

        //in the dialog enter the testString and add key
        onView(withId(R.id.key_to_be_added)).perform(typeText(testString));
        onView(withId(R.id.add_key_button)).perform(click());

        DataModel data = new DataModel(FILES_PATH);

        //get causes
        Iterator<String> it = addInterface.getData(data);
        boolean contains = false;

        //check whether they contain the testString
        while (it.hasNext()) {
            contains = it.next().equals(testString);
            if (contains)
                break;
        }
        assert(contains);

        //clean up and check whether key has been removed
        addInterface.removeKey(data, testString);

        it = addInterface.getData(data);

        contains = false;

        while (it.hasNext()) {
            contains = it.next().equals(testString);
            if (contains)
                break;
        }
        assert(!contains);


    }

    @Test
    public void addCauseKey(){
        addKey(new AddKey() {
            @Override
            public int getViewId() {
                return R.id.button_causes_view;
            }

            @Override
            public int getScrollViewId() {
                return R.id.causes_scroll_view;
            }

            @Override
            public Iterator<String> getData(DataModel data) {
                return data.getCauses();
            }

            @Override
            public void removeKey(DataModel data, String key) {
                data.removeCauseKey(key);
            }
        });
    }

    @Test
    public void addSymptomKey(){
        addKey(new AddKey() {
            @Override
            public int getViewId() {
                return R.id.button_symptoms_view;
            }

            @Override
            public int getScrollViewId() {
                return R.id.symptoms_scroll_view;
            }

            @Override
            public Iterator<String> getData(DataModel data) {
                return data.getSymptoms();
            }

            @Override
            public void removeKey(DataModel data, String key) {
                data.removeSymptomKey(key);
            }
        });


    }

    @Test
    public void addRemedyKey(){
        addKey(new AddKey() {
            @Override
            public int getViewId() {
                return R.id.button_remedies_view;
            }

            @Override
            public int getScrollViewId() {
                return R.id.remedies_scroll_view;
            }

            @Override
            public Iterator<String> getData(DataModel data) {
                return data.getRemedies();
            }

            @Override
            public void removeKey(DataModel data, String key) {
                data.removeRemedyKey(key);
            }
        });
    }


}
