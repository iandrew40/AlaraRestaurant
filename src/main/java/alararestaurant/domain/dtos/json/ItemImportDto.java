package alararestaurant.domain.dtos.json;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ItemImportDto {

    @Expose
    private String name;

    @Expose
    private String category;

    @Expose
    private BigDecimal price;

    public ItemImportDto() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
