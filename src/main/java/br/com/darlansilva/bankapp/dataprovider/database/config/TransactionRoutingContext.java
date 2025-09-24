package br.com.darlansilva.bankapp.dataprovider.database.config;

public class TransactionRoutingContext {

    private static final ThreadLocal<DatabaseSourceContext> context = new ThreadLocal<>();

    public static void setDataSourceType(DatabaseSourceContext dataSourceType) {
        context.set(dataSourceType);
    }

    public static DatabaseSourceContext getDataSourceType() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}