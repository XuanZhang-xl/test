package framework.solr;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by MSI-PC on 2017/5/24.
 */
public class Item {
    @Field
    private Long id;
    @Field
    private String title;
    @Field
    private Long price;

    public Item(Long id, String title, Long price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
