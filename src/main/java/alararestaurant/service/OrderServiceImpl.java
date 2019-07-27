package alararestaurant.service;

import alararestaurant.domain.dtos.xml.ItemDto;
import alararestaurant.domain.dtos.xml.OrderDto;
import alararestaurant.domain.dtos.xml.OrderRootDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String XML_ORDERS_FILE_PATH = "/Users/andreyivanov/Documents/SoftUni/Java/Java DB/" +
            "AlaraRestaurant/src/main/resources/files/orders.xml";

    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRpository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, EmployeeRepository employeeRpository, ItemRepository itemRepository, OrderItemRepository orderItemRepository, FileUtil fileUtil, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.orderRepository = orderRepository;
        this.employeeRpository = employeeRpository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public Boolean ordersAreImported() {
        return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {
        return this.fileUtil.readFile(XML_ORDERS_FILE_PATH);
    }

    @Override
    public String importOrders() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        JAXBContext contex = JAXBContext.newInstance(OrderRootDto.class);
        Unmarshaller unmarshaller = contex.createUnmarshaller();
        OrderRootDto list = (OrderRootDto) unmarshaller.unmarshal(new File(XML_ORDERS_FILE_PATH));

        OrderLoop:
        for (OrderDto orderDto : list.getOrderDtos()) {
            Order order = this.modelMapper.map(orderDto, Order.class);
            if (!this.validationUtil.isValid(order)){
                sb.append("Invalid data format.\n");

                continue;
            }

            Employee employee = this.employeeRpository.findByName(orderDto.getEmployee());
            if (employee == null){
                sb.append("Invalid data format.\n");

                continue;
            }
            order.setEmployee(employee);
            List<OrderItem> orderItems = new ArrayList<>();
            for (ItemDto item : orderDto.getItems().getItem()) {
                if (this.itemRepository.findByName(item.getName()) == null){
                    continue OrderLoop;
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setItem(this.itemRepository.findByName(item.getName()));
                orderItem.setQuantity(item.getQuantity());
                orderItems.add(orderItem);

                this.orderItemRepository.saveAndFlush(orderItem);
            }

            order.setOrderItems(orderItems);
            this.orderRepository.saveAndFlush(order);

            sb.append(String.format("Order for %s on %s added\n", order.getCustomer(), order.getDateTime()));
        }

        return sb.toString();
    }

    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {
        StringBuilder sb = new StringBuilder();

        List<Order> orders = this.orderRepository.finishedByBurgerFlipper();

        for (Order order : orders) {
            sb.append(String.format("Name: %s\n", order.getEmployee().getName()))
                    .append(String.format("Orders:\n"))
                    .append(String.format("  Customer: %s\n", order.getCustomer()))
                    .append(String.format("  Items:\n"));
            for (OrderItem orderItem : order.getOrderItems()) {
                sb.append(String.format("\tName: %s\n", orderItem.getItem().getName()))
                        .append(String.format("\tPrice: %.2f\n", orderItem.getItem().getPrice()))
                        .append(String.format("\tQuantity: %d\n", orderItem.getQuantity()))
                        .append(System.lineSeparator());
            }

        }

        return sb.toString();
    }
}
