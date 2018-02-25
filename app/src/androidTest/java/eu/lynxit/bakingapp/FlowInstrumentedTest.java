package eu.lynxit.bakingapp;

import android.content.Context;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.lynxit.bakingapp.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FlowInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickFirstRecyclerViewItem() {
        onView(withId(R.id.fragment_recipe_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.activity_main_toolbar_title)).check(matches(withText("Nutella Pie")));
    }

    @Test
    public void clickFirstStepInFirstRecipe(){
        onView(withId(R.id.fragment_recipe_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_recipe_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_step_description)).check(matches(withText("Recipe Introduction")));
    }
}
