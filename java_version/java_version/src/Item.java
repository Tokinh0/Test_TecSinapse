import java.util.Date;

public class Item {
    private String item;
    private double total;
    private int quantidade;
    private Date dia;

    public Item(String item, double total, int quantidade, Date dia) {
        this.item = item;
        this.total = total;
        this.quantidade = quantidade;
        this.dia = dia;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    @Override
    public String toString() {
        return "Item{" +
                "item='" + item + '\'' +
                ", total=" + total +
                ", quantidade=" + quantidade +
                ", dia='" + dia + '\'' +
                '}';
    }
}
