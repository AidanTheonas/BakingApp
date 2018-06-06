package com.example.aidan.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class BakingAppTest {
    private static final String SAMPLE_CAKE = "Nutella Pie";
    @Rule
    public IntentsTestRule<MainActivity> mainActivityActivityTestRule = new IntentsTestRule<>(MainActivity.class);
    private IdlingResource idlingResource;

    @Before
    public void registerVolleyIdlingResource() {
        idlingResource = MainActivity.volleyIdlingResource;
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void checkIfTheListIsDisplayedCorrectly() {
            onView(withId(R.id.rv_bakes_list)).check(matches(isDisplayed()));
            onView(withId(R.id.rv_bakes_list)).perform(RecyclerViewActions.scrollToPosition(0));
            onView(withText(SAMPLE_CAKE)).check(matches(isDisplayed()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void checkIfIngredientsAndStepsAreDisplayed() {
        onView(withId(R.id.rv_bakes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(hasComponent(BakesDetailsActivity.class.getName())));
        onView(withId(R.id.rv_ingredients_list)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_steps_list)).check(matches(isDisplayed()));
        onView(withText(R.string.steps)).check(matches(isDisplayed()));
        onView(withText(R.string.ingredients)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_to_widget)).check(matches(isDisplayed()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void checkIfVideoActivityIsDisplayed() {
        onView(withId(R.id.rv_bakes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_steps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.vw_step_video)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}