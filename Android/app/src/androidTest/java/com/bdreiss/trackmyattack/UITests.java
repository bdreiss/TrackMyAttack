package com.bdreiss.trackmyattack;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;

import android.util.Log;

import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.DataModel;

public class UITests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){

    }

    @Test
    public void addCauseKey(){

        String testString = "ßßß123124dadofajdf";
        onView(withId(R.id.button_causes_view)).perform(click());


        //because button was created dynamically
        onView(withTagValue(is((Object) "add_key_button"))).perform(click());
        onView(withId(R.id.item_to_be_added)).perform(typeText(testString));
        onView(withId(R.id.add_item_button)).perform(click());

        DataModel data = new DataModel(InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir().getAbsolutePath());

        Iterator<String> it = data.getCauses();
        boolean contains = false;

        while (it.hasNext()) {
            contains = it.next().equals(testString);
            if (contains)
                break;
        }
        assert(contains);

        //clean up
        data.removeCauseKey(testString);

        it = data.getCauses();

        contains = false;

        while (it.hasNext()) {
            contains = it.next().equals(testString);
            if (contains)
                break;
        }
        assert(!contains);

        data.save();


    }
}
