package com.nsu.db.aircraft.api.model.staff;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsu.db.aircraft.api.model.company.Site;

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

    public static final List<String> employeeCategories = asList(TESTER,
            ENGINEER, TECHNICIAN, WELDER, TURNER, PICKER, LOCKSMITH, GUILD_MANAGER,
            SITE_MANAGER, MASTER, ALL);

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

}

