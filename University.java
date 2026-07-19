import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class University {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("\n===== Course Management =====");
            System.out.println("1. Add Course");
            System.out.println("2. Add Student");
            System.out.println("3. Enroll Student");
            System.out.println("4. Assign Grade");
            System.out.println("5. Calculate Overall Grade");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            try {

                int option = input.nextInt();
                input.nextLine();

                switch (option) {

                    case 1:
                        System.out.print("Course code: ");
                        String code = input.nextLine();

                        if (CourseManagement.findCourse(code) == null) {
                            System.out.print("Course name: ");
                            String courseName = input.nextLine();

                            System.out.print("Maximum capacity: ");
                            int capacity = input.nextInt();
                            input.nextLine();

                            CourseManagement.addCourse(code, courseName, capacity);
                            System.out.println("Course added successfully.");
                        } else {
                            System.out.println("A course with this code already exists.");
                        }
                        break;

                    case 2:
                        System.out.print("Student name: ");
                        String studentName = input.nextLine();

                        System.out.print("Student ID: ");
                        int id = input.nextInt();
                        input.nextLine();

                        if (CourseManagement.findStudent(id) == null) {
                            CourseManagement.addStudent(studentName, id);
                            System.out.println("Student added successfully.");
                        } else {
                            System.out.println("A student with this ID already exists.");
                        }
                        break;

                    case 3:
                        System.out.print("Student ID: ");
                        id = input.nextInt();
                        input.nextLine();

                        System.out.print("Course code: ");
                        code = input.nextLine();

                        CourseManagement.enrollStudent(
                                CourseManagement.findStudent(id),
                                CourseManagement.findCourse(code));
                        break;

                    case 4:
                        System.out.print("Student ID: ");
                        id = input.nextInt();
                        input.nextLine();

                        System.out.print("Course code: ");
                        code = input.nextLine();

                        System.out.print("Grade: ");
                        double grade = input.nextDouble();
                        input.nextLine();

                        if (grade >= 0 && grade <= 100) {
                            CourseManagement.assignGrade(
                                    CourseManagement.findStudent(id),
                                    CourseManagement.findCourse(code),
                                    grade);
                        } else {
                            System.out.println("Grade must be between 0 and 100.");
                        }
                        break;

                    case 5:
                        System.out.print("Student ID: ");
                        id = input.nextInt();
                        input.nextLine();

                        Student student = CourseManagement.findStudent(id);

                        if (student != null) {
                            System.out.printf("Overall Grade: %.2f%n",
                                    CourseManagement.calculateOverallGrade(student));
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;

                    case 6:
                        running = false;
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                input.nextLine();
            }
        }

        input.close();
    }
}

class Student {
    private String name;
    private int id;
    private ArrayList<Course> enrolledCourses = new ArrayList<>();
    private HashMap<Course, Double> grades = new HashMap<>();

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public int getId() { return id; }
    public void setName(String name) { this.name = name; }
    public void setId(int id) { this.id = id; }
    public ArrayList<Course> getEnrolledCourses() { return enrolledCourses; }

    public void enrollCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            if (course.enrollStudent()) {
                enrolledCourses.add(course);
                System.out.println("Enrollment successful.");
            } else {
                System.out.println("Course is full.");
            }
        } else {
            System.out.println("Student already enrolled.");
        }
    }

    public void assignGrade(Course course, Double grade) {
        if (enrolledCourses.contains(course))
            grades.put(course, grade);
        else
            System.out.println("Student is not enrolled in this course.");
    }

    public Double getGrade(Course course) {
        return grades.get(course);
    }
}

class Course {
    private String code;
    private String name;
    private int capacity;
    private int enrolledStudents;
    private static int totalEnrolledStudents;

    public Course(String code, String name, int capacity) {
        this.code = code;
        this.name = name;
        this.capacity = capacity;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public int getEnrolledStudents() { return enrolledStudents; }
    public static int getTotalEnrolledStudents() { return totalEnrolledStudents; }

    public boolean enrollStudent() {
        if (enrolledStudents < capacity) {
            enrolledStudents++;
            totalEnrolledStudents++;
            return true;
        }
        return false;
    }
}

class CourseManagement {

    private static ArrayList<Course> courses = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();

    public static void addCourse(String code, String name, int capacity) {
        courses.add(new Course(code, name, capacity));
    }

    public static void addStudent(String name, int id) {
        students.add(new Student(name, id));
    }

    public static Student findStudent(int id) {
        for (Student s : students)
            if (s.getId() == id)
                return s;
        return null;
    }

    public static Course findCourse(String code) {
        for (Course c : courses)
            if (c.getCode().equals(code))
                return c;
        return null;
    }

    public static void enrollStudent(Student s, Course c) {
        if (s != null && c != null)
            s.enrollCourse(c);
        else
            System.out.println("Student or course not found.");
    }

    public static void assignGrade(Student s, Course c, Double grade) {
        if (s != null && c != null)
            s.assignGrade(c, grade);
        else
            System.out.println("Student or course not found.");
    }

    public static Double calculateOverallGrade(Student student) {
        double sum = 0;
        int count = 0;

        for (Course c : student.getEnrolledCourses()) {
            Double g = student.getGrade(c);
            if (g != null) {
                sum += g;
                count++;
            }
        }

        return count == 0 ? 0.0 : sum / count;
    }
}
