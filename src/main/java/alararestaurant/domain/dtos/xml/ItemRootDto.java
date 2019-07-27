package alararestaurant.domain.dtos.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemRootDto {

    @XmlElement(name = "item")
    private List<ItemDto> item;

    public ItemRootDto() {
    }

    public List<ItemDto> getItem() {
        return this.item;
    }

    public void setItem(List<ItemDto> item) {
        this.item = item;
    }
}
