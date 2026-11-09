package refactor.baseline;
import refactor.common.*;
import refactor.common.exception.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class EnrollSvc {
    List<EnrollmentRecord> recs = new ArrayList<>();

    public void add(Student s, Course c){
        // duplicated null check
        if(s == null){
            throw new IllegalArgumentException("null");
        }
        if(c == null){
            throw new IllegalArgumentException("null");
        }

        // check exists
        boolean f = false;
        for(var r : recs){
            if(r.student().equals(s) && r.course().equals(c)){
                f = true;
            }
        }
        if(f){
            throw new EnrollmentConflictException("conflict");
        }

        // count
        int cnt = 0;
        for(var r : recs){
            if(r.course().equals(c)) cnt++;
        }
        if(cnt >= 30){ // magic number, default capacity
            throw new CourseFullException("full");
        }

        recs.add(new EnrollmentRecord(s,c, LocalDateTime.now()));
        System.out.println("ok"); // mixed IO with service logic
    }

    public void remove(Student s, Course c){
        if(s == null){
            throw new IllegalArgumentException("null");
        }
        if(c == null){
            throw new IllegalArgumentException("null");
        }
        boolean f = false;
        for(var r : recs){
            if(r.student().equals(s) && r.course().equals(c)){
                f = true;
            }
        }
        if(!f){
            throw new NotEnrolledException("no");
        }
        recs.removeIf(r -> r.student().equals(s) && r.course().equals(c));
    }
}
