package com.projeto.uol.marvelista


import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.projeto.uol.marvelista.controller.activity.MainActivity
import com.projeto.uol.marvelista.controller.adapter.CharacterAdapter
import com.projeto.uol.marvelista.controller.adapter.ComicAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EspressoMainTests{

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun clickComicsPartOfScreen(){
        Espresso.onView(withId(R.id.my_recycler_view)).check(matches(isDisplayed()))

    }

    @Test
    fun clickCharacterPartOfScreen(){
        Espresso.onView(withId(R.id.my_recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun characterSearchBarWithSearchResult(){
        Espresso.onView(withId(R.id.search)).perform(click())
        Espresso.onView(withId(R.id.search_src_text)).perform(typeText("alex"))
        Espresso.onView(withId(R.id.search_src_text)).perform(pressImeActionButton())
        Espresso.onView(withId(R.id.my_recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun characterSearchBarWithNoSearchResult(){
        Espresso.onView(withId(R.id.search)).perform(click())
        Espresso.onView(withId(R.id.search_src_text)).perform(typeText("nao achei >:("))
        Espresso.onView(withId(R.id.search_src_text)).perform(pressImeActionButton())
        Thread.sleep(10000)
        Espresso.onView(withId(R.id.my_recycler_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun comicSearchBarWithNoSearchResult(){
        Espresso.onView(withId(R.id.search)).perform(click())
        Espresso.onView(withId(R.id.search_src_text)).perform(typeText("achei nada!"))
        Espresso.onView(withId(R.id.search_src_text)).perform(pressImeActionButton())
        Thread.sleep(10000)
        Espresso.onView(withId(R.id.my_recycler_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }


    @Test
    fun comicSearchBarWithSearchResult(){
        Espresso.onView(withId(R.id.search)).perform(click())
        Espresso.onView(withId(R.id.search_src_text)).perform(ViewActions.typeText("dead"))
        Espresso.onView(withId(R.id.search_src_text)).perform(pressImeActionButton())
        Espresso.onView(withId(R.id.my_recycler_view)).check(matches(isDisplayed()))
    }



    @Test
    fun characterClickOnResultinRecyclerview(){
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<CharacterAdapter.ItemHolder>(0, click()))
        Espresso.onView(withId(R.id.character_image)).check(matches(isDisplayed()))
    }

    @Test
    fun comicClickOnResultinRecyclerview(){
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<ComicAdapter.ItemHolder>(0, click()))
        Espresso.onView(withId(R.id.comic_image)).check(matches(isDisplayed()))
    }

    @Test
    fun scrollingComicsRecyclerList(){
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.my_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<ComicAdapter.ItemHolder>(20))
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.my_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<ComicAdapter.ItemHolder>(40))
    }

    @Test
    fun scrollingCharactersRecyclerList(){
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.my_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<CharacterAdapter.ItemHolder>(20))
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.my_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<CharacterAdapter.ItemHolder>(40))
    }

}

