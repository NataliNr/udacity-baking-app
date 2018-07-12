package com.chef.master.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.chef.master.bakingapp.TestUtil.atPosition;
import static com.chef.master.bakingapp.TestUtil.atPositionOnView;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.chef.master.bakingapp", appContext.getPackageName());
    }
    @Rule
    public final ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }
    @Test
    public void clickFirstElementRecyclerview(){
//        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
    }
    @Test
    public void checkIngredientinSecondList() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.recipe_ingredients))
                .check(matches(atPosition(3, hasDescendant(withText("salt")))));
    }
    @Test
    public void scrollAndCheck() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        onView(withId(R.id.recipe_desciption))
                .perform(RecyclerViewActions.scrollToPosition(12))
                .check(matches(atPosition(12, hasDescendant(withText("Frost cakes.")))));

    }


}

