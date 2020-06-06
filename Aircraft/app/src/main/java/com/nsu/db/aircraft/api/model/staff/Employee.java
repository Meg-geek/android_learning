package com.nsu.db.aircraft.api.model.staff;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsu.db.aircraft.api.model.company.Site;
import com.nsu.db.aircraft.api.model.tests.Range;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.util.Arrays.asList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    public final static String TESTER = "испытатель";
    public final static String ENGINEER = "инженер";
    public final static String TECHNICIAN = "техник";
    public final static String WELDER = "сварщик";
    public final static String TURNER = "токарь";
    public final static String PICKER = "сборщик";
    public final static String LOCKSMITH = "слесарь";
    public final static String GUILD_MANAGER = "начальник цеха";
    public final static String SITE_MANAGER = "начальник участка";
    public final static String MASTER = "мастер";
    public final static String ALL = "все";

    public static final String WORKER = "рабочий";
    public static final String ENGINEERING_STAFF = "инженерно-технический персонал";

    public static final List<String> employeeCategories = asList(TESTER,
            ENGINEER, TECHNICIAN, WELDER, TURNER, PICKER, LOCKSMITH, GUILD_MANAGER,
            SITE_MANAGER, MASTER, ALL);
    public static final List<String> employeeTables = asList(TESTER,
            ENGINEER, TECHNICIAN, WELDER, TURNER, PICKER, LOCKSMITH, MASTER, ALL);

    public static final List<String> workers = asList(WELDER, TURNER, PICKER, LOCKSMITH);
    public static final List<String> engineeringStaff = asList(ENGINEER, TECHNICIAN);
    public static final List<String> specialities = asList(WORKER,
            ENGINEERING_STAFF, TESTER);
    public static final List<String> categoriesAndMaster = asList(
            ENGINEER, TECHNICIAN, WELDER, TURNER, PICKER, LOCKSMITH,
            MASTER, ALL);

    private String employeeCategory;

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("surname")
    private String surname;

    @Expose
    @SerializedName("brigade")
    private Brigade brigade;

    @Expose
    @SerializedName("site")
    private Site site;

    @Expose
    @SerializedName("range")
    private Range range;

    @NonNull
    @Override
    public String toString() {
        if (employeeCategory != null) {
            return String.join(" ", name, surname, employeeCategory);
        }
        return String.join(" ", name, surname);
    }
}

