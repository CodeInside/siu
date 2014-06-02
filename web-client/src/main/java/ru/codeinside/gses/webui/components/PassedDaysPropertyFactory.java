package ru.codeinside.gses.webui.components;

import com.vaadin.data.util.ObjectProperty;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.calendar.DueDateCalculator;

import java.util.Date;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

public class PassedDaysPropertyFactory{

  DueDateCalculator businessCalendarCalculator;
  DueDateCalculator calendarCalculator;
  Date currentDate = new Date();

  public ObjectProperty<String> createProperty(Date startDate, Date finishDate, boolean isWorkDays) {
    DueDateCalculator calculator;
    if (isWorkDays) {
      if (businessCalendarCalculator == null) {
        businessCalendarCalculator = AdminServiceProvider.get().getBusinessCalendarBasedDueDateCalculator();
      }
      calculator = businessCalendarCalculator;
    } else {
      if (calendarCalculator == null) {
        calendarCalculator = AdminServiceProvider.get().getCalendarBasedDueDateCalculator();
      }
      calculator = calendarCalculator;
    }
    int maxBidDays = calculator.countDays(startDate, finishDate);
    int currentBidDays = calculator.countDays(startDate, currentDate);
    return stringProperty(currentBidDays + "/" + maxBidDays);
  }
}
