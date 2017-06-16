package com.csusb.cse455.trip.ui;

import com.csusb.cse455.trip.R;

import static android.support.test.espresso.Espresso.*;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)

@LargeTest
public class PasswordResetActivityTest {

    private void launchActivityWithIntent()
    {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
    }

    @Rule
    public ActivityTestRule<PasswordResetActivity> mActivityRule =
            new IntentsTestRule<>(PasswordResetActivity.class, true, false);


    @Test
    public void PasswordResetScreenToLoginScreenTransistion() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.backBtn)).perform(click());

    }

    // A Toast confirms with message that reset instructions were sent to email,
    // even if email might not be registered.
    @Test
    public void PasswordResetAttempt() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.recoveryEmail)).perform(typeText("name@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.recoveryBtn)).perform(click());

    }

    @Test
    public void PasswordResetAttemptWithEmptyEmail() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.recoveryEmail)).perform(typeText(""));

        closeSoftKeyboard();

        onView(withId(R.id.recoveryBtn)).perform(click());

        onView(withId(R.id.recoveryEmail)).check(matches(hasErrorText("Invalid email address format.")));

    }

    @Test
    public void PasswordResetAttemptWithDoubleAtSymbolEmail() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.recoveryEmail)).perform(typeText("name@@email.com"));

        closeSoftKeyboard();

        onView(withId(R.id.recoveryBtn)).perform(click());

        onView(withId(R.id.recoveryEmail)).check(matches(hasErrorText("Invalid email address format.")));

    }

}