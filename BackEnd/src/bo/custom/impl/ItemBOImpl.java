package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {

    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.getAll();
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        for (Item item : all) {
            allItems.add(new ItemDTO(
                    item.getCode(),
                    item.getDescription(),
                    item.getQtyOnHand(),
                    item.getUnitPrice()
            ));
        }
        return allItems;
    }

    @Override
    public ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(code);
        return new ItemDTO(
                item.getCode(),
                item.getDescription(),
                item.getQtyOnHand(),
                item.getUnitPrice()
        );
    }

    @Override
    public boolean addNewItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.add(new Item(
                itemDTO.getItemCode(),
                itemDTO.getItemDescription(),
                itemDTO.getItemQtyOnHand(),
                itemDTO.getItemUnitPrice()
        ));
    }

    @Override
    public boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item(
                itemDTO.getItemCode(),
                itemDTO.getItemDescription(),
                itemDTO.getItemQtyOnHand(),
                itemDTO.getItemUnitPrice()
        ));
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.remove(code);
    }

    @Override
    public boolean ifItemExist(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.ifItemExist(code);
    }
}
