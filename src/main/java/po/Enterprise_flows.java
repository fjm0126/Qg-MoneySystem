package po;

public class Enterprise_flows {
    private int id;
    private String enterprise_name;
    private double money;
    private String type;
    private String time;
    private String object;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Enterprise_flows() {
    }

    public Enterprise_flows(int id, String enterprise_name, double money, String type, String time) {
        this.id = id;
        this.enterprise_name = enterprise_name;
        this.money = money;
        this.type = type;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Enterprise_flows{" +
                "id=" + id +
                ", enterprise_name='" + enterprise_name + '\'' +
                ", money=" + money +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnterprise_name() {
        return enterprise_name;
    }

    public void setEnterprise_name(String enterprise_name) {
        this.enterprise_name = enterprise_name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
