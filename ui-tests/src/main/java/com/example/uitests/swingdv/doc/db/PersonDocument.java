package com.example.uitests.swingdv.doc.db;

import com.example.uitests.swingdv.doc.text.CSVDocument;
import com.example.uitests.swingdv.services.NavigationService;
import de.ganzer.core.util.Strings;
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.Document;
import de.ganzer.dv.DocumentCreationInfo;
import de.ganzer.dv.View;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PersonDocument extends CSVDocument {
    public static final int NAME_COLUMN = 0;
    public static final int BIRTHDAY_COLUMN = 1;
    public static final int SALARY_COLUMN = 2;

    public PersonDocument(DocumentCreationInfo<? extends Document> info) throws DVLoadException {
        super(info);
    }

    public void newPerson(View<?> originator) {
        int row = super.getRowCount();
        super.setValue("Enter name", row, NAME_COLUMN, originator);
    }

    public String getPersonName(int index) {
        return super.getValue(index, NAME_COLUMN);
    }

    public void setPersonName(int index, String name, View<?> originator) {
        super.setValue(name, index, NAME_COLUMN, originator);
    }

    public LocalDate getPersonBirthday(int index) {
        var value = super.getValue(index, BIRTHDAY_COLUMN);

        try {
            return Strings.isNullOrBlank(value) ? null : LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            NavigationService.getInstance().showError("Invalid date format: " + value, e);
            return null;
        }
    }

    public void setPersonBirthday(int index, LocalDate birthday, View<?> originator) {
        super.setValue(birthday == null ? "" : birthday.toString(), index, BIRTHDAY_COLUMN, originator);
    }

    public BigDecimal getPersonSalary(int index) {
        var value = super.getValue(index, SALARY_COLUMN);

        try {
            return Strings.isNullOrBlank(value) ? null : BigDecimal.valueOf(Double.parseDouble(super.getValue(index, SALARY_COLUMN)));
        } catch (NumberFormatException e) {
            NavigationService.getInstance().showError("Invalid salary format: " + value, e);
            return null;
        }
    }

    public void setPersonSalary(int index, BigDecimal salary, View<?> originator) {
        super.setValue(salary == null ? "" : salary.toString(), index, SALARY_COLUMN, originator);
    }
}
