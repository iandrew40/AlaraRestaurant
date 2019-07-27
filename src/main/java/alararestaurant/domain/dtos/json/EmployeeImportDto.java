package alararestaurant.domain.dtos.json;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class EmployeeImportDto implements Serializable {

    @Expose
    private String name;

    @Expose
    private Integer age;

    @Expose
    private String position;

    public EmployeeImportDto() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
