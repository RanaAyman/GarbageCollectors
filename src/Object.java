public class Object {
//
    private int id;
    private int from;
    private int to;
    private int size;
    private int mark;

    public Object(int id, int from, int to) {
        this.id = id;
        this.size = to - from;
    }

    public int getID() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public int getTo() {
        return to;
    }

    public int getFrom() {
        return from;
    }

    public int getMark() {
        return mark;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}
