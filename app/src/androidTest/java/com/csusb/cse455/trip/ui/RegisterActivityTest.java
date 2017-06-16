package com.csusb.cse455.trip.ui;

import com.csusb.cse455.trip.R;
import com.csusb.cse455.trip.utils.Format;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.widget.Toast;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)

@LargeTest
public class RegisterActivityTest {

    private void launchActivityWithIntent()
    {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
    }

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule =
            new IntentsTestRule<>(RegisterActivity.class, true, false);

    // This test FAILS whether it succeeds or fails registration.
    // Likely due to the creation of TWO toasts for each case.
    // TODO: Find a way to check for toast text with Espresso or other Unit Test Framework.
    @Test
    public void RegistrationAttempt() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.regFirstName)).perform(typeText("Mary"));

        closeSoftKeyboard();

        onView(withId(R.id.regLastName)).perform(typeText("Sue"));

        closeSoftKeyboard();

        onView(withId(R.id.regEmail)).perform(typeText("name2@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.regPassword)).perform(typeText("Password123"));

        closeSoftKeyboard();

        onView(withId(R.id.regRePassword)).perform(typeText("Password123"));

        closeSoftKeyboard();

        onView(withId(R.id.regBtn)).perform(click());

    }

    @Test
    public void RegistrationAttemptWithEmptyEmail() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.regFirstName)).perform(typeText("Gary"));

        closeSoftKeyboard();

        onView(withId(R.id.regLastName)).perform(typeText("Stu"));

        closeSoftKeyboard();

        onView(withId(R.id.regEmail)).perform(typeText(""));

        onView(withId(R.id.regPassword)).perform(typeText("Password123"));

        closeSoftKeyboard();

        onView(withId(R.id.regRePassword)).perform(typeText("Password123"));

        closeSoftKeyboard();

        onView(withId(R.id.regBtn)).perform(click());

        onView(withId(R.id.regEmail)).check(matches(hasErrorText("Invalid email address format.")));

    }

    @Test
    public void RegistrationAttemptWithDoubleAtSymbolEmail() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.regFirstName)).perform(typeText("Gary"));

        closeSoftKeyboard();

        onView(withId(R.id.regLastName)).perform(typeText("Stu"));

        closeSoftKeyboard();

        onView(withId(R.id.regEmail)).perform(typeText("name@@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.regPassword)).perform(typeText("Password123"));

        closeSoftKeyboard();

        onView(withId(R.id.regRePassword)).perform(typeText("Password123"));

        closeSoftKeyboard();

        onView(withId(R.id.regBtn)).perform(click());

        onView(withId(R.id.regEmail)).check(matches(hasErrorText("Invalid email address format.")));

    }

    @Test
    public void RegistrationAttemptWithShortPassword() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.regFirstName)).perform(typeText("Gary"));

        closeSoftKeyboard();

        onView(withId(R.id.regLastName)).perform(typeText("Stu"));

        closeSoftKeyboard();

        onView(withId(R.id.regEmail)).perform(typeText("name@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.regPassword)).perform(typeText("Pass"));

        closeSoftKeyboard();

        onView(withId(R.id.regRePassword)).perform(typeText("Pass"));

        closeSoftKeyboard();

        onView(withId(R.id.regBtn)).perform(click());

        onView(withId(R.id.regPassword)).check(matches(hasErrorText("Password is too " +
                "short.  Minimum length is " + Format.PASSWORD_LENGTH + ".")));

    }

    @Test
    public void RegistrationAttemptWithMismatchedPasswords() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.regFirstName)).perform(typeText("Gary"));

        closeSoftKeyboard();

        onView(withId(R.id.regLastName)).perform(typeText("Stu"));

        closeSoftKeyboard();

        onView(withId(R.id.regEmail)).perform(typeText("name@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.regPassword)).perform(typeText("Password1"));

        closeSoftKeyboard();

        onView(withId(R.id.regRePassword)).perform(typeText("Password2"));

        closeSoftKeyboard();

        onView(withId(R.id.regBtn)).perform(click());

        onView(withId(R.id.regRePassword)).check(matches(hasErrorText("Passwords do not match.")));

    }

    @Test
    public void RegistrationAttemptMissingFirstNameField() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.regFirstName)).perform(typeText(""));

        closeSoftKeyboard();

        onView(withId(R.id.regLastName)).perform(typeText("Stu"));

        closeSoftKeyboard();

        onView(withId(R.id.regEmail)).perform(typeText("name@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.regPassword)).perform(typeText("Password"));

        closeSoftKeyboard();

        onView(withId(R.id.regRePassword)).perform(typeText("Password"));

        closeSoftKeyboard();

        onView(withId(R.id.regBtn)).perform(click());

        onView(withId(R.id.regFirstName)).check(matches(hasErrorText("This field is required.")));

    }

    @Test
    public void RegistrationAttemptMissingLastNameField() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.regFirstName)).perform(typeText("Gary"));

        closeSoftKeyboard();

        onView(withId(R.id.regLastName)).perform(typeText(""));

        closeSoftKeyboard();

        onView(withId(R.id.regEmail)).perform(typeText("name@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.regPassword)).perform(typeText("Password"));

        closeSoftKeyboard();

        onView(withId(R.id.regRePassword)).perform(typeText("Password"));

        closeSoftKeyboard();

        onView(withId(R.id.regBtn)).perform(click());

        onView(withId(R.id.regLastName)).check(matches(hasErrorText("This field is required.")));

    }

}