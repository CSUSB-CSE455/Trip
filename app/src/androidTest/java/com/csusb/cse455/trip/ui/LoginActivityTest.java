package com.csusb.cse455.trip.ui;

import com.csusb.cse455.trip.R;
import com.csusb.cse455.trip.utils.Format;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.clearText;
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
public class LoginActivityTest {

    private void launchActivityWithIntent()
    {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new IntentsTestRule<>(LoginActivity.class, true, false);


    @Test
    public void LoginAttempt() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logEmail)).perform(typeText("name@email.com"));

        onView(withId(R.id.logPassword)).perform(typeText("Password"));

        // Close the Soft Keyboard that shows up. It blocks Espresso from
        // using the button.

        closeSoftKeyboard();

        onView(withId(R.id.logBtn)).perform(click());


        // ErrorText not showing up impilies that a login attempt was done.
        onView(withText("Invalid email address format.")).check(doesNotExist());
        onView(withId(R.id.regPassword)).check(matches(hasErrorText("Password is too " +
                "short.  Minimum length is " + Format.PASSWORD_LENGTH + ".")));

    }

    @Test
    public void LoginAttemptWithEmptyEmail() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logEmail)).perform(typeText(""));

        onView(withId(R.id.logPassword)).perform(typeText("Password"));

        // Close the Soft Keyboard that shows up. It blocks Espresso from
        // using the button.

        closeSoftKeyboard();

        onView(withId(R.id.logBtn)).perform(click());

        onView(withId(R.id.logEmail)).check(matches(hasErrorText("Invalid email address format.")));

    }

    @Test
    public void LoginAttemptWithShortPassword() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logEmail)).perform(typeText("name@email.com"));

        onView(withId(R.id.logPassword)).perform(typeText("passw"));

        // Close the Soft Keyboard that shows up. It blocks Espresso from
        // using the button.

        closeSoftKeyboard();

        onView(withId(R.id.logBtn)).perform(click());

        onView(withId(R.id.logPassword)).check(matches(hasErrorText("Password is too " +
                "short.  Minimum length is " + Format.PASSWORD_LENGTH + ".")));

    }

    @Test
    public void DoubleAtSymbolLoginFailure() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logEmail)).perform(typeText("name@@email.com"));

        onView(withId(R.id.logPassword)).perform(typeText("Password"));

        closeSoftKeyboard();

        onView(withId(R.id.logBtn)).perform(click());

        onView(withId(R.id.logEmail)).check(matches(hasErrorText("Invalid email address format.")));

    }

    @Test
    public void LoginAttemptWithExtraDomainExtension() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logEmail)).perform(typeText("name@email.domainExtension.com"));

        onView(withId(R.id.logPassword)).perform(typeText("Password"));

        closeSoftKeyboard();

        onView(withId(R.id.logBtn)).perform(click());

        onView(withText("Invalid email address format.")).check(doesNotExist());

    }


    // An Attempt at testing out General Character Symbols on the Keyboard.
    // TODO: Make class representation for more scalable tests (Ex: Inserts after sign-in, etc.)
    @Test
    public void LoginAttemptWithGeneralSymbolsBeforeSignIn() throws Exception {

        launchActivityWithIntent();

        String SpecialChars[] = { "`", "@", "#", "$", "%", "^", "&", "*" , "(", ")", "-", "_",
                "+", "=",  "*", "<", ">", "," , ";", ";", "{", "}", "[", "]", "|", ".", "/", "\\" , "!", "?"};

        String emailNameFirstHalf = "Gary";
        String emailNameSecondHalf = "Stu";
        String addressSign = "@";
        String domainName = "email";
        String dotDelimiter = ".";
        String domainExtension = "com";

        // Iterates through each of the Special Characters, concatenates them
        // BEFORE the fullName
        for (int i = 0; i < SpecialChars.length; i++){

            String fullName = SpecialChars[i] + emailNameFirstHalf + emailNameSecondHalf + addressSign +
                    domainName + dotDelimiter + domainExtension;

            onView(withId(R.id.logEmail)).perform(clearText(), typeText(fullName));

            onView(withId(R.id.logPassword)).perform(clearText(), typeText("Password"));

            // Close the Soft Keyboard that shows up. It blocks Espresso from
            // using the button.

            closeSoftKeyboard();

            onView(withId(R.id.logBtn)).perform(click());

        }

    }

    @Test
    public void LoginScreenTransitionToRegistrationScreen() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logRegister)).perform(click());

    }

    // Tests back-and-forth transitions between the LoginActivity and RegisterActivity.
    @Test
    public void LoginScreenTransitionToRegistrationScreenAndBackToLogin() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logRegister)).perform(click());

        //onView(withId(R.id.regBackBtn)).perform(click());

    }

    @Test
    public void LoginScreenTransitionToPasswordResetScreen() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logResetPass)).perform(click());

    }


    @Test
    public void LoginScreenTransitionToPasswordResetScreenAndBackToLogin() throws Exception {

        launchActivityWithIntent();

        onView(withId(R.id.logResetPass)).perform(click());

        //onView(withId(R.id.regBackBtn)).perform(click());

    }

}
