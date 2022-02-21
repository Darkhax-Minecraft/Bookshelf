package net.darkhax.bookshelf.api.lib;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class TableBuilder<T> {

    private static final String NEW_LINE = System.lineSeparator();

    private static final String DIVIDER_COLUMN = "|";

    private static final String DIVIDER_ROW = "-";

    private final List<TableColumn<T>> columns = new LinkedList<>();
    private int entryCount = 0;

    public TableBuilder<T> addColumn(String name, Function<T, Object> function) {

        this.columns.add(new TableColumn<>(name, function));
        return this;
    }

    public void addAll(Iterable<T> entries) {

        entries.forEach(this::addEntry);
    }

    public void addEntry(T entry) {

        columns.forEach(column -> column.processValue(entry));
        entryCount++;
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();

        // Column Names
        for (TableColumn<T> column : this.columns) {

            builder.append(DIVIDER_COLUMN).append(" ").append(StringUtils.rightPad(column.getTitle(), column.getMaxLength())).append(" ");
        }

        builder.append(DIVIDER_COLUMN).append(NEW_LINE);

        // Entry Separator
        for (TableColumn<T> column : this.columns) {

            builder.append(DIVIDER_COLUMN).append(DIVIDER_ROW.repeat(column.getMaxLength() + 2));
        }

        builder.append(DIVIDER_COLUMN).append(NEW_LINE);

        for (int entryIndex = 0; entryIndex < entryCount; entryIndex++) {

            for (TableColumn<T> column : this.columns) {

                builder.append(DIVIDER_COLUMN).append(" ").append(StringUtils.rightPad(column.getValue(entryIndex), column.getMaxLength())).append(" ");
            }

            builder.append(DIVIDER_COLUMN);

            if (entryIndex != entryCount - 1) {

                builder.append(NEW_LINE);
            }
        }

        return builder.toString();
    }

    public static class TableColumn<T> {

        private final String title;
        private final Function<T, Object> valueResolver;
        private final List<String> heldValues;
        private int maxLength;

        public TableColumn(String title, Function<T, Object> valueResolver) {

            this.title = title;
            this.valueResolver = valueResolver;
            this.heldValues = new LinkedList<>();
            this.maxLength = this.title.length();
        }

        public String getTitle() {

            return this.title;
        }

        public String getValue(int index) {

            return this.heldValues.get(index);
        }

        public void processValue(T value) {

            final String valueText = String.valueOf(this.valueResolver.apply(value));
            heldValues.add(valueText);

            if (valueText.length() > maxLength) {

                this.maxLength = valueText.length();
            }
        }

        public int getMaxLength() {

            return this.maxLength;
        }
    }
}