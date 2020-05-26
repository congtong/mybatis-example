package entity;

import java.util.Date;
import lombok.Data;

@Data
public class Student {
    private Integer studentId;

    private String name;

    private String phone;

    private String email;

    private Byte sex;

    private Byte locked;

    private Date gmtCreated;

    private Date gmtModified;
}
