package org.jessay;

import java.util.regex.Pattern;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author serg
 */
public class RegexMatcher extends TypeSafeMatcher<String>{
    
    private final String regex;
    
    public RegexMatcher(String regex){
        if (regex == null){
            throw new IllegalArgumentException("Non-null value required by RegexMatcher");
        }
        this.regex = regex;
    }

    @Override
    public boolean matchesSafely(String item) {
        if (item == null){
            return false;
        }
        return Pattern.matches(regex, item);       
    }

    public void describeTo(Description description) {
        description.appendText("/").appendText(regex).appendText("/");
    }
    
    public static Matcher<String> matchRegex(String regex){
        return new RegexMatcher(regex);
    }

}
