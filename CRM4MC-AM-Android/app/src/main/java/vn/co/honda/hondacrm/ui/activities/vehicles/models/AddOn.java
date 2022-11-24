package vn.co.honda.hondacrm.ui.activities.vehicles.models;

public class AddOn {
    public static final int JOB = 0;
    public static final int ACCESSORY = 1;

    private String job;
    private String price;
    private String salary;
    private Integer type;

    public AddOn() {

    }

    public AddOn(String job, String price, String salary, Integer type) {
        this.job = job;
        this.price = price;
        this.salary = salary;
        this.type = type;
    }

    public AddOn(String job, String price, Integer type) {
        this.job = job;
        this.price = price;
        this.type = type;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}