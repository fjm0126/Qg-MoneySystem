package po;

public class Enterprise {
    private String name;
    private int id;
    private String creator_name;
    private int peopleNumber;
    private String scale; //企业规模
    private String work_orientation; //工作方向
    private double money; //企业资金余额
    private String access_mode;

    public String getAccess_mode() {
        return access_mode;
    }

    public void setAccess_mode(String access_mode) {
        this.access_mode = access_mode;
    }

    public Enterprise() {
    }

    public Enterprise(String name, int peopleNumber, String scale, String creator_name,String work_orientation, String access_mode) {
        this.name = name;
        this.peopleNumber = peopleNumber;
        this.scale = scale;
        this.work_orientation = work_orientation;
        this.access_mode = access_mode;
        this.creator_name=creator_name;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getWork_orientation() {
        return work_orientation;
    }

    public void setWork_orientation(String work_orientation) {
        this.work_orientation = work_orientation;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "name='" + name + '\'' +
                ", peopleNumber='" + peopleNumber + '\'' +
                ", scale='" + scale + '\'' +
                ", work_orientation='" + work_orientation + '\'' +
                ", money='" + money + '\'' +
                '}';
    }
}
