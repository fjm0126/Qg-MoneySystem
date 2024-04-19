package po;

public class Personal_flows {
    private int id;
    private String username;
    private double money;
    private String type;
    private String time;

    public Personal_flows() {
    }

    public Personal_flows(int id, String username, double money, String type, String time) {
        this.id = id;
        this.username = username;
        this.money = money;
        this.type = type;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Personal_flows{" +
                "id=" + id +
                ", username='" + username + '\'' +
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
