package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GradeLevel {
    NA (0),
    Grade3 (1 << 0),
    Grade4 (1 << 1),
    Grade5  (1 << 2),
    Grade6 (1 << 3),
    Grade7 (1 << 4),
    Grade8 (1 << 5),
    Grade9 (1 << 6),
    Grade10 (1 << 7),
    Grade11 (1 << 8),
    Grade12(1 << 9),
    Elementary (GradeLevel.Grade3.getGradeLevel() | GradeLevel.Grade4.getGradeLevel()  | GradeLevel.Grade5.getGradeLevel() ),
    Middle (GradeLevel.Grade6.getGradeLevel() | GradeLevel.Grade7.getGradeLevel()  | GradeLevel.Grade8.getGradeLevel() ),
    High (GradeLevel.Grade9.getGradeLevel() | GradeLevel.Grade10.getGradeLevel()  | GradeLevel.Grade11.getGradeLevel()| GradeLevel.Grade12.getGradeLevel() ),
    All (GradeLevel.Elementary.getGradeLevel() | GradeLevel.Middle.getGradeLevel() | GradeLevel.High.getGradeLevel());

    private final int _gradeLevel;

    GradeLevel(int gradeLevel){
        _gradeLevel = gradeLevel;
    }

    @JsonProperty("gradeLevel")
    public int getGradeLevel() {
        return _gradeLevel;
    }


}
