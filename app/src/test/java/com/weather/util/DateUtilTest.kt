package com.weather.util

import com.weather.testutil.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
internal class DateUtilTest  {

    private lateinit var dateUtil: DateUtil

    @Before
    fun setup() {
        dateUtil = DateUtilImpl()
    }

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun `check conversion of epoch to local date from date util`() = coroutineTestRule.testDispatcher.runBlockingTest{
        val date = dateUtil.convertEpochToLocalDate(1613229084) //13/02/21
        Assert.assertEquals("13/02/21", date)

        val dateEmpty = dateUtil.convertEpochToLocalDate(-4) //13/02/21
        Assert.assertEquals("", dateEmpty)
    }

    @Test
    fun `check conversion of epoch to local time from date util`() = coroutineTestRule.testDispatcher.runBlockingTest{
        val date = dateUtil.convertEpochToLocalTime(1613229084) //13/02/21
        Assert.assertEquals("13/02/21 at 16:11:24", date)

        val dateEmpty = dateUtil.convertEpochToLocalTime(-4) //13/02/21
        Assert.assertEquals("", dateEmpty)
    }

    @Test
    fun `check get time of day from date util`() = coroutineTestRule.testDispatcher.runBlockingTest{
        val timeOfDay = dateUtil.getTimeOfDay()
        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR_OF_DAY]
        if(hour in 1..11) {
            Assert.assertEquals(TimeOfDay.MORNING, timeOfDay)
        } else if(hour in 12..15) {
            Assert.assertEquals(TimeOfDay.AFTERNOON, timeOfDay)
        } else if(hour in 16..20) {
            Assert.assertEquals(TimeOfDay.EVENING, timeOfDay)
        } else if(hour in 21..23) {
            Assert.assertEquals(TimeOfDay.NIGHT, timeOfDay)
        } else {
            Assert.assertEquals(TimeOfDay.MORNING, timeOfDay)
        }
    }

}