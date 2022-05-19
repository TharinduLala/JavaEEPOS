package bo;

import bo.custom.impl.*;

public class BOFactory {

    private static bo.BOFactory boFactory;

    private BOFactory() {
    }

    public static bo.BOFactory getBOFactory() {
        if (boFactory == null) {
            boFactory = new bo.BOFactory();
        }
        return boFactory;
    }

    public bo.SuperBO getBO(BoTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            default:
                return null;
        }
    }

    public enum BoTypes {
        CUSTOMER,ITEM
    }
}



