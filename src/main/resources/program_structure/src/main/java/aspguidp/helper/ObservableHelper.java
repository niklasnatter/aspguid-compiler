package aspguidp.helper;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Helper class which provides static methods regarding to the management of observable objects.
 */
public class ObservableHelper {

    /**
     * Construct a observable list which contains the result of the given function applied to the values of the given
     * observable source list. The values from the source list are applied to the given map function, and stored
     * in the returned observable list afterwards. If the source list gets updated, the changes are forwarded to
     * the returned list.
     * <p>
     * This enables to bind results of method calls on the items of an observable list to another observable list.
     * This method is used to get an observable list of respective display representation strings from an observable
     * list of entity instances.
     *
     * @param source      observable source list for the returned list
     * @param mapFunction function which is applied to the items of the source list before they are stored in the
     *                    returned list.
     * @param <S>         type of items in the source list
     * @param <D>         type of items in the returned list
     * @return observable list which contains the results of the given function applied on the items of the
     * source list.
     */
    public static <S, D> ObservableList<D> observableList(ObservableList<S> source, Function<S, D> mapFunction) {
        ObservableList<D> destination = FXCollections.observableArrayList();
        destination.setAll(source.stream().map(mapFunction).collect(Collectors.toList()));
        source.addListener((ListChangeListener<S>) c -> {
            while (c.next()) {
                destination.removeAll(c.getRemoved().stream().map(mapFunction).collect(Collectors.toList()));
                destination.addAll(c.getAddedSubList().stream().map(mapFunction).collect(Collectors.toList()));
            }
        });
        return destination;
    }
}
