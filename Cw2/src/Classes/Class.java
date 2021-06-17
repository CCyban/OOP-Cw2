package Classes;

import java.util.ArrayList;
import java.util.UUID;

public class Class implements java.io.Serializable{

    private final UUID classUUID;

    private String yearGroup;
    private String Subject;
    private ArrayList<UUID> assignedTeachers;
    private ArrayList<UUID> assignedStudents;

    public Class(String _yearGroup, String _Subject, ArrayList<UUID> _assignedTeachers, ArrayList<UUID> _assignedStudents) {
        // Generate a UUID for the class
        this.classUUID = UUID.randomUUID();

        // Use payload values
        this.yearGroup = _yearGroup;
        this.Subject = _Subject;
        this.assignedTeachers = _assignedTeachers;
        this.assignedStudents = _assignedStudents;
    }

    public String getYearGroup() {
        return yearGroup;
    }

    public void setYearGroup(String _yearGroup) {
        this.yearGroup = _yearGroup;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String _subject) {
        Subject = _subject;
    }

    public ArrayList<UUID> getAssignedTeachers() {
        return assignedTeachers;
    }

    public void setAssignedTeachers(ArrayList<UUID> _assignedTeachers) {
        this.assignedTeachers = _assignedTeachers;
    }

    public ArrayList<UUID> getAssignedStudents() {
        return assignedStudents;
    }

    public void setAssignedStudents(ArrayList<UUID> _assignedStudents) {
        this.assignedStudents = _assignedStudents;
    }
}
