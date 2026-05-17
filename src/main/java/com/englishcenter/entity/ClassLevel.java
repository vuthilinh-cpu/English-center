package com.englishcenter.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ClassLevels", schema="dbo")

public class ClassLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "sub_class")
    private String subClass;

    @Column(name = "year")
    private Integer year;

    @Column(name = "status")
    private String status;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "max_students")
    private Integer maxStudents;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }

    public String getSubClass() { return subClass; }
    public void setSubClass(String subClass) { this.subClass = subClass; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getMonthlyFee() { return monthlyFee; }
    public void setMonthlyFee(BigDecimal monthlyFee) { this.monthlyFee = monthlyFee; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }
}